package com.rackspace.usage

import java.io.File
import java.io.StringReader
import java.io.StringWriter

import java.net.URL

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.w3c.dom.Document

import scala.xml._

import com.rackspace.com.papi.components.checker.servlet.RequestAttributes._
import com.rackspace.cloud.api.wadl.Converters._
import com.rackspace.com.papi.components.checker.Converters._
import com.rackspace.com.papi.components.checker.servlet.RequestAttributes._

import com.rackspace.cloud.api.wadl.test.SchemaAsserter

import BaseUsageSuite._

import javax.xml.transform._
import javax.xml.transform.stream.StreamSource
import javax.xml.transform.stream.StreamResult

import org.apache.xml.security.c14n.Canonicalizer

import net.sf.saxon.lib.FeatureKeys
import net.sf.saxon.lib.Validation
import com.saxonica.config.EnterpriseTransformerFactory

//
//  Test to make sure that all product schemas and the artifacts that
//  they generate are valid.
//

@RunWith(classOf[JUnitRunner])
class ProductSchemaSuite extends BaseUsageSuite {
  //
  //  Resources
  //
  val productSchemaDir = new File("sample_product_schemas")
  val productSchemas = productSchemaDir.listFiles().filter(_.getName.endsWith(".xml"))

  val generatedXSDsDir = new File("generated_product_xsds")
  val generatedXSDs = generatedXSDsDir.listFiles().filter(_.getName.endsWith("xsd"))

  val productSchemaSchema = new File("product_schema_def/xsd/productSchema.xsd")
  val productSchemaXSDXSL = new File("product_schema_def/xsl/productSchema-standalone.xsl")
  val productSchemaWADLXSL = new File("product_schema_def/xsl/productSchema-wadl.xsl")

  val generatedWADL = new File("wadl/product.wadl")

  val xprocDirSchema = new File("src/test/resources/directory.xsd")

  //
  //  Tests
  //
  test("All product schemas should be valid") {
    productSchemas.foreach ( f => {
      printf("Checking %s/%s\n",productSchemaDir.getName(),f.getName())
      productSchema.assert(XML.loadFile(f))
    })
  }

  test("The number of generated XSDs and product schemas should match") {
    assert(productSchemas.length == generatedXSDs.length)
  }

  test("Each product should have a generated product XSD that matches it") {
    productSchemas.map(toConGenXSD).foreach(f => {
      printf("Checking %s/%s\n",generatedXSDsDir, f._3)
      if (f._1 != f._2) { printf("(%s)\n[%s]\n", f._1, f._2)}
      assert(f._1 == f._2)
    })
  }

  test("Product schema and generated product WADL should match") {
    printf("Checking %s\n", generatedWADL)
    val w = toConGenWADL(productSchemaDir)
    if (w._1 != w._2) { printf("(%s)\n[%s]\n", w._1, w._2) }
    assert (w._1 == w._2)
  }

  //
  //  Init xml security lib
  //
  org.apache.xml.security.Init.init()

  private val canonicalizer = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS)

  private val transFactory = TransformerFactory.newInstance("com.saxonica.config.EnterpriseTransformerFactory", null)
  transFactory.setAttribute(FeatureKeys.SCHEMA_VALIDATION, Validation.STRICT)
  transFactory.setAttribute(FeatureKeys.XSLT_SCHEMA_AWARE, true)
  transFactory.setAttribute(FeatureKeys.EXPAND_ATTRIBUTE_DEFAULTS, true)
  transFactory.setAttribute(FeatureKeys.XSD_VERSION, "1.1")
  transFactory.asInstanceOf[EnterpriseTransformerFactory].addSchema(new StreamSource(productSchemaSchema))
  transFactory.asInstanceOf[EnterpriseTransformerFactory].addSchema(new StreamSource(xprocDirSchema))

  //
  //  We have to use a different transfactory here because of what I
  //  think in a bug in SAXON -- when fix, use transFactory instead of
  //  transFactoryWADL.
  //
  private val transFactoryWADL = TransformerFactory.newInstance("com.saxonica.config.EnterpriseTransformerFactory", null)

  private val xsdTemplate  = transFactory.newTemplates(new StreamSource(productSchemaXSDXSL))
  private val wadlTemplate = transFactoryWADL.newTemplates(new StreamSource(productSchemaWADLXSL))

  private val defTransFactory = TransformerFactory.newInstance()

  private val productSchema = new SchemaAsserter(new URL(productSchemaSchema.toURI.toString), true)

  //
  //  Given a product schema, get cananicalized genereated XSDs
  //  (newGenerated, fileSystem, XSDFile) both as strings.
  //
  private def toConGenXSD (f : File) : (String, String, File) = {
    val trans = xsdTemplate.newTransformer()
    val idTrans = defTransFactory.newTransformer()
    val genWriter = new StringWriter()
    val fileWriter = new StringWriter()

    val xsdFile = new File(generatedXSDsDir,f.getName.replaceFirst(".xml", ".xsd"))

    trans.transform(new StreamSource(f), new StreamResult(genWriter))
    idTrans.transform(new StreamSource(xsdFile), new StreamResult(fileWriter))

    (new String(canonicalizer.canonicalize(genWriter.toString.getBytes),"UTF-8"),
     new String(canonicalizer.canonicalize(fileWriter.toString.getBytes), "UTF-8"),
     xsdFile)
  }

  //
  //  Given product schema directory, get cananicalized generaded WADs
  //  (newGenerated, fileSystem) both as strings.
  //
  private def toConGenWADL (f : File) : (String, String) = {
    val trans = wadlTemplate.newTransformer()
    val idTrans = defTransFactory.newTransformer()

    val genWriter = new StringWriter()
    val fileWriter = new StringWriter()

    trans.transform(new StreamSource(toProcStepList(f)), new StreamResult(genWriter))
    idTrans.transform(new StreamSource(generatedWADL), new StreamResult(fileWriter))

    (new String(canonicalizer.canonicalize(genWriter.toString.getBytes),"UTF-8"),
     new String(canonicalizer.canonicalize(fileWriter.toString.getBytes), "UTF-8"))
  }

  //
  //  Converts a file / directory to a ProcStepList
  //
  private def toProcStepList(f : File) : NodeSeq = {
    if (!f.isDirectory) {
      <file name={f.getAbsolutePath.substring(1)} />
    } else {
      <directory xmlns="http://www.w3.org/ns/xproc-step" name={f.getName}>
          {f.listFiles.filter(_.getName.endsWith(".xml")).map(toProcStepList)}
      </directory>
    }
  }
}
