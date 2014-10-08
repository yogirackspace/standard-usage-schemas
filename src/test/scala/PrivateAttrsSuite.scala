import com.rackspace.usage.BaseUsageSuite
import java.io.{StringReader, StringWriter, File}
import javax.xml.transform.stream.{StreamResult, StreamSource}
import javax.xml.transform.TransformerFactory
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.xml.{NodeSeq, XML}

/**
 * Test to make sure that sample product schemas with private="true"
 * attribute will be transformed by rm_private_attrs_for_obs.xsl properly.
 *
 */
@RunWith(classOf[JUnitRunner])
class PrivateAttrsSuite extends BaseUsageSuite {

  register ("csd","http://docs.rackspace.com/event/servers/slice")

  private val remove_private_attrs_xslt = new File("src/test/resources/rm_private_attrs_for_obs.xsl")
  private val templates = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null).newTemplates(new StreamSource(remove_private_attrs_xslt))

  private val serversSliceActionResponse =
    <atom:entry xmlns:atom="http://www.w3.org/2005/Atom" xmlns="http://wadl.dev.java.net/2009/02" xmlns:db="http://docbook.org/ns/docbook" xmlns:error="http://docs.rackspace.com/core/error" xmlns:wadl="http://wadl.dev.java.net/2009/02" xmlns:json="http://json-schema.org/schema#" xmlns:sum="http://docs.rackspace.com/core/usage/schema/summary" xmlns:d439e1="http://wadl.dev.java.net/2009/02" xmlns:cldfeeds="http://docs.rackspace.com/api/cloudfeeds">
      <atom:id>urn:uuid:560490c6-6c63-11e1-adfe-27851d5aed13</atom:id>
      <atom:category term="tid:555"/>
      <atom:category term="rgn:DFW"/>
      <atom:category term="dc:DFW1"/>
      <atom:category term="rid:4116"/>
      <atom:category term="cloudservers.slice.slice.info"/>
      <atom:category term="type:cloudservers.slice.slice.info"/>
      <atom:category term="action:RESIZE"/>
      <atom:category term="status:BUILD"/>
      <atom:title type="text">Slice Action</atom:title>
      <atom:content type="application/xml">
        <event xmlns="http://docs.rackspace.com/core/event" xmlns:csd="http://docs.rackspace.com/event/servers/slice" dataCenter="DFW1" environment="PROD" eventTime="2012-09-15T11:51:11Z" id="560490c6-6c63-11e1-adfe-27851d5aed13" region="DFW" resourceId="4116" tenantId="555" type="INFO" version="1">
          <csd:product action="RESIZE" createdAt="2011-05-15T11:51:11Z" customerId="100" dns1="1.1.1.1" dns2="1.1.1.1" flavorId="101" huddleId="202" imageId="101" imageName="Name" managed="false" options="5" privateIp="1.1.1.1" publicIp="1.1.1.1" resourceType="SLICE" rootPassword="xy9gh2z" serverId="10" serviceCode="CloudServers" sliceType="CLOUD" status="BUILD" version="1">
            <csd:sliceMetaData key="key1" value="value1"/>
            <csd:sliceMetaData key="key2" value="value2"/>
            <csd:additionalPublicAddress dns1="1.1.1.1" dns2="1.1.1.1" ip="1.1.1.1"/>
            <csd:additionalPublicAddress dns1="1.1.1.2" dns2="1.1.1.2" ip="1.1.1.2"/>
          </csd:product>
        </event>
      </atom:content>
      <atom:link href="/servers/events/entries/urn:uuid:560490c6-6c63-11e1-adfe-27851d5aed13" rel="self"/>
      <atom:updated>2014-10-07T23:20:26.972Z</atom:updated>
      <atom:published>2014-10-07T23:20:26.972Z</atom:published>
    </atom:entry>

  private val serversImageInfoResponse =
    <atom:entry xmlns:atom="http://www.w3.org/2005/Atom">
      <atom:id>urn:uuid:560490c6-6c63-11e1-adfe-27851d5aed13</atom:id>
      <atom:category term="tid:555" />
      <atom:category term="rgn:DFW" />
      <atom:category term="dc:DFW1" />
      <atom:category term="rid:4116" />
      <atom:category term="cloudservers.image.image.info" />
      <atom:category term="type:cloudservers.image.image.info" />
      <atom:title type="text">Image Action</atom:title>
      <atom:content type="application/xml">
        <event xmlns="http://docs.rackspace.com/core/event" xmlns:csd="http://docs.rackspace.com/event/servers/image" dataCenter="DFW1" environment="PROD" eventTime="2012-09-15T11:51:11Z" id="560490c6-6c63-11e1-adfe-27851d5aed13" region="DFW" resourceId="4116" tenantId="555" type="INFO" version="1">
          <csd:product action="SNAPSHOT" imageName="Name" resourceType="IMAGE" serviceCode="CloudServers" sliceId="578" version="1" />
        </event>
      </atom:content>
      <atom:link href="https://ord.feeds.api.rackspacecloud.com/servers/events/entries/urn:uuid:560490c6-6c63-11e1-adfe-27851d5aed13" rel="self" />
      <atom:updated>2013-02-27T21:30:54.312Z</atom:updated>
      <atom:published>2013-02-27T21:30:54.312Z</atom:published>
    </atom:entry>

  test ("server slice action usage event should not have private attributes after applying xslt") {
    val xPathPrivateAttrsList = List("/atom:entry/atom:content/event:event/csd:product/@rootPassword",
                                    "/atom:entry/atom:content/event:event/csd:product/@options",
                                    "/atom:entry/atom:content/event:event/csd:product/@huddleId",
                                    "/atom:entry/atom:content/event:event/csd:product/@serverId",
                                    "/atom:entry/atom:content/event:event/csd:product/@customerId",
                                    "/atom:entry/atom:content/event:event/csd:product/@flavorId",
                                    "/atom:entry/atom:content/event:event/csd:product/@sliceType",
                                    "/atom:entry/atom:content/event:event/csd:product/@privateIp")

    val xPathNonPrivateAttrsList = List("/atom:entry/atom:content/event:event/csd:product/@imageName",
                                      "/atom:entry/atom:content/event:event/csd:product/@publicIp")

    val beforeTransformationXML = serversSliceActionResponse

    //before transformation
    for (xPath <- xPathPrivateAttrsList) assert(beforeTransformationXML, "boolean(" + xPath + ") = true()")
    for (xPath <- xPathNonPrivateAttrsList) assert(beforeTransformationXML, "boolean(" + xPath + ") = true()")

    val afterTransformationXML = transformXML(serversSliceActionResponse.toString())

    //after transformation
    assert(beforeTransformationXML != afterTransformationXML)
    for (xPath <- xPathPrivateAttrsList) assert(afterTransformationXML, "boolean(" + xPath + ") = false()")
    for (xPath <- xPathNonPrivateAttrsList) assert(afterTransformationXML, "boolean(" + xPath + ") = true()")
  }


  test("server image info response should not alter after applying private attrs filter") {

    val beforeTransformationXML = serversImageInfoResponse
    val afterTransformationXML = transformXML(serversImageInfoResponse.toString())

    assert(beforeTransformationXML == afterTransformationXML)
  }

  def transformXML(inputXML: String) : NodeSeq = {
    val trans = templates.newTransformer()
    val writer = new StringWriter()

    trans.transform(new StreamSource(new StringReader(inputXML)), new StreamResult(writer))
    XML.loadString(writer.toString)
  }
}
