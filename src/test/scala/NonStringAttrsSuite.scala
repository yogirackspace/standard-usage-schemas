import com.rackspace.usage.BaseUsageSuite
import java.io.{StringReader, StringWriter, File}
import java.util
import javax.xml.transform.stream.{StreamResult, StreamSource}
import javax.xml.transform.TransformerFactory
import javax.xml.xpath.{XPathConstants, XPathFactory}
import net.sf.saxon.lib.NamespaceConstant
import net.sf.saxon.tree.tiny.TinyNodeImpl
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.language.implicitConversions
import scala.collection.JavaConversions._
import scala.collection.mutable.Buffer

/**
 * Tests for the generation of nonStringAttrs.xsl that is
 * consumed by the xml2json-feeds.xsl
 */
@RunWith(classOf[JUnitRunner])
class NonStringAttrsSuite extends BaseUsageSuite {

    // The list of non-string types per productSchema.xsd. We also
    // have to support the plural types (i.e: int*, integer*, etc)
    var nonStringTypes = "integer,int,short,byte,unsignedLong,unsignedInt,unsignedShort,unsignedByte,double,float,duration,boolean"
    //nonStringTypes.split(",").foreach ( nonStringTypes += "," + _ + "*")

    val genNonStringAttrsXslt = new File("src/main/xsl/productSchema-generate-nonStringAttrs.xsl")

    val xpathFactory = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON, "net.sf.saxon.xpath.XPathFactoryImpl", null)
    val templates = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null).newTemplates(new StreamSource(genNonStringAttrsXslt))

    register("sch", "http://docs.rackspace.com/core/usage/schema")
    register("cf", "http://docs.rackspace.com/api/cloudfeeds/non-string-attrs")

    // run the productSchema-generate-nonStringAttrs on the whole sample_product_schemas/* directory
    val nonStrAttrsFromXform = transform("<input>Does not matter. XSLT will read files under sample_product_schemas directory</input>")

    test ("widget usage schema's version=1: non string attributes are listed in XSLT output") {

        // grab the non string attributes from product schema file
        val version = "1"
        val v1NonStrAttrsPaths = getNonStringAttrsForASchemaFile("./sample_product_schemas/widget.xml", version)

        // from the result of the transform, grab the attributes for widget
        val result = new StreamSource(new StringReader(nonStrAttrsFromXform))
        val xpath = xpathFactory.newXPath()
        xpath.setNamespaceContext(this)
        val xpathExpr = xpath.compile("//cf:schema[@key='http://docs.rackspace.com/usage/widget' and @version='" + version + "']/cf:attributes/text()")
        val attributes = xpathExpr.evaluate(result.asInstanceOf[Any], XPathConstants.STRING).asInstanceOf[String]

        v1NonStrAttrsPaths.foreach ( path => {
                assert(attributes.contains(path.trim()), "path " + path.trim() + " is in generated nonStringAttrs.xsl list")
            }
        )
    }

    test ("widget usage schema's version=3 with attributeGroups: non string attributes are listed in XSLT output") {

        // grab the non string attributes from product schema file
        val version = "3"
        val v1NonStrAttrsPaths = getNonStringAttrsForASchemaFile("./sample_product_schemas/widget3.xml", version)

        // from the result of the transform, grab the attributes for widget
        val result = new StreamSource(new StringReader(nonStrAttrsFromXform))
        val xpath = xpathFactory.newXPath()
        xpath.setNamespaceContext(this)
        val xpathExpr = xpath.compile("//cf:schema[@key='http://docs.rackspace.com/usage/widget' and @version='" + version + "']/cf:attributes/text()")
        val attributes = xpathExpr.evaluate(result.asInstanceOf[Any], XPathConstants.STRING).asInstanceOf[String]

        v1NonStrAttrsPaths.foreach ( path => {
                assert(attributes.contains(path.trim()), "path " + path.trim() + " is in generated nonStringAttrs.xsl list")
            }
        )
    }

    def transform(inputXML: String) : String = {
        val trans = templates.newTransformer()
        val writer = new StringWriter()

        trans.setParameter("schemaDirectory", "./../../../sample_product_schemas")
        trans.transform(new StreamSource(new StringReader(inputXML)), new StreamResult(writer))

        writer.toString()
    }

    def getNonStringAttrsForASchemaFile(schemaFile : String, version : String) : Buffer[String] = {
        val xpath = xpathFactory.newXPath()
        xpath.setNamespaceContext(this)

        val widgetUsageSchema = new File(schemaFile)
        val src = new StreamSource(widgetUsageSchema)
        val xpathExpr = xpath.compile("//sch:productSchema[@version = '" + version + "']//sch:attribute[contains('" + nonStringTypes + "', string(@type))]")
        val attrs = xpathExpr.evaluate(src.asInstanceOf[Any], XPathConstants.NODESET).asInstanceOf[util.ArrayList[TinyNodeImpl]]
        attrs.map( attrNode => {
            val parent = attrNode.getParent();
            if ( parent.getLocalPart().equals("attributeGroup") ) {
                parent.getAttributeValue("", "name") + "/@" + attrNode.getAttributeValue("", "name")
            } else {
                "product/@" + attrNode.getAttributeValue("", "name")
            }
        })
    }
}
