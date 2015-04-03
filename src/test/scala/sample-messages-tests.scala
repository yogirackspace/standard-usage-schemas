package com.rackspace.usage

import java.io.{StringReader, File, StringWriter}

import java.net.URL
import javax.xml.xpath.{XPathConstants, XPathFactory}

import net.sf.saxon.lib.NamespaceConstant
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.xml._

import com.rackspace.cloud.api.wadl.test.SchemaAsserter

import BaseUsageSuite._

import javax.xml.transform._
import javax.xml.transform.stream.StreamSource
import javax.xml.transform.stream.StreamResult
import scala.util.parsing.json.JSON

//
//  Test to make sure that all sample messages are valid against the
//  product schemas and that all samples can pass the validator in
//  their respective feeds.
//

@RunWith(classOf[JUnitRunner])
class SampleMessagesSuite extends BaseUsageSuite {

  val sampleDir   = new File("message_samples")
  val sampleFiles = getSampleXMLFiles(sampleDir)
  val sampleJsonFiles = getSampleJsonFiles(sampleDir)
  val badSampleDir = new File("bad_message_samples")
  val badSampleFiles = getSampleXMLFiles(badSampleDir)

  val sampleXSD = new File("core_xsd/entry.xsd")

  private val prepocExtract = new File("src/test/resources/procinst-extract.xslt")
  private val preprocAHop   = new File("wadl/atom_hopper_pre.xsl")
  private val usageMsg = new SchemaAsserter(new URL(sampleXSD.toURI.toString), true)
  private val templates = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null).newTemplates(new StreamSource(prepocExtract))
  private val preproc_template = TransformerFactory.newInstance("org.apache.xalan.xsltc.trax.TransformerFactoryImpl",null).newTemplates(new StreamSource(preprocAHop))
  private val xpathFactory = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON, "net.sf.saxon.xpath.XPathFactoryImpl", null)
  
  sampleFiles.foreach ( f => {
    test("Sample "+f.getAbsolutePath+" should be valid according to the product schema ") {
      printf("Checking %s\n",f.getAbsolutePath)
      val trans = preproc_template.newTransformer()
      val writer = new StringWriter()

      trans.transform(new StreamSource(f), new StreamResult(writer))
      usageMsg.assert(XML.loadString(writer.toString))
    }
  })

  getSampleXMLFiles(new File("message_samples/corexsd")).foreach ( f => {
    test("Sample "+f.getAbsolutePath+" should add valid categories") {
      printf("Checking %s\n",f.getAbsolutePath)
      val trans = preproc_template.newTransformer()
      val writer = new StringWriter()

      trans.transform(new StreamSource(f), new StreamResult(writer))
      val transformedXML: Elem = XML.loadString(writer.toString)
      usageMsg.assert(transformedXML)

      assert(transformedXML, "count(/atom:entry/atom:category[starts-with(@term,'tid:')]) = 1")
      assert(transformedXML, "count(/atom:entry/atom:category[starts-with(@term,'dc:')]) = 1")
      assert(transformedXML, "count(/atom:entry/atom:category[starts-with(@term,'rgn:')]) = 1")
      assert(transformedXML, "count(/atom:entry/atom:id[starts-with(text(),'urn:uuid:')]) = 1")

      val xpath = xpathFactory.newXPath()
      xpath.setNamespaceContext(this)

      //for a cadf event with contentType as 'auditData'
      if (xpath.evaluate("exists(/atom:entry/atom:content/cadf:event)", new StreamSource(new StringReader(transformedXML.toString())), XPathConstants.BOOLEAN).asInstanceOf[Boolean] &&
        xpath.evaluate("exists(/atom:entry/atom:content/cadf:event/cadf:attachments/cadf:attachment/cadf:content/*[local-name() = 'auditData'])", new StreamSource(new StringReader(transformedXML.toString())), XPathConstants.BOOLEAN).asInstanceOf[Boolean]) {
        
        assert(transformedXML, "count(/atom:entry/atom:category[starts-with(@term,'username:')]) = 1")
      }
      
      //if its not a cadf event
      if (!xpath.evaluate("exists(/atom:entry/atom:content/cadf:event)", new StreamSource(new StringReader(transformedXML.toString())), XPathConstants.BOOLEAN).asInstanceOf[Boolean]) {
        assert(transformedXML, "count(/atom:entry/atom:category[starts-with(@term,'rid:')]) = 1")
      }
      
    }
  })
  
  sampleJsonFiles.foreach ( f => {
    test("Json Sample "+f.getAbsolutePath+" should be valid ") {
      printf("Checking %s\n",f.getAbsolutePath)
      val json_sample = scala.io.Source.fromFile(f.getAbsolutePath).mkString

      // parse and assert valid json
      val jsonObjects = JSON.parseFull(json_sample).get.asInstanceOf[Map[String,Any]]
      assert(jsonObjects != null, " valid json objects")

      //assert entry element
      val entryObject = jsonObjects.get("entry").get.asInstanceOf[Map[String,Any]]
      assert(entryObject != null, "  should have an entry")

      //assert content element
      var eventParent = entryObject.get("content").get.asInstanceOf[Map[String,Any]]
      assert(eventParent != null, "  entry should have a content")

      //assert event element
      if ( f.getAbsolutePath().contains("usagedeadletter") ) {
        // special handling for usagedeadletter
        eventParent = eventParent.get("eventError").get.asInstanceOf[Map[String,Any]]
        assert(eventParent != null, "  content should have an eventError")
      }

      val eventObject = eventParent.get("event").get.asInstanceOf[Map[String,Any]]
      assert(eventObject != null, "  content should have an event")
      assert(eventObject.get("id") != null, "  event should have an id")
      assert(eventObject.get("type") != null, "  event should have a type")
    }
  })

  val sampleFeedToFilePairs = sampleFiles.map(toFeedFile).flatten

  sampleFeedToFilePairs.foreach {
    case (feed, fl) => {

      test("Sample " + fl.getAbsolutePath + " should be valid against feed " + feed) {
        printf("Checking %s against feed %s\n", fl.getAbsolutePath, feed)
          atomValidator.validate(request("POST", feed, "application/atom+xml", XML.loadFile(fl), SERVICE_ADMIN), response, chain)
      }
    }
  }

  sampleFeedToFilePairs.collect { case (feed: String, fl: File) => feed }.distinct.foreach { feed =>
      test("Getting feed " + feed + " should work") {
        atomValidator.validate(requestRole("GET", feed, SERVICE_ADMIN), response, chain)
      }
      test("Getting individual entry for feed " + feed + " should work") {
        atomValidator.validate(requestRole("GET", feed + "/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4", SERVICE_ADMIN), response, chain)
      }
  }

  badSampleFiles.map(toFeedCodeMessagesFile).filter(_ != None).map(_.get).foreach(f => {

    test("Sample " + f._4.getAbsolutePath + " should fail in the expected way when posted on feed " + f._1) {
      printf("Checking %s against feed %s\n", f._4.getAbsolutePath, f._1)
        val r = assertResultFailed(atomValidator.validate(request("POST", f._1, "application/atom+xml", XML.loadFile(f._4), SERVICE_ADMIN), response, chain), f._2, f._3)
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
      feedAttr.split(" ").toList.map { feed =>
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
