<!--
  This XSLT transforms the URL for a tenanted feed request into an URL which can be interpreted by atom hopper.

  E.g., http://10.14.209.232:8080/demo/events/5821027
         ->  http://10.14.209.232:8080/demo/events/?search=(cat%3Dtid:5821027)
	 
	 or

        http://10.14.209.232:8080/demo/events/5821027?search=(cat%3Drgn:ORD)
	 -> http://10.14.209.232:8080/demo/events/?search=(AND(cat%3Dtid:5821027)(cat%3Drgn:ORD))

-->
<xsl:stylesheet version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:xs="http://www.w3.org/2001/XMLSchema"
		xmlns:httpx="http://openrepose.org/repose/httpx/v1.0">
  
  <xsl:output method="xml"/>
  
  <xsl:param name="input-headers-uri" />
  <xsl:param name="input-query-uri" />
  <xsl:param name="input-request-uri" />
  <xsl:param name="output-headers-uri" />
  <xsl:param name="output-query-uri" />
  <xsl:param name="output-request-uri" />	      
  
  <xsl:variable name="headerDoc" select="doc($input-headers-uri)"/>
  <xsl:variable name="queryDoc" select="doc($input-query-uri)"/>
  <xsl:variable name="requestDoc" select="doc($input-request-uri)"/>
  
  
  <xsl:template match="/">
<!--
    
    This prints the input documents to stderr.

    <xsl:message> ============= </xsl:message>		  
    <xsl:message>input-headers-uri</xsl:message>
    <xsl:message> <xsl:copy-of select="$headerDoc"/> </xsl:message>
    <xsl:message> ============= </xsl:message>
    <xsl:message>input-request-uri</xsl:message>
    <xsl:message> <xsl:copy-of select="$requestDoc"/> </xsl:message>
    <xsl:message> ============= </xsl:message>
    <xsl:message>input-query-uri</xsl:message>
    <xsl:message> <xsl:copy-of select="$queryDoc"/> </xsl:message>
    <xsl:message> ============= </xsl:message>              
-->
<xsl:copy-of select="."/>
<xsl:apply-templates select="$headerDoc/*"/>
<xsl:apply-templates select="$queryDoc/*"/>
<xsl:apply-templates select="$requestDoc/*" />
  </xsl:template>
  
  <xsl:variable name="tenantId">
    <xsl:call-template name="getTenantId">
      <xsl:with-param name="uri"
		      select="$requestDoc/httpx:request-information/httpx:uri"/>            
    </xsl:call-template>
  </xsl:variable>
  
  <xsl:template name="getTenantId">
    <xsl:param name="uri"/>        
    <xsl:analyze-string select="$uri"
			regex="(.*/events/)([^/?]+)/?(\?.*)?">
      <xsl:matching-substring>
	<xsl:value-of select="regex-group(2)"/>
      </xsl:matching-substring>
    </xsl:analyze-string>                      
  </xsl:template>
  
  <xsl:template name="parseURI">
    <xsl:param name="uri"/>
    <xsl:analyze-string select="$uri"
			regex="(.*/events/)([^/?]+)/?(\?.*)?">
      <xsl:matching-substring>
	
	<xsl:value-of select="regex-group(1)"/>
      </xsl:matching-substring>                
    </xsl:analyze-string>
  </xsl:template>
  
  <xsl:template match="httpx:request-information">
    
    <xsl:result-document method="xml" include-content-type="no" href="{$output-request-uri}">
      
      <request-information xmlns="http://openrepose.org/repose/httpx/v1.0">
	<uri>
	  <xsl:call-template name="parseURI">
	    <xsl:with-param name="uri"
			    select="httpx:uri"/>
	  </xsl:call-template>                
	</uri> 
	
	<url>
	  <xsl:call-template name="parseURI">
	    <xsl:with-param name="uri"
			    select="httpx:uri"/>
	  </xsl:call-template>                
	</url> 
	
      </request-information>
    </xsl:result-document>
  </xsl:template>
  
  <xsl:template match="httpx:parameters">
    
    <xsl:result-document method="xml" include-content-type="no" href="repose:output:query.xml">
      <parameters
	  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
	  xmlns='http://openrepose.org/repose/httpx/v1.0'>
	<xsl:apply-templates/>    
	<xsl:if test="not( httpx:parameter[@name='search'] )">
	  <parameter name="search">
            <xsl:attribute name="value">(cat=tid:<xsl:value-of select="$tenantId"/>)</xsl:attribute>
	  </parameter>
	</xsl:if>
      </parameters>
    </xsl:result-document>
  </xsl:template>
  
  <xsl:template match="httpx:parameter">
    <xsl:if test="@name = 'search'">
      <xsl:element name="httpx:parameter">
	<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
	<xsl:attribute name="value">
	  <xsl:text>(AND(cat=tid:</xsl:text>
	  <xsl:value-of select="$tenantId"/>
	  <xsl:text>)</xsl:text>
	  <xsl:value-of select="@value"/>
	  <xsl:text>)</xsl:text>
	</xsl:attribute>
      </xsl:element>
    </xsl:if>            
    <xsl:if test="@name != 'search'">
      <xsl:element name="httpx:parameter">
	<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
	<xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
      </xsl:element>
    </xsl:if>            
  </xsl:template>
  
</xsl:stylesheet>
