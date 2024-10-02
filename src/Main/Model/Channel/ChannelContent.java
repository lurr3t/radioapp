package Main.Model.Channel;

/**
 * <h1>Holds information regarding each episode</h1>
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class ChannelContent {

    private String errorMessage;
    private String episodeID;
    private String title;
    private String description;
    private String startTimeString;
    private String endTimeString;
    private String imageURL;

    public ChannelContent() {
        this.errorMessage = null;
    }

    /* Setters */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setEpisodeID(String episodeID) {
        this.episodeID = episodeID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /* Getters */
    public String getErrorMessage() {
        return errorMessage;
    }

    public String getEpisodeID() {
        return episodeID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public String getImageURL() {
        return imageURL;
    }
}
