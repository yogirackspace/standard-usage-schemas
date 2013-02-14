package com.rackspace.usage

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.xml._

import com.rackspace.com.papi.components.checker.servlet.RequestAttributes._
import com.rackspace.cloud.api.wadl.Converters._
import com.rackspace.com.papi.components.checker.Converters._

import BaseUsageSuite._

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
}
