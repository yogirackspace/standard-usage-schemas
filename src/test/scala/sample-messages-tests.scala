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
  val sampleFiles = getSampleXMLFiles(sampleDir)
  val badSampleDir = new File("bad_message_samples")
  val badSampleFiles = getSampleXMLFiles(badSampleDir)

  val sampleXSD = new File("core_xsd/entry.xsd")

  def getSampleXMLFiles(dir: File) : List[File] = {

    def getSampleXMLFiles(fdir : List[File]) : List[File] = fdir match {
      case List() => List()
      case fi :: rest => if (!fi.isDirectory() && fi.getName().endsWith(".xml")) {
        fi :: getSampleXMLFiles(rest)
      } else if (fi.isDirectory()) {
        getSampleXMLFiles(rest ++ fi.listFiles().toList)
      } else {
        getSampleXMLFiles(rest)
      }
    }

    getSampleXMLFiles(dir.listFiles().toList)
  }

  test("All sample files in the samples directory should be valid according to the product schema") {
    sampleFiles.foreach ( f => {
      printf("Checking %s\n",f.getAbsolutePath)
      usageMsg.assert(XML.loadFile(f))
    })
  }

  test("All sample files annotated with <?atom feed=... ..> in the sample directory should pass validation on that feed") {
    sampleFiles.map(toFeedFile).filter(_ != None).map(_.get).foreach (f => {
      printf("Checking %s against feed %s\n", f._2.getAbsolutePath, f._1)
      if ( f._2.getAbsolutePath().indexOf("/identity/") != -1 ) {
        atomValidatorIdentity.validate(request("POST", f._1, "application/atom+xml", XML.loadFile(f._2)), response, chain)
      } else {
        atomValidator.validate(request("POST", f._1, "application/atom+xml", XML.loadFile(f._2)), response, chain)
      }
    })
  }

  test("All sample files in the bad samples directly and annotated with <?atom...> and <?expect..> should fail in the expected way") {
    badSampleFiles.map(toFeedCodeMessagesFile).filter(_ != None).map(_.get).foreach(f => {
      printf("Checking %s against feed %s\n",  f._4.getAbsolutePath, f._1)
      if ( f._4.getAbsolutePath().indexOf("/identity/") != -1 ) {
        val r = assertResultFailed(atomValidatorIdentity.validate(request("POST", f._1, "application/atom+xml", XML.loadFile(f._4)), response, chain), f._2, f._3)
      } else {
        val r = assertResultFailed(atomValidator.validate(request("POST", f._1, "application/atom+xml", XML.loadFile(f._4)), response, chain), f._2, f._3)
      }
    })
  }

  private val prepocExtract = new File("src/test/resources/procinst-extract.xslt")
  private val usageMsg = new SchemaAsserter(new URL(sampleXSD.toURI.toString))
  private val templates = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null).newTemplates(new StreamSource(prepocExtract))

  //
  //  Converts a sample file to an optional (feed/path, file)
  //
  def toFeedFile (f : File) : Option[(String, File)] = {
    val feed = (getProcs(f, "atom") \\ "atom" \\ "@feed").text
    if (feed.isEmpty()) {
      None
    } else {
      Some((feed, f))
    }
  }

  //
  //  Converts a sample file to an aptional (feed/path, code, messages, file)
  //
  def toFeedCodeMessagesFile (f: File) : Option[(String, Int, List[String], File)] = {
    val procs = getProcs(f, "atom expect")
    val feed = (procs \\ "atom" \\ "@feed").text
    val codeTxt = (procs \\ "expect" \\ "@code").text
    val message = (procs \\ "expect" \\ "@message").text

    if (feed.isEmpty() || codeTxt.isEmpty()) {
      None
    } else {
      Some ((feed, codeTxt.toInt, message.split("\\s").toList, f))
    }
  }

  def getProcs (f : File, ps : String) : NodeSeq = {
    val trans = templates.newTransformer()
    val writer = new StringWriter()

    trans.setParameter("procList", ps)
    trans.transform(new StreamSource(f), new StreamResult(writer))
    XML.loadString(writer.toString)
  }
}
