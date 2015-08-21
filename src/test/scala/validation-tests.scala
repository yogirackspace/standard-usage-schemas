package com.rackspace.usage

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.xml._

import com.rackspace.com.papi.components.checker.servlet.RequestAttributes._
import com.rackspace.cloud.api.wadl.Converters._
import com.rackspace.com.papi.components.checker.Converters._

import BaseUsageSuite._

//
//  Simple validation tests on validated, unvalidated, and product feeds.
//

@RunWith(classOf[JUnitRunner])
class ValidatorSuite extends BaseUsageSuite {


  val failureMsgByCategoryPrefixMap = Map("tid:" -> "Bad Content: The \"tid:[tenantId]\" category is automatically added by Cloud Feeds.  Alternative \"tid:[other]\" categories cannot be submitted.",
    "rgn:" -> "Bad Content: The \"rgn:[region]\" category is automatically added by Cloud Feeds.  Alternative \"rgn:[other]\" categories cannot be submitted.",
    "dc:" -> "Bad Content: The \"dc:[dataCenter]\" category is automatically added by Cloud Feeds.  Alternative \"dc:[other]\" categories cannot be submitted.",
    "rid:" -> "Bad Content: The \"rid:[resourceId]\" category is automatically added by Cloud Feeds.  Alternative \"rid:[other]\" categories cannot be submitted.",
    "type:" -> "Bad Content: The \"type:[eventType]\" category is automatically added by Cloud Feeds.  Alternative \"type:[other]\" categories cannot be submitted.",
    "username:" -> "Bad Content: The \"username:[username]\" category is automatically added by Cloud Feeds.  Alternative \"username:[other]\" categories cannot be submitted.")


  test( "Getting an entry on a Validated feed should always succeed by service-admin" ) {

    atomValidator.validate( requestRole( "GET", "/usagetest10/events/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4", SERVICE_ADMIN), response, chain )
  }

  test( "Getting an entry on a Validated feed should always succeed by service-observer" ) {

    atomValidator.validate( requestRole( "GET", "/usagetest10/events/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4", SERVICE_OBSERVER), response, chain )
  }

  test( "Getting an entry on an Unvalidated feed should always succeed by service-admin" ) {

    atomValidator.validate( requestRole( "GET", "/demo/events/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4",  SERVICE_ADMIN ), response, chain )
  }

  test( "Getting an entry on an Unvalidated feed should always succeed by service-observer" ) {

    atomValidator.validate( requestRole( "GET", "/demo/events/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4",  SERVICE_OBSERVER ), response, chain )
  }

  test( "Getting an entry on a product feed should always succeed by service-admin" ) {

    atomValidator.validate( requestRole( "GET", "/bigdata/events/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4",  SERVICE_ADMIN ), response, chain )
  }

  test( "Getting an entry on a product feed should always succeed by service-observer" ) {

    atomValidator.validate( requestRole( "GET", "/bigdata/events/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4",  SERVICE_OBSERVER ), response, chain )
  }

  test("A GET on /buildinfo should always succeed") {
    atomValidator.validate(request("GET", "/buildinfo"), response, chain)
  }

  test("A POST on /buildinfo should fail with a 405") {
    assertResultFailed(atomValidator.validate(request("POST", "/buildinfo"), response, chain), 405)
  }

  test("Posting text on a validated feed (usagetest1/events) should fail with 415") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "plain/text", "foo", false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 415)
  }

  test("Posting text on an unvalidated feed (usagetest7/events) should fail with 415") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest7/events", "plain/text", "foo", false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 415)
  }

  test("Posting text on a validated product feed (cbs/events) should fail with 415") {
    assertResultFailed(atomValidator.validate(request("POST", "/cbs/events", "plain/text", "foo", false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 415)
  }

  test("Posting non-atom XML on a validated feed (usagetest1/events) with content-type of application/atom+xml should fail with a 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml", <some_xml />, SERVICE_ADMIN), response, chain), 400)
  }

  test("Posting non-atom XML on an unvalidated feed (usagetest7/events) with content-type of application/atom+xml should fail with a 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml", <some_xml />, SERVICE_ADMIN), response, chain), 400)
  }

  test("Posting valid entry with non-usage xml by a cloudfeeds:service-observer should fail with 403") {
    assertResultFailed( atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml",
      <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
        <atom:title>Foo Atom Data</atom:title>
        <atom:content type="application/xml">
          <foo xmlns="fooBar.com">
          </foo>
        </atom:content>
      </atom:entry>, false, Map("X-ROLES"->List(SERVICE_OBSERVER ) ) ), response, chain), 403 )
  }


  test("Posting valid entry with non-usage xml should succeed on a validated feed (usagetest1/events)") {
    atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/xml">
                                             <foo xmlns="fooBar.com">
                                             </foo>
                                         </atom:content>
                                    </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) ) ), response, chain)
  }

  test("Posting valid entry with non-usage xml should succeed on a unvalidated feed (usagetest7/events)") {
    atomValidator.validate(request("POST", "/usagetest7/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/xml">
                                             <foo xmlns="fooBar.com">
                                             </foo>
                                         </atom:content>
                                    </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain)
  }

  test("Posting valid entry with non-usage xml should succeed on a validated product feed (cbs/events)") {
    atomValidator.validate(request("POST", "/cbs/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/xml">
                                             <foo xmlns="fooBar.com">
                                             </foo>
                                         </atom:content>
                                    </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain)
  }

  //
  //  JSON to embed in the following tests...
  //
  val json = """
        { "foo" : true }
  """
  test("Posting valid entry with non-usage json should succeed on a validated feed (usagetest1/events)") {
    atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/json">
                                             {json}
                                         </atom:content>
                                    </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain)
  }

  test("Posting valid entry with non-usage json should succeed on an unvalidated feed (usagetest7/events)") {
    atomValidator.validate(request("POST", "/usagetest7/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/json">
                                             {json}
                                         </atom:content>
                                    </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain)
  }

  test("Posting valid entry with non-usage json should succeed on a validated product feed (cbs/events)") {
    atomValidator.validate(request("POST", "/cbs/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/json">
                                             {json}
                                         </atom:content>
                                    </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain)
  }

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

  val invalidCBSMessage = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                   <atom:title>CBS Usage</atom:title>
                                   <atom:content type="application/xml">
                                     <event xmlns="http://docs.rackspace.com/core/event"
                                             xmlns:cbs="http://docs.rackspace.com/usage/cbs"
                                             version="1" tenantId="12334"
                                             username="a1@_-."
                                             resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
                                             resourceName="MyVolume"
                                             id="560490c6-6c63-11e1-adfe-27851d5aed13"
                                             type="USAGE" dataCenter="DFW1" region="DFW"
                                             startTime="2012-03-12T11:51:11Z"
                                             endTime="2012-03-12T15:51:11Z">
                                       <cbs:product version="1" serviceCode="CloudBlockStorage"
                                                    resourceType="VOLUME"
                                                    type="fooooo"
                                                    provisioned="120"/>
                                     </event>
                                     </atom:content>
                                   </atom:entry>

  test("Posting valid usage entry should succeed on a validated feed (usagetest1/events)") {
    atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessage, SERVICE_ADMIN), response, chain)
  }

  test("Posting valid usage entry should succeed on an unvalidated feed (usagetest7/events)") {
    atomValidator.validate(request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage, SERVICE_ADMIN), response, chain)
  }

  test("Posting valid usage entry should succeed on correct product feed (cbs/events)") {
    atomValidator.validate(request("POST", "/cbs/events", "application/atom+xml", validCBSMessage, SERVICE_ADMIN), response, chain)
  }

  test("Posting an invalid usage entry should fail on a validated feed (usagetest1/events) with a 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml", invalidCBSMessage, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 400)
  }

  test("Posting invalid usage entry should succeed on an unvalidated feed (usagetest7/events)") {
    atomValidator.validate(request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain)
  }

  test("Posting an invalid usage entry should fail on correct product feed (cbs/events) with a 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/cbs/events", "application/atom+xml", invalidCBSMessage, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 400)
  }

  test("Posting an valid usage entry should fail on incorrect product feed (files/events) with a 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/files/events", "application/atom+xml", invalidCBSMessage, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 400)
  }

  test( "Posting username with bad character on feed fails with 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml",  <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
      <atom:title>CBS Usage</atom:title>
      <atom:content type="application/xml">
        <event xmlns="http://docs.rackspace.com/core/event"
               xmlns:cbs="http://docs.rackspace.com/usage/cbs"
               version="1" tenantId="12334"
               username="a!"
               resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
               resourceName="MyVolume"
               id="560490c6-6c63-11e1-adfe-27851d5aed13"
               type="USAGE" dataCenter="DFW1" region="DFW"
               startTime="2012-03-12T11:51:11Z"
               endTime="2012-03-12T15:51:11Z">
          <cbs:product version="1" serviceCode="CloudBlockStorage"
                       resourceType="VOLUME"
                       type="fooooo"
                       provisioned="120"/>
        </event>
      </atom:content>
    </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 400)
  }

  test( "Posting username which starts with a non-letter on feed fails with 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml",   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
      <atom:title>CBS Usage</atom:title>
      <atom:content type="application/xml">
        <event xmlns="http://docs.rackspace.com/core/event"
               xmlns:cbs="http://docs.rackspace.com/usage/cbs"
               version="1" tenantId="12334"
               username="1a"
               resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
               resourceName="MyVolume"
               id="560490c6-6c63-11e1-adfe-27851d5aed13"
               type="USAGE" dataCenter="DFW1" region="DFW"
               startTime="2012-03-12T11:51:11Z"
               endTime="2012-03-12T15:51:11Z">
          <cbs:product version="1" serviceCode="CloudBlockStorage"
                       resourceType="VOLUME"
                       type="fooooo"
                       provisioned="120"/>
        </event>
      </atom:content>
    </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 400)
  }

  test( "Posting username of length 0 on feed fails with 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml",  <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
      <atom:title>CBS Usage</atom:title>
      <atom:content type="application/xml">
        <event xmlns="http://docs.rackspace.com/core/event"
               xmlns:cbs="http://docs.rackspace.com/usage/cbs"
               version="1" tenantId="12334"
               username=""
               resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
               resourceName="MyVolume"
               id="560490c6-6c63-11e1-adfe-27851d5aed13"
               type="USAGE" dataCenter="DFW1" region="DFW"
               startTime="2012-03-12T11:51:11Z"
               endTime="2012-03-12T15:51:11Z">
          <cbs:product version="1" serviceCode="CloudBlockStorage"
                       resourceType="VOLUME"
                       type="fooooo"
                       provisioned="120"/>
        </event>
      </atom:content>
    </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 400)
  }

  test( "should fail when validating the usagetest feed request with 404" ) {
    assertResultFailed(atomValidator.validate( request( "GET", "/usagetest100/events/", "plain/text", "foo", false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 404)
    }

  test("Posting text on an unvalidated feed (usagetest8/events) should fail with 415") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest8/events", "plain/text", "foo", false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 415)
  }

  test("Posting valid entry with non-usage xml should succeed on a validated feed (usagetest72/events)") {
      atomValidator.validate(request("POST", "/usagetest72/events", "application/atom+xml",
        <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
          <atom:title>Foo Atom Data</atom:title>
          <atom:content type="application/xml">
            <foo xmlns="fooBar.com">
            </foo>
          </atom:content>
        </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain)
  }


  val validBigDataMessage = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom"
                                        xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                        xmlns="http://www.w3.org/2001/XMLSchema">
                                <atom:title>BigData</atom:title>
                                <atom:content type="application/xml">
                                <event xmlns="http://docs.rackspace.com/core/event"
                                        xmlns:sample="http://docs.rackspace.com/usage/bigdata"
                                        id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
                                        version="1"
                                        resourceId="4a2b42f4-6c63-11e1-815b-7fcbcf67f549"
                                        tenantId="1234"
                                        startTime="2013-03-15T11:51:11Z"
                                        endTime="2013-03-15T23:59:59Z"
                                        type="USAGE"
                                        dataCenter="DFW1"
                                        region="DFW">
                                            <sample:product serviceCode="BigData"
                                                  version="1"
                                                  resourceType="HADOOP_HDP1_1"
                                                  flavorId="a"
                                                  flavorName="a"
                                                  numberServersInCluster="1"
                                                  bandwidthIn="0"
                                                  bandwidthOut="0"/>
                                </event>
                                </atom:content>
                            </atom:entry>


  test("Posting valid usage entry for bigdata atom feed)") {
    atomValidator.validate(request("POST", "/usagetest9/events", "application/atom+xml", validBigDataMessage, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain)
  }

  var validLbassMessage = <atom:entry xmlns="http://docs.rackspace.com/core/event" xmlns:atom="http://www.w3.org/2005/Atom"
                                      xmlns:lbhm="http://docs.rackspace.com/event/lbaas/health-monitor">
                            <atom:title type="text">Health Monitor Create</atom:title>
                            <atom:summary type="text">Health Monitor Created.</atom:summary>

                            <atom:content type="application/xml">
                                   <event resourceName="My Health Monitor"
                                          resourceId="65"
                                          resourceURI="http://dfw1.lbaas.rackspace.com/path/to/monitor/65"
                                          severity="INFO"
                                          eventTime="2012-06-15T10:19:52Z" region="DFW" dataCenter="DFW1" type="CREATE"
                                          id="7ba76892-4058-11e3-806b-002500a28a7a"
                                          tenantId="1223" version="1">
                                    <lbhm:product version="1" resourceType="HEALTH_MONITOR"
                                                  serviceCode="CloudLoadBalancers"
                                                  type="HTTP" delay="20"
                                                  timeout="39" attemptsBeforeDeactivation="3"
                                                  path="/foo" monitorStatusRegex="2.." bodyRegex="Okay"/>
                                    </event>
                            </atom:content>
                            </atom:entry>

  test("Posting valid usage entry for cloud load balancers") {
    atomValidator.validate(request("POST", "/usagetest98/events", "application/atom+xml", validLbassMessage, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain)
  }

  // verify that prefix categories are not allowed to be posted to validated and product feeds

  List( "tid:", "rgn:", "dc:", "rid:", "type:" ).foreach( prefix => {

    List( "/usagetest1/events", "cbs/events" ).foreach( feed => {

      test( "Posting category of with prefix of '" + prefix + "' on '" + feed + "' fails with 400" ) {

        assertResultFailed( atomValidator.validate( request( "POST", feed, "application/atom+xml", <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
          <atom:title>CBS Usage</atom:title>
          <atom:category term={ prefix + "xxxx"}/>
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
                           type="SSD"
                           provisioned="120"/>
            </event>
          </atom:content>
        </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 400, failureMsgByCategoryPrefixMap(prefix))
      }
      
     })
  })

  
  // verify that prefix categories are not allowed to be posted to validated and user access event feeds

  List( "tid:", "rgn:", "dc:", "username:").foreach( prefix => {

    List( "/functest1/events").foreach( feed => {

      test( "Posting category with prefix of '" + prefix + "' on '" + feed + "' fails with 400" ) {

        assertResultFailed( atomValidator.validate( request( "POST", feed, "application/atom+xml", <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
          <atom:title>CBS Usage</atom:title>
          <atom:content type="application/xml">
            <atom:category term={ prefix + "xxxx"}/>
            <cadf:event xmlns:cadf="http://schemas.dmtf.org/cloud/audit/1.0/event"
                        xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
                        xmlns:ua="http://feeds.api.rackspacecloud.com/cadf/user-access-event"
                        id="6fa234aea93f38c26fa234aea93f38c2"
                        eventType="activity"
                        typeURI="http://schemas.dmtf.org/cloud/audit/1.0/event"
                        eventTime="2015-03-12T13:20:00-05:00"
                        action="create/post"
                        outcome="success">
              <!--racker -->
              <cadf:initiator id="10.1.2.3" typeURI="network/node" name="jackhandy">
                <cadf:host address="10.1.2.3" agent="curl/7.8 (i386-redhat-linux-gnu) libcurl 7.8" />
              </cadf:initiator>
              <cadf:target id="x.x.x.x" typeURI="service" name="IDM" >
                <cadf:host address="lon.identity.api.rackspacecloud.com" />
              </cadf:target>
              <cadf:attachments>
                <cadf:attachment name="auditData" contentType="ua:auditData">
                  <cadf:content>
                    <ua:auditData>
                      <ua:region>DFW</ua:region>
                      <ua:dataCenter>DFW1</ua:dataCenter>
                      <ua:methodLabel>createToken</ua:methodLabel>
                      <ua:requestURL>https://lon.identity.api.rackspacecloud.com/v2.0/tokens</ua:requestURL>
                      <ua:queryString></ua:queryString>
                      <ua:tenantId>123456</ua:tenantId>
                      <ua:responseMessage>OK</ua:responseMessage>
                      <ua:userName>jackhandy</ua:userName>
                      <ua:roles>xxx</ua:roles>
                    </ua:auditData>
                  </cadf:content>
                </cadf:attachment>
              </cadf:attachments>
              <cadf:observer id="IDM-1-1" name="repose-6.1.1.1" typeURI="service/security">
                <cadf:host address="repose" />
              </cadf:observer>
              <cadf:reason reasonCode="200"
                           reasonType="http://www.iana.org/assignments/http-status-codes/http-status-codes.xml"/>
            </cadf:event>
          </atom:content>
        </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 400, failureMsgByCategoryPrefixMap(prefix))
      }
      
    })
  })

  test( "Posting to customer_access_policy/events with cloudfeeds:service-admin should fail with 403" ) {

    assertResultFailed( atomValidator.validate( request( "POST", "customer_access_policy/events", "application/atom+xml", <atom:entry xmlns:atom="http://www.w3.org/2005/Atom"
                                                                                                                                      xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                                                                                                                      xmlns="http://www.w3.org/2001/XMLSchema">
      <atom:title>CustomerService</atom:title>
      <atom:content type="application/xml">
        <event xmlns="http://docs.rackspace.com/core/event"
               xmlns:sample="http://docs.rackspace.com/event/customer/access_policy"
               id="e53d007a-fc23-11e1-975c-cfa6b29bb814"
               version="2"
               eventTime="2013-03-15T11:51:11Z"
               type="INFO"
               tenantId="123456"
               dataCenter="DFW1"
               region="DFW">
          <sample:product serviceCode="CustomerService"
                          version="1"
                          status="FULL"
                          previousEvent="a"/>
        </event>
      </atom:content>
    </atom:entry>, false, Map("X-ROLES"->List(SERVICE_ADMIN ) )), response, chain), 403)
  }
}
