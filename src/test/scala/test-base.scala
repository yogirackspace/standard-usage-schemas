package com.rackspace.usage

import java.io.File
import java.net.URL

import javax.xml.transform.stream.StreamSource

import javax.servlet.http.HttpServletRequest

import com.rackspace.com.papi.components.checker.BaseValidatorSuite
import com.rackspace.com.papi.components.checker.AssertResultHandler
import com.rackspace.com.papi.components.checker.handler._

import com.rackspace.com.papi.components.checker.Validator
import com.rackspace.com.papi.components.checker.Config

import com.rackspace.com.papi.components.checker.servlet.RequestAttributes.PARSED_XML

import com.rackspace.cloud.api.wadl.test.XPathAssertions
import com.rackspace.cloud.api.wadl.test.SchemaAsserter

import org.w3c.dom.Document


object BaseUsageSuite {
  //
  //  Assert handler used when validating test requests
  //
  val assertHandler = new DispatchResultHandler(List[ResultHandler](new ConsoleResultHandler(), 
                                                                    new AssertResultHandler(),
                                                                    new ServletResultHandler()))

  //
  //  Default validator configuration
  //
  val usageConfig = new Config()

  usageConfig.removeDups = true
  usageConfig.validateChecker = true
  usageConfig.useSaxonEEValidation = true
  usageConfig.checkWellFormed = true
  usageConfig.checkXSDGrammar = true
  usageConfig.doXSDGrammarTransform = true
  usageConfig.checkElements = true
  usageConfig.xpathVersion = 2
  usageConfig.checkPlainParams = true
  usageConfig.enablePreProcessExtension = true
  usageConfig.enableIgnoreXSDExtension = true
  usageConfig.enableMessageExtension
  usageConfig.xslEngine = "XalanC"
  usageConfig.joinXPathChecks = true
  usageConfig.checkHeaders = false
  usageConfig.resultHandler = assertHandler

  //
  //  The atom hopper validator
  //
  lazy val atomValidator = Validator(new StreamSource(new File("atom_hopper.wadl").toURI.toString), usageConfig)

  lazy val usageMsg = new SchemaAsserter(new URL((new File("core_xsd/entry.xsd")).toURI.toString))

  lazy val productSchema = new SchemaAsserter(new URL((new File("product_schema_def/xsd/productSchema.xsd")).toURI.toString))

  //
  //  Convinece function to get to the XML of a request
  //
  def getProcessedXML(req : HttpServletRequest) : Document = req.getAttribute(PARSED_XML).asInstanceOf[Document]
}

class BaseUsageSuite extends BaseValidatorSuite with XPathAssertions {
  //
  //  Default namespaces for atom assertions
  //
  register ("atom", "http://www.w3.org/2005/Atom")
  register ("event", "http://docs.rackspace.com/core/event")
}
