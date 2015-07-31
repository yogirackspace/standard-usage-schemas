import com.rackspace.usage.BaseUsageSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.xml._

import BaseUsageSuite._

/**
 * Tests the JsonContentOnly resource type on the unvalidatedjsoncontentonlytest/events feed.
 */
@RunWith(classOf[JUnitRunner])
class JsonContentOnlySuite extends BaseUsageSuite {

  val genericXml = <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
    <atom:content type="application/xml">
      <genericxml/>
    </atom:content>
  </atom:entry>

  val genericJson =
    XML.loadString( """<atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
      |   <atom:content type="application/json">{ "key1" : "value" }</atom:content>
      |</atom:entry>""" )

  test( "atom:content of type='appplication/xml' is invalid for JsonContentOnly feed" ) {

    val req = request( "POST", "unvalidatedjsoncontentonlytest/events", "application/atom+xml", genericXml, SERVICE_ADMIN)
    assertResultFailed( atomValidator.validate( req, response, chain ), 400 )
  }

  test( "atom:content of type='appplication/json' is valid for JsonContentOnly feed" ) {

    val req = request( "POST", "unvalidatedjsoncontentonlytest/events", "application/atom+xml", genericJson, SERVICE_ADMIN)
    atomValidator.validate( req, response, chain )
  }

  test( "GET JsonContentOnly feed" ) {

    val req = request( "GET", "unvalidatedjsoncontentonlytest/events", "", "", false,
      Map("ACCEPT"->List("application/atom+xml"), "X-ROLES"->List(SERVICE_ADMIN)))
    atomValidator.validate( req, response, chain )
  }
}
