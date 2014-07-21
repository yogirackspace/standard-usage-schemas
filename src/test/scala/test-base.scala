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

    //
    //  The atom hopper validator
    //
    val atomValidator = Validator(new StreamSource(new File("atom_hopper.wadl")), usageConfig)
    val atomValidatorIdentity = Validator(new StreamSource(new File("allfeeds.wadl")), usageConfig)
    val atomValidatorObserver = Validator(new StreamSource(new File("allfeeds_observer.wadl")), usageConfig)

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

}
