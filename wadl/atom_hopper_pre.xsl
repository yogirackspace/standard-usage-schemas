<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE stylesheet[
   <!ENTITY UPPERCASE "ABCDEFGHIJKLMNOPQRSTUVWXYZ">
   <!ENTITY LOWERCASE "abcdefghijklmnopqrstuvwxyz">
   <!ENTITY UPPER_TO_LOWER " '&UPPERCASE;', '&LOWERCASE;'">
]>

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:event="http://docs.rackspace.com/core/event"
    xmlns:atom="http://www.w3.org/2005/Atom"
    exclude-result-prefixes="event"
    version="1.0">

    <xsl:output method="xml" encoding="UTF-8"/>

    <xsl:template match="node() | @*">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="atom:entry[atom:content/event:event]">
        <xsl:variable name="event" select="atom:content/event:event"/>
        <xsl:variable name="prod" select="atom:content/event:event/child::*[1]"/>
        <xsl:variable name="nsPart">
            <xsl:if test="$prod">
                <xsl:call-template name="getNSPart">
                    <xsl:with-param name="ns" select="namespace-uri($prod)"/>
                </xsl:call-template>
            </xsl:if>
        </xsl:variable>
        <xsl:copy>
            <xsl:call-template name="addCategory">
                <xsl:with-param name="term" select="$event/@tenantId"/>
            </xsl:call-template>
            <xsl:call-template name="addCategory">
                <xsl:with-param name="term" select="$event/@region"/>
                <xsl:with-param name="default" select="'GLOBAL'"/>
            </xsl:call-template>
            <xsl:call-template name="addCategory">
                <xsl:with-param name="term" select="$event/@dataCenter"/>
                <xsl:with-param name="default" select="'GLOBAL'"/>
            </xsl:call-template>
            <xsl:choose>
                <!--
                    If there is a resourceType then create a catagory from
                    the message like this:

                    {serviceCode}.{NSPart}.{resourceType}.{eventType}

                    Resource Type and eventType are converted to lowercase.

                    NSPart is the last path segment in the product
                    namespace.
                -->
                <xsl:when test="$prod/@resourceType">
                    <xsl:call-template name="addCategory">
                        <xsl:with-param name="term"
                                        select="concat(translate($prod/@serviceCode, &UPPER_TO_LOWER;),'.',$nsPart,'.',translate($prod/@resourceType, &UPPER_TO_LOWER;),'.',translate($event/@type, &UPPER_TO_LOWER;))" />
                    </xsl:call-template>
                </xsl:when>
                <!--
                    If there is no resourceType then we create a
                    catagory from the message like this:

                    {serviceCode}.{NSPart}.{eventType}

                    eventType is converted to lowercase

                    NSPart is the last path segment in the product
                    namespace.
                -->
                <xsl:when test="$event">
                    <xsl:call-template name="addCategory">
                        <xsl:with-param name="term"
                                        select="concat(translate($prod/@serviceCode, &UPPER_TO_LOWER;),'.',$nsPart,'.',translate($event/@type, &UPPER_TO_LOWER;))"/>
                    </xsl:call-template>
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="addCategory">
        <xsl:param name="term"/>
        <xsl:param name="default" select="''"/>
        <xsl:variable name="actualTerm">
            <xsl:choose>
                <xsl:when test="boolean($term)">
                    <xsl:value-of select="$term"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$default"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!--
            If the category term is not empty and a category for that
            term does not already exist, add it.
        -->
        <xsl:if test="boolean($actualTerm) and not(atom:category[@term = $actualTerm])">
            <atom:category term="{$actualTerm}"/>
        </xsl:if>
    </xsl:template>

    <xsl:template name="getNSPart">
        <xsl:param name="ns"/>
        <xsl:choose>
            <xsl:when test="contains($ns,'/')">
                <xsl:call-template name="getNSPart">
                    <xsl:with-param name="ns" select="substring-after($ns, '/')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$ns"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>
