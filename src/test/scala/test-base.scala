package com.rackspace.usage

import java.io.File

import javax.xml.transform.stream.StreamSource

import javax.servlet.http.HttpServletRequest

import com.rackspace.com.papi.components.checker.BaseValidatorSuite
import com.rackspace.com.papi.components.checker.AssertResultHandler
import com.rackspace.com.papi.components.checker.handler._

import com.rackspace.com.papi.components.checker.Validator
import com.rackspace.com.papi.components.checker.Config

import com.rackspace.com.papi.components.checker.servlet.RequestAttributes.PARSED_XML

import com.rackspace.cloud.api.wadl.test.XPathAssertions

import org.w3c.dom.Document

import scala.xml.NodeSeq


object BaseUsageSuite {
    //
    //  Assert handler used when validating test requests
    //
    val assertHandler = new DispatchResultHandler(List[ResultHandler](new ConsoleResultHandler(),
        new AssertResultHandler(),
        new ServletResultHandler()))
    val assertHandlerWithDot = new DispatchResultHandler(List[ResultHandler](new ConsoleResultHandler(),
        new SaveDotHandler(new File("target/validator.dot"), false, false),
        new AssertResultHandler(),
        new ServletResultHandler()))

    //
    //  Default validator configuration
    //
    val usageConfig = new Config()

    usageConfig.removeDups = true
    usageConfig.validateChecker = true
    usageConfig.xsdEngine = System.getProperty("usage.tests.xsd.impl", "SaxonEE")
    usageConfig.checkWellFormed = true
    usageConfig.checkXSDGrammar = true
    usageConfig.doXSDGrammarTransform = true
    usageConfig.checkElements = true
    usageConfig.xpathVersion = 2
    usageConfig.checkPlainParams = true
    usageConfig.enablePreProcessExtension = true
    usageConfig.enableIgnoreXSDExtension = true
    usageConfig.enableMessageExtension = true
    usageConfig.xslEngine = "XalanC"
    usageConfig.joinXPathChecks = true
    usageConfig.checkHeaders = true
    usageConfig.preserveRequestBody = true
    usageConfig.resultHandler = assertHandler
    usageConfig.enableRaxRolesExtension = true

    //
    //  The atom hopper validator
    //
    val atomValidator = Validator(new StreamSource(new File("allfeeds.wadl")), usageConfig)

    //
    //  Convenience function to get to the XML of a request
    //
    def getProcessedXML(req: HttpServletRequest): Document = req.getAttribute(PARSED_XML).asInstanceOf[Document]
}

class BaseUsageSuite extends BaseValidatorSuite with XPathAssertions {
    //
    //  Default namespaces for atom assertions
    //
    register("atom", "http://www.w3.org/2005/Atom")
    register("event", "http://docs.rackspace.com/core/event")
    register("cldfeeds", "http://docs.rackspace.com/api/cloudfeeds")
    register("cadf", "http://schemas.dmtf.org/cloud/audit/1.0/event")

  val SERVICE_ADMIN = "cloudfeeds:service-admin"
  val SERVICE_OBSERVER = "cloudfeeds:service-observer"
  val OBSERVER = "cloudfeeds:observer"
  val IDENTITY_USER_ADMIN = "identity:user-admin"
  val REG_ADMIN = "admin"
  val REG_OBSERVER = "observer"

  def request(method : String,
              url : String,
              contentType : String,
              content : NodeSeq,
              xRole : String ) : javax.servlet.http.HttpServletRequest = {

    request( method, url, contentType, content, false, Map( "X-ROLES"->List( xRole ) ) )
  }

  def requestRole(method : String,
              url : String,
              xRole : String ) : javax.servlet.http.HttpServletRequest = {

    request( method, url, "", "", false, Map("ACCEPT"->List("*/*"), "X-ROLES"->List( xRole ) ) )
  }

  def getSampleJsonFiles(dir: File) : List[File] = {
    getFiles(dir, ".json")
  }

  def getSampleXMLFiles(dir: File) : List[File] = {
    getFiles(dir, ".xml")
  }

  def getFiles(dir: File, fileExtension: String) : List[File] = {

    def getFiles(fdir : List[File]) : List[File] = fdir match {
      case List() => List()
      case fi :: rest => if (!fi.isDirectory() && fi.getName().endsWith(fileExtension)) {
        fi :: getFiles(rest)
      } else if (fi.isDirectory()) {
        getFiles(rest ++ fi.listFiles().toList)
      } else {
        getFiles(rest)
      }
    }

    getFiles(dir.listFiles().toList)
  }
}
