package Main.Controller;

import Main.Model.Channel.Channel;
import Main.Model.Channel.ChannelContainer;
import Main.Model.Channel.ChannelContent;
import Main.Model.Channel.ChannelHolder;
import Main.Model.Model;
import Main.View.View;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * <h1>Runs a radio schedule application</h1>
 *
 * Combines the model and view part of the radio application.
 * Also makes sure that the Model is running on a separate thread from the EDT.
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class Controller implements ActionListener {

    private String[] categories;
    private MouseAdapter mouseAdapter;
    private Map<String, Channel> allChannels;
    private View view;
    private Model model;
    private String currentChannel;

    /**
     * Sets the controller objects start attributes
     *
     * <p>Dictates in which order the application parts will run.</p>
     */
    public Controller(Model model, View view) {
        this.view = view;
        this.model = model;
        this.currentChannel = null;
        this.mouseAdapter = null;
        this.allChannels = new HashMap<>();
        this.categories = new String[]{
                "P1",
                "P2",
                "P3",
                "P4",
                "SR",
                "other"
        };
        this.view.setMenuCategories(this.categories);
        this.view.setListeners(this);
        startWorker(this);
        scheduleWork();
    }

    /**
     * Starts the swing worker, or changes table
     *
     * <p>Starts the swing worker if refresh is clicked
     * Also clears the current table and loads a new one if a channel
     * is clicked on.</p>
     *
     * @param e The action event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("refreshButton")) {
            startWorker(this);
        }
        // checks every channel.
        for (String channelName : this.allChannels.keySet()) {
            if (e.getActionCommand().equals(channelName)) {
                clearTable();
                loadTableAndListeners(allChannels.get(channelName));
                break;
            }
        }
    }

    /**
     * Creates a new swing worker for calling the run() method in Model.
     *
     * <p>Runs the model on a separate thread for performance gains
     * and making sure that the GUI wont freeze when downloading API data.
     *
     * The done part takes the information gathered from the model and stores
     * them in controller attributes. Also displays the channels in each
     * category, and displays the last shown table. Also displays eventual
     * error messages.
     * </p>
     *
     * @param actionListener The actionListener
     */
    protected void startWorker(ActionListener actionListener) {
        view.disableRefreshButton();

        SwingWorker<ChannelContainer, Void> worker = new SwingWorker<>() {

            /**
             * Starts the Model run method on a separate thread.
             *
             * <p>Makes API calls on a separate thread.</p>
             *
             * @return A ChannelContainer containing the channels and eventual
             *         error message.
             */
            @Override
            protected ChannelContainer doInBackground() {
                return model.run(categories);
            };

            /**
             * Writes the test result from the model to the gui. Or an error
             * message if there were an exception.
             */
            @Override
            protected void done() {
                try {
                    ChannelContainer channelContainer = get();
                    String errorMessage = channelContainer
                            .getErrorMessage();
                    // checks if channelContainer contains an error message
                    if (errorMessage == null) {
                        if (categories != null) {
                            clearCategories();
                            setMenuCategoryItems(channelContainer);
                        } else {
                            view.showErrorPopup("There exists no" +
                                    " channels that could be shown");
                        }

                        // loads the table last seen.
                        if (currentChannel != null) {
                            clearTable();
                            loadTableAndListeners(
                                    allChannels.get(currentChannel));
                        }

                    } else { view.showErrorPopup(errorMessage); }
                    view.defaultRefreshButton();

                } catch (InterruptedException | ExecutionException e) {
                    view.showErrorPopup("An error occurred when" +
                            " running swing worker: " + e.getMessage());
                }
            }

            /*
            Stores each channel in it's corresponding channel category.
            Also displays the channels in the category tab.
             */
            private void setMenuCategoryItems(
                    ChannelContainer channelContainer) {
                Map<String, ChannelHolder> sortedChannels = channelContainer
                        .getSortedChannels();
                for (String category : categories) {
                    ChannelHolder channelHolder = sortedChannels.get(category);
                    if (channelHolder != null) {
                        for (Channel channel : channelHolder.getChannels()) {
                            if (channel != null) {
                                String channelName = channel.getChannelName();
                                /* stores the channels in a hashmap with
                                each name as a key */
                                if (!allChannels.containsKey(channelName)) {
                                    allChannels.put(channelName, channel);
                                }
                                view.setMenuCategoryItem(category,
                                        channelName, actionListener);
                            }
                        }
                    }
                }
            }
        };
        worker.execute();
    }

    /*
    Runs the initializeWorker(swing worker) method once every hour.
     */
    private void scheduleWork() {
        ActionListener listener = lst -> {
            startWorker(this);
        };
        int hourInMilli = 60 * 60 * 1000;
        Timer timer = new Timer(hourInMilli, listener);
        timer.setRepeats(true);
        timer.start();
    }

    /*
    Clears all the channels in each category.
     */
    private void clearCategories() {
        if (!this.allChannels.isEmpty()) {
            view.clearCategories(this.allChannels.keySet(), this);
            allChannels = new HashMap<>();
        }
    }

    /*
    Clears the current table displaying, and removes it's listeners.
     */
    private void clearTable() {
        this.view.clearTableModel();
        if (this.mouseAdapter != null) {
            this.view.removeMouseListener(this.mouseAdapter);
        }
    }

    /*
    Loads and displays the table containing episodes from a channel.
    Also set's and describes a listener event.
     */
    private void loadTableAndListeners(Channel channel) {
        final int hoursBefore = 12;
        final int hoursAfter = 12;

        loadTable(channel, hoursBefore, hoursAfter);
        tableListenerEvent(channel, hoursBefore, hoursAfter);
    }

    /*
    Sets a listener on the table, and describes what will happen when
    an episode is clicked on.
     */
    private void tableListenerEvent(Channel channel,
                                    int hoursBefore,
                                    int hoursAfter) {
        this.mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                int index = table.getSelectedRow();
                // makes sure that there is no index out of bounds
                if (index >= 0) {
                    setContentPopup(channel.getChannelContentByHourRange(
                            hoursBefore, hoursAfter).get(index));
                }
            }
        };
        this.view.addMouseListener(this.mouseAdapter);
    }

    /*
    Displays the episodes in a channel in the hour range between,
    hoursBefore and hoursAfter.
     */
    private void loadTable(Channel channel, int hoursBefore, int hoursAfter) {
        this.currentChannel = channel.getChannelName();
        for (ChannelContent content : channel.getChannelContentByHourRange(
                hoursBefore, hoursAfter)) {
            String[] rowContent = new String[3];
            rowContent[0] = content.getTitle();
            rowContent[1] = content.getStartTimeString();
            rowContent[2] = content.getEndTimeString();
            this.view.updateTableModel(rowContent);
        }
    }

    /*
    Displays content of an episode in a popup window.
     */
    private void setContentPopup(ChannelContent content) {
        String description = content.getDescription();
        String title = content.getTitle();
        String imgURL = content.getImageURL();
        ImageIcon img = null;
        try {
            img = new ImageIcon(ImageIO.read(new URL(imgURL)));
        } catch (IOException e) { }
        view.showContentPopup(img, title, description);
    }
}

