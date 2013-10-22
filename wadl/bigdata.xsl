<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:event="http://docs.rackspace.com/core/event"
    xmlns:bigdata="http://docs.rackspace.com/usage/bigdata"
    xmlns:atom="http://www.w3.org/2005/Atom"
    exclude-result-prefixes="event"
    version="2.0">

    <!-- Import utility templates -->
    <xsl:import href="util.xsl" />

    <xsl:output method="xml" encoding="UTF-8"/>

    <xsl:template match="bigdata:product">
        <xsl:copy>
            <xsl:attribute name="aggregatedClusterDuration"
                           select="xsd:integer((xsd:dateTime(../@endTime) - xsd:dateTime(../@startTime))
                                   div xsd:dayTimeDuration('PT1S') * @numberServersInCluster)">
            </xsl:attribute>
            <xsl:apply-templates select="@*[(local-name() != 'aggregatedClusterDuration')] | node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
