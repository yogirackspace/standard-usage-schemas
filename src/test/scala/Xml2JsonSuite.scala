import com.rackspace.usage.{BaseUsageSuite}
import java.io.{StringReader, StringWriter, File}
import javax.xml.transform.stream.{StreamResult, StreamSource}
import javax.xml.transform.TransformerFactory
import javax.xml.xpath.XPathFactory
import net.sf.saxon.Controller
import net.sf.saxon.lib.NamespaceConstant
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.language.implicitConversions
import scala.util.parsing.json.JSON
import scala.xml._

/**
 * Tests for the XSLT that transforms XML events to JSON events
 */
@RunWith(classOf[JUnitRunner])
class Xml2JsonSuite extends BaseUsageSuite {

    // During the "initialize" phase, we copy this file from src/main/xsl to target/
    // directory. This xslt "xsl:includes" another xslt that's generated (nonStringAttrs.xsl)
    // and lives in the target/ directory. The "xsl:include" href attribute can not be
    // parameterized. And since at runtime, the two files live in the same directory
    // (/etc/cloudfeeds/translation), it would be good to just deal with this at
    // build time.
    val xml2JsonXslt = new File("target/xslt-artifacts/xml2json-feeds.xsl")

    val xpathFactory = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON, "net.sf.saxon.xpath.XPathFactoryImpl", null)
    val templates = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null).newTemplates(new StreamSource(xml2JsonXslt))

    register("atom", "http://www.w3.org/2005/Atom")
    val dedicatedvCloudMixedNonAdjacentCategoryXml = "<entry xmlns=\"http://www.w3.org/2005/Atom\">" +
            "  <title type=\"text\">Dedicated vCloud event</title>" +
            "  <category term=\"dedicatedvcloud.server.create.vm\"/>" +
            "  <id>urn:uuid:1f1b37e2-503b-4f32-bc8b-6d1d054efc21</id>" +
            "  <content type=\"application/json\">" +
            "    {\"tenant\":\"hybrid:2848639\",\"source\":\"qe.virtops.rackspacecloud.com\",\"type\":\"virtops.vm.create\",\"datacenter\":\"IAD3\",\"timestamp\":\"2014-10-29T16:23:13.431+0000\",\"vmProperties\":{\"organization\":\"urn:vcloud:org:0bc35ece-34d8-45bc-a61e-9e223e022165\",\"vcdName\":\"vcd02-2848639.mv.rackspace.com\",\"containerOS\":\"Microsoft Windows Server 2012 (64-bit)\",\"hypervisor\":\"581846\",\"vmName\":\"WindowsServer_2012_R2_Standard_vcloud_core\",\"computerName\":\"WindowsServ-001\",\"vcenterUuid\":\"4219fe9a-fc2d-cd52-d55d-4d6842c4beb2\",\"vcdUrn\":\"urn:vcloud:vm:fc51f8a1-11c5-4f78-89bf-611af92d8b83\",\"memoryMb\":\"4096\",\"cpuInfo\":{\"cpuCount\":\"4\",\"coresPerSocket\":\"1\"},\"networks\":[{\"name\":\"ExNet-Inside-VLAN1470\",\"ipAddress\":\"192.168.100.5\",\"vlan\":\"1470\",\"isPrimary\":\"true\"}]},\"raxData\":{\"platform.DVC_WINDOWS_UNSUPPORTED\":{\"category\":\"platform\",\"name\":\"DVC_WINDOWS_UNSUPPORTED\",\"attributes\":{},\"type\":\"DeviceConfig\"},\"osName.WINDOWS_2012_R2_STD_X64\":{\"category\":\"osName\",\"name\":\"WINDOWS_2012_R2_STD_X64\",\"attributes\":{\"Dedicated vCloud Director OS Type\":\"WINDOWS\"},\"type\":\"DeviceConfig\"}}}" +
            "  </content>" +
            "  <updated>2014-10-29T16:24:02.856Z</updated>" +
            "  <published>2014-10-29T16:24:02.856Z</published>" +
            "  <category term=\"DataCenter:IAD3\"/>" +
            "  <link href=\"https://atom.staging.ord1.us.ci.rackspace.net/dedicatedvcloud/events/entries/urn:uuid:1f1b37e2-503b-4f32-bc8b-6d1d054efc21\" rel=\"self\" />" +
            "</entry>"

    test("should transform /dedicatedvcloud event with mixed-content JSON inside XML and non-adjacent category properly") {
        val jsonResult = transform(new StreamSource(new StringReader(dedicatedvCloudMixedNonAdjacentCategoryXml)))
        //println(jsonResult)

        // check for entry
        val jsonObjects = JSON.parseFull(jsonResult).get.asInstanceOf[Map[String,Any]]
        assert(jsonObjects.get("entry").get != null, "should have an 'entry' root element")
        val entryObject = jsonObjects.get("entry").get.asInstanceOf[Map[String,Any]]

        // check for id, updated, title, published
        assert(entryObject.get("id").get != null, "entry should have an id")
        assert(entryObject.get("updated").get != null, "entry should have an updated")
        assert(entryObject.get("title").get != null, "entry should have a title")
        assert(entryObject.get("published").get != null, "entry should have a published")

        // check for content
        assert(entryObject.get("content").get != null, "entry should have a content")
        val contentObject = entryObject.get("content").get.asInstanceOf[Map[String,Any]]
        assert(contentObject.get("type").get.asInstanceOf[String] == "application/json", "content type must be application/json")
        assert(contentObject.get("@text").get != null, "content must have @text")

        // check for the content payload
        val textObject = contentObject.get("@text").get.asInstanceOf[Map[String, Any]]
        assert(textObject.get("tenant").get.asInstanceOf[String] == "hybrid:2848639", "text needs to have tenant")

        // check handling of links
        val linkObjects = entryObject.get("link").get.asInstanceOf[List[Map[String, Any]]]
        assert(linkObjects.size == 1, "there is only 1 link object")
        assert(linkObjects.exists ( link => {
                  link.get("rel").get == "self" &&
                      link.get("href").get == "https://atom.staging.ord1.us.ci.rackspace.net/dedicatedvcloud/events/entries/urn:uuid:1f1b37e2-503b-4f32-bc8b-6d1d054efc21"
               }),
               "should have correct self link" )

        // check handling of categories
        val categoryObjects = entryObject.get("category").get.asInstanceOf[List[Map[String, Any]]]
        assert(categoryObjects.size == 2, "there are 2 category objects")
        assert(categoryObjects.exists ( category => {
                    category.get("term").get == "DataCenter:IAD3" || category.get("term").get == "dedicatedvcloud.server.create.vm"
              }),
              "should have category term=DataCenter:IAD3 or term=dedicatedvcloud.server.create.vm")
    }

    val errorXmls = List(
        ("should transform Abdera error XML properly",
         """<?xml version='1.0' encoding='UTF-8'?>
<error xmlns="http://abdera.apache.org">
  <code>400</code>
  <message>Invalid LDAP Search Parameter</message>
</error>"""),
        ("should transform LBaaS validation error XML properly",
    """<?xml version="1.0" encoding="UTF-8"?>
<error xmlns="http://abdera.apache.org"
       xmlns:db="http://docbook.org/ns/docbook"
       xmlns:xs="http://www.w3.org/2001/XMLSchema"
       xmlns:event="http://docs.rackspace.com/core/event"
       xmlns:dbaas="http://docs.rackspace.com/usage/dbaas"
       xmlns:maas="http://docs.rackspace.com/usage/maas"
       xmlns:lbaas="http://docs.rackspace.com/usage/lbaas"
       xmlns:lbaas-account="http://docs.rackspace.com/usage/lbaas/account"
       xmlns:cbs="http://docs.rackspace.com/usage/cbs"
       xmlns:cbs-snap="http://docs.rackspace.com/usage/cbs/snapshot"
       xmlns:cf-b="http://docs.rackspace.com/usage/cloudfiles/bandwidth"
       xmlns:cf-cdn="http://docs.rackspace.com/usage/cloudfiles/cdnbandwidth"
       xmlns:cf-str="http://docs.rackspace.com/usage/cloudfiles/storage"
       xmlns:rax="http://docs.rackspace.com/api"
       xmlns:atom="http://www.w3.org/2005/Atom">
  <code>400</code>
  <message>Bad Content: The synthesized attribute 'avgConcurrentConnectionsSum' should not be included in the original event.</message>
</error>""")
    )

    errorXmls.foreach( input => {
        val (title, xml) = input
        test(title) {
            val jsonResult = transform(new StreamSource(new StringReader(xml)))
            val jsonObjects = JSON.parseFull(jsonResult).get.asInstanceOf[Map[String,Any]]

            // check for error
            assert(jsonObjects.get("error").get != null, "should have an 'error' root element")
            val errorObject = jsonObjects.get("error").get.asInstanceOf[Map[String,Any]]

            // check for code
            assert(errorObject.get("code").get != null, "should have a 'code' element")
            assert(errorObject.get("code").get == "400",  "error code should be 400")

            // check for message
            assert(errorObject.get("message").get != null, "should have a 'message' element")
        }
    })

    test("should transform an empty feed response properly") {
        val emptyFeedXml =
            """
              |<feed xmlns="http://www.w3.org/2005/Atom">
              |  <link href="https://atom.test.ord1.us.ci.rackspace.net/functest2/events/" rel="current" />
              |  <link href="https://atom.test.ord1.us.ci.rackspace.net/functest2/events/" rel="self" />
              |  <link href="https://atom.test.ord1.us.ci.rackspace.net/functest2/events/?marker=last&amp;limit=25&amp;search=&amp;direction=backward" rel="last" />
              |</feed>
            """.stripMargin

        val jsonResult = transform(new StreamSource(new StringReader(emptyFeedXml)))
        val jsonObjects = JSON.parseFull(jsonResult).get.asInstanceOf[Map[String,Any]]

        // check for feed
        assert(jsonObjects.get("feed").get != null, "should have a 'feed' root element")
        val feedObject = jsonObjects.get("feed").get.asInstanceOf[Map[String,Any]]

        // check for links
        assert(feedObject.get("link").get != null, "should have 'link' elements")
        val linkObjects = feedObject.get("link").get.asInstanceOf[List[Map[String, Any]]]
        assert(linkObjects.size == 3, "should have 3 link elements")
    }

    test("should transform /dedicatedvcloud feed response properly") {
        val jsonResult = transform(new StreamSource(new File("src/test/resources/dedicatedvcloud-feed-response.xml")))
        val jsonObjects = JSON.parseFull(jsonResult).get.asInstanceOf[Map[String,Any]]

        // check for feed
        assert(jsonObjects.get("feed").get != null, "should have a 'feed' root element")
        val feedObject = jsonObjects.get("feed").get.asInstanceOf[Map[String,Any]]

        // check for link
        assert(feedObject.get("link").get != null, "should have 'link' elements")
        val linkObjects = feedObject.get("link").get.asInstanceOf[List[Map[String, Any]]]
        assert(linkObjects.size == 5, "should have 5 link elements")

        // check for entry
        assert(feedObject.get("entry").get != null, "should have 'entry' elements")
        val entryObjects = feedObject.get("entry").get.asInstanceOf[List[Map[String, Any]]]
        assert(entryObjects.size == 25, "should have 25 entry elements")

        entryObjects.foreach ( entry => {

            // check for link inside entry
            assert(entry.get("link").get != null, "should have link inside entry element")
            val linkObjects = entry.get("link").get.asInstanceOf[List[Map[String, Any]]]
            assert(linkObjects.size == 1, "should have 1 link inside entry element")
            assert(linkObjects.exists ( link => {
                        link.get("rel").get == "self" &&
                        link.get("href").get != ""
                   }),
                   "should have correct self link" )

            // check for category
            assert(entry.get("category").get != null, "should have category inside entry element")
            val categoryObjects = entry.get("category").get.asInstanceOf[List[Map[String, Any]]]
            assert(categoryObjects.size >= 1, "should have at least 1 category element")
            assert(categoryObjects.exists( cat => {
                     cat.get("term") != null
                   }), "should have category term")

            // check for id, updated, published
            assert(entry.get("id").get != null, "should have an id inside entry element")
            assert(entry.get("updated").get != null, "should have an updated inside entry element")
            assert(entry.get("published").get != null, "should have a published inside entry element")

            // check for content
            assert(entry.get("content").get != null, "should have a content inside entry element")
        })
    }

    test("should transform /dbaas event with non-string attributes properly") {
        val dbaasXml = """<?xml version="1.0"?>
              |<entry xmlns="http://www.w3.org/2005/Atom">
              |  <title>trove.instance.exists</title>
              |  <category term="DFW2"/>
              |  <category term="6093728"/>
              |  <category term="trove.instance.exists"/>
              |  <content type="application/xml">
              |    <event xmlns="http://docs.rackspace.com/core/event" xmlns:dbaas="http://docs.rackspace.com/usage/dbaas" dataCenter="DFW2"
              |           startTime="2014-08-06T21:11:55Z"
              |           endTime="2014-08-06T22:11:55Z"
              |           environment="QA" id="170d03b4-9582-4e34-80fb-d01911ef6469" region="DFW" resourceId="88939024-bf82-49ec-a177-d7ca55d60c9d" resourceName="ba858553-30f4-4f69-a65d-6addc5eafef5" rootAction="trove.instance.exists"
              |           tenantId="6093728" type="USAGE" version="1">
              |      <dbaas:product dbVersion="1.0" memory="32768" resourceType="MARIADB" serviceCode="CloudDatabase" storage="0" version="2"/>
              |    </event>
              |  </content>
              |</entry>
            """.stripMargin

        // check for entry
        val jsonResult = transform(new StreamSource(new StringReader(dbaasXml)))
        val jsonObjects = JSON.parseFull(jsonResult).get.asInstanceOf[Map[String,Any]]
        assert(jsonObjects.get("entry").get != null, "should have an 'entry' root element")
        val entryObject = jsonObjects.get("entry").get.asInstanceOf[Map[String,Any]]

        // check for content
        assert(entryObject.get("content").get != null, "entry should have a content")
        val contentObject = entryObject.get("content").get.asInstanceOf[Map[String,Any]]
        assert(contentObject.get("type") == None, "content must not have type attribute")
        assert(contentObject.get("@text") == None, "content must NOT @text")

        // check for event
        assert(contentObject.get("event").get != null, "content should have event element")
        val eventObject = contentObject.get("event").get.asInstanceOf[Map[String, Any]]

        // check for product
        assert(eventObject.get("product").get != null, "event should have product element")
        val productObject = eventObject.get("product").get.asInstanceOf[Map[String, Any]]
        val memory = productObject.get("memory").get
        assert(memory == 32768, "memory should be numeric and 32768")

    }

    def transform(inputXML: StreamSource) : String = {
        val trans = templates.newTransformer()
        val writer = new StringWriter()

        trans.setParameter("schemaDirectory", "./../../../sample_product_schemas")
        trans.asInstanceOf[Controller].setInitialTemplate("main")
        trans.transform(inputXML, new StreamResult(writer))

        writer.toString()
    }
}
