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


  test( "Getting an entry on a Validated feed should always succeed" ) {

    atomValidator.validate( request( "GET", "/usagetest10/events/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4",  "", "", false, Map("Accept"->List("*/*"))), response, chain )
  }

  test( "Getting an entry on an Unvalidated feed should always succeed" ) {

    atomValidator.validate( request( "GET", "/demo/events/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4",  "", "", false, Map("Accept"->List("*/*")) ), response, chain )
  }

  test( "Getting an entry on a product feed should always succeed" ) {

    atomValidator.validate( request( "GET", "/bigdata/events/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4",  "", "", false, Map("Accept"->List("*/*")) ), response, chain )
  }

  test("A GET on /buildinfo should always succeed") {
    atomValidator.validate(request("GET", "/buildinfo"), response, chain)
  }

  test("A POST on /buildinfo should fail with a 405") {
    assertResultFailed(atomValidator.validate(request("POST", "/buildinfo"), response, chain), 405)
  }

  test("Posting text on a validated feed (usagetest1/events) should fail with 415") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "plain/text", "foo"), response, chain), 415)
  }

  test("Posting text on an unvalidated feed (usagetest7/events) should fail with 415") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest7/events", "plain/text", "foo"), response, chain), 415)
  }

  test("Posting text on a validated product feed (cbs/events) should fail with 415") {
    assertResultFailed(atomValidator.validate(request("POST", "/cbs/events", "plain/text", "foo"), response, chain), 415)
  }

  test("Posting non-atom XML on a validated feed (usagetest1/events) with content-type of application/atom+xml should fail with a 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml", <some_xml />), response, chain), 400)
  }

  test("Posting non-atom XML on an unvalidated feed (usagetest7/events) with content-type of application/atom+xml should fail with a 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml", <some_xml />), response, chain), 400)
  }


  test("Posting valid entry with non-usage xml should succeed on a validated feed (usagetest1/events)") {
    atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/xml">
                                             <foo xmlns="fooBar.com">
                                             </foo>
                                         </atom:content>
                                    </atom:entry>), response, chain)
  }

  test("Posting valid entry with non-usage xml should succeed on a unvalidated feed (usagetest7/events)") {
    atomValidator.validate(request("POST", "/usagetest7/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/xml">
                                             <foo xmlns="fooBar.com">
                                             </foo>
                                         </atom:content>
                                    </atom:entry>), response, chain)
  }

  test("Posting valid entry with non-usage xml should succeed on a validated product feed (cbs/events)") {
    atomValidator.validate(request("POST", "/cbs/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/xml">
                                             <foo xmlns="fooBar.com">
                                             </foo>
                                         </atom:content>
                                    </atom:entry>), response, chain)
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
                                    </atom:entry>), response, chain)
  }

  test("Posting valid entry with non-usage json should succeed on an unvalidated feed (usagetest7/events)") {
    atomValidator.validate(request("POST", "/usagetest7/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/json">
                                             {json}
                                         </atom:content>
                                    </atom:entry>), response, chain)
  }

  test("Posting valid entry with non-usage json should succeed on a validated product feed (cbs/events)") {
    atomValidator.validate(request("POST", "/cbs/events", "application/atom+xml",
                                   <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
                                         <atom:title>Foo Atom Data</atom:title>
                                         <atom:content type="application/json">
                                             {json}
                                         </atom:content>
                                    </atom:entry>), response, chain)
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
    atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml", validCBSMessage), response, chain)
  }

  test("Posting valid usage entry should succeed on an unvalidated feed (usagetest7/events)") {
    atomValidator.validate(request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage), response, chain)
  }

  test("Posting valid usage entry should succeed on correct product feed (cbs/events)") {
    atomValidator.validate(request("POST", "/cbs/events", "application/atom+xml", validCBSMessage), response, chain)
  }

  test("Posting an invalid usage entry should fail on a validated feed (usagetest1/events) with a 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/usagetest1/events", "application/atom+xml", invalidCBSMessage), response, chain), 400)
  }

  test("Posting invalid usage entry should succeed on an unvalidated feed (usagetest7/events)") {
    atomValidator.validate(request("POST", "/usagetest7/events", "application/atom+xml", validCBSMessage), response, chain)
  }

  test("Posting an invalid usage entry should fail on correct product feed (cbs/events) with a 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/cbs/events", "application/atom+xml", invalidCBSMessage), response, chain), 400)
  }

  test("Posting an valid usage entry should fail on incorrect product feed (files/events) with a 400") {
    assertResultFailed(atomValidator.validate(request("POST", "/files/events", "application/atom+xml", invalidCBSMessage), response, chain), 400)
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
    </atom:entry>), response, chain), 400)
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
    </atom:entry>), response, chain), 400)
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
    </atom:entry>), response, chain), 400)
  }
}
