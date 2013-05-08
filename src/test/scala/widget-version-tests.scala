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
//  Transformation tests on widget versions.
//

@RunWith(classOf[JUnitRunner])
class WidgetVersionTransformSuite extends BaseUsageSuite {

  val widget1 = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
    <atom:content type="application/xml">
        <event xmlns="http://docs.rackspace.com/core/event"
               xmlns:widget="http://docs.rackspace.com/usage/widget"
               version="1" tenantId="12334"
               resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
               id="560490c6-6c63-11e1-adfe-27851d5aed13"
               type="USAGE" dataCenter="DFW1" region="DFW"
               startTime="2012-03-12T11:51:11Z"
               endTime="2012-03-12T15:51:11Z">
            <widget:product version="1" label="Test Label"
                            serviceCode="Widget"
                            resourceType="WIDGET"
                            enumList="BETTER WORST"
                            mid="94c61976-9f4c-11e1-bddf-ab57017a9899"
                            str="Test Type"
                            widget_id_list="1 2 3 4 5"
                            num_checks="3"
                            disabled="false"
                            time="06:42:44Z"
                            dateTime="2012-05-16T06:42:44Z"/>
        </event>
    </atom:content>
  </atom:entry>

  val widget2 = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
    <atom:content type="application/xml">
        <event xmlns="http://docs.rackspace.com/core/event"
               xmlns:widget="http://docs.rackspace.com/usage/widget"
               version="1" tenantId="12334"
               resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
               id="560490c6-6c63-11e1-adfe-27851d5aed13"
               type="USAGE" dataCenter="DFW1" region="DFW"
               startTime="2012-03-12T11:51:11Z"
               endTime="2012-03-12T15:51:11Z">
            <widget:product version="2" label="Test Label"
                            serviceCode="Widget"
                            resourceType="THINGY"
                            enumList="BETTER BAD WORST"
                            mid="94c61976-9f4c-11e1-bddf-ab57017a9899"
                            str="Test Type"
                            widget_id_list="1 2 3 4 5"
                            num_checks="3"
                            disabled="false"
                            time="06:42:44Z"
                            dateTime="2012-05-16T06:42:44Z"/>
        </event>
    </atom:content>
   </atom:entry>

  val widget3 = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
    <atom:content type="application/xml">
        <event xmlns="http://docs.rackspace.com/core/event"
               xmlns:widget="http://docs.rackspace.com/usage/widget"
               version="1" tenantId="12334"
               resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
               id="560490c6-6c63-11e1-adfe-27851d5aed13"
               type="UPDATE" dataCenter="DFW1" region="DFW"
               startTime="2012-03-12T11:51:11Z"
               endTime="2012-03-12T15:51:11Z">
            <widget:product version="3" label="Test Label"
                            serviceCode="Widget"
                            resourceType="THINGY"
                            mid="94c61976-9f4c-11e1-bddf-ab57017a9899">
                <widget:metaData key="foo" value="bar"/>
                <widget:metaData key="fruit" value="loops"/>
            </widget:product>
        </event>
    </atom:content>
    </atom:entry>

  test("The str attribute should be exposed as a category is version 2 messages") {
    val req = request("POST", "usagetest6/events", "application/atom+xml", widget2)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'str:Test Type']) = 1")
  }

  test("The str attribute should *not* be exposed as a category is version 1 messages") {
    val req = request("POST", "usagetest6/events", "application/atom+xml", widget1)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'str:Test Type']) = 0")
  }

  test("The label and metaData key attributes should be exposed as a categories in version 3 messages") {
    val req = request("POST", "usagetest6/events", "application/atom+xml", widget3)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'label:Test Label']) = 1")
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'metaData.key:foo']) = 1")
    assert(getProcessedXML(req), "count(/atom:entry/atom:category[@term = 'metaData.key:fruit']) = 1")
  }
}
