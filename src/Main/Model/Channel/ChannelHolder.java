package Main.Model.Channel;

import java.util.ArrayList;

/**
 * <h1>Holds multiple channels</h1>
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class ChannelHolder {

    private ArrayList<Channel> channels;
    private String channelsErrorMessage;

    /**
     * Initializes attributes.
     */
    public ChannelHolder() {
        this.channels = new ArrayList<>();
        this.channelsErrorMessage = null;
    }

    /**
     * Adds a channel to the channels ArrayList
     *
     * @param channel The channel that will be added.
     */
    public void addChannel(Channel channel) {
        this.channels.add(channel);
    }

    public ArrayList<Channel> getChannels() {
        return this.channels;
    }

    /**
     * Retrieves the channel by it's name.
     *
     * @param name The name of the channel.
     * @return A channel object. Null if the channel doesn't exist.
     */
    public Channel getChannelByName(String name) {
        Channel channel = null;
        for (Channel each : this.channels) {
            if (each.getChannelName().compareTo(name) == 0) {
                channel = each;
                break;
            }
        }
        return channel;
    }

    public void setChannelsErrorMessage(String channelsErrorMessage) {
        this.channelsErrorMessage = channelsErrorMessage;
    }

    public String getChannelsErrorMessage() {
        return channelsErrorMessage;
    }
}
