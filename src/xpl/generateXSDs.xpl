<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
    xmlns:psch="http://docs.rackspace.com/core/usage/schema"
    xmlns:c="http://www.w3.org/ns/xproc-step" version="1.0">
    <p:input port="source">
        <p:empty/>
    </p:input>
    <p:input port="parameters" kind="parameter"/>
    <p:import href="../../product_schema_def/xpl/productSchema.xpl"/>
    <p:directory-list path="../../sample_product_schemas" name="schemas">
        <p:with-option name="include-filter" select="'.*xml$'"/>
    </p:directory-list>
    <!-- Validate all product schemas stop processing if there is an error -->
    <p:for-each>
        <p:iteration-source select="//c:file"/>
        <p:load>
            <p:with-option name="href" select="concat('../../sample_product_schemas/',//c:file/@name)"/>
        </p:load>
        <p:try>
            <p:group>
                <p:validate-with-xml-schema assert-valid="true" mode="strict" name="validate">
                    <p:input port="schema">
                        <p:document href="../../product_schema_def/xsd/productSchema.xsd"></p:document>
                    </p:input>
                </p:validate-with-xml-schema>
            </p:group>
            <p:catch name="catch">
                <!-- TODO:  Want to list validation error filename/line number -->
                <p:error code="psch:Invalid">
                    <p:input port="source">
                        <p:pipe port="error" step="catch"/>
                    </p:input>
                </p:error>
            </p:catch>
        </p:try>
    </p:for-each>
    <p:sink/>
    <!-- Remove old generated files -->
    <p:directory-list path="../../generated_product_xsds" name="oldSchemas">
        <p:with-option name="include-filter" select="'.*xsd$'"/>
    </p:directory-list>
    <p:for-each>
        <p:iteration-source select="//c:file"/>
        <p:exec command="rm" result-is-xml="false" source-is-xml="false" errors-is-xml="false">
            <p:with-option name="args" select="concat('../../generated_product_xsds/',//c:file/@name)"/>
            <p:input port="source">
                <p:empty/>
            </p:input>
        </p:exec>
    </p:for-each>
    <p:sink/>
    <!-- Merge alternative schemas into single files and translate them to XSDs -->
    <p:xslt name="genXSDs">
        <p:input port="stylesheet">
            <p:document href="../../product_schema_def/xsl/productSchema-alternatives.xsl"/>
        </p:input>
        <p:input port="source">
            <p:pipe port="result" step="schemas"/>
        </p:input>
        <p:with-param name="outputBaseURI" select="'../../generated_product_xsds'"/>
    </p:xslt>
    <p:sink/>
    <p:for-each>
        <p:iteration-source>
            <p:pipe step="genXSDs" port="secondary" />
        </p:iteration-source>
        <p:variable name="name" select="replace(p:base-uri(),'.xml$','.xsd')"/>
        <psch:translateProductSchema/>
        <p:store method="xml" indent="true" encoding="UTF-8"
                 omit-xml-declaration="false" name="xsdStore">
            <p:with-option name="href" select="$name"/>
        </p:store>
    </p:for-each>
</p:declare-step>
