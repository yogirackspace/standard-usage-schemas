
<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
  name="genSamples" version="1.0">

  <p:input port="source" primary="true"/>
  <p:input port="parameters" kind="parameter"/>

  <p:validate-with-xml-schema name="validateProductSchema" assert-valid="true" mode="strict">
    <p:input port="schema">
      <p:document href="../../product_schema_def/xsd/productSchema.xsd"></p:document>
    </p:input>
  </p:validate-with-xml-schema>

  <p:xslt name="genEntry">
    <p:input port="source">
      <p:pipe port="result" step="validateProductSchema"/>
    </p:input>
    <p:input port="stylesheet">
      <p:document href="../../product_schema_def/xsl/productSchema-samples.xsl" />
    </p:input>
    <p:input port="parameters">
      <p:pipe step="genSamples" port="parameters"/>
    </p:input>
  </p:xslt>

  <p:sink/>

  <p:choose name="genSummaryCheck" >

      <p:xpath-context>
        <p:pipe port="result" step="validateProductSchema"/>
      </p:xpath-context>

<!--
    This looks like a better way to test for usage events.  However, non-usage events throw an exception, which looks like
    an internal saxon error.

    <p:when test="boolean(//*:productSchema[not(@type) or contains( @type, 'USAGE') or contains( @type, 'USAGE_SNAPSHOT')])">
-->
    <p:when test="boolean( //*:productSchema[contains( @namespace, 'usage')])">

      <p:output port="secondary" sequence="true">
        <p:pipe step="genSummary" port="secondary"/>
      </p:output>

      <p:xslt name="genSummary">
        <p:input port="source">
          <p:pipe port="result" step="validateProductSchema"/>
        </p:input>
        <p:input port="stylesheet">
          <p:document href="../../product_schema_def/xsl/productSchema-summarySamples.xsl" />
        </p:input>
        <p:input port="parameters">
          <p:pipe step="genSamples" port="parameters"/>
        </p:input>
      </p:xslt>

      <p:sink/>
    </p:when>
    <p:otherwise>

     <p:output port="secondary">
       <p:empty/>
     </p:output>

     <p:sink>
       <p:input port="source">
         <p:pipe port="result" step="validateProductSchema"/>
       </p:input>
     </p:sink>

    </p:otherwise>
  </p:choose>

  <p:for-each name="entryVersions">
    <p:iteration-source>
      <p:pipe step="genEntry" port="secondary"/>
      <p:pipe step="genSummaryCheck" port="secondary"/>
    </p:iteration-source>

    <p:variable name="entry" select="p:base-uri()"/>

    <!-- write -entry.xml and -summary-entry.xml -->
    <p:store method="xml" indent="true" encoding="UTF-8"
	     omit-xml-declaration="false" name="entryStore">
	     	     <p:input port="source">
         	       <p:pipe step="entryVersions" port="current"/>
         	     </p:input>
      <p:with-option name="href" select="$entry"/>
    </p:store>

    <!-- convert xml to JSON for entryVersions current -->
    <p:xslt name="genEntryJson" template-name="genSample">
      <p:input port="source">
        <p:pipe step="entryVersions" port="current"/>
      </p:input>
      <p:input port="stylesheet">
        <p:document href="../../target/xslt-artifacts/xml2json-feeds.xsl" />
      </p:input>
      <p:input port="parameters">
        <p:empty/>
      </p:input>
    </p:xslt>

    <!-- write -entry.json and -summary-entry.json -->
    <p:store method="text" name="entryJsonStore">
         <p:input port="source">
           <p:pipe step="genEntryJson" port="result"/>
         </p:input>
      <p:with-option name="href" select="replace(replace($entry, 'xml/', 'json/'), '.xml$', '.json')"/>
    </p:store>

    <p:xslt name="genBigData">
      <p:input port="source">
        <p:pipe step="entryVersions" port="current"/>
      </p:input>
      <p:input port="stylesheet">
        <p:document href="../../wadl/bigdata.xsl" />
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
        <p:document href="../../wadl/synthesize_lbaas.xsl" />
      </p:input>
      <p:input port="parameters">
        <p:empty/>
      </p:input>
    </p:xslt>

    <p:xslt name="genCat">
      <p:input port="source">
        <p:pipe step="genLbaas" port="result"/>
      </p:input>
      <p:input port="stylesheet">
        <p:document href="../../wadl/atom_hopper_pre.xsl" />
      </p:input>
      <p:input port="parameters">
        <p:empty/>
      </p:input>
    </p:xslt>

    <p:xslt name="genResp">
      <p:input port="source">
        <p:pipe step="genCat" port="result"/>
      </p:input>
      <p:input port="stylesheet">
        <p:document href="../../product_schema_def/xsl/productSchema-samplesResp.xsl" />
      </p:input>
      <p:input port="parameters">
        <p:pipe step="genSamples" port="parameters"/>
      </p:input>
    </p:xslt>

    <!-- write -response.xml and summary-response.xml -->
    <p:store method="xml" indent="true" encoding="UTF-8"
	     omit-xml-declaration="false" name="respStore">
	     <p:input port="source">
	       <p:pipe step="genResp" port="result"/>
	     </p:input>
      <p:with-option name="href" select="replace($entry, '-entry.xml$', '-response.xml')"/>
    </p:store>

    <!-- convert xml to JSON for response -->
    <p:xslt name="genResponseJson" template-name="genSample">
      <p:input port="source">
        <p:pipe step="genResp" port="result"/>
      </p:input>
      <p:input port="stylesheet">
        <p:document href="../../target/xslt-artifacts/xml2json-feeds.xsl" />
      </p:input>
      <p:input port="parameters">
        <p:empty/>
      </p:input>
    </p:xslt>

    <!-- write -response.json and summary-response.json -->
    <p:store method="text" name="responseJsonStore">
      <p:input port="source">
        <p:pipe step="genResponseJson" port="result"/>
      </p:input>
      <p:with-option name="href" select="replace(replace($entry, 'xml/', 'json/'), '-entry.xml$', '-response.json')"/>
    </p:store>

  </p:for-each>
  
</p:declare-step>
