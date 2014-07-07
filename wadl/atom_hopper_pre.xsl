<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:event="http://docs.rackspace.com/core/event"
    xmlns:atom="http://www.w3.org/2005/Atom"
    exclude-result-prefixes="event"
    version="1.0">

    <!-- Import utility templates -->
    <xsl:import href="util.xsl" />

    <xsl:output method="xml" encoding="UTF-8"/>

    <xsl:template match="/processing-instruction('atom')">
        <!-- ignore atom unit test processing instruction -->
    </xsl:template>

    <xsl:template match="atom:category[starts-with(@term,'tid:')]">
        <xsl:if test="/atom:entry/atom:content/event:event">
            <xsl:variable name="tenantId" select="substring-after(@term, 'tid:')"/>
            <xsl:choose>
                <xsl:when test="/atom:entry/atom:content/event:event/@tenantId = $tenantId">
                    <xsl:copy>
                        <xsl:apply-templates select="@* | node()"/>
                    </xsl:copy>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:message terminate="yes">The "tid:[tenantId]" category is automatically added by Cloud Feeds.  Alternative "tid:[other]" categories cannot be submitted.</xsl:message>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

    <xsl:template match="atom:category[starts-with(@term,'rid:')]">
        <xsl:if test="/atom:entry/atom:content/event:event">
            <xsl:variable name="value" select="substring-after(@term, 'rid:')"/>
            <xsl:choose>
                <xsl:when test="/atom:entry/atom:content/event:event/@resourceId = $value">
                    <xsl:copy>
                        <xsl:apply-templates select="@* | node()"/>
                    </xsl:copy>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:message terminate="yes">The "rid:[resourceId]" category is automatically added by Cloud Feeds.  Alternative "rid:[other]" categories cannot be submitted.</xsl:message>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

    <xsl:template match="atom:category[starts-with(@term,'type:')]">
        <xsl:if test="/atom:entry/atom:content/event:event">
            <xsl:variable name="value" select="substring-after(@term, 'type:')"/>
            <xsl:variable name="eventType">
                <xsl:call-template name="getEventType">
                    <xsl:with-param name="event" select="../atom:content/event:event"/>
                </xsl:call-template>
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="$eventType = $value">
                    <xsl:copy>
                        <xsl:apply-templates select="@* | node()"/>
                    </xsl:copy>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:message terminate="yes">The "type:[eventType]" category is automatically added by Cloud Feeds.  Alternative "type:[other]" categories cannot be submitted.</xsl:message>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

    <xsl:template match="atom:category[starts-with(@term,'rgn:')]">
        <xsl:if test="/atom:entry/atom:content/event:event">
            <xsl:variable name="value" select="substring-after(@term, 'rgn:')"/>
            <xsl:variable name="nsUri" select="namespace-uri(../atom:content/event:event/*)"/>
            <xsl:choose>
                <xsl:when test="$nsUri = 'http://docs.rackspace.com/usage/maas' or $nsUri = 'http://docs.rackspace.com/usage/sites/subscription' or $nsUri = 'http://docs.rackspace.com/event/domain'">
                    <xsl:choose>
                        <xsl:when test="/atom:entry/atom:content/event:event/@region = $value or 'GLOBAL' = $value">
                            <xsl:copy>
                                <xsl:apply-templates select="@* | node()"/>
                            </xsl:copy>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:message terminate="yes">The "rgn:[region]" category is automatically added by Cloud Feeds.  Alternative "rgn:[other]" categories cannot be submitted.</xsl:message>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="/atom:entry/atom:content/event:event/@region = $value">
                            <xsl:copy>
                                <xsl:apply-templates select="@* | node()"/>
                            </xsl:copy>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:message terminate="yes">The "rgn:[region]" category is automatically added by Cloud Feeds.  Alternative "rgn:[other]" categories cannot be submitted.</xsl:message>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

    <xsl:template match="atom:category[starts-with(@term,'dc:')]">
        <xsl:if test="/atom:entry/atom:content/event:event">
            <xsl:variable name="value" select="substring-after(@term, 'dc:')"/>
            <xsl:variable name="nsUri" select="namespace-uri(../atom:content/event:event/*)"/>
            <xsl:choose>
                <xsl:when test="$nsUri = 'http://docs.rackspace.com/usage/maas' or $nsUri = 'http://docs.rackspace.com/usage/sites/subscription' or $nsUri = 'http://docs.rackspace.com/event/domain'">
                    <xsl:choose>
                        <xsl:when test="/atom:entry/atom:content/event:event/@dataCenter = $value or 'GLOBAL' = $value">
                            <xsl:copy>
                                <xsl:apply-templates select="@* | node()"/>
                            </xsl:copy>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:message terminate="yes">The "dc:[dataCenter]" category is automatically added by Cloud Feeds.  Alternative "dc:[other]" categories cannot be submitted.</xsl:message>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:choose>
                        <xsl:when test="/atom:entry/atom:content/event:event/@dataCenter = $value">
                            <xsl:copy>
                                <xsl:apply-templates select="@* | node()"/>
                            </xsl:copy>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:message terminate="yes">The "dc:[dataCenter]" category is automatically added by Cloud Feeds.  Alternative "dc:[other]" categories cannot be submitted.</xsl:message>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

    <xsl:template match="atom:entry[atom:content/event:event]">
        <xsl:variable name="event" select="atom:content/event:event"/>
        <xsl:variable name="nsUri" select="namespace-uri(atom:content/event:event/*)"/>
        <xsl:copy>
            <xsl:element name="atom:id" namespace="http://www.w3.org/2005/Atom">
                <xsl:value-of select="concat('urn:uuid:',$event/@id)"/>
            </xsl:element>
            <xsl:call-template name="addCategory">
                <xsl:with-param name="term" select="$event/@tenantId"/>
                <xsl:with-param name="prefix" select="'tid:'"/>
            </xsl:call-template>
            <xsl:choose>
                <xsl:when test="$nsUri = 'http://docs.rackspace.com/usage/maas' or $nsUri = 'http://docs.rackspace.com/usage/sites/subscription' or $nsUri = 'http://docs.rackspace.com/event/domain'">
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
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="addCategory">
                        <xsl:with-param name="term" select="$event/@region"/>
                        <xsl:with-param name="prefix" select="'rgn:'"/>
                        <xsl:with-param name="default" select="''"/>
                    </xsl:call-template>
                    <xsl:call-template name="addCategory">
                        <xsl:with-param name="term" select="$event/@dataCenter"/>
                        <xsl:with-param name="prefix" select="'dc:'"/>
                        <xsl:with-param name="default" select="''"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:call-template name="addCategory">
                <xsl:with-param name="term" select="$event/@resourceId"/>
                <xsl:with-param name="prefix" select="'rid:'"/>
            </xsl:call-template>
            <xsl:call-template name="addIdCategory">
                <xsl:with-param name="event" select="$event"/>
            </xsl:call-template>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="atom:entry[atom:content/event:event]/atom:id">
        <!-- Ignore passed in ID -->
    </xsl:template>


    <!--
        A summary attribute to a product who's event is of type
        USAGE_SUMMARY.
    -->
    <xsl:template match="event:event[@type='USAGE_SUMMARY']">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="node()" mode="addSummary"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="node()" mode="addSummary">
        <xsl:copy>
            <xsl:attribute name="summary">true</xsl:attribute>
            <xsl:apply-templates select="@*[local-name() != 'summary'] | node()"/>
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

</xsl:stylesheet>
