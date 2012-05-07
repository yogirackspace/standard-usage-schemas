<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:html="http://www.w3.org/1999/xhtml"
    xmlns:schema="http://docs.rackspace.com/usage/core/schema"
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
        </schema>
    </xsl:template>
    <xsl:template match="text()"/>
</xsl:stylesheet>