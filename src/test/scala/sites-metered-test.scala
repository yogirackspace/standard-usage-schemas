import com.rackspace.usage.BaseUsageSuite
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
//  Tests on sites metered transformations
//

@RunWith(classOf[JUnitRunner])
class SitesMeteredSuite extends BaseUsageSuite {
  register ("m", "http://docs.rackspace.com/usage/sites/metered")

  val sitesMeteredWithNoComputeCycles = <atom:entry xmlns="http://docs.rackspace.com/core/event" xmlns:atom="http://www.w3.org/2005/Atom"
              xmlns:m="http://docs.rackspace.com/usage/sites/metered">
    <atom:title type="text">Sites</atom:title>
    <atom:updated>2012-06-14T09:46:31.867-05:00</atom:updated>
    <atom:content type="application/xml">
      <event startTime="2012-06-14T10:19:52Z"
             endTime="2012-06-14T11:19:52Z"
             type="USAGE"
             resourceId="my.site.com" resourceName="my.site.com"
             id="47b574c4-0400-11e2-b19d-7b8e208cade0"
             dataCenter="DFW1" region="DFW"
             tenantId="12882" version="1">
        <m:product serviceCode="CloudSites" version="1"
                   resourceType="SITE" bandWidthOut="998798976"
                   requestCount="1000"/>
      </event>
    </atom:content>
  </atom:entry>

  test("Sites metered usage: non existence of computeCycles should set computeCycles to 0") {
    val req = request("POST", "/sites/events", "application/atom+xml", sitesMeteredWithNoComputeCycles)
    atomValidator.validate(req, response, chain)
    assert(getProcessedXML(req), "/atom:entry/atom:content/event:event/m:product/@computeCycles = 0.0")
  }
}
