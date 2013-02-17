package com.rackspace.usage

import java.io.File
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

//
//  Test to make sure that all sample messages are valid against the
//  product schemas and that all samples can pass the validator in
//  their respective feeds.
//

@RunWith(classOf[JUnitRunner])
class SampleMessagesSuite extends BaseUsageSuite {

  val sampleDir   = new File("message_samples")
  val sampleFiles = sampleDir.listFiles.filter(_.getName().endsWith(".xml"))
  val sampleXSD = new File("core_xsd/entry.xsd")

  test("All sample files should be valid according to the product schema") {
    sampleFiles.foreach ( f => {
      printf("Checking %s/%s\n",sampleDir.getName,f.getName)
      usageMsg.assert(XML.loadFile(f))
    })
  }

  test("All sample files annotated with <?feed ..> should pass validation on that feed") {
    sampleFiles.map(toFeedFile).filter(_ != None).map(_.get).foreach (f => {
      printf("Checking %s/%s against feed %s\n",sampleDir.getName, f._2.getName, f._1)
      atomValidator.validate(request("POST", f._1, "application/atom+xml", XML.loadFile(f._2)), response, chain)
    })
  }

  private val getFeedXSD = new File("src/test/resources/get-feed.xsl")
  private val usageMsg = new SchemaAsserter(new URL(sampleXSD.toURI.toString))
  private val templates = TransformerFactory.newInstance().newTemplates(new StreamSource(getFeedXSD))

  //
  //  Converts a sample file to an optional (feed/path, file)
  //
  def toFeedFile (f : File) : Option[(String, File)] = {
    val trans = templates.newTransformer()
    val writer = new StringWriter()

    trans.transform(new StreamSource(f), new StreamResult(writer))
    if (writer.toString().isEmpty()) {
      None
    } else {
      Some((writer.toString, f))
    }
  }
}
