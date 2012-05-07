<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:html="http://www.w3.org/1999/xhtml"
    xmlns:schema="http://docs.rackspace.com/usage/core/schema"
    xmlns:usage="http://docs.rackspace.com/usage/core"
    xmlns="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="schema"
    version="2.0">
    
    <xsl:output method="xml" indent="yes"/>
    
    <xsl:variable name="serviceName" select="/schema:schema/@serviceName" as="xsd:string"/>
    
    <xsl:template match="schema:schema">
        <xsl:variable name="resourceTypes" as="xsd:string*" select="tokenize(@resourceTypes, ' ')"></xsl:variable>
        <schema
              elementFormDefault="qualified"
              attributeFormDefault="unqualified"
              xmlns:xsd="http://www.w3.org/2001/XMLSchema"
              xmlns:html="http://www.w3.org/1999/xhtml"
              xmlns="http://www.w3.org/2001/XMLSchema">
            <xsl:namespace name="p" select="@targetNamespace"/>
            <xsl:attribute name="targetNamespace">
                <xsl:value-of select="@targetNamespace"/>
            </xsl:attribute>
            
            <element name="usage">
                <xsl:attribute name="type">
                    <xsl:value-of select="concat('p:',$serviceName,'Type')"/>
                </xsl:attribute>
            </element>
            
            <complexType>
                <xsl:attribute name="type">
                    <xsl:value-of select="concat($serviceName,'Type')"/>
                </xsl:attribute>
                <annotation>
                    <documentation>
                        <html:p>
                            <xsl:value-of select="normalize-space(schema:description)"/>
                        </html:p>
                    </documentation>
                </annotation>
                <attribute name="version" type="xsd:string" use="required">
                    <xsl:attribute name="fixed" select="@schemaVersion"/>
                </attribute>
                <attribute name="resourceType"  use="required" type="p:ResourceTypes"/>
                <xsl:apply-templates/>
            </complexType>
            
            <simpleType name="ResourceTypes">
                <annotation>
                    <documentation>
                        <html:p>Resource Types for this product.</html:p>
                    </documentation>
                </annotation>
                <restriction base="xsd:token">
                    <xsl:for-each select="$resourceTypes">
                        <enumeration>
                            <xsl:attribute name="value">
                                <xsl:value-of select="."/>
                            </xsl:attribute>
                        </enumeration>
                    </xsl:for-each>
                </restriction>
            </simpleType>
            <xsl:if test="schema:attribute[@type='UUID']">
                <simpleType name="UUID">
                    <restriction base="xsd:string">
                        <length value="36" fixed="true"/>
                        <pattern value="[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"/>
                    </restriction>
                </simpleType>
            </xsl:if>
            <xsl:apply-templates mode="AddTypes"/>
        </schema>
    </xsl:template>
    <xsl:template match="schema:attribute">
        <attribute>
            <xsl:attribute name="name" select="@name"/>
            <xsl:if test="@use">
                <xsl:attribute name="use" select="@use"/>
            </xsl:if>
            <xsl:if test="@fixed">
                <xsl:attribute name="fixed" select="@fixed"/>
            </xsl:if>
            <xsl:if test="@default">
                <xsl:attribute name="default" select="@default"/>
            </xsl:if>
            <xsl:attribute name="type">
                <xsl:choose>
                    <xsl:when test="ends-with(@type, '*')">
                        <xsl:value-of select="concat(@name,'List')"/>
                    </xsl:when>
                    <xsl:when test="@allowedValues">
                        <xsl:value-of select="concat(@name,'Enum')"/>
                    </xsl:when>
                    <xsl:when test="@type='UUID'">
                        <xsl:value-of select="'p:UUID'"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="concat('xsd:',@type)"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <annotation>
                <documentation>
                    <html:p>
                        <xsl:value-of select="normalize-space(.)"/>
                    </html:p>
                    <appinfo>
                        <usage:attributes>
                            <xsl:if test="@display-name">
                                <xsl:attribute name="display-name" select="@display-name"/>
                            </xsl:if>
                            <xsl:if test="@aggregate-function">
                                <xsl:attribute name="aggregate-function" select="@aggregate-function"/>
                            </xsl:if>
                            <xsl:if test="@unit-of-measure">
                                <xsl:attribute name="unit-of-measure" select="@unit-of-measure"/>
                            </xsl:if>
                            <xsl:if test="@group">
                                <xsl:attribute name="group" select="@group"/>
                            </xsl:if>
                        </usage:attributes>
                    </appinfo>
                </documentation>
            </annotation>
        </attribute>
    </xsl:template>
    <xsl:template mode="#all" match="text()"/>
</xsl:stylesheet>