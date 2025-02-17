package Main.Model.Parser;

import Main.Model.Channel.ChannelHolder;
import javax.management.modelmbean.XMLParseException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * <h1>Downloads data from the SverigesRadio API and parses it</h1>
 *
 * Stores the data for each channel and episode in a ChannelHolder object.
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class Parser extends ParseTools {

    ArrayList<ParseChannel> channelParsers;
    ChannelHolder channelHolder;
    String mainChannelsURL;

    /**
     * Initializes attributes and sets as many channel parsers as there
     * are days in the range from daysBefore to daysAfter the current date.
     *
     * @param daysBefore Days before the current date to be parsed.
     * @param daysAfter Days after the current date to be parsed.
     * @throws XMLParseException If there was and error during the parsing.
     */
    public Parser(int daysBefore, int daysAfter) throws XMLParseException{
        this.channelParsers = new ArrayList<>();
        this.channelHolder = new ChannelHolder();
        String[] parameters = {"pagination=false"};
        this.mainChannelsURL = createQueryString(
                apiDefault + apiChannels, parameters);
        setChannelParsers(daysBefore,daysAfter);
    }

    /**
     * Starts the parsing of all the parsers stored in the
     * channelParsers attribute. And thereafter returns a ChannelHolder with
     * all the channels gathered,
     *
     * @return All the channels stored in a ChannelHolder object.
     * @throws XMLParseException If there was an error during the parsing.
     */
    public ChannelHolder getChannels() throws XMLParseException {
        for (ParseChannel parser : this.channelParsers) {
            parser.parseAllChannels(this.channelHolder);
        }
        return this.channelHolder;
    }

    /**
     * Gets a ParseChannel object. Can be used for overriding the useAPI
     * parameter in the getStream.
     *
     * @param url Url of the stream.
     * @param date The date that the parser will parse.
     * @return A ParseChannel object.
     * @throws XMLParseException If there was an error during the parsing.
     */
    protected ParseChannel getParseChannel(String url, String date)
            throws XMLParseException {
        try {
            return new ParseChannel(getStream(url, true), date);
        } catch (IOException e) {
            throw new XMLParseException(
                    "Couldn't open stream from main channel xml:" +
                            " check your internet connection!");
        }
    }

    /*
    Adds parsers to the channelParsers attribute. Each parser gets it's own
    date. And the dates depends on the daysBefore and daysAfter parameters.
    Note: before and after the current date.
     */
    private void setChannelParsers(int daysBefore, int daysAfter)
            throws XMLParseException {

        int dateRange = daysBefore + daysAfter + 1;
        int daysBeforeCounter = daysBefore;
        for (int i = 0; i < dateRange; i++) {
            LocalDate date = LocalDate.now().minusDays(daysBeforeCounter);
            String dateString = date.toString();

            this.channelParsers.add(getParseChannel(createQueryString(
                    this.mainChannelsURL, new String[]{dateString}),
                    dateString));
            daysBeforeCounter--;
        }
    }
}
