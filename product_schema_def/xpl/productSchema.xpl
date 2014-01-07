<?xml version="1.0" encoding="UTF-8"?>
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
    xmlns:c="http://www.w3.org/ns/xproc-step"
    xmlns:psch="http://docs.rackspace.com/core/usage/schema"
    version="1.0"
    name="ProcessProductSchema"
    type="psch:translateProductSchema">
    <p:input port="source" />

    <p:output port="result" primary="true">
      <p:pipe step="tryvalidation" port="result"/>
    </p:output>
    <p:output port="report" sequence="true">
      <p:pipe step="tryvalidation" port="report"/>
    </p:output>
    <p:input port="parameters" kind="parameter"/>

    <p:try name="tryvalidation">
        <p:group name="mygroup">
          <p:output port="result">
            <p:pipe step="productTransform" port="result"/>
          </p:output>
          <p:output port="report" sequence="true">
            <p:empty/>
          </p:output>

            <p:validate-with-xml-schema assert-valid="true" mode="strict" name="validate">
                <p:input port="schema">
                    <p:document href="../xsd/productSchema.xsd"></p:document>
                </p:input>
            </p:validate-with-xml-schema>
            <p:xslt name="productTransform" version="2.0">
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

          <p:output port="result">
            <p:pipe step="ProcessProductSchema" port="source"/>
          </p:output>
          <p:output port="report">
            <p:pipe step="printerrors" port="result"/>
          </p:output>

          <p:xslt name="printerrors">
            <p:input port="source">
              <p:pipe step="catch" port="error"/>
            </p:input>
            <p:input port="stylesheet">
              <p:inline>
                <xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
                  <xsl:template match="/">
<xsl:message terminate="yes"><xsl:text>

@@@@@@@@@@@@@@@@@@@@@@
!!!VALIDATION ERRORS!!
!!!!!!!!!!!!!!!!!!!!!!

</xsl:text><xsl:copy-of select="."/><xsl:text>

!!!!!!!!!!!!!!!!!!!!!!
!!!VALIDATION ERRORS!!
@@@@@@@@@@@@@@@@@@@@@@

</xsl:text>
</xsl:message>
		  </xsl:template>
		</xsl:stylesheet>
	      </p:inline>
	    </p:input>
            <p:input port="parameters" >
              <p:pipe step="ProcessProductSchema" port="parameters"/>
            </p:input>
	  </p:xslt>

            <p:error code="psch:Invalid">
                <p:input port="source">
                    <p:pipe port="error" step="catch"/>
                </p:input>
            </p:error>
	    <p:sink/>
        </p:catch>
    </p:try>
</p:declare-step>
