<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:c="http://www.w3.org/ns/xproc-step"
    xmlns:sch="http://docs.rackspace.com/core/usage/schema"
    xmlns:rax="http://docs.rackspace.com/api"
    xmlns:event="http://docs.rackspace.com/core/event"
    xmlns:atom="http://www.w3.org/2005/Atom"
    xmlns="http://wadl.dev.java.net/2009/02"
    exclude-result-prefixes="sch c"
    version="2.0">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:template match="c:directory">
        <xsl:variable name="productSchemas" as="node()">
            <sch:productSchemas>
                <xsl:for-each select="c:file">
                    <xsl:copy-of select="document(concat(base-uri(),'/',@name))/sch:productSchema"/>
                </xsl:for-each>
            </sch:productSchemas>
        </xsl:variable>
        <application>
            <xsl:for-each-group select="$productSchemas//sch:productSchema" group-by="@serviceCode">
                <xsl:variable name="id" select="current-group()[1]/@serviceCode"/>
                <xsl:variable name="isUsageEvent">/atom:entry/atom:content/event:event[@type='USAGE']</xsl:variable>
                <xsl:variable name="isSnapshotEvent">/atom:entry/atom:content/event:event[@type='USAGE_SNAPSHOT']</xsl:variable>
                <xsl:variable name="usageSchema" select="current-group()[not(@isSnapshot)]" as="node()*"/>
                <xsl:variable name="snapShotSchema" select="current-group()[@isSnapshot]" as="node()*"/>
                <resource_type id="{$id}">
                    <method id="add{$id}Entry" name="POST">
                        <request>
                            <representation mediaType="application/atom+xml" element="atom:entry">
                                <xsl:if test="$usageSchema">
                                    <xsl:variable name="events" as="xs:string*">
                                        <xsl:for-each select="$usageSchema">
                                            <xsl:value-of select="concat($isUsageEvent,'/ns',position(),':product')"/>
                                        </xsl:for-each>
                                    </xsl:variable>
                                    <xsl:for-each select="$usageSchema">
                                        <xsl:namespace name="ns{position()}" select="@namespace"/>
                                    </xsl:for-each>
                                    <param name="usage" style="plain" required="true">
                                        <xsl:attribute name="path">
                                            <xsl:text>if (</xsl:text><xsl:value-of select="$isUsageEvent"/>
                                            <xsl:text>) then (</xsl:text>
                                            <xsl:value-of select='$events' separator=","/>
                                            <xsl:text>) else true()</xsl:text>
                                        </xsl:attribute>
                                    </param>
                                </xsl:if>
                                <rax:preprocess href="atom_hopper_pre.xsl"/>
                            </representation>
                        </request>
                        <!-- Okay -->
                        <response status="201">
                            <representation mediaType="application/atom+xml"/>
                        </response>
                        <!-- On Error -->
                        <response status="400 401 409 500 503">
                            <representation mediaType="application/xml"/>
                        </response>
                    </method>
                </resource_type>
            </xsl:for-each-group>
        </application>
    </xsl:template>
    <xsl:template match="text()" mode="#all"/>
</xsl:stylesheet>