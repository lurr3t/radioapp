package Main.Model.Parser;

import Main.Model.Channel.Channel;
import Main.Model.Channel.ChannelHolder;
import javax.management.modelmbean.XMLParseException;
import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <h1>Downloads data from each channel in the SverigesRadio API
 *     and parses it</h1>
 *
 * Stores the data for each channel and episode in a ChannelHolder object.
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class ParseChannel extends ParseTools {

    private ChannelHolder channelHolder;
    private String date;
    private Document doc;

    /**
     * Initializes the date and doc attribute.
     *
     * <p>date is the date that will be parsed. Note that the date
     *    must be of the form 2022-01-01</p>
     *
     * @param stream Stream that will be parsed.
     * @param date The date that will be parsed.
     * @throws XMLParseException If there was an error during the parsing.
     */
    public ParseChannel(InputStream stream, String date)
            throws XMLParseException {
        this.date = date;
        if ((this.doc = documentBuilder(stream)) == null) {
            throw new XMLParseException("There was an error building " +
                    "parser from XML");
        }
    }

    /**
     * Takes a ChannelHolder object and fills it with channels.
     *
     * @param channelHolder Will be filled with channels during the parsing.
     */
    public void parseAllChannels(ChannelHolder channelHolder) {
        this.channelHolder = channelHolder;
        Element parent = this.doc.getDocumentElement();
        parseEachChannel(parent);
    }

    /**
     * Retrieves a ParseChannelContent object for the corresponding url.
     *
     * <p>Takes the schedule url for the channel and adds needed parameters,
     *    including which date to be parsed.
     *
     *    Can be overridden, eg change the useAPI parameter in getStream
     *    to use local files instead.
     *
     *    Note: set the date parameter to null if it shouldn't be used</p>
     *
     * @param url The schedule url for the channel.
     * @param date The date the parsing should be done upon. Null if no date.
     * @return A ParseChannelContent object.
     * @throws XMLParseException If there was an error when opening the stream.
     */
    protected ParseChannelContent getContentParser(String url, String date)
            throws XMLParseException {
        ParseChannelContent parser = null;
        try {
            if (url != null) {
                String[] parameters = new String[2];
                parameters[0] = "pagination=false";

                if (date != null) { parameters[1] = "date=" + date; }

                String completeURL = createQueryString(url, parameters);
                parser = new ParseChannelContent(getStream(
                        completeURL, true));
            }
        } catch (IOException e) {
            throw new XMLParseException("Couldn't open xml from schedule url");
        }
        return parser;
    }

    /*
    Goes through each channel and parses it.
     */
    private void parseEachChannel(Element parent) {
        // parses channel information
        NodeList nodes = parent.getElementsByTagName("channel");
        for (int i = 0; i < nodes.getLength(); i++) {
            // checks if channel already exists in channelHolder
            boolean channelAlreadyExists = true;
            String name = ((Element) nodes.item(i)).getAttribute("name");
            Channel channel = this.channelHolder.getChannelByName(name);

            if (channel == null) {
                channel = new Channel();
                channelAlreadyExists = false;
            }
            setChannelAttributes(nodes, i, channel);

            // gets the channel contents
            try {
                ParseChannelContent contentParser = getContentParser(
                                    channel.getScheduleURL(), this.date);
                if (contentParser != null) {
                    contentParser.parseChannelContent(channel);
                }
            }
            // Sets the error message attribute in Channel object
            catch (XMLParseException e) {
                channel.setChannelContentErrorMessage(e.getMessage());
            }
            /*
            only adds the channel if it has any content,
            and the channel doesn't already exists in the holder.
             */
            if (!channel.getChannelContent().isEmpty()) {
                if (!channelAlreadyExists) {
                    channelHolder.addChannel(channel);
                }
            }
        }
    }

    /*
    Finds relevant information from the API and stores it in the correct
    Channel attribute.
     */
    private void setChannelAttributes(NodeList nodes, int i,
                                      Channel channel) {
        //finds the id and name attributes;
        Element item = (Element) nodes.item(i);
        String name = item.getAttribute("name");
        String id = item.getAttribute("id");
        channel.setChannelName(name);
        channel.setChannelId(Integer.parseInt(id));

        channel.setImageURLString(getContentFromNodeList(
                i, nodes, "image"));
        channel.setColorCode(getContentFromNodeList(
                i, nodes, "color"));
        channel.setChannelDescription(getContentFromNodeList(
                i, nodes, "tagline"));
        channel.setSiteURLString(getContentFromNodeList(
                i, nodes, "siteurl"));
        channel.setScheduleURL(getContentFromNodeList(
                i, nodes, "scheduleurl"));
        channel.setChannelType(getContentFromNodeList(
                i, nodes, "channeltype"));
    }
}
