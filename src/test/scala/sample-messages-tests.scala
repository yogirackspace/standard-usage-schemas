package com.rackspace.usage

import java.io.File
import java.io.StringWriter

import java.net.URL

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.xml._

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

  private val prepocExtract = new File("src/test/resources/procinst-extract.xslt")
  private val preprocAHop   = new File("wadl/atom_hopper_pre.xsl")
  private val usageMsg = new SchemaAsserter(new URL(sampleXSD.toURI.toString))
  private val templates = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null).newTemplates(new StreamSource(prepocExtract))
  private val preproc_template = TransformerFactory.newInstance("org.apache.xalan.xsltc.trax.TransformerFactoryImpl",null).newTemplates(new StreamSource(preprocAHop))

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

  sampleFiles.foreach ( f => {
    test("Sample "+f.getAbsolutePath+" should be valid according to the product schema ") {
      printf("Checking %s\n",f.getAbsolutePath)
      val trans = preproc_template.newTransformer()
      val writer = new StringWriter()

      trans.transform(new StreamSource(f), new StreamResult(writer))
      usageMsg.assert(XML.loadString(writer.toString))
    }
  })

  val sampleFeedToFilePairs = sampleFiles.map(toFeedFile).flatten

  sampleFeedToFilePairs.foreach { case(feed, fl) =>
      test("Sample "+fl.getAbsolutePath+" should be valid against feed "+feed) {
          printf("Checking %s against feed %s\n", fl.getAbsolutePath, feed)
          if ( fl.getAbsolutePath().indexOf("/identity/") != -1 ) {
              atomValidatorIdentity.validate(request("POST", feed, "application/atom+xml", XML.loadFile(fl)), response, chain)
          } else {
              atomValidator.validate(request("POST", feed, "application/atom+xml", XML.loadFile(fl)), response, chain)
          }
      }
  }

  sampleFeedToFilePairs.collect { case (feed: String, fl: File) => feed }.distinct.foreach { feed =>
      test("Getting feed " + feed + " should work") {
          if ( feed.startsWith("identity/") ) {
              atomValidatorIdentity.validate(request("GET", feed), response, chain)
          } else {
              atomValidator.validate(request("GET", feed), response, chain)
          }
      }
      test("Getting individual entry for feed " + feed + " should work") {
          if ( feed.startsWith("identity/") ) {
              atomValidatorIdentity.validate(request("GET", feed + "/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4"), response, chain)
          } else {
              atomValidator.validate(request("GET", feed + "/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4"), response, chain)
          }
      }
  }


  badSampleFiles.map(toFeedCodeMessagesFile).filter(_ != None).map(_.get).foreach(f => {
    test("Sample "+f._4.getAbsolutePath+" should fail in the expected way when posted on feed "+f._1) {
      printf("Checking %s against feed %s\n",  f._4.getAbsolutePath, f._1)
      if ( f._4.getAbsolutePath().indexOf("/identity/") != -1 ) {
        val r = assertResultFailed(atomValidatorIdentity.validate(request("POST", f._1, "application/atom+xml", XML.loadFile(f._4)), response, chain), f._2, f._3)
      } else {
        val r = assertResultFailed(atomValidator.validate(request("POST", f._1, "application/atom+xml", XML.loadFile(f._4)), response, chain), f._2, f._3)
      }
    }
  })

  //
  //  Converts a sample file to a list of (feed/path, file)
  //
  def toFeedFile (f : File) : List[(String, File)] = {
    val feedAttr = (getProcs(f, "atom") \\ "atom" \\ "@feed").text
    if (feedAttr.isEmpty()) {
      List()
    } else {
      feedAttr.split(' ').toList.map { feed =>
          (feed, f)
      }
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
