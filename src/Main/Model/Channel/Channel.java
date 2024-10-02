package Main.Model.Channel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * <h1>Holds information regarding a channel</h1>
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class Channel {

    private String channelName;
    private int channelId;
    private String channelContentErrorMessage;
    private ArrayList<ChannelContent> channelContent;
    private ArrayList<ChannelContent> channelContentByRange;
    private String scheduleURL;
    private String imageURLString;
    private String colorCode;
    private String channelDescription;
    private String siteURLString;
    private String channelType;

    /**
     * Initializes the channelContent attribute, and sets
     * channelContentErrorMessage to null.
     */
    public Channel() {

        this.channelContent = new ArrayList<>();
        this.channelContentErrorMessage = null;
    }

    /* Setters */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setChannelContentErrorMessage(
            String channelContentErrorMessage) {
        this.channelContentErrorMessage = channelContentErrorMessage;
    }

    public void addChannelContent(ChannelContent channelContent) {
        this.channelContent.add(channelContent);
    }

    public void setScheduleURL(String scheduleURL) {
        this.scheduleURL = scheduleURL;
    }

    public void setImageURLString(String imageURLString) {
        this.imageURLString = imageURLString;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }

    public void setSiteURLString(String siteURLString) {
        this.siteURLString = siteURLString;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    /* Getters */
    public int getChannelId() { return channelId; }

    public String getChannelName() { return channelName; }

    public String getChannelContentErrorMessage() {
        return channelContentErrorMessage;
    }

    public ArrayList<ChannelContent> getChannelContent() {
        return this.channelContent;
    }

    /**
     * Gets the episodes in a certain hour range.
     *
     * <p>Retrieves an ArrayList of ChannelContent in an hour frame
     * between hoursBefore and hoursAfter the current dateTime.
     *
     * The end time of the episode is the first variable in the range,
     * and the start time of the episode is the second variable
     * in the range.</p>
     *
     * @param hoursBefore
     * @param hoursAfter
     * @return An ArrayList containing the episodes in the hours range.
     */
    public ArrayList<ChannelContent> getChannelContentByHourRange(
            int hoursBefore, int hoursAfter) {
        if (this.channelContentByRange == null) {
            this.channelContentByRange = new ArrayList<>();

            DateTimeFormatter format = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd '|' HH:mm");

            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime firstRange = currentDateTime.minusHours(hoursBefore);
            LocalDateTime lastRange = currentDateTime.plusHours(hoursAfter);

            for (ChannelContent content : this.channelContent) {
                LocalDateTime startDate = LocalDateTime.parse(content
                        .getStartTimeString(), format);
                LocalDateTime endDate = LocalDateTime.parse(content
                        .getEndTimeString(), format);
                if (endDate.isAfter(firstRange)
                        && startDate.isBefore(lastRange)) {
                    channelContentByRange.add(content);
                }
            }
        }
        return this.channelContentByRange;
    }

    public String getScheduleURL() {
        return scheduleURL;
    }

    public String getImageURLString() {
        return imageURLString;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public String getSiteURLString() {
        return siteURLString;
    }

    public String getChannelType() {
        return channelType;
    }
}
