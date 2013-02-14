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
  test("A GET on /buildinfo should always succeed") {
    atomValidator.validate(request("GET", "/buildinfo"), response, chain)
  }

  test("A POST on /buildinfo should fail with a 405") {
    assertResultFailed(atomValidator.validate(request("POST", "/buildinfo"), response, chain), 405)
  }

  test("A GET on /logtest should always succeed") {
    atomValidator.validate(request("GET", "/logtest"), response, chain)
  }

  test("A POST on /logtest should fail with a 405") {
    assertResultFailed(atomValidator.validate(request("POST", "/logtest"), response, chain), 405)
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
}
