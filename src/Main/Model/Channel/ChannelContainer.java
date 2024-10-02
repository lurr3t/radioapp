package Main.Model.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * <h1>Holds a Map of the categories and it's corresponding channels.</h1>
 *
 * Also holds en eventual error message.
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class ChannelContainer {

    private String errorMessage;
    private Map<String, ChannelHolder> sortedChannels;

    /**
     * Initializes attributes.
     */
    public ChannelContainer() {
        this.sortedChannels = new HashMap<>();
        this.errorMessage = null;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSortedChannels(Map<String, ChannelHolder> sortedChannels) {
        this.sortedChannels = sortedChannels;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<String, ChannelHolder> getSortedChannels() {
        return sortedChannels;
    }
}
