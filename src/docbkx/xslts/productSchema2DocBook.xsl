<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:math="http://www.w3.org/2005/xpath-functions/math"
    xmlns="http://docbook.org/ns/docbook" 
    xmlns:db="http://docbook.org/ns/docbook" 
    xmlns:ps="http://docs.rackspace.com/core/usage/schema"
    exclude-result-prefixes="xs math"
    version="2.0" 
    xpath-default-namespace="http://docs.rackspace.com/core/usage/schema">
    
    <xsl:output indent="yes"/>
    
    <xsl:preserve-space elements="db:programlisting"/>
    
    <xsl:template match="products">
        <xsl:apply-templates select="db:*"/>
    </xsl:template>
    
    <xsl:template match="db:appendix|db:chapter">
        <xsl:copy>
            <xsl:copy-of select="@*|node()"/>
            <xsl:apply-templates select="/products/product"/>        
        </xsl:copy>
    </xsl:template>

    <xsl:template match="db:section">
        <xsl:copy>
            <xsl:copy-of select="@*|node()"/>
            <xsl:call-template name="alternatives">
                <xsl:with-param name="productSchema" select="document(resolve-uri(ancestor::product/@schemaFile,base-uri(.)))"/>
                <xsl:with-param name="sampleFile" select="ancestor::product/@sampleFile"/>
                <xsl:with-param name="title" select="db:title|db:info/db:title"/>
            </xsl:call-template>
            <para security="writeronly"><remark>!!!!!Generated File. Do not edit by hand!!!!!</remark></para>            
        </xsl:copy>
    </xsl:template>
     
    <xsl:template name="alternatives">
        <xsl:param name="sampleFile"/>
        <xsl:param name="title"/>
        <xsl:param name="productSchema"/>
        <xsl:variable
            name="content"
            as="xs:string"
            select="if ($sampleFile != '')
            then unparsed-text(resolve-uri($sampleFile, base-uri()))
            else xs:string(.)"/>        
            <xsl:if test="$productSchema//attribute">
            <informaltable frame="all">
                <xsl:processing-instruction name="dbhtml">table-width="100%"</xsl:processing-instruction>
                <xsl:apply-templates select="$productSchema//productSchema">
                    <xsl:sort select="@version" order="descending"/>
                </xsl:apply-templates>
            </informaltable>
            </xsl:if>
            <xsl:if test="$sampleFile">
                <example>
                    <title>Sample <xsl:value-of select="$title"/> message</title>
<programlisting language="xml"><xsl:copy-of select="$content"/></programlisting>
                </example>
            </xsl:if>
    </xsl:template>
       
    <xsl:template match="productSchema">
        <tgroup cols="4">
            <colspec colname="c1" colnum="1" colwidth="3*"/>
            <colspec colname="c2" colnum="2" colwidth="5.39*"/>
            <colspec colname="c3" colnum="3" colwidth="2*"/>
            <colspec colname="c4" colnum="4" colwidth="1.5*"/>
            <thead>                
                <row>
                    <entry namest="c1" nameend="c4" align="center">Version <xsl:value-of select="@version"/></entry>
                </row>
                <row>
                    <entry>Attribute Name</entry>
                    <entry>Description</entry>
                    <entry>Type</entry>
                    <entry>Optionality</entry>
                </row>                
            </thead>
            <tbody>
                <xsl:apply-templates select="attribute"/>
            </tbody>
        </tgroup>        
    </xsl:template>   
    
    <xsl:template match="attribute">
        <row>
            <entry>
                <para><code><xsl:value-of select="@name"></xsl:value-of></code></para>
            </entry>
            <entry>
                <para><xsl:apply-templates/></para>
                <xsl:if test="@allowedValues"><para>Allowed Values: <xsl:value-of select="@allowedValues"/></para></xsl:if>
            </entry>
            <entry>
                <para><xsl:value-of select="@type"/></para>
            </entry>
            <entry>
                <para><xsl:value-of select="if(@use = 'optional') then 'Optional' else 'Required'"/></para>
            </entry>
        </row>
    </xsl:template>
       
</xsl:stylesheet>