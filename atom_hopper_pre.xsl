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
    version="1.0">

    <xsl:template match="node() | @*">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="atom:entry[atom:content/event:event]">
        <xsl:variable name="event" select="atom:content/event:event"/>
        <xsl:variable name="prod" select="atom:content/event:event/child::*[1]"/>
        <xsl:copy>
            <xsl:choose>
                <!--
                    If there is a resourceType then create a catagory from
                    the message like this:

                    {serviceCode}.{resourceType}.{eventType}

                    Resource Type and eventType are converted to lowercase.
                -->
                <xsl:when test="$prod/@resourceType">
                    <atom:category term="{concat($prod/@serviceCode,'.',translate($prod/@resourceType, &UPPER_TO_LOWER;),'.',translate($event/@type, &UPPER_TO_LOWER;))}" />
                </xsl:when>
                <!--
                    If there is no resourceType then we create a
                    catagory from the message like this:

                    {serviceCode}.{NSPart}.{eventType}

                    NSPart is the last path segment in the product
                    namespace.
                -->
                <xsl:when test="$event">
                    <xsl:variable name="nsPart">
                        <xsl:call-template name="getNSPart">
                            <xsl:with-param name="ns" select="namespace-uri($prod)"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <atom:category term="{concat($prod/@serviceCode,'.',$nsPart,'.',translate($event/@type, &UPPER_TO_LOWER;))}" />
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
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