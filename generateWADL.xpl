<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
    xmlns:l="http://xproc.org/library"
    xmlns:c="http://www.w3.org/ns/xproc-step" version="1.0">

    <p:input port="source">
        <p:empty/>
    </p:input>
    
    <p:import href="recursive-directory-list.xpl"/>
    
    <l:recursive-directory-list path="." name="schemas">
        <p:with-option name="include-filter" select="'.*'"/>
    </l:recursive-directory-list>
    <p:xslt name="genarate_WADL">
        <p:input port="stylesheet">
            <p:document href="product_schema_def/xsl/productSchema-wadl.xsl"/>
        </p:input>
        <p:input port="source">
            <p:pipe port="result" step="schemas"/>
        </p:input>
        <p:input port="parameters">
            <p:empty/>
        </p:input>
    </p:xslt>
    <p:store method="xml" indent="true" encoding="UTF-8"
        omit-xml-declaration="false" href="wadl/product.wadl"/>
</p:declare-step>
