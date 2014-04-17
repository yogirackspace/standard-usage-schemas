<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:html="http://www.w3.org/1999/xhtml"
                xmlns:schema="http://docs.rackspace.com/core/usage/schema"
                xmlns:usage="http://docs.rackspace.com/core/usage"
                xmlns:xerces="http://xerces.apache.org"
                xmlns:saxon="http://saxon.sf.net/"
                xmlns:vc="http://www.w3.org/2007/XMLSchema-versioning"
                xmlns:atom="http://www.w3.org/2005/Atom"
                xmlns="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="schema"
                version="2.0">

    <xsl:import href="../../wadl/util.xsl" />

    <xsl:param name="base_feed_name"/>

    <xsl:output method="xml" indent="yes"/>
    
    <xsl:variable name="MAX_STRING">255</xsl:variable>
    <xsl:variable name="MAX_OCCURS_VALUE">5000</xsl:variable>

    <xsl:template match="//schema:productSchema">
        <xsl:variable name="product_namespace">
            <xsl:value-of select="@namespace"/>
        </xsl:variable>
      
        <xsl:variable name="nsPart">
            <xsl:call-template name="getNSPart">
                <xsl:with-param name="ns" select="$product_namespace"/>
            </xsl:call-template>
        </xsl:variable>

        <!--
           I was unable to access these attributes as a list, yet when I pass them into lower-case() or tokenize()
           exceptions are thrown claiming they are a list.  This is a hack ensure that I can access the first item.
        -->
        <xsl:variable name="sample_type" select="if (@type) then tokenize( string-join(@type, ' '), '\s+' )[1] else 'USAGE'"/>
        <xsl:variable name="resource_type" select="if (@resourceTypes) then tokenize( string-join(@resourceTypes, ' '), '\s+' )[1] else false()"/>

        <xsl:variable name="file_name">
    	  <xsl:choose>
	       <xsl:when test="$resource_type">message_samples/<xsl:value-of select="$base_feed_name"/>/<xsl:value-of select="lower-case(@serviceCode)"/>-<xsl:value-of select="$nsPart"/>-<xsl:value-of select="lower-case( $resource_type )"/>-<xsl:value-of select="lower-case($sample_type)"/>-v<xsl:value-of select="@version"/>-entry.xml</xsl:when>
	        <xsl:otherwise>message_samples/<xsl:value-of select="$base_feed_name"/>/<xsl:value-of select="lower-case(@serviceCode)"/>-<xsl:value-of select="$nsPart"/>-<xsl:value-of select="lower-case($sample_type)"/>-v<xsl:value-of select="@version"/>-entry.xml</xsl:otherwise>
    	  </xsl:choose>
	    </xsl:variable>

        <xsl:result-document href="{$file_name}">
            <xsl:processing-instruction name="atom">
            feed="<xsl:value-of select="$base_feed_name"/>/events"</xsl:processing-instruction>
            <xsl:text>&#x0a;</xsl:text>
            <xsl:comment>
    This example has been generated using
    'mvn -P generate-samples clean generate-sources -DproductSchema=<xsl:value-of select="base-uri()"/> -DfeedName=<xsl:value-of select="$base_feed_name"/>' call.
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

                <atom:title><xsl:value-of select="@serviceCode"/></atom:title>
                <atom:content type="application/xml">
                    <event xmlns="http://docs.rackspace.com/core/event">
                        <xsl:namespace name="sample" select="$product_namespace"/>
                        <xsl:attribute name="id">e53d007a-fc23-11e1-975c-cfa6b29bb814</xsl:attribute>
                        <xsl:attribute name="version"><xsl:value-of select="@version"/></xsl:attribute>
                        <!-- defaulting to start/end time for usage -->

                        <xsl:if test="@resourceTypes">
                            <xsl:attribute name="resourceId">4a2b42f4-6c63-11e1-815b-7fcbcf67f549</xsl:attribute>
                        </xsl:if>
                        <xsl:if test="$sample_type = 'USAGE' or $sample_type = 'USAGE_SNAPSHOT'">
                            <xsl:attribute name="tenantId">1234</xsl:attribute>
                        </xsl:if>

                        <xsl:choose>
                            <xsl:when test="$sample_type = 'USAGE'">
                                <xsl:attribute name="startTime">2013-03-15T11:51:11Z</xsl:attribute>
                                <xsl:attribute name="endTime">2013-03-16T11:51:11Z</xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:attribute name="eventTime">2013-03-15T11:51:11Z</xsl:attribute>
                            </xsl:otherwise>
                        </xsl:choose>
                        <xsl:attribute name="type"><xsl:value-of select="$sample_type"/></xsl:attribute>
                        <xsl:attribute name="dataCenter">DFW1</xsl:attribute>
                        <xsl:attribute name="region">DFW</xsl:attribute>
                        <xsl:element name="sample:product" namespace="{$product_namespace}">
                            <xsl:attribute name="serviceCode"><xsl:value-of select="@serviceCode"></xsl:value-of></xsl:attribute>
                            <xsl:attribute name="version"><xsl:value-of select="@version"/></xsl:attribute>
			    <xsl:if test="$resource_type">
			      <xsl:attribute name="resourceType"><xsl:value-of select="$resource_type"/></xsl:attribute>
			    </xsl:if>
                            <xsl:apply-templates select="schema:attribute[@use='required']"/>
                            <xsl:apply-templates select="schema:attributeGroup">
                                <xsl:with-param name="namespace" select="$product_namespace"/>
                            </xsl:apply-templates>
                        </xsl:element>            
                    </event>
                </atom:content>                
        </atom:entry>
        </xsl:result-document>
     </xsl:template>

    <xsl:template name="listValue">
        <xsl:param name="sampleValue"/>
        <xsl:variable name="singleItem">
            <xsl:call-template name="getValue">
                <xsl:with-param name="defaultValue" select="$sampleValue"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="$singleItem"/> <xsl:text> </xsl:text> <xsl:value-of select="$singleItem"/>
    </xsl:template>

    <xsl:template name="listIntValue">
        <xsl:param name="sampleValue"/>
        <xsl:variable name="singleItem">
            <xsl:call-template name="getIntValue">
                <xsl:with-param name="defaultValue" select="$sampleValue"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:value-of select="$singleItem"/> <xsl:text> </xsl:text> <xsl:value-of select="$singleItem"/>
    </xsl:template>

    <xsl:template name="getIntValue">
        <xsl:param name="defaultValue"/>
        <xsl:choose>
            <xsl:when test="@fixed">
                <xsl:value-of select="format-number( @fixed, '#' )"/>
            </xsl:when>
            <xsl:when test="@min">
                <xsl:value-of select="format-number( @min, '#' )"/>
            </xsl:when>
            <xsl:when test="@max">
                <xsl:value-of select="format-number( @max, '#' )"/>
            </xsl:when>
            <xsl:when test="@allowedValues">
                <xsl:value-of select="tokenize( string-join( @allowedValues, ' ' ), '\s+')[1]"/>
            </xsl:when>
            <xsl:when test="@maxLength or @minLength">
                <xsl:variable name="minLength" select="if (@minLength) then max( (@minLength, 1) ) else 1"/>
                <xsl:variable name="finalLength" select="if (@maxLength) then min( (@maxLength, $minLength) ) else $minLength"/>
                <xsl:value-of select="substring( 'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz', 1, $finalLength )"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$defaultValue"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="getValue">
        <xsl:param name="defaultValue"/>
        <xsl:choose>
            <xsl:when test="@fixed">
                <xsl:value-of select="@fixed"/>
            </xsl:when>
            <xsl:when test="@min">
                <xsl:value-of select="@min"/>
            </xsl:when>
            <xsl:when test="@max">
                <xsl:value-of select="@max"/>
            </xsl:when>
            <xsl:when test="@allowedValues">
                <xsl:value-of select="tokenize( string-join( @allowedValues, ' ' ), '\s+')[1]"/>
            </xsl:when>            
            <xsl:when test="@maxLength or @minLength">
                <xsl:variable name="minLength" select="if (@minLength) then max( (@minLength, 1) ) else 1"/>                   
                <xsl:variable name="finalLength" select="if (@maxLength) then min( (@maxLength, $minLength) ) else $minLength"/>  
                <xsl:value-of select="substring( 'abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz', 1, $finalLength )"/>                
            </xsl:when>            
            <xsl:otherwise>
                <xsl:value-of select="$defaultValue"/>
            </xsl:otherwise>
        </xsl:choose>        
    </xsl:template>
    
    <xsl:template name="getOccurs">
        <xsl:param name="attributeGroup"/>
        <xsl:variable name="minOccurs" select=" if (@minOccurs) then max( (@minOccurs, 1) )  else 1"/>
        <xsl:value-of select="if (@maxOccurs and xsd:string(@maxOccurs) != 'unbounded') then min( ($minOccurs, @maxOccurs) ) else $minOccurs"/>
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
                <xsl:apply-templates select="$current_node/schema:attribute[@use='required']"/>
            </xsl:element>
       </xsl:for-each> 
    </xsl:template>
    
    <xsl:template match="schema:attribute[@use='required']">
        <xsl:variable name="type" select="@type[1]"/>
        
        <xsl:attribute name="{@name}">
            <xsl:choose>                 
                <xsl:when test="$type = 'string'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'sampleString'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'Name'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'sample_Name'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'Name*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'sample_Name'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'string*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'sampleString'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'integer'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'1'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'long'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'11'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'int'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'1'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'short'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'2'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'byte'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'3'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'unsignedLong'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'44'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'unsignedInt'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'111222'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'unsignedShort'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'222'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'unsignedByte'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'333'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'double'">
                    <xsl:call-template name="getIntValue">
                        <xsl:with-param name="defaultValue" select="'55555'"/>
                    </xsl:call-template>                    
                </xsl:when>
                <xsl:when test="$type = 'float'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'6.66'"/>
                    </xsl:call-template>     
                </xsl:when>
                <xsl:when test="$type = 'integer*'">
                    <xsl:call-template name="listIntValue">
                        <xsl:with-param name="sampleValue" select="'1'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'long*'">
                    <xsl:call-template name="listIntValue">
                        <xsl:with-param name="sampleValue" select="'11'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'int*'">
                    <xsl:call-template name="listIntValue">
                        <xsl:with-param name="sampleValue" select="'1'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'short*'">
                    <xsl:call-template name="listIntValue">
                        <xsl:with-param name="sampleValue" select="'2'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'byte*'">
                    <xsl:call-template name="listIntValue">
                        <xsl:with-param name="sampleValue" select="'3'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'unsignedLong*'">
                    <xsl:call-template name="listIntValue">
                        <xsl:with-param name="sampleValue" select="'44'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'unsignedInt*'">
                    <xsl:call-template name="listIntValue">
                        <xsl:with-param name="sampleValue" select="'111222'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'unsignedShort*'">
                    <xsl:call-template name="listIntValue">
                        <xsl:with-param name="sampleValue" select="'222'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'unsignedByte*'">
                    <xsl:call-template name="listIntValue">
                        <xsl:with-param name="sampleValue" select="'333'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'double*'">
                    <xsl:call-template name="listIntValue">
                        <xsl:with-param name="sampleValue" select="'55555'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'float*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'6.66'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'boolean'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'true'"/>
                    </xsl:call-template>   
                </xsl:when>
                <xsl:when test="$type = 'boolean*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'true'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'duration'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'P3Y7M2DT8H32M12S'"/>
                    </xsl:call-template>                       
                </xsl:when>
                <xsl:when test="$type = 'date'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'2013-09-26'"/>
                    </xsl:call-template> 
                </xsl:when>
                <xsl:when test="$type = 'dateTime'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'2013-09-26T15:32:00-05:00'"/>
                    </xsl:call-template> 
                </xsl:when>
                <xsl:when test="$type = 'utcDateTime'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'2013-09-26T15:32:00Z'"/>
                    </xsl:call-template> 
                </xsl:when>
                <xsl:when test="$type = 'time'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'15:32:00-05:00'"/>
                    </xsl:call-template>                     
                </xsl:when>
                <xsl:when test="$type = 'utcTime'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'15:32:00Z'"/>
                    </xsl:call-template>  
                </xsl:when>
                <xsl:when test="$type = 'duration*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'P4Y8M2DT8H32M12S'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'date*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'2013-09-26'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'dateTime*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'2013-09-26 2013-10-26'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'utcDateTime*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'2013-09-26T15:32:00Z 2013-09-27T15:32:00Z'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'time*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'15:32:00-05:00'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'utcTime*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'15:32:00Z'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'gDay*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'---02'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'gMonth*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'--04'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'gYear*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'2013-5:00 2012'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'gMonthDay*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'--04-12'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'gMonthYear*'">
                    <xsl:text>2013-07 2013-10</xsl:text>
                </xsl:when>
                <xsl:when test="$type = 'base64Binary'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'BASE64BINARYSTRING'"/>
                    </xsl:call-template>           
                </xsl:when>
                <xsl:when test="$type = 'anyURI'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'http://rackspace.com'"/>
                    </xsl:call-template>  
                </xsl:when>
                <xsl:when test="$type = 'UUID'">
                    <xsl:call-template name="getValue">
                        <xsl:with-param name="defaultValue" select="'6e8bc430-9c3a-11d9-9669-0800200c9a66'"/>
                    </xsl:call-template>  
                </xsl:when>
                <xsl:when test="$type = 'anyURI*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'http://www.rackspace.com'"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:when test="$type = 'UUID*'">
                    <xsl:call-template name="listValue">
                        <xsl:with-param name="sampleValue" select="'6e8bc430-9c3a-11d9-9669-0800200c9a66'"/>
                    </xsl:call-template>
                </xsl:when>
            </xsl:choose>
        </xsl:attribute>                          
    </xsl:template>

</xsl:stylesheet>
