<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:schema="http://docs.rackspace.com/core/usage/schema"
                xmlns:atom="http://www.w3.org/2005/Atom"
                xmlns="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="schema"
                version="2.0">

    <!--
        I was unable to access these attributes as a list, yet when I pass them into lower-case() or tokenize()
        exceptions are thrown claiming they are a list.  This is a hack ensure that I can access the first item.
    -->
    <xsl:template name="getSampleType">
        <xsl:value-of select="if (@type) then tokenize( string-join(@type, ' '), '\s+' )[1] else 'USAGE'"/>
    </xsl:template>

    <xsl:template name="getResourceType">
        <xsl:value-of select="if (@resourceTypes) then tokenize( string-join(@resourceTypes, ' '), '\s+' )[1] else ''"/>
    </xsl:template>

    <xsl:template name="getFileName">
        <xsl:param name="resource_type"/>
        <xsl:param name="base_feed_name"/>
        <xsl:param name="nsPart"/>
        <xsl:param name="sample_type"/>

        <xsl:choose>
            <xsl:when test="$resource_type!=''">message_samples/<xsl:value-of select="$base_feed_name"/>/xml/<xsl:value-of select="lower-case(@serviceCode)"/>-<xsl:value-of select="$nsPart"/>-<xsl:value-of select="lower-case( $resource_type )"/>-<xsl:value-of select="lower-case($sample_type)"/>-v<xsl:value-of select="@version"/></xsl:when>
            <xsl:otherwise>message_samples/<xsl:value-of select="$base_feed_name"/>/xml/<xsl:value-of select="lower-case(@serviceCode)"/>-<xsl:value-of select="$nsPart"/>-<xsl:value-of select="lower-case($sample_type)"/>-v<xsl:value-of select="@version"/></xsl:otherwise>
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
        <xsl:param name="noMinMax" select="false()"/>
        <xsl:choose>
            <xsl:when test="@fixed">
                <xsl:value-of select="format-number( @fixed, '#' )"/>
            </xsl:when>
            <xsl:when test="@min and not($noMinMax)">
                <xsl:value-of select="format-number( @min, '#' )"/>
            </xsl:when>
            <xsl:when test="@max and not($noMinMax)">
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

    <xsl:template name="getValueByType">
        <xsl:param name="type"/>
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
    </xsl:template>

    <xsl:template name="getOccurs">
        <xsl:param name="attributeGroup"/>
        <xsl:variable name="minOccurs" select=" if (@minOccurs) then max( (@minOccurs, 1) )  else 1"/>
        <xsl:value-of select="if (@maxOccurs and xsd:string(@maxOccurs) != 'unbounded') then min( ($minOccurs, @maxOccurs) ) else $minOccurs"/>
    </xsl:template>

</xsl:stylesheet>