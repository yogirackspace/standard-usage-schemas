<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:event="http://docs.rackspace.com/core/event"
    xmlns:lbaas="http://docs.rackspace.com/usage/lbaas"
    xmlns:atom="http://www.w3.org/2005/Atom"
    exclude-result-prefixes="event"
    version="2.0">

    <!-- Import utility templates -->
    <xsl:import href="util.xsl" />

    <xsl:output method="xml" encoding="UTF-8"/>

    <xsl:template match="lbaas:product">
        <xsl:copy>
            <xsl:attribute name="avgConcurrentConnectionsSum"
                           select="xsd:double(@avgConcurrentConnections) + xsd:double(@avgConcurrentConnectionsSsl)"/>
            <xsl:choose>
                <xsl:when test="@vipType = 'PUBLIC'">
                    <xsl:attribute name="publicBandWidthInSum"
                           select="xsd:long(@bandWidthIn) + xsd:long(@bandWidthInSsl)"/>
                    <xsl:attribute name="publicBandWidthOutSum"
                           select="xsd:long(@bandWidthOut) + xsd:long(@bandWidthOutSsl)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="publicBandWidthInSum" select="xsd:long(0)"/>
                    <xsl:attribute name="publicBandWidthOutSum" select="xsd:long(0)"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:attribute name="hasSSLConnection"
                           select="xsd:boolean(@sslMode = 'ON' or @sslMode = 'MIXED')"/>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
