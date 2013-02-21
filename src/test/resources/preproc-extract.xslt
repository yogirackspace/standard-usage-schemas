<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://docs.rackspace.com/core/usage/test/preproc"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xsd"
    version="2.0">
    <xsl:param name="preProcList" as="xsd:string"/>
    <xsl:variable name="preprocs" as="xsd:string*" select="tokenize($preProcList,' ')"/>

    <xsl:output method="xml" indent="yes" use-character-maps="x"/>

    <xsl:character-map name="x">
        <xsl:output-character character="&#x2780;" string="&lt;"/>
        <xsl:output-character character="&#x2781;" string="&gt;"/>
    </xsl:character-map>

    <xsl:template match="text()" />

    <xsl:template match="/">
        <preprocs>
            <xsl:apply-templates/>
        </preprocs>
    </xsl:template>

    <xsl:template match="processing-instruction()">
        <xsl:if test="name() = $preprocs">
            <xsl:text>&#x2780;</xsl:text>
            <xsl:value-of select="name()"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="."/>
            <xsl:text> /&#x2781;</xsl:text>
        </xsl:if>
    </xsl:template>
</xsl:transform>
