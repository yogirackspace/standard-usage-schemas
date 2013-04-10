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
//  Tests on servers action transformations
//

@RunWith(classOf[JUnitRunner])
class ServersActionSuite extends BaseUsageSuite {
  register ("csd", "http://docs.rackspace.com/event/servers/slice")

  val sliceAction = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
    <atom:title>Slice Action</atom:title>
    <atom:content type="application/xml">
                <event xmlns="http://docs.rackspace.com/core/event"
                                xmlns:csd="http://docs.rackspace.com/event/servers/slice"
                                version="1" tenantId="555"
                                id="560490c6-6c63-11e1-adfe-27851d5aed13"
                                resourceId="4116"
                                type="INFO" dataCenter="DFW1" region="DFW"
                                eventTime="2012-09-15T11:51:11Z">
                        <csd:product version="1" serviceCode="CloudServers"
                                        resourceType="SLICE" managed="false" imageId="101"
                                        options="5" huddleId="202" serverId="10"
                                        action="RESIZE" imageName="Name" customerId="100"
                                        flavorId="101" status="BUILD" sliceType="CLOUD"
                                        privateIp="1.1.1.1" publicIp="1.1.1.1"
                                        dns1="1.1.1.1" dns2="1.1.1.1"
                                        createdAt="2011-05-15T11:51:11Z">
                                <csd:sliceMetaData key="key1" value="value1"/>
                                <csd:sliceMetaData key="key2" value="value2"/>
                                <csd:additionalPublicAddress ip="1.1.1.1"
                                        dns1="1.1.1.1" dns2="1.1.1.1"/>
                                <csd:additionalPublicAddress ip="1.1.1.2"
                                        dns1="1.1.1.2" dns2="1.1.1.2"/>
                        </csd:product>
                </event>
    </atom:content>
    </atom:entry>

  var sliceActionMissingResourceType = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
    <atom:title>Slice Action</atom:title>
    <atom:author><atom:name>Atom Hopper Team</atom:name></atom:author>
    <atom:category label="atom-hopper-test" term="atom-hopper-test" />
    <atom:content type="application/xml">
      <event xmlns="http://docs.rackspace.com/core/event"
             xmlns:csd="http://docs.rackspace.com/event/servers/slice"
             version="1" tenantId="555"
             id="1223f427-27b6-80a0-d892-02f996212d88"
             resourceId="4116"
             type="INFO" dataCenter="DFW1" region="DFW"
             eventTime="2012-09-15T11:51:11Z">
        <csd:product version="1" serviceCode="CloudServers"
                     managed="false" imageId="101"
                     action="RESIZE"
                     options="5" huddleId="202" serverId="10"
                     imageName="Name" customerId="100"
                     flavorId="101" status="PREP_MOVE" sliceType="CLOUD"
                     privateIp="1.1.1.1" publicIp="1.1.1.1"
                     dns1="1.1.1.1" dns2="1.1.1.1"
                     createdAt="2011-05-15T11:51:11Z">
          <csd:sliceMetaData key="key1" value="value1"/>
          <csd:sliceMetaData key="key2" value="value2"/>
          <csd:additionalPublicAddress ip="1.1.1.1"
                                       dns1="1.1.1.1" dns2="1.1.1.1"/>
          <csd:additionalPublicAddress ip="1.1.1.2"
                                       dns1="1.1.1.2" dns2="1.1.1.2"/>
        </csd:product>
      </event>
    </atom:content>
  </atom:entry>

  var sliceActionWithoutServiceCode = <atom:entry xmlns="http://docs.rackspace.com/core/event"
                                                  xmlns:atom="http://www.w3.org/2005/Atom">
    <atom:title>Slice</atom:title>
    <atom:content type="application/xml">
      <event xmlns="http://docs.rackspace.com/core/event"
             xmlns:cs="http://docs.rackspace.com/event/servers"
             version="1" tenantId="2882"
             id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
             resourceId="56"
             type="USAGE" dataCenter="DFW1" region="DFW"
             startTime="2012-09-15T11:51:11Z"
             endTime="2012-09-16T11:51:11Z">
        <cs:product version="1"
                    resourceType="SLICE" flavor="10"
                    extraPublicIPs="0" extraPrivateIPs="0"
                    isRedHat="true" isMSSQL="false"
                    isMSSQLWeb="false" isWindows="false"
                    isSELinux="false" isManaged="false"/>
      </event>
    </atom:content>
  </atom:entry>


  test("In a slice action event action and status should be added as categories") {
    val req = request("POST", "/servers/events", "application/atom+xml", sliceAction)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'action:RESIZE']) = 1")
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'status:BUILD']) = 1")
  }

  test("Slice Action without serviceCode should generate 400 with the appropriate response message") {
    val req = request("POST", "/servers/events", "application/atom+xml", sliceActionWithoutServiceCode)
    assertResultFailed(atomValidator.validate(req, response, chain), 400, "Bad Content: Required attribute @serviceCode is missing")
  }

//  this is currently broken, see defect D-12072, D-12007
//  test ("Slice Action without resourceType") {
//    val req = request("POST", "/servers/events", "application/atom+xml", sliceActionMissingResourceType)
//    assertResultFailed(atomValidator.validate(req, response, chain), 400, "Bad Content: Required attribute @resourceType is missing")
//  }
}
