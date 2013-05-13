<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:c="http://www.w3.org/ns/xproc-step"
    xmlns:s="http://docs.rackspace.com/core/usage/schema"
    xmlns="http://docs.rackspace.com/core/usage/schema"
    exclude-result-prefixes="c s"
    version="2.0">

    <xsl:output method="text" />

    <xsl:param name="outputBaseURI" select="'target/merged_schemas'"/>

    <xsl:template match="c:directory">
        <xsl:apply-templates select="s:sortSchemaAlts(s:unmergeSingleSchemaAlts(s:mergeRougeSchema(s:mergeAlternatives(s:mergeProductSchemas(s:collectSchemas(.))))))"
                             mode="outputSchemas"/>
    </xsl:template>

    <!--
        Collect all of the schemas in a directory and combine them together
        in a productSchemasElement
    -->
    <xsl:function name="s:collectSchemas" as="node()">
        <xsl:param name="directory" as="node()"/>
        <s:productSchemas>
            <xsl:for-each select="$directory/c:file">
                <xsl:sort select="@name"/>
                <xsl:variable name="doc" select="document(concat(base-uri(),'/',@name))"/>
                <xsl:variable name="rootName" select="local-name($doc/s:*[1])"/>
                <xsl:element name="{$rootName}">
                    <xsl:attribute name="name" select="tokenize(@name,'/')[position() = last()]"/>
                    <xsl:attribute name="gen">false</xsl:attribute>
                    <xsl:copy-of select="$doc/s:*[1]/(* | @*)"/>
                </xsl:element>
            </xsl:for-each>
        </s:productSchemas>
    </xsl:function>

    <!--
         Given a collection of product schemas, combine multiple version of the same
         schema into alternatives
    -->
    <xsl:function name="s:mergeProductSchemas" as="node()">
        <xsl:param name="productSchemas" as="node()"/>
        <s:productSchemas>
            <xsl:apply-templates select="$productSchemas/s:alternatives" mode="copy"/>
            <xsl:for-each-group select="$productSchemas/s:productSchema" group-by="@serviceCode">
                <xsl:for-each-group select="current-group()" group-by="@namespace">
                    <xsl:choose>
                        <xsl:when test="count(current-group()) &gt; 1">
                            <xsl:variable name="outFile" as="xsd:string"
                                          select="concat(lower-case(current-group()[1]/@serviceCode),'_', s:nspart(current-group()[1]),'.xml')"/>
                            <alternatives name="{$outFile}" gen="true">
                                <xsl:apply-templates select="current-group()" mode="copy"/>
                            </alternatives>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:apply-templates select="current-group()" mode="copy"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each-group>
            </xsl:for-each-group>
        </s:productSchemas>
    </xsl:function>

    <!--
        Merge existing alternatives of the same version into a single alternatives
    -->
    <xsl:function name="s:mergeAlternatives" as="node()">
        <xsl:param name="productSchemas" as="node()"/>
        <s:productSchemas>
            <xsl:apply-templates select="$productSchemas/s:productSchema" mode="copy"/>
            <xsl:for-each-group select="$productSchemas/s:alternatives" group-by="s:productSchema[1]/@namespace">
                <xsl:choose>
                    <xsl:when test="count(current-group()) &gt; 1">
                        <xsl:variable name="outFile" as="xsd:string" select="current-group()[@gen = 'false'][1]/@name"/>
                        <alternatives name="{$outFile}" gen="true">
                            <xsl:apply-templates select="current-group()//s:productSchema" mode="copy"/>
                        </alternatives>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="current-group()" mode="copy"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each-group>
        </s:productSchemas>
    </xsl:function>

    <!--
        Merge rouge single schema into existing alternatives
    -->
    <xsl:function name="s:mergeRougeSchema" as="node()">
        <xsl:param name="productSchemas" as="node()"/>
        <s:productSchemas>
            <xsl:apply-templates select="$productSchemas/s:productSchema[not(@namespace = $productSchemas/s:alternatives/s:productSchema/@namespace)]" 
                                 mode="copy"/>
            <xsl:for-each select="$productSchemas/s:alternatives">
                <xsl:variable name="alt" select="."/>
                <xsl:choose>
                    <xsl:when test="$productSchemas/s:productSchema[@namespace = $alt/s:productSchema/@namespace]">
                        <alternatives name="{@name}" gen="{@gen}">
                            <xsl:apply-templates select="node()" mode="copy"/>
                            <xsl:apply-templates select="$productSchemas/s:productSchema[@namespace = $alt/s:productSchema/@namespace]"
                                                 mode="copy"/>
                        </alternatives>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="." mode="copy"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </s:productSchemas>
    </xsl:function>

    <!--
        Unmerge alternatives that contain only one product schema
    -->
    <xsl:function name="s:unmergeSingleSchemaAlts" as="node()">
        <xsl:param name="productSchemas" as="node()"/>
        <s:productSchemas>
            <xsl:apply-templates select="$productSchemas/s:productSchema" mode="copy"/>
            <xsl:for-each select="$productSchemas/s:alternatives">
                <xsl:choose>
                    <xsl:when test="count(s:productSchema) = 1">
                        <productSchema name="{@name}" gen="{@gen}">
                            <xsl:apply-templates select="s:productSchema/@* | s:productSchema/node()" mode="copy"/>
                        </productSchema>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="." mode="copy"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </s:productSchemas>
    </xsl:function>

    <!--
        Sort schemas within alternatives
    -->
    <xsl:function name="s:sortSchemaAlts" as="node()">
        <xsl:param name="productSchemas" as="node()"/>
        <s:productSchemas>
            <xsl:apply-templates select="$productSchemas/s:productSchema" mode="copy"/>
            <xsl:for-each select="$productSchemas/s:alternatives">
                <alternatives name="{@name}" gen="{@gen}">
                    <xsl:apply-templates select="s:productSchema" mode="copy">
                        <xsl:sort select="@version"/>
                    </xsl:apply-templates>
                </alternatives>
            </xsl:for-each>
        </s:productSchemas>
    </xsl:function>

    <!--
        Computes the namespace part of the schema and returns it as a string.
    -->
    <xsl:function name="s:nspart" as="xsd:string">
        <xsl:param name="productSchema" as="node()"/>
        <xsl:value-of select="tokenize($productSchema/@namespace,'/')[position() = last()]"/>
    </xsl:function>

    <xsl:template match="node() | @*" mode="copy">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()" mode="copy"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="s:alternatives | s:productSchema" mode="outputSchemas">
        <xsl:result-document href="{concat($outputBaseURI,'/',@name)}" method="xml"
                             encoding="UTF-8" indent="yes">
            <xsl:copy>
                <xsl:apply-templates select="@* | node()" mode="outputSchemasCopy"/>
            </xsl:copy>
        </xsl:result-document>
    </xsl:template>

    <xsl:template match="node() | @*" mode="outputSchemasCopy">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()" mode="outputSchemasCopy"/>
        </xsl:copy>
    </xsl:template>

    <!-- Remove temp attributes before saving -->
    <xsl:template match="s:alternatives/@name | s:alternatives/@gen |
                         s:productSchema/@name | s:productSchema/@gen"
                  mode="outputSchemasCopy"/>
</xsl:stylesheet>
