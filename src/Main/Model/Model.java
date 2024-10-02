package Main.Model;

import Main.Model.Channel.Channel;
import Main.Model.Channel.ChannelContainer;
import Main.Model.Channel.ChannelHolder;
import Main.Model.Parser.Parser;
import javax.management.modelmbean.XMLParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>Handles all of the logic regarding downloading and parsing
 *     from the Sverige radio API</h1>
 *
 * Stores the parsed information in a ChannelContainer, and eventual
 * error message.
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class Model {

    private Map<String, ChannelHolder> sortedChannels;
    private static final int daysBefore = 1;
    private static final int daysAfter = 1;

    /**
     * Initializes sortedChannelsContainer attribute.
     */
    public Model() {

    }

    /**
     * The main method for running the parser. Returns the data in a
     * Channel container.
     *
     * <p>Downloads information from the Sverige Radio API and parses it.
     *    Thereafter sorting each channel into it's correct category.
     *
     *    The method is synchronized to make sure that only one parsing
     *    instance can run at once.</p>
     * @param categories Channels will be sorted into each category.
     * @return A ChannelContainer object containing the parsed channels.
     */
    public synchronized ChannelContainer run(String[] categories) {
        ChannelContainer sortedChannelsContainer = new ChannelContainer();
        try {
            this.sortedChannels = new HashMap<>();
            sortChannels(getParser(daysBefore, daysAfter).getChannels(),
                    categories);
            sortedChannelsContainer.setSortedChannels(this.sortedChannels);

        } catch (XMLParseException e) {
            sortedChannelsContainer.setErrorMessage(e.getMessage());
        }
        return sortedChannelsContainer;
    }

    /**
     * Sorts the channels into each category.
     *
     * <p>The first characters in front of a space in the channel name
     *    will be compared to the categories.
     *
     *    For example, the (SR radio) channel will
     *    be added to the (SR) category.
     *    category: SR
     *    channel: SR radio
     *    </p>
     *
     * @param unsortedChannels ChannelHolder holding the channels but unsorted.
     * @param categories Holds the names of the different categories.
     */
    private void sortChannels(ChannelHolder unsortedChannels,
                             String[] categories) {
        // loops through all of the channels
        for (Channel channel : unsortedChannels.getChannels()) {
            // loops through all of the categories
            boolean foundCategory = false;
            for (String category : categories) {
                // checks if the name of a channel should be in a category
                if (channel.getChannelName().split(" ")[0]
                        .compareTo(category) == 0) {
                    /* checks if the dictionary already has channelHolder
                    initialized */
                    addChannelToCategory(channel, category);
                    foundCategory = true;
                    break;
                }
            }
            // adds to the other category
            if (!foundCategory) {
                addChannelToCategory(channel, "other");
            }
        }
    }

    /**
     * Retrieves the initialized Parser object.
     *
     * <p>Can be overridden to change the Parser object</p>
     *
     * @param daysBefore Days before the current date time to be parsed.
     * @param daysAfter Days after the current date time to be parsed.
     * @return A Parser object.
     * @throws XMLParseException If there were to be an error when parsing.
     */
    protected Parser getParser(int daysBefore, int daysAfter)
            throws XMLParseException {
        return new Parser(daysBefore, daysAfter);
    }

    /*
    Adds a channel to a category, specified by the categoryString parameter.
     */
    private void addChannelToCategory(Channel channel, String categoryString) {
        if (!this.sortedChannels.containsKey(categoryString)) {
            this.sortedChannels.put(categoryString,
                    new ChannelHolder());
        }
        this.sortedChannels.get(categoryString)
                .addChannel(channel);
    }
}
