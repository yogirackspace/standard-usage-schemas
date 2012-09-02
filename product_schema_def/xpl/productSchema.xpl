<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
    xmlns:c="http://www.w3.org/ns/xproc-step"
    xmlns:psch="http://docs.rackspace.com/core/usage/schema"
    version="1.0"
    name="ProcessProductSchema"
    type="psch:translateProductSchema">
    <p:input port="source" />
    <p:output port="result" />
    <p:try>
        <p:group>
            <p:validate-with-xml-schema assert-valid="true" mode="strict" name="validate">
                <p:input port="schema">
                    <p:document href="../xsd/productSchema.xsd"></p:document>
                </p:input>
            </p:validate-with-xml-schema>
            <p:xslt name="productTransform">
                <p:input port="source">
                    <p:pipe port="result" step="validate"/>
                </p:input>
                <p:input port="stylesheet">
                    <p:document href="../xsl/productSchema-standalone.xsl"/>
                </p:input>
                <p:input port="parameters">
                    <p:empty/>
                </p:input>
            </p:xslt>
        </p:group>
        <p:catch name="catch">
            <p:error code="psch:Invalid">
                <p:input port="source">
                    <p:pipe port="error" step="catch"/>
                </p:input>
            </p:error>
        </p:catch>
    </p:try>
</p:declare-step>