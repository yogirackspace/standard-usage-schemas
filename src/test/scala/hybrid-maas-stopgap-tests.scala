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
//  Tests on hybrid stopgap transformations
//

@RunWith(classOf[JUnitRunner])
class HybridMaasStopGapSuite extends BaseUsageSuite {
  val maasEventWithHybridTenant = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom"
                                              xmlns:maas="http://docs.rackspace.com/usage/maas"
                                              xmlns="http://docs.rackspace.com/core/event">
                                              <atom:title>MaaSEvent</atom:title>
                                              <atom:category term="monitoring.check.usage"/>
                                              <atom:content type="application/xml">
                                                  <event version="1" tenantId="hybrid:7777"
                                                         resourceId="chAAAA"
                                                         id="a2869958-a020-11e1-b15c-a38f4c3d83a9"
                                                         type="USAGE" startTime="2012-04-30T03:27:35Z"
                                                         endTime="2012-04-30T03:27:36Z">
                                                    <maas:product version="1" serviceCode="CloudMonitoring" resourceType="CHECK"
                                                                  monitoringZones="3" checkType="remote.http"/>
                                                  </event>
                                              </atom:content>
                                   </atom:entry>

  val maasEventWithoutHybridTenant = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom"
                                              xmlns:maas="http://docs.rackspace.com/usage/maas"
                                              xmlns="http://docs.rackspace.com/core/event">
                                              <atom:title>MaaSEvent</atom:title>
                                              <atom:category term="monitoring.check.usage"/>
                                              <atom:content type="application/xml">
                                                  <event version="1" tenantId="7777"
                                                         resourceId="chAAAA"
                                                         id="a2869958-a020-11e1-b15c-a38f4c3d83a9"
                                                         type="USAGE" startTime="2012-04-30T03:27:35Z"
                                                         endTime="2012-04-30T03:27:36Z">
                                                    <maas:product version="1" serviceCode="CloudMonitoring" resourceType="CHECK"
                                                                  monitoringZones="3" checkType="remote.http"/>
                                                  </event>
                                              </atom:content>
                                   </atom:entry>

  test("In a validated feed (usagetest1/events) a message with a hybrid event should have a category of a category term of {serviceCode}.{NSPart}.{resourceType}.{eventType}.hybrid") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", maasEventWithHybridTenant)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'cloudmonitoring.maas.check.usage.hybrid'")
  }

  test("In a validated feed (usagetest1/events) a message with a non-hybrid event should have a category of a category term of {serviceCode}.{NSPart}.{resourceType}.{eventType}") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", maasEventWithoutHybridTenant)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:category/@term = 'cloudmonitoring.maas.check.usage'")
  }

  test("In a validated feed (usagetest1/events) a hybrid event should remove category term of monitoring.check.usage") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", maasEventWithHybridTenant)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count (/atom:entry/atom:category[@term = 'monitoring.check.usage']) = 0")
  }

  test("In a validated feed (usagetest1/events) a hybrid event should NOT remove category term of monitoring.check.usage") {
    val req = request("POST", "/usagetest1/events", "application/atom+xml", maasEventWithoutHybridTenant)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count (/atom:entry/atom:category[@term = 'monitoring.check.usage']) = 1")
  }
}
