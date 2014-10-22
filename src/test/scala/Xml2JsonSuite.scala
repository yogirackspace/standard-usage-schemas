import com.rackspace.usage.{SampleMessagesSuite, BaseUsageSuite}
import java.io.{StringReader, StringWriter, File}
import java.util
import javax.xml.transform.stream.{StreamResult, StreamSource}
import javax.xml.transform.TransformerFactory
import javax.xml.xpath.{XPathConstants, XPathFactory}
import net.sf.saxon.Controller
import net.sf.saxon.lib.NamespaceConstant
import net.sf.saxon.tree.tiny.TinyNodeImpl
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.language.implicitConversions
import scala.collection.JavaConversions._
import scala.collection.mutable.Buffer
import scala.util.parsing.json.JSON
import scala.xml.XML

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

    def transform(inputXML: StreamSource) : String = {
        val trans = templates.newTransformer()
        val writer = new StringWriter()

        trans.setParameter("schemaDirectory", "./../../../sample_product_schemas")
        trans.asInstanceOf[Controller].setInitialTemplate("main")
        trans.transform(inputXML, new StreamResult(writer))

        writer.toString()
    }
}
