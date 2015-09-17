<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns="http://docs.rackspace.com/api/cloudfeeds/non-string-attrs"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:c="http://www.w3.org/ns/xproc-step"
    xmlns:sch="http://docs.rackspace.com/core/usage/schema"
    xmlns:rax="http://docs.rackspace.com/api"
    xmlns:atom="http://www.w3.org/2005/Atom"
    xmlns:xslout="http://www.rackspace.com/repose/wadl/checker/Transform"
    exclude-result-prefixes="sch c rax xs"
    version="2.0">

    <xsl:namespace-alias stylesheet-prefix="xslout" result-prefix="xsl"/>
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

    <!-- The directory where all the product schema files are located -->
    <xsl:param name="schemaDirectory"/>
    
    <!-- This is the list of Product Schema types we support today that
         are non string types and must not be quoted. The complete list 
         of supported types are in the productSchema.xsd.
     -->
    <xsl:variable name="nonStringTypes" select="tokenize('integer,int,short,byte,unsignedLong,unsignedInt,unsignedShort,unsignedByte,double,float,duration,boolean', ',')"/>

    <xsl:variable name="productSchemas" select="collection(concat($schemaDirectory, '?select=*.xml;recurse=yes'))"/>
    
    <xsl:template match="/">
        <xslout:stylesheet
            xmlns:event="http://docs.rackspace.com/core/event"
            xmlns:atom="http://www.w3.org/2005/Atom"
            version="2.0">
            <xslout:variable name="nonStringAttrsList">
                <xsl:copy>
                    <!-- loop over all the found productSchemas.
                         The select here is irrelevant.
                     -->
                    <xsl:apply-templates select="." mode="loop"/>
                </xsl:copy>               
            </xslout:variable>
        </xslout:stylesheet>
    </xsl:template>
    
    <xsl:template match="*" mode="loop"> 
        <xsl:for-each select="$productSchemas//sch:productSchema">
            <xsl:sort select="current()[namespace]"
                      data-type="text"
                      order="ascending" />
            <xsl:if test="count(current()//sch:attribute[exists(index-of($nonStringTypes, @type))]) gt 0">
                <schema key="{@namespace}" version="{@version}">
                    <attributes>
                        <xsl:for-each select="current()//sch:attribute[exists(index-of($nonStringTypes, @type))]">
                            <xsl:variable name="myParent" select="parent::node()"/>
    
                            <!-- The following choose-when-otherwise assumes that we only
                                have 2 levels of hierarchy. If the current attribute parent's
                                node is 'attributeGroup', we print the name of that 
                                attributeGroup. Otherwise, we print 'product'.
                                
                                For example:
                                * product/@huddleID
                                * slice/@id
                                
                                This assumption *must* match with the code in xml2json-feeds.xsl
                            -->
                            <xsl:choose>
                                <xsl:when test="local-name($myParent) eq 'attributeGroup'">
                                    <xsl:value-of select="concat($myParent/@name, '/@', @name)"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="concat('product/@',@name)"/>    
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:if test="position() ne last()">
                                <xsl:text>,</xsl:text>
                            </xsl:if>
                       </xsl:for-each>
                    </attributes> 
                </schema>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
