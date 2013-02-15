package com.rackspace.usage

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
//  Tests on usage transformations
//

@RunWith(classOf[JUnitRunner])
class TransformSuite extends BaseUsageSuite {
  //
  //  A valid CBS entry, used for the tests below
  //
  val validCBSMessage = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                   <atom:title>CBS Usage</atom:title>
                                   <atom:content type="application/xml">
                                     <event xmlns="http://docs.rackspace.com/core/event"
                                             xmlns:cbs="http://docs.rackspace.com/usage/cbs"
                                             version="1" tenantId="12334"
                                             resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
                                             resourceName="MyVolume"
                                             id="560490c6-6c63-11e1-adfe-27851d5aed13"
                                             type="USAGE" dataCenter="DFW1" region="DFW"
                                             startTime="2012-03-12T11:51:11Z"
                                             endTime="2012-03-12T15:51:11Z">
                                       <cbs:product version="1" serviceCode="CloudBlockStorage"
                                                    resourceType="VOLUME"
                                                    type="SATA"
                                                    provisioned="120"/>
                                     </event>
                                     </atom:content>
                                   </atom:entry>
  //
  //  A valid CBS entry with an entryID  used for the tests below
  //
  val validCBSMessageWithID = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                   <atom:id>urn:uuid:123f6548-778c-11e2-95e4-002500a28a7a</atom:id>
                                   <atom:title>CBS Usage</atom:title>
                                   <atom:content type="application/xml">
                                     <event xmlns="http://docs.rackspace.com/core/event"
                                             xmlns:cbs="http://docs.rackspace.com/usage/cbs"
                                             version="1" tenantId="12334"
                                             resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
                                             resourceName="MyVolume"
                                             id="560490c6-6c63-11e1-adfe-27851d5aed13"
                                             type="USAGE" region="DFW" dataCenter="DFW1"
                                             startTime="2012-03-12T11:51:11Z"
                                             endTime="2012-03-12T15:51:11Z">
                                       <cbs:product version="1" serviceCode="CloudBlockStorage"
                                                    resourceType="VOLUME"
                                                    type="SATA"
                                                    provisioned="120"/>
                                     </event>
                                     </atom:content>
                                   </atom:entry>

  val validCFCDNMessage = <atom:entry xmlns="http://docs.rackspace.com/core/event"
                                      xmlns:cf-cdn="http://docs.rackspace.com/usage/cloudfiles/cdnbandwidth"
                                      xmlns:atom="http://www.w3.org/2005/Atom">
                                        <atom:content type="application/xml">
                                          <event endTime="2012-06-20T10:19:52Z" startTime="2012-06-19T10:19:52Z"
                                                 type="USAGE"
                                                 id="8d89673c-c989-11e1-895a-0b3d632a8a89"
                                                 tenantId="1234" version="1">
                                              <cf-cdn:product version="1" serviceCode="CloudFiles"
                                                              cdnBandwidthOut="2999283"/>
                                          </event>
                                        </atom:content>
                                      </atom:entry>

  test("The generated event ID should become the entry ID on a validated feed (usagetest1/events)") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:id) = 1")
    assert(getProcessedXML(req), "/atom:entry/atom:id = 'urn:uuid:560490c6-6c63-11e1-adfe-27851d5aed13'")
  }

  test("The generated event ID should become the entry ID on a validated feed, existing ids should be overwirtten (usagetest1/events)") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessageWithID)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:id) = 1")
    assert(getProcessedXML(req), "/atom:entry/atom:id = 'urn:uuid:560490c6-6c63-11e1-adfe-27851d5aed13'")
  }

  test("No IDs should be inserted on an unvalidated feed (usagetest7/events)") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:id) = 0")
  }

  test("Existing IDs should remain unchanged on an unvalidated feed (usagetest7/events)") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessageWithID)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:id) = 1")
    assert(getProcessedXML(req), "/atom:entry/atom:id = 'urn:uuid:123f6548-778c-11e2-95e4-002500a28a7a'")
  }

  test ("Tenat ID should be added as a category on a validated feed (usagetest1/events)") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'tid:12334'")
  }

  test ("Tenat ID should not be added as a category on an unvalidated feed (usagetest7/events)") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'tid:12334']) = 0")
  }

  test ("Resource ID should be added as a category on a validated feed (usagetest1/events)") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'rid:4a2b42f4-6c63-11e1-815b-7fcbcf67f549'")
  }

  test ("Resource ID should not be added as a category on an validated feed (usagetest1/events) for a non-resource message") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCFCDNMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[contains(@term,'rid:')]) = 0")
  }

  test ("Resource ID should not be added as a category on an unvalidated feed (usagetest7/events)") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'rid:4a2b42f4-6c63-11e1-815b-7fcbcf67f549']) = 0")
  }

  test ("Datacenter should be added as a category on validaded feed (usagetest1/events)") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'dc:DFW1'")
  }

  test ("Global Datacenter should be added as a category on validaded feed (usagetest1/events) when the entry does not specfy a datacenter") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCFCDNMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'dc:GLOBAL'")
  }

  test ("Datacenter should not be added as a category on an unvalidated feed (usagetest7/events)") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'dc:DFW1']) = 0")
  }

  test ("Global Datacenter should not be added as a category on unvalidaded feed (usagetest7/events) when the entry does not specfy a datacenter") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCFCDNMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'dc:GLOBAL']) = 0")
  }

  test ("Region should be added as a category on validaded feed (usagetest1/events)") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'rgn:DFW'")
  }

  test ("Global Region should be added as a category on validaded feed (usagetest1/events) when the entry does not specfy a region") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCFCDNMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'rgn:GLOBAL'")
  }

  test ("Region should not be added as a category on an unvalidated feed (usagetest7/events)") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'rgn:DFW']) = 0")
  }

  test ("Global Region should not be added as a category on unvalidaded feed (usagetest7/events) when the entry does not specfy a region") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCFCDNMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'rgn:GLOBAL']) = 0")
  }

  test("In a validated feed (usagetest1/events) a message with a resource type should have a category term of {serviceCode}.{NSPart}.{resourceType}.{eventType}") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'cloudblockstorage.cbs.volume.usage'")
  }

  test("In an uvalidated feed (usagetest7/events) a message with a resource type should NOT have a category term of {serviceCode}.{NSPart}.{resourceType}.{eventType}") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'cloudblockstorage.cbs.volume.usage']) = 0")
  }

  test("In a validated feed (usagetest1/events) a message without a resource type should have a category term of {serviceCode}.{NSPart}.{eventType}") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCFCDNMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'cloudfiles.cdnbandwidth.usage'")
  }

  test("In an uvalidated feed (usagetest7/events) a message with a resource type should NOT have a category term of {serviceCode}.{NSPart}.{eventType}") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCFCDNMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'cloudfiles.cdnbandwidth.usage']) = 0")
  }
}
