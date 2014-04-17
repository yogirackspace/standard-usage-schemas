
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
  name="genSamples" version="1.0">

  <p:input port="source" primary="true"/>
  <p:input port="parameters" kind="parameter"/>

  <p:validate-with-xml-schema name="validateProductSchema" assert-valid="true" mode="strict">
    <p:input port="schema">
      <p:document href="product_schema_def/xsd/productSchema.xsd"></p:document>
    </p:input>
  </p:validate-with-xml-schema>

  <p:xslt name="genEntry">
    <p:input port="source">
      <p:pipe port="result" step="validateProductSchema"/>
    </p:input>
    <p:input port="stylesheet">
      <p:document href="product_schema_def/xsl/productSchema-samples.xsl" />
    </p:input>
    <p:input port="parameters">
      <p:pipe step="genSamples" port="parameters"/>
    </p:input>
  </p:xslt>

  <p:sink/>

  <p:for-each name="entryVersions">
    <p:iteration-source>
      <p:pipe step="genEntry" port="secondary"/>
    </p:iteration-source>

    <p:variable name="entry" select="p:base-uri()"/>
    <p:store method="xml" indent="true" encoding="UTF-8"
	     omit-xml-declaration="false" name="entryStore">
	     	     <p:input port="source">
         	       <p:pipe step="entryVersions" port="current"/>
         	     </p:input>
      <p:with-option name="href" select="$entry"/>
    </p:store>

    <p:xslt name="genBigData">
      <p:input port="source">
        <p:pipe step="entryVersions" port="current"/>
      </p:input>
      <p:input port="stylesheet">
        <p:document href="wadl/bigdata.xsl" />
      </p:input>
      <p:input port="parameters">
        <p:empty/>
      </p:input>
    </p:xslt>

    <p:xslt name="genLbaas">
      <p:input port="source">
        <p:pipe step="genBigData" port="result"/>
      </p:input>
      <p:input port="stylesheet">
        <p:document href="wadl/bigdata.xsl" />
      </p:input>
      <p:input port="parameters">
        <p:empty/>
      </p:input>
    </p:xslt>

    <p:xslt name="genResp">
      <p:input port="source">
        <p:pipe step="genLbaas" port="result"/>
      </p:input>
      <p:input port="stylesheet">
        <p:document href="wadl/atom_hopper_pre.xsl" />
      </p:input>
      <p:input port="parameters">
        <p:empty/>
      </p:input>
    </p:xslt>

  <p:validate-with-xml-schema name="validateProcessedEntry" assert-valid="true" mode="strict">
    <p:input port="source">
      <p:pipe step="genResp" port="result"/>
    </p:input>
    <p:input port="schema">
      <p:document href="core_xsd/entry.xsd"></p:document>
    </p:input>
  </p:validate-with-xml-schema>

    <p:store method="xml" indent="true" encoding="UTF-8"
	     omit-xml-declaration="false" name="respStore">
	     <p:input port="source">
	       <p:pipe step="genResp" port="result"/>
	     </p:input>
      <p:with-option name="href" select="replace($entry, '-entry.xml$', '-response.xml')"/>
    </p:store>

  </p:for-each>
  
</p:declare-step>

