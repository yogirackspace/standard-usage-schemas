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
//  Test to make sure that all sample messages are valid against the
//  product schemas.
//

@RunWith(classOf[JUnitRunner])
class SampleMessagesSuite extends BaseUsageSuite {
  test("All sample messages should be valid according to the product schema") {
    new File("message_samples").listFiles().filter(f => f.getName().endsWith(".xml")).foreach ( f => {
      println("Checking message_samples/"+f.getName())
      usageMsg.assert(XML.loadFile(f))
    })
  }
}
