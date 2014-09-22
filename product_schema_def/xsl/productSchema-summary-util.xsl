<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:sum="http://docs.rackspace.com/core/usage/schema/summary"
                xmlns:atom="http://www.w3.org/2005/Atom"
                xmlns="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="sum"
                version="2.0">
    <!--
       This file contains all the functions which define the optionality & types for attributes related to USAGE_SUMMARY.
       These functions are used to generate the product schema XSDs as well as to generate the documentation in the
       product.wadl.

       The functions for these 2 uses are collected here to aid in maintaining any changes related to USAGE_SUMMARY.  If
       the functionality for one of the formats change, say for XSDs, a corresponding update most likely needs to occur
       for the documentation.

       NOTE:  There is a conditional statement in productSchema-standalone.xsl which deals with max & min values of an
       attribute.  It is not dealt with here as it doesn't impact the documenation.
    -->
    <xsl:function name="sum:getOptionality">
        <xsl:param name="use"/>
        <xsl:param name="summary"/>
        <xsl:param name="aggregate"/>
        <xsl:param name="groupBy"/>

        <xsl:choose>
            <xsl:when test="$summary and $use = 'required'">
                <xsl:value-of select="if(($aggregate = 'NONE' or not($aggregate)) and ($groupBy = false() or not($groupBy)) ) then 'optional' else 'required'" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$use" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <xsl:function name="sum:getOptionalityDoc">
        <xsl:param name="use"/>
        <xsl:param name="summary"/>
        <xsl:param name="aggregate"/>
        <xsl:param name="groupBy"/>

        <xsl:value-of select="if(sum:getOptionality( $use, $summary, $aggregate, $groupBy) = 'required') then 'Required' else 'Optional'" />
    </xsl:function>

    <xsl:function name="sum:getTypeDoc">
        <xsl:param name="type"/>
        <xsl:param name="summary"/>
        <xsl:param name="aggregate"/>
        <xsl:value-of select="sum:getTypeHelper( $type, $summary, $aggregate, false())"/>
    </xsl:function>

    <xsl:function name="sum:getTypeXSD">
        <xsl:param name="type"/>
        <xsl:param name="summary"/>
        <xsl:param name="aggregate"/>
        <xsl:value-of select="sum:getTypeHelper( $type, $summary, $aggregate, true())"/>
    </xsl:function>

    <xsl:function name="sum:getTypeHelper">
        <xsl:param name="type"/>
        <xsl:param name="summary"/>
        <xsl:param name="aggregate"/>
        <xsl:param name="xsd"/>
        <xsl:choose>
            <xsl:when test="$summary and $aggregate = 'SUM' and $type = ('int', 'long', 'short', 'byte')">
                <xsl:choose>
                    <xsl:when test="$xsd">
                        <xsl:value-of>xsd:integer</xsl:value-of>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of>integer</xsl:value-of>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="$summary and $aggregate = 'SUM' and $type = ('unsignedInt', 'unsignedLong', 'unsignedShort', 'unsignedByte')">
                <xsl:choose>
                    <xsl:when test="$xsd">
                        <xsl:value-of>xsd:nonNegativeInteger</xsl:value-of>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of>unsignedLong</xsl:value-of>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="$summary and $aggregate = ('AVG', 'WEIGHTED_AVG')">
                <xsl:choose>
                    <xsl:when test="$xsd">
                        <xsl:value-of>xsd:double</xsl:value-of>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of>double</xsl:value-of>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="$xsd">
                        <xsl:value-of select="concat('xsd:',$type)"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$type"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>
</xsl:stylesheet>