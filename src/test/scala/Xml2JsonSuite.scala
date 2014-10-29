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

    val sampleDir   = new File("message_samples")
    val sampleFiles = getSampleXMLFiles(sampleDir)

    register("atom", "http://www.w3.org/2005/Atom")
    val dedicatedvCloudMixedJson = "<entry xmlns=\"http://www.w3.org/2005/Atom\">" +
            "  <title type=\"text\">Dedicated vCloud event</title>" +
            "  <category term=\"dedicatedvcloud.server.create.vm\"/>" +
            "  <id>urn:uuid:1f1b37e2-503b-4f32-bc8b-6d1d054efc21</id>" +
            "  <content type=\"application/json\">" +
            "    {\"tenant\":\"hybrid:2848639\",\"source\":\"qe.virtops.rackspacecloud.com\",\"type\":\"virtops.vm.create\",\"datacenter\":\"IAD3\",\"timestamp\":\"2014-10-29T16:23:13.431+0000\",\"vmProperties\":{\"organization\":\"urn:vcloud:org:0bc35ece-34d8-45bc-a61e-9e223e022165\",\"vcdName\":\"vcd02-2848639.mv.rackspace.com\",\"containerOS\":\"Microsoft Windows Server 2012 (64-bit)\",\"hypervisor\":\"581846\",\"vmName\":\"WindowsServer_2012_R2_Standard_vcloud_core\",\"computerName\":\"WindowsServ-001\",\"vcenterUuid\":\"4219fe9a-fc2d-cd52-d55d-4d6842c4beb2\",\"vcdUrn\":\"urn:vcloud:vm:fc51f8a1-11c5-4f78-89bf-611af92d8b83\",\"memoryMb\":\"4096\",\"cpuInfo\":{\"cpuCount\":\"4\",\"coresPerSocket\":\"1\"},\"networks\":[{\"name\":\"ExNet-Inside-VLAN1470\",\"ipAddress\":\"192.168.100.5\",\"vlan\":\"1470\",\"isPrimary\":\"true\"}]},\"raxData\":{\"platform.DVC_WINDOWS_UNSUPPORTED\":{\"category\":\"platform\",\"name\":\"DVC_WINDOWS_UNSUPPORTED\",\"attributes\":{},\"type\":\"DeviceConfig\"},\"osName.WINDOWS_2012_R2_STD_X64\":{\"category\":\"osName\",\"name\":\"WINDOWS_2012_R2_STD_X64\",\"attributes\":{\"Dedicated vCloud Director OS Type\":\"WINDOWS\"},\"type\":\"DeviceConfig\"}}}" +
            "  </content>" +
            "  <link href=\"https://atom.staging.ord1.us.ci.rackspace.net/dedicatedvcloud/events/entries/urn:uuid:1f1b37e2-503b-4f32-bc8b-6d1d054efc21\" rel=\"self\" />" +
            "  <updated>2014-10-29T16:24:02.856Z</updated>" +
            "  <published>2014-10-29T16:24:02.856Z</published>" +
            "</entry>"

    // load all sample XML files and translate them to JSON
    sampleFiles.foreach ( f => {
        test("Transforming sample " + f.getAbsolutePath + " to JSON ") {
            printf("Transforming %s to JSON\n",f.getAbsolutePath)

            val jsonResult = transform(new StreamSource(f))
            //println(jsonResult)

            val jsonObjects = JSON.parseFull(jsonResult).get.asInstanceOf[Map[String,Any]]
            assert(jsonObjects.get("entry").get != null, "  should have an entry")
            val entryObject = jsonObjects.get("entry").get.asInstanceOf[Map[String,Any]]
            assert(entryObject.get("content").get != null, "  entry should have a content")
            var eventParent = entryObject.get("content").get.asInstanceOf[Map[String,Any]]

            // special handling for usagedeadletter
            if ( f.getAbsolutePath().contains("usagedeadletter") ) {
                assert(eventParent.get("eventError").get != null, "  usagedeadletter's content should have an eventError")
                eventParent = eventParent.get("eventError").get.asInstanceOf[Map[String,Any]]
            }

            assert(eventParent.get("event").get != null, "  should have an event")
        }
    })

    test("DedicatedvCloud mixed-content JSON inside XML is transformed properly") {
        val jsonResult = transform(new StreamSource(new StringReader(dedicatedvCloudMixedJson)))
        println(jsonResult)

        val jsonObjects = JSON.parseFull(jsonResult).get.asInstanceOf[Map[String,Any]]
        assert(jsonObjects.get("entry").get != null, "  should have an entry")
        val entryObject = jsonObjects.get("entry").get.asInstanceOf[Map[String,Any]]
        assert(entryObject.get("content").get != null, "  entry should have a content")
        val contentObject = entryObject.get("content").get.asInstanceOf[Map[String,Any]]
        assert(contentObject.get("type").get.asInstanceOf[String] == "application/json", "content type must be application/json")
        assert(contentObject.get("@text").get != null, "content must have @text")
        val textObject = contentObject.get("@text").get.asInstanceOf[Map[String, Any]]
        assert(textObject.get("tenant").get.asInstanceOf[String] == "hybrid:2848639", "text needs to have tenant")
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
