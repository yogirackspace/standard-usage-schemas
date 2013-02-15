package com.rackspace.usage

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.w3c.dom.Document

import scala.xml._

import com.rackspace.com.papi.components.checker.servlet.RequestAttributes._
import com.rackspace.cloud.api.wadl.Converters._
import com.rackspace.com.papi.components.checker.Converters._
import com.rackspace.com.papi.components.checker.servlet.RequestAttributes._

import BaseUsageSuite._

//
//  Test to make sure that all product schemas are valid.
//

@RunWith(classOf[JUnitRunner])
class ProductSchemaSuite extends BaseUsageSuite {
  test("All product schemas should be valid") {
    new File("sample_product_schemas").listFiles().filter(f => f.getName().endsWith(".xml")).foreach ( f => {
      println("Checking sample_product_schemas/"+f.getName())
      productSchema.assert(XML.loadFile(f))
    })
  }
}
