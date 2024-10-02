
package Main.Model.Parser;

import Main.Model.Channel.Channel;
import Main.Model.Channel.ChannelHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.modelmbean.XMLParseException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Makes sure that a channel is parsed correctly.
 */
class ParseChannelTest {

    ParseChannel parser;
    ChannelHolder holder = new ChannelHolder();

    @BeforeEach
    void init(){
        try {
            InputStream stream = new FileInputStream(
                    "src/tests/Resources/channel_complete.xml");
            this.parser = new ParseChannel(stream, "2022-01-01");
        } catch (XMLParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getChannels() {
        this.parser.parseAllChannels(this.holder);
        int i = 0;
        for (Channel channel : holder.getChannels()) {
            if (i == 0) {
                Assertions.assertEquals(
                        channel.getChannelType(), "Rikskanal");
                Assertions.assertEquals(
                        channel.getChannelDescription(), "test1");
                Assertions.assertEquals(
                        channel.getColorCode(), "31a1bd");
                Assertions.assertEquals(
                        channel.getImageURLString(), "test1");
                Assertions.assertEquals(
                        channel.getScheduleURL(), "132");
                Assertions.assertEquals(
                        channel.getSiteURLString(),
                        "https://sverigesradio.se/p1");
            } else {
                Assertions.assertEquals(
                        channel.getChannelType(), "Rikskanal");
                Assertions.assertEquals(
                        channel.getChannelDescription(), "test2");
                Assertions.assertEquals(
                        channel.getColorCode(), "ff5a00");
                Assertions.assertEquals(
                        channel.getImageURLString(), "test2");
                Assertions.assertEquals(
                        channel.getScheduleURL(), "163");
                Assertions.assertEquals(
                        channel.getSiteURLString(),
                        "https://sverigesradio.se/p2");
            }
            /*
            Makes sure that the channelContentErrorMessage attribute has
            content.
             */
            Assertions.assertNotNull(channel.getChannelContentErrorMessage());
            i++;
        }

    }
}

