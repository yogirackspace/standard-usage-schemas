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

  val validMaaSMessage = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom"
                                        xmlns:maas="http://docs.rackspace.com/usage/maas"
                                        xmlns="http://docs.rackspace.com/core/event">
                              <atom:title>MaaSEvent</atom:title>
                              <atom:content type="application/xml">
                                <event version="1" tenantId="7777"
                                       resourceId="chAAAA"
                                       id="a2869958-a020-11e1-b15c-a38f4c3d83a9"
                                       type="USAGE"
                                       startTime="2012-04-30T03:27:35Z"
                                       endTime="2012-04-30T03:27:36Z">
                                  <maas:product version="1" serviceCode="CloudMonitoring" resourceType="CHECK"
                                                monitoringZones="3" checkType="remote.http"/>
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

  test ("Tenant ID should be added as a category on a validated feed (usagetest1/events)") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'tid:12334'")
  }

  test ("Tenant ID should not be added as a category on an unvalidated feed (usagetest7/events)") {
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

  test("In a validated feed (usagetest1/events) a message with a resource type should have a category term of {serviceCode}.{NSPart}.{resourceType}.{eventType}") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'cloudblockstorage.cbs.volume.usage'")
  }

  test("In an unvalidated feed (usagetest7/events) a message with a resource type should NOT have a category term of {serviceCode}.{NSPart}.{resourceType}.{eventType}") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'cloudblockstorage.cbs.volume.usage']) = 0")
  }

  test("In a validated feed (usagetest1/events) a message without a resource type should have a category term of {serviceCode}.{NSPart}.{eventType}") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validCFCDNMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'cloudfiles.cdnbandwidth.usage'")
  }

  test("In an unvalidated feed (usagetest7/events) a message with a resource type should NOT have a category term of {serviceCode}.{NSPart}.{eventType}") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validCFCDNMessage)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'cloudfiles.cdnbandwidth.usage']) = 0")
  }

  test("Mark cloudserversopenstack.nova.server.usage with cloudfeeds:private category" ) {
    val body = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
      <atom:title>Nagios Event</atom:title>
      <atom:content type="application/xml">
        <event xmlns="http://docs.rackspace.com/core/event"
               xmlns:nova="http://docs.rackspace.com/event/nova"
               version="1" id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
               resourceId="f37bca20-29c5-4e08-97f4-e47908887bc1" resourceName="testserver78193259535"
               dataCenter="IAD3" region="IAD"
               tenantId="231423"
               startTime="2013-05-15T11:51:11Z" endTime="2013-05-16T11:51:11Z"
               type="USAGE">
          <nova:product version="1" serviceCode="CloudServersOpenStack"
                        resourceType="SERVER" flavorId="3" flavorName="1024MB" status="ACTIVE"
                        osLicenseType="RHEL" bandwidthIn="640034"
                        bandwidthOut="345123"
          />
        </event>
      </atom:content>
    </atom:entry>

    val req = request("POST", "/usagetest1/events", "application/atom+xml", body)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'cloudfeeds:private'")

  }
}
