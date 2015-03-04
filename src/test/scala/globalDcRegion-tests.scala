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
//  Tests on usage transformations on DC/Region. Requirements:
//  
//  Event Type   |            DC/Region
//  ---------------------------------------------
//               | GLOBAL | not GLOBAL | not sent
//  ---------------------------------------------
//  MaaS Check   | Allow  |  Allow     | default to GLOBAL
//  Sites subs   | Allow  |  Allow     | default to GLOBAL
//  Domain Reg   | Allow  |  Allow     | default to GLOBAL
//  Other events | Reject |  Allow     | Reject
// 
//  Matrix coverage:
//  ================
//  1. Against Product feeds
//
//  Event Type   |            DC/Region
//  ---------------------------------------------
//               | GLOBAL | not GLOBAL | not sent
//  ---------------------------------------------
//  MaaS Check   |   #1   |     #2     |   #3
//  Other events |   #4   |     #5     |   #6
//
//  2. Same as above except against the /usagetest* validated feed
//
//  Event Type   |            DC/Region
//  ---------------------------------------------
//               | GLOBAL | not GLOBAL | not sent
//  ---------------------------------------------
//  MaaS Check   |   #7   |     #8     |   #9
//  Other events |   #10  |     #11    |   #12
//
//  3. Same as above except against the /usagetest* non validated feed
//
//  Event Type   |            DC/Region
//  ---------------------------------------------
//               | GLOBAL | not GLOBAL | not sent
//  ---------------------------------------------
//  MaaS Check   |   #13  |     #14    |   #15
//  Other events |   #16  |     #17    |   #18
//

@RunWith(classOf[JUnitRunner])
class GlobalDcRegionSuites extends BaseUsageSuite {


  val maasMessageWithGlobalDCRegion = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom"
                                                       xmlns:maas="http://docs.rackspace.com/usage/maas"
                                                       xmlns="http://docs.rackspace.com/core/event">
                                    <atom:title>MaaSEvent</atom:title>
                                    <atom:content type="application/xml">
                                      <event version="1" tenantId="7777"
                                             resourceId="chAAAA"
                                             id="a2869958-a020-11e1-b15c-a38f4c3d83a9"
                                             type="USAGE" dataCenter="GLOBAL" region="GLOBAL"
                                             startTime="2012-04-30T03:27:35Z"
                                             endTime="2012-04-30T03:27:36Z">
                                        <maas:product version="1" serviceCode="CloudMonitoring" resourceType="CHECK"
                                                      monitoringZones="3" checkType="remote.http"/>
                                      </event>
                                    </atom:content>
                                  </atom:entry>

  val maaSMessageWithORDDCRegion = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom"
                                                       xmlns:maas="http://docs.rackspace.com/usage/maas"
                                                       xmlns="http://docs.rackspace.com/core/event">
                          <atom:title>MaaSEvent</atom:title>
                          <atom:content type="application/xml">
                            <event version="1" tenantId="7777"
                                   resourceId="chAAAA"
                                   id="a2869958-a020-11e1-b15c-a38f4c3d83a9"
                                   type="USAGE" dataCenter="ORD1" region="ORD"
                                   startTime="2012-04-30T03:27:35Z"
                                   endTime="2012-04-30T03:27:36Z">
                              <maas:product version="1" serviceCode="CloudMonitoring" resourceType="CHECK"
                                            monitoringZones="3" checkType="remote.http"/>
                            </event>
                          </atom:content>
                        </atom:entry>


  val validMaaSMessageWithoutDCRegion = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom"
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

  val maasNonStandardMessage = "<entry xmlns=\"http://www.w3.org/2005/Atom\">" +
                               "<title>MaasEvent</title>" +
                               "<category term=\"monitoring.lifecycle\"/>" +
                               "<content type=\"application/json\">" +
                               "  {\"txn_id\": \".rh-DL6s.h-ord1-maas-stage-api0.r-3hj7YV2w.c-3297.ts-1384473268889.v-31b5052\", \"tenant_id\": \"5821444\"," +
                               "   \"type\": \"check.create\", \"account_id\": \"acaHrD8D0G\", \"key\": \"chCZwVK1b1\",\"entity_id\":\"ensrj2sgwg\", " +
                               "    \"timestamp\": 1384473269241}" +
                               "</content>" +
                               "</entry>"

  val sitesMessageWithoutDCRegion = <atom:entry xmlns="http://docs.rackspace.com/core/event" xmlns:atom="http://www.w3.org/2005/Atom"
                                                xmlns:s="http://docs.rackspace.com/usage/sites/ssl">
                              <atom:title type="text">Sites</atom:title>
                              <atom:content type="application/xml">
                                <event
                                xmlns="http://docs.rackspace.com/core/event"
                                xmlns:s="http://docs.rackspace.com/usage/sites/ssl"
                                eventTime="2012-06-14T10:19:52Z"
                                type="USAGE_SNAPSHOT"
                                resourceId="my.site.com" resourceName="my.site.com"
                                id="47b574c4-0400-11e2-b19d-7b8e208cade0"
                                tenantId="12882" version="1">
                                  <s:product serviceCode="CloudSites" version="1"
                                             resourceType="SITE" SSLenabled="true"/>
                                </event>
                              </atom:content>
                            </atom:entry>

  //
  //  A valid CBS entry, used for the tests below
  //
  val cbsMessageWithDFWDcRegion = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
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
  //  An invalid CBS entry without a DC/Region
  //
  val cbsMessageWithoutDCRegion = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                   <atom:id>urn:uuid:123f6548-778c-11e2-95e4-002500a28a7a</atom:id>
                                   <atom:title>CBS Usage</atom:title>
                                   <atom:content type="application/xml">
                                     <event xmlns="http://docs.rackspace.com/core/event"
                                             xmlns:cbs="http://docs.rackspace.com/usage/cbs"
                                             version="1" tenantId="12334"
                                             resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
                                             resourceName="MyVolume"
                                             id="560490c6-6c63-11e1-adfe-27851d5aed13"
                                             type="USAGE"
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
  //  An invalid CBS entry without a DC/Region
  //
  val cbsMessageWithGlobalDCRegion = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                        <atom:id>urn:uuid:123f6548-778c-11e2-95e4-002500a28a7a</atom:id>
                                        <atom:title>CBS Usage</atom:title>
                                        <atom:content type="application/xml">
                                          <event xmlns="http://docs.rackspace.com/core/event"
                                                 xmlns:cbs="http://docs.rackspace.com/usage/cbs"
                                                 version="1" tenantId="12334"
                                                 resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
                                                 resourceName="MyVolume"
                                                 id="560490c6-6c63-11e1-adfe-27851d5aed13"
                                                 type="USAGE" dataCenter="GLOBAL" region="GLOBAL"
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
  //  An invalid autoscale USAGE entry with a bad region and dc combination
  //
  val autoscaleUsageMessageWithBadRegionAndDc = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                                  <atom:id>urn:uuid:123f6548-778c-11e2-95e4-002500a28a7a</atom:id>
                                                  <atom:title>autoscale</atom:title>
                                                  <atom:content type="application/xml">
                                                    <event xmlns="http://docs.rackspace.com/core/event"
                                                           xmlns:sample="http://docs.rackspace.com/event/autoscale"
                                                           id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
                                                           version="2"
                                                           eventTime="2013-03-15T11:51:11Z"
                                                           type="USAGE"
                                                           region="DFW"
                                                           dataCenter="ORD1">
                                                      <sample:product serviceCode="Autoscale"
                                                                      version="1"
                                                                      scalingGroupId="6e8bc430-9c3a-11d9-9669-0800200c9a66"
                                                                      desiredCapacity="5"
                                                                      currentCapacity="3"
                                                                      message="Launching 2 servers"/>
                                                    </event>
                                                  </atom:content>
                                                </atom:entry>

  //
  //  An invalid autoscale USAGE_SUMMARY entry with a bad region and dc combination
  //
  val autoscaleUsageSummaryMessageWithBadRegionAndDc = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                                        <atom:id>urn:uuid:123f6548-778c-11e2-95e4-002500a28a7a</atom:id>
                                                        <atom:title>autoscale</atom:title>
                                                        <atom:content type="application/xml">
                                                          <event xmlns="http://docs.rackspace.com/core/event"
                                                                 xmlns:sample="http://docs.rackspace.com/event/autoscale"
                                                                 id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
                                                                 version="2"
                                                                 eventTime="2013-03-15T11:51:11Z"
                                                                 type="USAGE_SUMMARY"
                                                                 region="DFW"
                                                                 dataCenter="HKG1">
                                                            <sample:product serviceCode="Autoscale"
                                                                            version="1"
                                                                            scalingGroupId="6e8bc430-9c3a-11d9-9669-0800200c9a66"
                                                                            desiredCapacity="5"
                                                                            currentCapacity="3"
                                                                            message="Launching 2 servers"/>
                                                          </event>
                                                        </atom:content>
                                                      </atom:entry>

  //
  //  An invalid autoscale USAGE_SNAPSHOT entry with a bad region and dc combination
  //
  val autoscaleUsageSnapshotMessageWithBadRegionAndDc = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                                          <atom:id>urn:uuid:123f6548-778c-11e2-95e4-002500a28a7a</atom:id>
                                                          <atom:title>autoscale</atom:title>
                                                          <atom:content type="application/xml">
                                                            <event xmlns="http://docs.rackspace.com/core/event"
                                                                   xmlns:sample="http://docs.rackspace.com/event/autoscale"
                                                                   id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
                                                                   version="2"
                                                                   eventTime="2013-03-15T11:51:11Z"
                                                                   type="USAGE_SNAPSHOT"
                                                                   region="ORD"
                                                                   dataCenter="HKG1">
                                                              <sample:product serviceCode="Autoscale"
                                                                              version="1"
                                                                              scalingGroupId="6e8bc430-9c3a-11d9-9669-0800200c9a66"
                                                                              desiredCapacity="5"
                                                                              currentCapacity="3"
                                                                              message="Launching 2 servers"/>
                                                            </event>
                                                          </atom:content>
                                                        </atom:entry>

  //
  //  A valid autoscale NON-USAGE entry with a bad region and dc combination
  //
  val autoscaleInfoMessageWithBadRegionAndDc = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                                <atom:id>urn:uuid:123f6548-778c-11e2-95e4-002500a28a7a</atom:id>
                                                <atom:title>autoscale</atom:title>
                                                <atom:content type="application/xml">
                                                  <event xmlns="http://docs.rackspace.com/core/event"
                                                         xmlns:sample="http://docs.rackspace.com/event/autoscale"
                                                         id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
                                                         version="2"
                                                         eventTime="2013-03-15T11:51:11Z"
                                                         type="INFO"
                                                         region="ORD"
                                                         dataCenter="HKG1">
                                                    <sample:product serviceCode="Autoscale"
                                                                    version="1"
                                                                    scalingGroupId="6e8bc430-9c3a-11d9-9669-0800200c9a66"
                                                                    desiredCapacity="5"
                                                                    currentCapacity="3"
                                                                    message="Launching 2 servers"/>
                                                  </event>
                                                </atom:content>
                                              </atom:entry>

  //
  //  A valid autoscale NON-USAGE entry without region and datacenter attributes
  //
  val autoscaleInfoMessageWithoutRegionAndDc = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                          <atom:id>urn:uuid:123f6548-778c-11e2-95e4-002500a28a7a</atom:id>
                                          <atom:title>autoscale</atom:title>
                                          <atom:content type="application/xml">
                                            <event xmlns="http://docs.rackspace.com/core/event"
                                                 xmlns:sample="http://docs.rackspace.com/event/autoscale"
                                                 id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
                                                 version="2"
                                                 eventTime="2013-03-15T11:51:11Z"
                                                 type="INFO">
                                              <sample:product serviceCode="Autoscale"
                                                version="1"
                                                scalingGroupId="6e8bc430-9c3a-11d9-9669-0800200c9a66"
                                                desiredCapacity="5"
                                                currentCapacity="3"
                                                message="Launching 2 servers"/>
                                            </event>
                                          </atom:content>
                                        </atom:entry>

  //
  //  A valid autoscale NON-USAGE entry with a valid region and no dc
  //
  val autoscaleInfoMessageWithRegionAndNoDc = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                            <atom:id>urn:uuid:123f6548-778c-11e2-95e4-002500a28a7a</atom:id>
                                            <atom:title>autoscale</atom:title>
                                            <atom:content type="application/xml">
                                              <event xmlns="http://docs.rackspace.com/core/event"
                                                     xmlns:sample="http://docs.rackspace.com/event/autoscale"
                                                     id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
                                                     version="2"
                                                     eventTime="2013-03-15T11:51:11Z"
                                                     type="INFO"
                                                     region="DFW">
                                                <sample:product serviceCode="Autoscale"
                                                                version="1"
                                                                scalingGroupId="6e8bc430-9c3a-11d9-9669-0800200c9a66"
                                                                desiredCapacity="5"
                                                                currentCapacity="3"
                                                                message="Launching 2 servers"/>
                                              </event>
                                            </atom:content>
                                          </atom:entry>

  // #1
  test ("#1 - Datacenter should be added as a category on product feed (monitoring/events) for MaaS events with GLOBAL DC/Region") {
    val req = request("POST", "/monitoring/events", "application/atom+xml", maasMessageWithGlobalDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'dc:GLOBAL'")
  }

  // #2
  test ("#2 - Datacenter should be added as a category on product feed (monitoring/events) for MaaS events with ORD DC/Region") {
    val req = request("POST", "/monitoring/events", "application/atom+xml", maaSMessageWithORDDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'dc:ORD1'")
  }
  
  // #3
  test ("#3 - GLOBAL Datacenter should be added as a category on product feed (monitoring/events) for MaaS events when the entry does not specify a datacenter") {
    val req = request("POST", "/monitoring/events", "application/atom+xml", validMaaSMessageWithoutDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'dc:GLOBAL'")
  }

  // #3a
  test ("#3a - Monitoring non-standard events should be accepted on product feed (monitoring/events)") {
    val req = request("POST", "/monitoring/events", "application/atom+xml", XML.loadString( maasNonStandardMessage ), SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
  }

  // #4
  test ("#4 - GLOBAL Datacenter should be not be allowed on product feed (cbs/events) for CBS events") {
    val req = request("POST", "/cbs/events", "application/atom+xml", cbsMessageWithGlobalDCRegion, SERVICE_ADMIN)
    assertResultFailed(atomValidator.validate(req, response, chain))
  }

  // #5
  test ("#5 - Datacenter should be added as a category on product feed (cbs/events) for CBS events") {
    val req = request("POST", "/cbs/events", "application/atom+xml", cbsMessageWithDFWDcRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'dc:DFW1'")
  }

  // #6
  test ("#6 - Events without Datacenter should be not be allowed on product feed (cbs/events) for CBS events") {
    val req = request("POST", "/cbs/events", "application/atom+xml", cbsMessageWithoutDCRegion, SERVICE_ADMIN)
    assertResultFailed(atomValidator.validate(req, response, chain))
  }

  // #6
  test ("#6 - Events without Datacenter should be not be allowed on product feed (sites/events) for Sites SSL events") {
    val req = request("POST", "/sites/events", "application/atom+xml", sitesMessageWithoutDCRegion, SERVICE_ADMIN)
    assertResultFailed(atomValidator.validate(req, response, chain))
  }

  // #7
  test ("#7 - Datacenter should be added as a category on validated feed (usagetest1/events) for MaaS events with GLOBAL DC/Region") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", maasMessageWithGlobalDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'dc:GLOBAL'")
  }

  // #8
  test ("#8 - Datacenter should be added as a category on validated feed (usagetest1/events) for MaaS events with ORD DC/Region") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", maaSMessageWithORDDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'dc:ORD1'")
  }

  // #9
  test ("#9 - GLOBAL Datacenter should be added as a category on validated feed (usagetest1/events) for MaaS events when the entry does not specify a datacenter") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", validMaaSMessageWithoutDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'dc:GLOBAL'")
  }

  // #10
  // This test is disabled for now because this particular check is only available in product.wadl,
  // and not included in Validated feeds, such as /usagetest1 feed.
//  test ("#10 - GLOBAL Datacenter should be not be allowed on validated feed (usagetest1/events) for CBS events") {
//    val req = request("POST", "/usagetest1/events", "application/atom+xml", cbsMessageWithGlobalDCRegion)
//    assertResultFailed(atomValidator.validate(req, response, chain))
//  }

  // #11
  test ("#11 - Datacenter should be added as a category on validated feed (usagetest1/events) for CBS events") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", cbsMessageWithDFWDcRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'dc:DFW1'")
  }

  // #12
  // This test is disabled for now because this particular check is only available in product.wadl,
  // and not included in Validated feeds, such as /usagetest1 feed.
//  test ("#12 - Events without Datacenter should be not be allowed on validated feed (usagetest1/events) for CBS events") {
//    val req = request("POST", "/usagetest1/events", "application/atom+xml", cbsMessageWithoutDCRegion)
//    assertResultFailed(atomValidator.validate(req, response, chain))
//  }

  // #13
  test ("#13 - Datacenter should NOT be added as a category on non validated feed (usagetest7/events) for MaaS events with GLOBAL DC/Region") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", maasMessageWithGlobalDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'dc:GLOBAL']) = 0")
  }

  // #14
  test ("#14 - Datacenter should NOT be added as a category on non validated feed (usagetest7/events) for MaaS events with ORD DC/Region") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", maaSMessageWithORDDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'dc:ORD1']) = 0")
  }

  // #15
  test ("#15 - GLOBAL Datacenter should NOT be added as a category on non validated feed (usagetest7/events) for MaaS events when the entry does not specify a datacenter") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", validMaaSMessageWithoutDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'dc:GLOBAL']) = 0")
  }

  // #16
  test ("#16 - GLOBAL Datacenter should be be allowed on non validated feed (usagetest7/events) for CBS events") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", cbsMessageWithGlobalDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
  }

  // #17
  test ("#17 - Datacenter should NOT be added as a category on non validated feed (usagetest7/events) for CBS events") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", cbsMessageWithDFWDcRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'dc:DFW1']) = 0")
  }

  // #18
  test ("#18 - Events without Datacenter should be allowed on non validated feed (usagetest7/events) for CBS events") {
    val req = request("POST", "/usagetest7/events", "application/atom+xml", cbsMessageWithoutDCRegion, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
  }

  // #19
  test ("#19 - Events with invalid region and datacenter combination should NOT be allowed on product feed for Autoscale USAGE events") {
    val req = request("POST", "/autoscale/events", "application/atom+xml", autoscaleUsageMessageWithBadRegionAndDc, SERVICE_ADMIN)
    assertResultFailed(atomValidator.validate(req, response, chain))
  }

  // #20
  test ("#20 - Events with invalid region and datacenter combination should NOT be allowed on product feed for Autoscale USAGE_SUMMARY events") {
    val req = request("POST", "/autoscale/events", "application/atom+xml", autoscaleUsageSummaryMessageWithBadRegionAndDc, SERVICE_ADMIN)
    assertResultFailed(atomValidator.validate(req, response, chain))
  }

  // #21
  test ("#21 - Events with invalid region and datacenter combination should NOT be allowed on product feed for Autoscale USAGE_SNAPSHOT events") {
    val req = request("POST", "/autoscale/events", "application/atom+xml", autoscaleUsageSnapshotMessageWithBadRegionAndDc, SERVICE_ADMIN)
    assertResultFailed(atomValidator.validate(req, response, chain))
  }

  // #22
  test ("#22 - Events with invalid region and datacenter combination should be allowed on product feed for Autoscale non-usage events") {
    val req = request("POST", "/autoscale/events", "application/atom+xml", autoscaleInfoMessageWithBadRegionAndDc, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
  }

  // #23
  test ("#23 - Events without datacenter and region should be allowed on product feed (e.g autoscale/events) for Autoscale non-usage events") {
    val req = request("POST", "/autoscale/events", "application/atom+xml", autoscaleInfoMessageWithoutRegionAndDc, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
  }

  // #24
  test ("#24 - Events with region and no datacenter should be allowed on product feed (e.g autoscale/events) for Autoscale non-usage events") {
    val req = request("POST", "/autoscale/events", "application/atom+xml", autoscaleInfoMessageWithRegionAndNoDc, SERVICE_ADMIN)
    atomValidator.validate(req, response, chain)
  }
}
