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
//  Tests on lbaas stopgap transformations
//

@RunWith(classOf[JUnitRunner])
class LbaasStopGapSuite extends BaseUsageSuite {

  register ("lbaas","http://docs.rackspace.com/usage/lbaas")

  val lbaasSSLOff = <atom:entry xmlns="http://docs.rackspace.com/core/event" xmlns:atom="http://www.w3.org/2005/Atom"
                                xmlns:lbaas="http://docs.rackspace.com/usage/lbaas">
                                <atom:title type="text">LBAAS</atom:title>
                                <atom:content type="application/xml">
                                    <event resourceName="LoadBalancer" endTime="2012-06-15T10:19:52Z"
                                          startTime="2012-06-14T10:19:52Z" region="DFW" dataCenter="DFW1" type="USAGE"
                                          id="b79cc3de-b399-3883-b555-61829bb7f966"
                                          resourceId="b79cc3de-b399-3883-b555-61829bbccd38" tenantId="1" version="1">
                                      <lbaas:product sslMode="OFF" vipType="PUBLIC" numVips="44" numPolls="10" bandWidthOutSsl="345345346"
                                                    bandWidthInSsl="364646770" bandWidthOut="3460346" bandWidthIn="43456346"
                                                    avgConcurrentConnectionsSsl="4566.0"
                                                    avgConcurrentConnections="30000.0" serviceCode="CloudLoadBalancers"
                                                    resourceType="LOADBALANCER" status="ACTIVE"
                                                    version="1"/>
                                    </event>
                                  </atom:content>
                                </atom:entry>

  val lbaasSSLOn = <atom:entry xmlns="http://docs.rackspace.com/core/event" xmlns:atom="http://www.w3.org/2005/Atom"
                                xmlns:lbaas="http://docs.rackspace.com/usage/lbaas">
                                <atom:title type="text">LBAAS</atom:title>
                                <atom:content type="application/xml">
                                    <event resourceName="LoadBalancer" endTime="2012-06-15T10:19:52Z"
                                          startTime="2012-06-14T10:19:52Z" region="DFW" dataCenter="DFW1" type="USAGE"
                                          id="b79cc3de-b399-3883-b555-61829bb7f966"
                                          resourceId="b79cc3de-b399-3883-b555-61829bbccd38" tenantId="1" version="1">
                                      <lbaas:product sslMode="ON" vipType="PUBLIC" numVips="44" numPolls="10" bandWidthOutSsl="345345346"
                                                    bandWidthInSsl="364646770" bandWidthOut="0" bandWidthIn="0"
                                                    avgConcurrentConnectionsSsl="4566.0"
                                                    avgConcurrentConnections="0" serviceCode="CloudLoadBalancers"
                                                    resourceType="LOADBALANCER" status="ACTIVE"
                                                    version="1"/>
                                    </event>
                                  </atom:content>
                                </atom:entry>

  val lbaasSSLMixed = <atom:entry xmlns="http://docs.rackspace.com/core/event" xmlns:atom="http://www.w3.org/2005/Atom"
                                xmlns:lbaas="http://docs.rackspace.com/usage/lbaas">
                                <atom:title type="text">LBAAS</atom:title>
                                <atom:content type="application/xml">
                                    <event resourceName="LoadBalancer" endTime="2012-06-15T10:19:52Z"
                                          startTime="2012-06-14T10:19:52Z" region="DFW" dataCenter="DFW1" type="USAGE"
                                          id="b79cc3de-b399-3883-b555-61829bb7f966"
                                          resourceId="b79cc3de-b399-3883-b555-61829bbccd38" tenantId="1" version="1">
                                      <lbaas:product sslMode="MIXED" vipType="PUBLIC" numVips="44" numPolls="10" bandWidthOutSsl="345345346"
                                                    bandWidthInSsl="364646770" bandWidthOut="3460346" bandWidthIn="43456346"
                                                    avgConcurrentConnectionsSsl="4566.0"
                                                    avgConcurrentConnections="30000.0" serviceCode="CloudLoadBalancers"
                                                    resourceType="LOADBALANCER" status="ACTIVE"
                                                    version="1"/>
                                    </event>
                                  </atom:content>
                                </atom:entry>

  test ("A usage event with SSL OFF submitted to LBAAS feed (lbaas/events) should set bandWidthInSsl, bandWidthOutSsl, and avgConcurrentConnectionsSsl to zero") {
    val req = request("POST", "/lbaas/events", "application/atom+xml", lbaasSSLOff)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/lbaas:product/@bandWidthInSsl = '0'")
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/lbaas:product/@bandWidthOutSsl = '0'")
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/lbaas:product/@avgConcurrentConnectionsSsl = '0'")
  }

  test ("A usage event with SSL ON submitted to LBAAS feed (lbaas/events) should NOT set bandWidthInSsl, bandWidthOutSsl, and avgConcurrentConnectionsSsl to zero") {
    val req = request("POST", "/lbaas/events", "application/atom+xml", lbaasSSLOn)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/lbaas:product/@bandWidthInSsl = '364646770'")
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/lbaas:product/@bandWidthOutSsl = '345345346'")
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/lbaas:product/@avgConcurrentConnectionsSsl = '4566.0'")
  }

  test ("A usage event with SSL MIXED submitted to LBAAS feed (lbaas/events) should NOT set bandWidthInSsl, bandWidthOutSsl, and avgConcurrentConnectionsSsl to zero") {
    val req = request("POST", "/lbaas/events", "application/atom+xml", lbaasSSLMixed)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/lbaas:product/@bandWidthInSsl = '364646770'")
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/lbaas:product/@bandWidthOutSsl = '345345346'")
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/lbaas:product/@avgConcurrentConnectionsSsl = '4566.0'")
  }
}
