import com.rackspace.com.papi.components.checker.Converters._
import com.rackspace.usage.BaseUsageSuite
import com.rackspace.usage.BaseUsageSuite._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * User: shin4590
 * Date: 4/3/14
 */
@RunWith(classOf[JUnitRunner])
class LbaasSuite extends BaseUsageSuite {

  register ("lbaas","http://docs.rackspace.com/usage/lbaas")

  val sampleEntry = <atom:entry xmlns="http://docs.rackspace.com/core/event" xmlns:atom="http://www.w3.org/2005/Atom"
                                    xmlns:lbaas="http://docs.rackspace.com/usage/lbaas">
                        <atom:title type="text">LBAAS</atom:title>
                        <atom:content type="application/xml">
                            <event resourceName="LoadBalancer" endTime="2012-06-15T10:19:52Z"
                                   startTime="2012-06-14T10:19:52Z" region="DFW" dataCenter="DFW1" type="USAGE"
                                   id="b79cc3de-b499-3883-b555-61829bb7f966" resourceId="b79cc3de-b399-3883-b555-61829bbccd38" tenantId="1" version="1">
                                <lbaas:product sslMode="MIXED" vipType="PUBLIC" numVips="44" numPolls="10" bandWidthOutSsl="345345346"
                                               bandWidthInSsl="364646770" bandWidthOut="3460346" bandWidthIn="43456346"
                                               avgConcurrentConnectionsSsl="4566.0"
                                               avgConcurrentConnections="30000.0" serviceCode="CloudLoadBalancers"
                                               resourceType="LOADBALANCER" status="ACTIVE"
                                               version="1"/>
                            </event>
                        </atom:content>
                    </atom:entry>


  val sampleServiceNetEntry = <atom:entry xmlns="http://docs.rackspace.com/core/event" xmlns:atom="http://www.w3.org/2005/Atom"
                                          xmlns:lbaas="http://docs.rackspace.com/usage/lbaas">
                                <atom:title type="text">LBAAS</atom:title>
                                <atom:content type="application/xml">
                                  <event resourceName="LoadBalancer" endTime="2012-06-15T10:19:52Z"
                                         startTime="2012-06-14T10:19:52Z" region="DFW" dataCenter="DFW1" type="USAGE"
                                         id="b79cc3de-b499-3883-b555-61829bb7f966" resourceId="b79cc3de-b399-3883-b555-61829bbccd38" tenantId="1" version="1">
                                    <lbaas:product sslMode="MIXED" vipType="SERVICENET" numVips="44" numPolls="10" bandWidthOutSsl="345345346"
                                                   bandWidthInSsl="364646770" bandWidthOut="3460346" bandWidthIn="43456346"
                                                   avgConcurrentConnectionsSsl="4566.0"
                                                   avgConcurrentConnections="30000.0" serviceCode="CloudLoadBalancers"
                                                   resourceType="LOADBALANCER" status="ACTIVE"
                                                   version="1"/>
                                  </event>
                                </atom:content>
                              </atom:entry>

  val sampleSSLOffEntry = <atom:entry xmlns="http://docs.rackspace.com/core/event" xmlns:atom="http://www.w3.org/2005/Atom"
                                          xmlns:lbaas="http://docs.rackspace.com/usage/lbaas">
                            <atom:title type="text">LBAAS</atom:title>
                            <atom:content type="application/xml">
                              <event resourceName="LoadBalancer" endTime="2012-06-15T10:19:52Z"
                                     startTime="2012-06-14T10:19:52Z" region="DFW" dataCenter="DFW1" type="USAGE"
                                     id="b79cc3de-b499-3883-b555-61829bb7f966" resourceId="b79cc3de-b399-3883-b555-61829bbccd38" tenantId="1" version="1">
                                <lbaas:product sslMode="OFF" vipType="PUBLIC" numVips="44" numPolls="10" bandWidthOutSsl="0"
                                               bandWidthInSsl="0" bandWidthOut="3460346" bandWidthIn="43456346"
                                               avgConcurrentConnectionsSsl="0.0"
                                               avgConcurrentConnections="30000.0" serviceCode="CloudLoadBalancers"
                                               resourceType="LOADBALANCER" status="ACTIVE"
                                               version="1"/>
                              </event>
                            </atom:content>
                          </atom:entry>

  test ("LBaaS usage event should have synthesized attributes") {
    val req = request("POST", "/lbaas/events", "application/atom+xml", sampleEntry)
    atomValidator.validate(req, response, chain)
    val doc = getProcessedXML(req)
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@avgConcurrentConnectionsSum = '34566'")
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@publicBandWidthInSum = '408103116'")
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@publicBandWidthOutSum = '348805692'")
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@hasSSLConnection = 'true'")
  }

  test ("LBaaS usage event with vipMode=SERVICENET should have zero publicBandWidth* synthesized attributes") {
    val req = request("POST", "/lbaas/events", "application/atom+xml", sampleServiceNetEntry)
    atomValidator.validate(req, response, chain)
    val doc = getProcessedXML(req)
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@avgConcurrentConnectionsSum = '34566'")
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@publicBandWidthInSum = '0'")
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@publicBandWidthInSum = '0'")
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@hasSSLConnection = 'true'")
  }

  test ("LBaaS usage event with sslMode=OFF should have hasSSLConnection=false synthesized attribute") {
    val req = request("POST", "/lbaas/events", "application/atom+xml", sampleSSLOffEntry)
    atomValidator.validate(req, response, chain)
    val doc = getProcessedXML(req)
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@avgConcurrentConnectionsSum = '30000'")
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@publicBandWidthInSum = '43456346'")
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@publicBandWidthOutSum = '3460346'")
    assert(doc, "/atom:entry/atom:content/event:event/lbaas:product/@hasSSLConnection = 'false'")
  }
}
