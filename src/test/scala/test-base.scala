package com.rackspace.usage

import java.io.File
import javax.xml.transform.stream.StreamSource

import com.rackspace.com.papi.components.checker.BaseValidatorSuite
import com.rackspace.com.papi.components.checker.AssertResultHandler
import com.rackspace.com.papi.components.checker.handler._

import com.rackspace.com.papi.components.checker.Validator
import com.rackspace.com.papi.components.checker.Config


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

  val atomValidator = Validator(new StreamSource(new File("atom_hopper.wadl").toURI.toString), usageConfig)
}

class BaseUsageSuite extends BaseValidatorSuite {

}
