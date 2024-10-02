package Main.Model.Parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.management.modelmbean.XMLParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;

/**
 * <h1>Supplies common methods for the parser</h1>
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class ParseTools {

    protected String apiDefault = "https://api.sr.se/api/v2";
    protected String apiChannels = "/channels";

    public ParseTools() { }

    /**
     * Initializes a Document object and parses an InputStream.
     *
     * @param stream The stream that will be parsed.
     * @return A Document object.
     * @throws XMLParseException Thrown if the stream couldn't be parsed.
     */
    protected Document documentBuilder(InputStream stream)
                                       throws XMLParseException {
        Document doc;
        try {
            DocumentBuilderFactory docFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();

            doc = builder.parse(stream);
            doc.getDocumentElement().normalize();

        } catch (ParserConfigurationException
                 | IOException
                 | SAXException e){
            throw new XMLParseException("There was en exception loading the" +
                    "XML file from stream");
        }
        return doc;
    }

    /**
     * Retrieves the content inside of a parent element in the form of a string.
     *
     * @param parentElement The parent element.
     * @param elementName Name of the element in the parent.
     * @return The content in the form of a string,
     *         or null if there is no content.
     */
    protected String getContentFromElement(Element parentElement,
                                           String elementName) {
        String content = null;
        NodeList items = parentElement.getElementsByTagName(elementName);
        Node node;
        if ((node = items.item(0)) != null) {
            content = node.getTextContent();
        }
        return content;
    }

    /**
     * Converts a Node into an Element
     *
     * @param node The node to be converted.
     * @return An Element object.
     */
    protected Element nodeElementConverter(Node node) {
        return (Element) node;
    }

    /**
     * Retrieves content in the form of a string from a NodeList.
     *
     * @param index The node in the index position.
     * @param nodeList The node list containing the nodes.
     * @param elementName Name of the element to retrieve.
     * @return The content in the form of a string.
     */
    protected String getContentFromNodeList(int index, NodeList nodeList,
                                            String elementName) {
        return getContentFromElement(
                nodeElementConverter(nodeList.item(index)), elementName);
    }

    /**
     * Builds a string used for accessing the API
     *
     * <p>Adds parameters to the default string and calculates where
     * to but ? and & respectively.</p>
     *
     * @param defaultString The default string.
     * @param parameterArray A string array containing parameters.
     * @return A string with the parameters added to the default string.
     */
    protected String createQueryString(String defaultString,
                                       String[] parameterArray) {
        StringBuilder completeString = new StringBuilder();
        completeString.append(defaultString);
        int i = 0;
        for (String subString : parameterArray) {
            // Start parameter
            if (i == 0 && !defaultString.contains("?")) {
                completeString.append("?");
            }
            else {
                completeString.append("&");
            }
            completeString.append(subString);
            i++;
        }
        return completeString.toString();
    }

    /**
     * Opens a stream from the url parameter.
     *
     * <p>The stream is opened using the URL or FileInputStream
     * depending on the useAPI parameter. This is used for easier testing,
     * which enables the use of local xml files.</p>
     *
     * @param url The url or path of the file that the stream should be opened
     *            upon.
     *
     * @param useAPI True if the api should be used. False if local files
     *               should be used.
     *
     * @return An InputStream object.
     * @throws IOException If the stream couldn't be opened.
     */
    protected InputStream getStream(String url, boolean useAPI)
            throws IOException {
        InputStream stream;
        if (useAPI) {
            stream = new URL(url).openStream();
        } else {
            stream = new FileInputStream(url);
        }
        return stream;
    }

}
