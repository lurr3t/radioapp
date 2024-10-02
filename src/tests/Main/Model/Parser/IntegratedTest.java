package Main.Model.Parser;

import Main.Controller.Controller;
import Main.Model.Model;
import Main.View.View;
import javax.management.modelmbean.XMLParseException;
import javax.swing.*;
import java.io.IOException;

/**
 * A complete test of the application, but the data is gathered from local
 * files instead of the API. Is mainly used to assert that the application
 * updates what's showing if the refresh button is pressed.
 *
 * Remember to change the channel xml's date-time to be within 12 hours
 * of current time. Otherwise it won't show.
 */
public class IntegratedTest {

    public static void main(String[] args) {

        String mainFileURL = "src/tests/Resources/IntegratedTest/" +
                "main_channels.xml";

        Model model = new Model() {
            @Override
            protected Parser getParser(int daysBefore, int daysAfter)
                    throws XMLParseException {
                return new Parser(0,0) {
                    @Override
                    protected ParseChannel getParseChannel(String url,
                                                           String date)
                            throws XMLParseException {
                        try {
                            return new ParseChannel(getStream(
                                    mainFileURL, false), date) {
                                @Override
                                protected ParseChannelContent getContentParser(
                                        String url, String date)
                                        throws XMLParseException{
                                    try {
                                        System.out.println(url);
                                        return new ParseChannelContent(
                                                getStream(url, false));
                                    } catch (IOException e) {
                                        e.getStackTrace();
                                        throw new XMLParseException();
                                    }
                                }
                            };
                        } catch (IOException e) {
                            e.getStackTrace();
                            throw new XMLParseException();
                        }
                    }
                };
            }
        };
        SwingUtilities.invokeLater(() -> new Controller(model, new View()));
    }
}
