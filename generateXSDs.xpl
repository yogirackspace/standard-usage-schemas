<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
    xmlns:psch="http://docs.rackspace.com/core/usage/schema"
    xmlns:c="http://www.w3.org/ns/xproc-step" version="1.0">
    <p:input port="source">
        <p:empty/>
    </p:input>
    <p:import href="product_schema_def/xpl/productSchema.xpl"/>
    <p:directory-list path="sample_product_schemas">
        <p:with-option name="include-filter" select="'.*xml$'"/>
    </p:directory-list>
    <p:for-each>
        <p:iteration-source select="//c:file"/>
        <p:variable name="inName" select="concat('generated_product_xsds/',/c:file/@name)"/>
        <p:variable name="name" select="concat(substring-before($inName,'.xml'),'.xsd')"/>
        <p:load>
            <p:with-option name="href" select="concat('sample_product_schemas/',//c:file/@name)"/>
        </p:load>
        <psch:translateProductSchema/>
        <p:store method="xml" indent="true" encoding="UTF-8" omit-xml-declaration="false">
            <p:with-option name="href" select="$name"/>
        </p:store>
    </p:for-each>
</p:declare-step>