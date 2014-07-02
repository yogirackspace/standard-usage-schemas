<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:httpx="http://openrepose.org/repose/httpx/v1.0"
    version="2.0">

    <!-- Import utility templates -->
    <xsl:import href="util.xsl" />

    <xsl:output method="xml"/>
    
    <xsl:param name="input-headers-uri" />
    <xsl:param name="input-request-uri" />
    <xsl:param name="output-headers-uri" />
    
    <xsl:variable name="inputHeaderDoc" select="doc($input-headers-uri)"/>
    <xsl:variable name="outputHeaderDoc" select="doc($input-headers-uri)"/>
    <xsl:variable name="requestDoc" select="doc($input-request-uri)"/>

    <xsl:template match="/">
<!--

            This prints the input documents to stderr.
            <xsl:message> ============= </xsl:message>
            <xsl:message>input-headers-uri</xsl:message>
            <xsl:message> <xsl:copy-of select="$inputHeaderDoc"/> </xsl:message>
            <xsl:message> ============= </xsl:message>
            <xsl:message>input-request-uri</xsl:message>
            <xsl:message> <xsl:copy-of select="$requestDoc"/> </xsl:message>
            <xsl:message> ============= </xsl:message>
    -->

        <xsl:apply-templates/>
        <xsl:apply-templates select="$inputHeaderDoc/*"/>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:variable name="tenantId">
        <xsl:call-template name="getTenantId">
            <xsl:with-param name="uri" select="$requestDoc/httpx:request-information/httpx:informational/httpx:path-info"/>
        </xsl:call-template>
    </xsl:variable>

    <xsl:param name="environment" select="document('/etc/feedscatalog/feedscatalog.xml')"/>

    <xsl:template name="makeExternalTenantUrl">
        <xsl:param name="url"/>
        
        <xsl:variable name="internal_host">
            
            <xsl:analyze-string select="$url" regex="(https?://[^/:]+)/.*">
                <xsl:matching-substring>
                    <xsl:value-of select="regex-group(1)"/>
                </xsl:matching-substring>
            </xsl:analyze-string>
        </xsl:variable>        
        
        <xsl:variable name="external_url">
            <xsl:value-of select="replace( $url, $internal_host, $environment/environment/vipURL/text() )"/>
        </xsl:variable>
        
        <xsl:choose>
            <xsl:when test="$tenantId != ''">
                <xsl:value-of select="replace( $external_url, '/events', concat( '/events/', $tenantId ) )"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$external_url"/>
            </xsl:otherwise>
        </xsl:choose>
        
    </xsl:template>

    <xsl:template match="@href">
                   
        <xsl:attribute name="href">
            <xsl:call-template name="makeExternalTenantUrl">
                <xsl:with-param name="url"
                                select="."></xsl:with-param>
            </xsl:call-template>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="httpx:headers">
        <xsl:result-document method="xml" include-content-type="no" href="{$output-headers-uri}">

            <headers xmlns="http://openrepose.org/repose/httpx/v1.0">
                <xsl:apply-templates/>
            </headers>
        </xsl:result-document>
    </xsl:template>

    <xsl:template match="httpx:header">
        <xsl:choose>
            <xsl:when test="lower-case( @name ) = 'link' or lower-case( @name ) = 'location'">
                <xsl:element name="httpx:header">
                    <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
                    <xsl:attribute name="value">
                      <xsl:call-template name="makeExternalTenantUrl">
                         <xsl:with-param name="url"
                                  select="@value"></xsl:with-param>
                      </xsl:call-template>
                    </xsl:attribute>
                    <xsl:attribute name="quality">1.0</xsl:attribute>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="." />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
