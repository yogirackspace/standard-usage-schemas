package com.rackspace.usage

import java.io.File

import java.net.URL

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import com.rackspace.cloud.api.wadl.test.SchemaAsserter

import scala.xml._


//
//  Test to make sure that all product schemas are valid according to
//  their XSDs.
//

@RunWith(classOf[JUnitRunner])
class ProductSchemaXSDSuite extends BaseUsageSuite {

  //
  //  Resources
  //
  val productSchemaDir = new File("sample_product_schemas")
  val productSchemaSchema = new File("product_schema_def/xsd/productSchema.xsd")
  val productSchemas = productSchemaDir.listFiles().filter(_.getName.endsWith(".xml"))

  private val productSchema = new SchemaAsserter(new URL(productSchemaSchema.toURI.toString))

  //
  //  TESTs
  //

  productSchemas.foreach ( f => {
    test("Product schema "+productSchemaDir.getName()+"/"+f.getName()+" should validate against product schema xsd") { 
      printf("Checking %s/%s\n",productSchemaDir.getName(),f.getName())
      productSchema.assert(XML.loadFile(f))
    }
  })

}
