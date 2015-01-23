<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:schema="http://docs.rackspace.com/core/usage/schema"
                xmlns:atom="http://www.w3.org/2005/Atom"
                xmlns="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="schema"
                version="2.0">

    <xsl:import href="../../wadl/util.xsl" />
    <xsl:import href="productSchema-util.xsl" />

    <xsl:param name="base_feed_name"/>
    <xsl:param name="input_path"/>

    <xsl:output method="xml" indent="yes"/>

    <xsl:variable name="MAX_STRING">255</xsl:variable>
    <xsl:variable name="MAX_OCCURS_VALUE">5000</xsl:variable>

    <xsl:template match="//schema:productSchema[@type and not( contains( string(@type), 'USAGE' ) ) and not( contains( string(@type), 'USAGE_SNAPSHOT')) ]"/>

    <xsl:template match="//schema:productSchema[not(@type) or contains( string(@type), 'USAGE') or contains( string(@type), 'USAGE_SNAPSHOT')]">
        <xsl:variable name="product_namespace">
            <xsl:value-of select="@namespace"/>
        </xsl:variable>

        <xsl:variable name="nsPart">
            <xsl:call-template name="getNSPart">
                <xsl:with-param name="ns" select="$product_namespace"/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:variable name="sample_type">
            <xsl:call-template name="getSampleType"/>
        </xsl:variable>

        <xsl:variable name="resource_type">
            <xsl:call-template name="getResourceType"/>
        </xsl:variable>

        <xsl:variable name="file_name">
            <xsl:call-template name="getFileName">
                <xsl:with-param name="resource_type" select="$resource_type"/>
                <xsl:with-param name="base_feed_name" select="$base_feed_name"/>
                <xsl:with-param name="nsPart" select="$nsPart"/>
                <xsl:with-param name="sample_type" select="$sample_type"/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:result-document href="{$file_name}-summary-entry.xml">
            <xsl:processing-instruction name="atom">
                feed="usagesummary/<xsl:value-of select="$base_feed_name"/>/events"</xsl:processing-instruction>
            <xsl:text>&#x0a;</xsl:text>
            <xsl:comment>
    This example summary entry has been generated using
    'mvn -P generate-samples clean generate-sources -DproductSchema=<xsl:value-of select="$input_path"/> -DfeedName=<xsl:value-of select="$base_feed_name"/>' call.
    Some assumptions have been made when generating this and might not be correct.  Manual modification
    might be required for the unit tests to pass.

    The assumptions:

      - If the productSchema requires a 'resourceId' attribute, its value is set to
        '4a2b42f4-6c63-11e1-815b-7fcbcf67f549'.
      - If the productSchema has &lt;xpathAssertion&gt; nodes, the assertions might not be satisfied
        by the generated content.
      - No optional nodes or attributes are generated.
      - Does not process the 'withEventType' and 'withResource' attributes.
            </xsl:comment>
            <xsl:text>&#x0a;</xsl:text>
            <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                <atom:title><xsl:value-of select="@serviceCode"/> Summary</atom:title>
                <atom:content type="application/xml">
                    <event xmlns="http://docs.rackspace.com/core/event">
                        <xsl:namespace name="sample" select="$product_namespace"/>
                        <xsl:attribute name="id">e53d007a-fc23-11e1-975c-cfa6b29bb814</xsl:attribute>
                        <xsl:attribute name="version">2</xsl:attribute>

                        <xsl:if test="@resourceTypes">
                            <xsl:attribute name="resourceId">4a2b42f4-6c63-11e1-815b-7fcbcf67f549</xsl:attribute>
                        </xsl:if>
    
                        <xsl:attribute name="tenantId">123456</xsl:attribute>
                        <xsl:attribute name="rackspaceAccountNumber">020-123456</xsl:attribute>
                        <xsl:attribute name="startTime">2013-03-15T11:51:11Z</xsl:attribute>
                        <xsl:attribute name="endTime">2013-03-16T00:00:00Z</xsl:attribute>
                        <xsl:attribute name="duration">PT20M</xsl:attribute>
                        <xsl:attribute name="type">USAGE_SUMMARY</xsl:attribute>
                        <xsl:attribute name="dataCenter">DFW1</xsl:attribute>
                        <xsl:attribute name="region">DFW</xsl:attribute>

                        <xsl:element name="sample:product" namespace="{$product_namespace}">
                            <xsl:attribute name="serviceCode"><xsl:value-of select="@serviceCode"></xsl:value-of></xsl:attribute>
                            <xsl:attribute name="version"><xsl:value-of select="@version"/></xsl:attribute>
                            <xsl:if test="$resource_type!=''">
                                <xsl:attribute name="resourceType"><xsl:value-of select="$resource_type"/></xsl:attribute>
                            </xsl:if>
                            <xsl:apply-templates select="schema:attribute[not(@groupBy=false() and @aggregateFunction='NONE') and (@use='required' or @use='synthesized')]"/>
                            <xsl:apply-templates select="schema:attributeGroup">
                                <xsl:with-param name="namespace" select="$product_namespace"/>
                            </xsl:apply-templates>
                        </xsl:element>
                    </event>
                </atom:content>
            </atom:entry>
        </xsl:result-document>
    </xsl:template>

    <xsl:template match="schema:attributeGroup">
        <xsl:param name="namespace"/>
        <xsl:variable name="occurs">
            <xsl:call-template name="getOccurs">
                <xsl:with-param name="attributeGroup" select="."/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="current_node" select="."/>
        <xsl:for-each select="1 to $occurs">
            <xsl:element name="sample:{$current_node/@name}" namespace="{$namespace}">
                <xsl:apply-templates select="$current_node/schema:attribute[not(@groupBy=false() and @aggregateFunction='NONE') and @use='required']"/>
            </xsl:element>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="schema:attribute[not(@groupBy=false() and @aggregateFunction='NONE') and  (@use='required' or @use='synthesized')]">
        <xsl:variable name="type" select="@type[1]"/>
        
        <xsl:attribute name="{@name}">
            <xsl:choose>
                <xsl:when test="@aggregateFunction='SUM'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'1000'"/>
                        <xsl:with-param name="noMinMax" select="true()"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="@aggregateFunction=('AVG','WEIGHTED_AVG')">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'1'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="getValueByType">
                        <xsl:with-param name="type" select="$type"/>
                    </xsl:call-template>
                </xsl:otherwise>                
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
</xsl:stylesheet>
