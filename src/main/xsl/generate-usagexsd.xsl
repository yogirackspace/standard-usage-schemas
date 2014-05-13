<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:html="http://www.w3.org/1999/xhtml"
    xmlns:schema="http://docs.rackspace.com/core/usage/schema"
    xmlns:usage="http://docs.rackspace.com/core/usage"
    xmlns="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="schema usage"
    version="2.0">

    <xsl:output method="xml" indent="yes" omit-xml-declaration="yes"/>
    <xsl:param name="input_dir" select="'file:///Users/shin4590/src/forks/standard-usage-schemas/sample_product_schemas'"/>
    
    <xsl:template match="/">
<schema
     elementFormDefault="qualified"
     attributeFormDefault="unqualified"
     xmlns="http://www.w3.org/2001/XMLSchema"
     xmlns:event="http://docs.rackspace.com/core/event"
     xmlns:xsd="http://www.w3.org/2001/XMLSchema"
     xmlns:html="http://www.w3.org/1999/xhtml"
     xmlns:vc="http://www.w3.org/2007/XMLSchema-versioning"
     targetNamespace="http://docs.rackspace.com/core/event">
            
        <xsl:text>&#x0a;    </xsl:text>
        <xsl:comment>
        This schema provides a place to include/import relevant XSDs
        and defines the current usage element.    
    </xsl:comment>
        <xsl:text>&#x0a;    </xsl:text>
        <xsl:comment>
        Loads Event Version 1. Other versions may be loaded here.  
    </xsl:comment>
        <xsl:text>&#x0a;    </xsl:text>
    <include schemaLocation="core.xsd"/>

        <xsl:comment> Core annotations declared here </xsl:comment>
        <xsl:text>&#x0a;    </xsl:text>
    <import namespace="http://docs.rackspace.com/core/usage"
            schemaLocation="ProductExtension-Annotations.xsd"/>

        <xsl:text>&#x0a;    </xsl:text>   
        <xsl:comment> Load Product Schemas </xsl:comment>
        <xsl:text>&#x0a;    </xsl:text>   
        <xsl:call-template name="processAllProductSchemas"/>
    
    <element name="event" type="event:EventV1"/>
</schema>
    </xsl:template>
    
    <xsl:template name="processAllProductSchemas">
        <xsl:for-each select="collection(concat($input_dir, '?select=*.xml;recurse=yes'))">
            <xsl:variable name="product_namespace"><xsl:value-of select="//schema:productSchema[position() = 1]/@namespace"/></xsl:variable>
            <xsl:variable name="filename"><xsl:value-of select="(tokenize(document-uri(.),'/'))[last()]"/></xsl:variable>
            <xsl:variable name="xsd_filename"><xsl:value-of select="replace($filename,'.xml','.xsd')"/></xsl:variable>
            
            <!-- Only do this if xsd file exist. widget3.xsd for example gets combined to widget.xsd -->
            <xsl:choose>
                <xsl:when test="document(concat($input_dir, '/../generated_product_xsds/',$xsd_filename))">

            <import namespace="{$product_namespace}"
                schemaLocation="../generated_product_xsds/{$xsd_filename}"/>
            <xsl:text>&#x0a;&#x0a;</xsl:text>            

                </xsl:when>
            </xsl:choose>
        </xsl:for-each>    
    </xsl:template>

</xsl:stylesheet>
