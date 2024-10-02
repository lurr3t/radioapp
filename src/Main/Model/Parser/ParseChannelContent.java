package Main.Model.Parser;

import Main.Model.Channel.Channel;
import Main.Model.Channel.ChannelContent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.management.modelmbean.XMLParseException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * <h1>Downloads data for each episode from the API and stores it</h1>
 *
 * Stores the information for an episode in a ChannelContent object.
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class ParseChannelContent extends ParseTools {

    private Document doc;
    private Channel channel;

    /**
     * Parses the stream and stores the initialized Document object in the
     * doc attribute.
     *
     * @param stream The stream to be parsed
     * @throws XMLParseException If there was an error parsing the stream.
     */
    public ParseChannelContent(InputStream stream) throws XMLParseException {
        if ((this.doc = documentBuilder(stream)) == null) {
            throw new XMLParseException("There was an error building " +
                    "channel content parser from schedule XML");
        }
    }

    /**
     * Parses each episode from the stream and stores it in the channel
     * parameter.
     *
     * @param channel Will hold the parsed episode data.
     */
    public void parseChannelContent(Channel channel) {
        this.channel = channel;
        Element parent = doc.getDocumentElement();
        parseEachChannelContent(parent);
    }

    /*
    Goes through each episode in the channel schedule and adds it to the
    channel attribute.
     */
    private void parseEachChannelContent(Element parent) {
        // parses channel information
        NodeList nodes = parent.getElementsByTagName("scheduledepisode");

        for (int i = 0; i < nodes.getLength(); i++) {
            ChannelContent channelContent = new ChannelContent();
            setChannelContentAttributes(nodes, i, channelContent);
            this.channel.addChannelContent(channelContent);
        }
    }

    /*
    Finds relevant information from the API and stores it in the
    channelContent parameter.
     */
    private void setChannelContentAttributes(NodeList nodes, int i,
                                      ChannelContent channelContent) {
        channelContent.setTitle(getContentFromNodeList(
                i, nodes, "title"));
        channelContent.setEpisodeID(getContentFromNodeList(
                i, nodes, "episodeid"));
        channelContent.setDescription(getContentFromNodeList(
                i, nodes, "description"));
        channelContent.setStartTimeString(formatTimeString(getContentFromNodeList(
                i, nodes, "starttimeutc")));
        channelContent.setEndTimeString(formatTimeString(getContentFromNodeList(
                i, nodes, "endtimeutc")));
        channelContent.setImageURL(getContentFromNodeList(
                i, nodes, "imageurl"));
    }

    /*
    Formats the received date time from the api, and also makes it CET instead
    of UTC.
     */
    private String formatTimeString(String timeString) {
        try {
            SimpleDateFormat inputFormat =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("yyyy-MM-dd '|' HH:mm");
            /* Makes sure that the timeZone is set to (CET) utc+1.
            Note that in this case it's GMT+2. Dont know why, but in this
            case it is needed. */
            outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
            Date date = inputFormat.parse(timeString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
