package com.rackspace.usage

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import BaseUsageSuite._
/**
 * Created by paulbenoit on 4/10/14.
 */
@RunWith(classOf[JUnitRunner])
class ObserverSuite extends BaseUsageSuite{
  test( "Getting an entry on a non-existent Observer feed should always fail" ) {

    assertResultFailed(atomValidator.validate(requestRole("GET", "/usagetest10/events/12334", OBSERVER ), response, chain), 404 )
  }

  test( "Getting an entry on a Validated Observer feed with no tenant ID should always fail" ) {

    assertResultFailed(atomValidator.validate(requestRole("GET", "/bigdata/events", OBSERVER ), response, chain), 403 )
  }

  test( "Getting an entry on a Validated Observer feed should always succeed" ) {

    atomValidator.validate(requestRole("GET", "/bigdata/events/12334", OBSERVER ), response, chain)
  }

  test( "Getting an entry on a Validated Observer feed should always fail for service-admin" ) {

    assertResultFailed( atomValidator.validate(requestRole("GET", "/bigdata/events/12334", SERVICE_ADMIN), response, chain), 403 )
  }

  test( "Getting an entry on a Validated Observer feed with UUID should always succeed" ) {

    atomValidator.validate(requestRole("GET", "/bigdata/events/12334/entries/urn:uuid:2d6c6484-52ca-b414-6739-bc2062cda7a4", OBSERVER ), response, chain)
  }

  test("A GET on /buildinfo should always succeed") {
    atomValidator.validate(requestRole("GET", "/buildinfo", OBSERVER), response, chain)
  }
}
