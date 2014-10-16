import com.rackspace.usage.BaseUsageSuite
import com.rackspace.usage.BaseUsageSuite._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * Created with IntelliJ IDEA.
 * User: nare4013
 * Date: 10/16/14
 * Time: 1:39 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class ValidateWithRoles extends BaseUsageSuite {

  test("should pass when getting /monitoring/events feed XML Content with Accept: application/xml and X-ROLES:cloudfeeds:service-admin ") {
    atomValidator.validate(request("GET", "bigdata/events", "", "", false,
      Map("ACCEPT"->List("application/xml"), "X-ROLES" ->List("cloudfeeds:service-admin")) ), response, chain)
  }

  test("should pass when getting bigdata/events/12345 feed with Accept: application/xml and X-ROLES:cloudfeeds:observer ") {
    atomValidator.validate(request("GET", "bigdata/events/12345", "", "", false,
      Map("ACCEPT"->List("application/xml"), "X-ROLES" ->List("cloudfeeds:observer")) ), response, chain)
  }

  test("should pass when GET with /bigdata entry as observer with Accept application/xml") {
    atomValidator.validate(request("GET", "bigdata/events/12345/entries/urn:uuid:8d89673c-c989-11e1-895a-0b3d632a8a89", "", "",
      false, Map("ACCEPT"->List("application/xml"), "X-ROLES" ->List("cloudfeeds:observer")) ), response, chain)
  }

  test("should fail when getting /monitoring/events feed XML Content with Accept: application/xml and X-ROLES:cloudfeeds:observer ") {
    assertResultFailed(atomValidator.validate(request("GET", "bigdata/events", "", "", false,
      Map("ACCEPT"->List("application/xml"), "X-ROLES" ->List("cloudfeeds:observer")) ), response, chain), 403)
  }

}
