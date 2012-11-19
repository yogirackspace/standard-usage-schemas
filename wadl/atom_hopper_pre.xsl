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

    <xsl:template match="atom:entry">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="atom:entry[atom:content/event:event]">
        <xsl:variable name="event" select="atom:content/event:event"/>
        <xsl:copy>
            <xsl:call-template name="addCategory">
                <xsl:with-param name="term" select="$event/@tenantId"/>
                <xsl:with-param name="prefix" select="'tid:'"/>
            </xsl:call-template>
            <xsl:call-template name="addCategory">
                <xsl:with-param name="term" select="$event/@region"/>
                <xsl:with-param name="prefix" select="'rgn:'"/>
                <xsl:with-param name="default" select="'GLOBAL'"/>
            </xsl:call-template>
            <xsl:call-template name="addCategory">
                <xsl:with-param name="term" select="$event/@dataCenter"/>
                <xsl:with-param name="prefix" select="'dc:'"/>
                <xsl:with-param name="default" select="'GLOBAL'"/>
            </xsl:call-template>
            <xsl:call-template name="addCategory">
                <xsl:with-param name="term" select="$event/@resourceId"/>
                <xsl:with-param name="prefix" select="'rid:'"/>
            </xsl:call-template>
            <xsl:if test="$event">
                <xsl:call-template name="addIdCategory">
                    <xsl:with-param name="event" select="$event"/>
                </xsl:call-template>
            </xsl:if>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <!--
        STOP GAP: If the category is 'monitoring.check.usage' and the
        tenantId starts with hybrid: , then remove the category.
        Otherwise copy it.
    -->
    <xsl:template match="atom:category[@term='monitoring.check.usage']">
        <xsl:variable name="event" select="../atom:content/event:event"/>
        <xsl:choose>
            <xsl:when test="starts-with($event/@tenantId,'hybrid:')"/>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates select="@* | node()"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="addIdCategory">
        <xsl:param name="event"/>
        <xsl:variable name="prod" select="$event/child::*[1]"/>
        <xsl:variable name="nsPart">
            <xsl:if test="$prod">
                <xsl:call-template name="getNSPart">
                    <xsl:with-param name="ns" select="namespace-uri($prod)"/>
                </xsl:call-template>
            </xsl:if>
        </xsl:variable>
        <xsl:variable name="Id_1">
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
                    <xsl:value-of
                        select="concat(translate($prod/@serviceCode, &UPPER_TO_LOWER;),'.',$nsPart,'.',
                                translate($prod/@resourceType, &UPPER_TO_LOWER;),'.',translate($event/@type, &UPPER_TO_LOWER;))" />
                </xsl:when>
                <!--
                    If there is no resourceType then we create a
                    catagory from the message like this:

                    {serviceCode}.{NSPart}.{eventType}

                    eventType is converted to lowercase

                    NSPart is the last path segment in the product
                    namespace.
                -->
                <xsl:otherwise>
                    <xsl:value-of
                        select="concat(translate($prod/@serviceCode, &UPPER_TO_LOWER;),'.',$nsPart,'.',translate($event/@type, &UPPER_TO_LOWER;))"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!--
            STOP GAP: Append .hybrid to the id when a tenantId starts with hybrid:
        -->
        <xsl:variable name="Id_2">
            <xsl:choose>
                <xsl:when test="starts-with($event/@tenantId,'hybrid:')">
                    <xsl:value-of select="concat($Id_1,'.hybrid')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$Id_1"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:call-template name="addCategory">
            <xsl:with-param name="term" select="$Id_2"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="addCategory">
        <xsl:param name="term"/>
        <xsl:param name="default" select="''"/>
        <xsl:param name="prefix" select="''" />
        <xsl:variable name="actualTerm">
            <xsl:choose>
                <xsl:when test="$term != ''">
                    <xsl:value-of select="concat($prefix, $term)"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test="$default != ''">
                        <xsl:value-of select="concat($prefix, $default)"/>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!--
            If the category term is not empty and a category for that
            term does not already exist, add it.
        -->
        <xsl:if test="$actualTerm != '' and not(atom:category[@term = $actualTerm])">
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
