package com.rackspace.usage

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.rackspace.usage.BaseUsageSuite._
import junit.framework.TestSuite

/**
 * User: shin4590
 * Date: 5/21/14
 */
@RunWith(classOf[JUnitRunner])
class DisableJSONSuite extends BaseUsageSuite {

    new TestSuite("Getting feeds in JSON") {

        // the following are the 4 feeds we allow JSON
        test("Getting /automation feed JSON Content with Accept: application/json should succeed") {
            atomValidator.validate(request("GET", "/automation/events", "", "", false, Map("ACCEPT"->List("application/json")) ), response, chain)
        }

        test("Getting /monitoring feed JSON Content with Accept: application/json should succeed") {
            atomValidator.validate(request("GET", "/monitoring/events", "", "", false, Map("ACCEPT"->List("application/json")) ), response, chain)
        }

        test("Getting /nova feed JSON Content with Accept: application/json should succeed") {
            atomValidator.validate(request("GET", "/nova/events", "", "", false, Map("ACCEPT"->List("application/json")) ), response, chain)
        }

        test("Getting /servers feed JSON Content with Accept: application/json should succeed") {
            atomValidator.validate(request("GET", "/servers/events", "", "", false, Map("ACCEPT"->List("application/json")) ), response, chain)
        }

        // JSON is not allowed on these feeds
        test("Getting /files feed JSON Content with Accept: application/json should fail with 406") {
            assertResultFailed(atomValidator.validate(request("GET", "/files/events", "", "", false, Map("ACCEPT"->List("application/json;q=.5")) ), response, chain), 406 )
        }

        test("Getting /identity feed JSON Content with Accept: application/vnd.collection+json should fail with 406") {
            assertResultFailed(atomValidator.validate(request("GET", "/identity/events", "", "", false, Map("ACCEPT"->List("application/vnd.collection+json")) ), response, chain), 406 )
        }

        test("Getting /monitoring feed JSON Content as observer with Accept: application/json should fail with 406") {
            assertResultFailed(atomValidatorObserver.validate(request("GET", "/monitoring/events/12345", "", "", false, Map("ACCEPT"->List("application/json")) ), response, chain), 406 )
        }

        test("Getting /functest1 feed JSON Content with Accept: application/json should fail with 406") {
            assertResultFailed(atomValidator.validate(request("GET", "/functest1/events", "", "", false, Map("ACCEPT"->List("application/json")) ), response, chain), 406 )
        }

    }

    new TestSuite("Getting feeds with non-JSON Accept header") {

        test("Getting /bigdata feed with Accept application/xml should succeed") {
            atomValidator.validate(request("GET", "/bigdata/events", "", "", false, Map("ACCEPT"->List("application/xml")) ), response, chain)
        }

        test("Getting /dbaas feed with Accept application/atom+xml should succeed") {
            atomValidator.validate(request("GET", "/dbaas/events", "", "", false, Map("ACCEPT"->List("application/atom+xml")) ), response, chain)
        }

        test("Getting /files feed with Accept text/html,image/gif,image/jpeg,*;q=.2,*/*;q=.5 should work") {
            atomValidator.validate(request("GET", "/files/events", "", "", false, Map("ACCEPT"->List("*/*;q=.2,text/html,image/gif,image/jpeg,*;q=.2")) ), response, chain)
        }

        test("Getting /queues feed with Accept application/atom+xml,application/rdf+xml,application/rss+xml,application/x-netcdf,application/xml;q=0.9,text/xml;q=0.2,*/*;q=0.1 should succeed") {
            atomValidator.validate(request("GET", "/queues/events", "", "", false, Map("ACCEPT"->List("application/atom+xml,application/rdf+xml,application/rss+xml,application/x-netcdf,application/xml;q=0.9,text/xml;q=0.2,*/*;q=0.1")) ), response, chain)
        }

        test("Getting /identity feed with Accept application/atom+xml should succeed") {
            atomValidator.validate(request("GET", "/identity/events", "", "", false, Map("ACCEPT"->List("application/atom+xml")) ), response, chain)
        }

        test("Getting /ssl feed as observer with Accept application/atom+xml should succeed") {
            atomValidatorObserver.validate(request("GET", "/ssl/events/12345", "", "", false, Map("ACCEPT"->List("application/atom+xml")) ), response, chain)
        }

    }

    new TestSuite("Getting a single atom entry") {
        // Unvalidated feed
        test("Getting a single /autoscale entry JSON Content with Accept: application/json should fail with 406") {
            assertResultFailed(atomValidator.validate(request("GET", "/autoscale/events/entries/urn:uuid:8d89673c-c989-11e1-895a-0b3d632a8a89", "", "", false, Map("ACCEPT"->List("application/json")) ), response, chain), 406 )
        }

        // Validated feed
        test("Getting a single /encore entry JSON Content with Accept: application/json should fail with 406") {
            assertResultFailed(atomValidator.validate(request("GET", "/encore/events/entries/urn:uuid:8d89673c-c989-11e1-895a-0b3d632a8a89", "", "", false, Map("ACCEPT"->List("application/json")) ), response, chain), 406 )
        }

        // Product Schema validated feed
        test("Getting a single /support entry JSON Content with Accept: application/json should fail with 406") {
            assertResultFailed(atomValidator.validate(request("GET", "/support/events/entries/urn:uuid:8d89673c-c989-11e1-895a-0b3d632a8a89", "", "", false, Map("ACCEPT"->List("application/json")) ), response, chain), 406 )
        }

        // observer entry
        test("Getting a single /ssl entry JSON Content as observer with Accept application/json should fail with 406") {
            assertResultFailed(atomValidatorObserver.validate(request("GET", "/ssl/events/12345/entries/urn:uuid:8d89673c-c989-11e1-895a-0b3d632a8a89", "", "", false, Map("ACCEPT"->List("application/json")) ), response, chain), 406)
        }
    }
}
