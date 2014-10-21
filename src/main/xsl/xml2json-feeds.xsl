<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:cldfeeds="http://docs.rackspace.com/api/cloudfeeds"
    xmlns:cf-nsattrs="http://docs.rackspace.com/api/cloudfeeds/non-string-attrs"
    xmlns:saxon="http://saxon.sf.net/"
    exclude-result-prefixes="cldfeeds">
    
    <xsl:output method="text" encoding="utf-8"/>
 
    <!-- we always put these nodes in an array, even though they may only
         have one element -->
    <xsl:variable name="arrayNodesInEntry" select="tokenize('category link', ' ')"/>
    <xsl:variable name="printNamespaceOnNodes" select="tokenize('event product', ' ')"/>
    
    <!-- this nonStringAttrs.xsl is generated -->
    <xsl:include href="nonStringAttrs.xsl"/>
    
    <xsl:template match="/*[node()]">
        <xsl:text>{</xsl:text>
        <xsl:apply-templates select="." mode="detect" />
        <xsl:text>}</xsl:text>
    </xsl:template>
 
    <xsl:template match="*" mode="detect">
        <xsl:choose>
            <xsl:when test="name(preceding-sibling::*[1]) = name(current()) and name(following-sibling::*[1]) != name(current())">
                <xsl:apply-templates select="." mode="obj-content" />
                <xsl:text>]</xsl:text>
                <xsl:if test="count(following-sibling::*[name() != name(current())]) &gt; 0">, </xsl:if>
            </xsl:when>
            <xsl:when test="name(preceding-sibling::*[1]) = name(current())">
                <xsl:apply-templates select="." mode="obj-content" />
                <xsl:if test="name(following-sibling::*[1]) = name(current())">, </xsl:if>
            </xsl:when>
            <xsl:when test="exists(index-of($arrayNodesInEntry, local-name(current()))) or following-sibling::*[1][name() = name(current())]">
                <xsl:text>"</xsl:text><xsl:value-of select="local-name()"/><xsl:text>" : [</xsl:text>
                <xsl:apply-templates select="." mode="obj-content" />
                <xsl:if test="exists(index-of($arrayNodesInEntry, local-name(current()))) and following-sibling::*[1][name() != name(current())]"><xsl:text>]</xsl:text></xsl:if>
                <xsl:text>, </xsl:text>
            </xsl:when>
            <xsl:when test="count(./child::*) > 0 or count(@*) > 0">
                <xsl:text>"</xsl:text><xsl:value-of select="local-name()"/>" : <xsl:apply-templates select="." mode="obj-content" />
                <xsl:if test="count(following-sibling::*) &gt; 0">, </xsl:if>
            </xsl:when>
            <xsl:when test="count(./child::*) = 0">
                <xsl:text>"</xsl:text><xsl:value-of select="local-name()"/>" : "<xsl:apply-templates select="."/><xsl:text>"</xsl:text>
                <xsl:if test="count(following-sibling::*) &gt; 0">, </xsl:if>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
 
    <xsl:template match="*" mode="obj-content">
        <xsl:text>{</xsl:text>
            <xsl:if test="exists(index-of($printNamespaceOnNodes, local-name()))"><xsl:text>"@type": "</xsl:text><xsl:value-of select="namespace-uri()"/><xsl:text>", </xsl:text></xsl:if>
            <xsl:choose>
                <xsl:when test="exists(index-of($printNamespaceOnNodes, local-name()))">
                    <xsl:variable name="namespace" select="string(namespace-uri())" as="xs:string"/>
                    <xsl:variable name="version"   select="@version"                as="xs:string"/>

                    <xsl:variable name="nonStringAttrs"
                        select="tokenize(
                                  $nonStringAttrsList/cf-nsattrs:schema[string(@key) = $namespace and string(@version) = $version]/cf-nsattrs:attributes/text(),
                                  ',')"/>
                    <xsl:apply-templates select="@*" mode="raxAttr">
                        <xsl:with-param name="namespace" select="$namespace"/>
                        <xsl:with-param name="version" select="$version"/>
                        <xsl:with-param name="nonStringAttrs" select="$nonStringAttrs"/>
                    </xsl:apply-templates>    
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="@*" mode="normalAttr" />    
                </xsl:otherwise>
            </xsl:choose>

            <xsl:if test="count(@*) &gt; 0 and (count(child::*) &gt; 0 or text())">, </xsl:if>
            <xsl:apply-templates select="./*" mode="detect" />
            <xsl:if test="count(child::*) = 0 and text() and not(@*)">
                <xsl:text>"</xsl:text><xsl:value-of select="local-name()"/>" : "<xsl:value-of select="text()"/><xsl:text>"</xsl:text>
            </xsl:if>
        
            <!-- handles element that has both text child node and attributes -->
            <xsl:if test="count(child::*) = 0 and text() and @*">
                <xsl:text>"@text" : "</xsl:text>
                <xsl:call-template name="removeBreaks">
                    <xsl:with-param name="pText" select="text()"/>
                </xsl:call-template>
                <xsl:text>"</xsl:text>
            </xsl:if>
        <xsl:text>}</xsl:text>
        <xsl:if test="position() &lt; last()">, </xsl:if>
    </xsl:template>

    <xsl:template match="@*" mode="normalAttr">
        <xsl:text>"</xsl:text><xsl:value-of select="name()"/>" : "<xsl:value-of select="."/><xsl:text>"</xsl:text>
        <xsl:if test="position() &lt; last()">,</xsl:if>
    </xsl:template>

    <xsl:template match="@*" mode="raxAttr">
        <xsl:param name="namespace"/>
        <xsl:param name="version"/>
        <xsl:param name="nonStringAttrs"/>
        
        <xsl:text>"</xsl:text><xsl:value-of select="name()"/><xsl:text>" : </xsl:text>

        <xsl:variable name="mypath">
            <xsl:call-template name="buildPathToProduct"/>
        </xsl:variable>

        <xsl:variable name="isNonString" as="xs:boolean">
            <xsl:value-of select="cldfeeds:isNonString($mypath, $namespace, $version, $nonStringAttrs)"></xsl:value-of>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$isNonString">
                <xsl:value-of select="."/>    
            </xsl:when>
            <xsl:otherwise>
                 <xsl:text>"</xsl:text><xsl:value-of select="."/><xsl:text>"</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="position() &lt; last()">,</xsl:if>
    </xsl:template>
    
    <xsl:template match="node/@TEXT | text()" name="removeBreaks">
        <xsl:param name="pText" select="normalize-space(.)"/>
        <xsl:choose>
            <xsl:when test="not(contains($pText, '&#xA;'))"><xsl:copy-of select="$pText"/></xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="normalize-space(translate(translate($pText, '&#xD;',''),'&#xA;', ''))"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- This function determines if the attribute of a particular element with
         a particular 'namespace' is defined in the product schemas as numeric
         or boolean type (i.e: non-string). This is important in JSON because
         numeric and boolean are not quoted.
    -->
    <xsl:function name="cldfeeds:isNonString" as="xs:boolean">
        <xsl:param name="attributePath"/>
        <xsl:param name="namespace"/>
        <xsl:param name="version"/>
        <xsl:param name="nonStringAttrs"/>
        
        <xsl:choose>
            <xsl:when test="$attributePath ne '' and exists($nonStringAttrs)">
                <xsl:value-of select="exists(index-of($nonStringAttrs, $attributePath))"/>                
            </xsl:when> 
            <xsl:otherwise>
                <xsl:value-of select="false()"/>    
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>
    
    <xsl:template match="@*" name="buildPathToProduct">
        <!-- This is not perfect because it assumes that we only have 2 levels elements:
               *) product
               *) product/attributeGroup
             To make this work for any levels, I would have to recursively build the path.
             I'm not sure it's worth doing at this point.
             
             This assumption needs to be held the same in the XSLT that generates the
             list of non-string attributes.
        -->
        <!-- <xsl:for-each select="ancestor-or-self::*"> -->
            <xsl:if test="parent::node()/local-name() eq 'product'">
                <xsl:value-of select="concat('product/@', local-name())"/>
            </xsl:if>
            <xsl:if test="parent::node()/parent::node()/local-name() eq 'product'">
                <xsl:value-of select="concat(parent::node()/local-name(), '@', local-name())"/>
            </xsl:if>
        <!--</xsl:for-each> -->
    </xsl:template>
</xsl:stylesheet>
