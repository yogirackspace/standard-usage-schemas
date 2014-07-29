<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:atom="http://www.w3.org/2005/Atom"
                version="2.0">

    <xsl:param name="base_feed_name"/>

    <!--
        Identity transform.
    -->
    <xsl:template match="node() | @*">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="//atom:entry">
     <xsl:copy>
         <xsl:apply-templates select="@* | node()"/>
         <xsl:text>   </xsl:text>
         <xsl:element name="atom:link">
             <xsl:attribute name="href">
                 <xsl:value-of select="concat( concat( 'https://ord.feeds.api.rackspacecloud.com/', $base_feed_name), '/events/entries/urn:uuid:e53d007a-fc23-11e1-975c-cfa6b29bb814')" />
             </xsl:attribute>
             <xsl:attribute name="rel">self</xsl:attribute>
         </xsl:element>
         <xsl:element name="atom:updated">2013-03-01T19:42:35.507Z</xsl:element>
         <xsl:element name="atom:published">2013-03-01T19:42:35.507</xsl:element>
     </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>