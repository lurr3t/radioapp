package Main.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <h1>Handles the GUI part of the application</h1>
 *
 * Contains all the parts that is needed for the construction of the GUI:
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-10
 */
public class View extends JFrame {

    private JMenu menu;
    private Map<String, JMenu> menuCategories;
    private Map<String, JMenuItem> menuItems;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton refreshButton;

    /**
     * Initiates the View object, and adds the different parts of the gui
     * to a JFrame
     */
    public View() {
        JMenuBar menuBar = setMenuBar();
        JScrollPane table = setTable();
        JPanel panelBottom = setPanelBottom();

        /* Layout order */
        add(menuBar, BorderLayout.NORTH);
        add(table);
        add(panelBottom, BorderLayout.SOUTH);

        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Displays the categories in the menu described in
     * the categories parameter.
     *
     * @param categories String array containing the names of the categories.
     */
    public void setMenuCategories(String[] categories) {
        SwingUtilities.invokeLater(() -> {
            if (categories != null) {
                // adds categories to hashmap
                this.menuCategories = new HashMap<>();
                this.menuItems = new HashMap<>();
                for (String category : categories) {
                    JMenu subMenu = new JMenu(category);
                    if (!this.menuCategories.containsKey(category)) {
                        this.menuCategories.put(category, subMenu);
                    }
                    this.menu.add(subMenu);
                }
            }
        });
    }

    /**
     * Adds a channel to a category
     *
     * @param category The category that the channel will be added to.
     * @param channel The channel name that will be added to the category.
     * @param actionListener A listener for the channel.
     */
    public void setMenuCategoryItem(String category, String channel,
                                    ActionListener actionListener) {
        SwingUtilities.invokeLater(() -> {
            JMenuItem itemCategory = this.menuCategories.get(category);
            JMenuItem newItem = new JMenuItem(channel);
            this.menuItems.put(channel, newItem);
            //adds actionListener to each item
            newItem.addActionListener(actionListener);
            itemCategory.add(newItem);
        });
    }

    /**
     * Removes all the channels from the channel categories
     *
     * <p>Removes all of the channels in the menu and it's listeners</p>
     *
     * @param channelNames A set of all the channel names.
     * @param actionListener The listener.
     */
    public void clearCategories(Set<String> channelNames,
                                ActionListener actionListener) {
        SwingUtilities.invokeLater(() -> {
            for (String categoryName : this.menuCategories.keySet()) {
                this.menuCategories.get(categoryName).removeAll();
            }
            for (String channelName : channelNames) {
                JMenuItem item = this.menuItems.get(channelName);
                if (item != null) {
                    item.removeActionListener(actionListener);
                    this.menuItems.remove(item);
                }
            }
        });
    }

    /**
     * Sets action listener for the refresh button.
     *
     * @param actionListener The ActionListener object
     */
    public void setListeners(ActionListener actionListener)
    {
        refreshButton.addActionListener(actionListener);
    }

    /**
     * Makes a popup come up with an error message.
     *
     * @param output The error message that will be shown.
     */
    public void showErrorPopup(String output) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, output,
                    "An error was found",
                    JOptionPane.ERROR_MESSAGE);
        });
    }

    /**
     * Adds content to a row in the table.
     *
     * @param rowContent String array containing content for each column
     */
    public void updateTableModel(String[] rowContent) {
        SwingUtilities.invokeLater(() -> {
            this.tableModel.addRow(rowContent);
        });
    }

    /**
     * Adds a mouse listener to the table.
     *
     * @param m A mouse adapter.
     */
    public void addMouseListener(MouseAdapter m) {
        this.table.addMouseListener(m);
    }

    /**
     * Displays channel content in a popup.
     *
     * @param img An image.
     * @param title Title of the channel.
     * @param description Description of the channel.
     */
    public void showContentPopup(ImageIcon img,
                                 String title, String description) {
        SwingUtilities.invokeLater(() -> {
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(1, 2));
            Container mainContainer = new Container();
            mainContainer.setLayout(new GridBagLayout());
            Container contentContainer = new Container();
            contentContainer.setLayout(new GridLayout(2, 1));
            mainContainer.add(contentContainer);
            JLabel labelImg = new JLabel();
            JLabel jTitle = new JLabel(title);
            jTitle.setFont(new Font("Courier", Font.BOLD,12));
            JLabel jDesc = new JLabel(description);

            if (img != null) {
                labelImg.setIcon(img);
                mainPanel.add(labelImg);
            }
            contentContainer.add(jTitle);
            contentContainer.add(jDesc);
            mainPanel.add(mainContainer);

            JOptionPane.showMessageDialog(null, mainPanel,
                    "Content", JOptionPane.PLAIN_MESSAGE);
        });
    }

    /**
     * Removes the content in the table.
     */
    public void clearTableModel() {
        SwingUtilities.invokeLater(() -> {
            this.tableModel.setRowCount(0);
        });
    }

    /**
     * Removes mouse listener from the table.
     *
     * @param m A mouse adapter.
     */
    public void removeMouseListener(MouseAdapter m) {
        this.table.removeMouseListener(m);
    }

    /**
     * Shows the default look of the refresh button.
     */
    public void defaultRefreshButton() {
        SwingUtilities.invokeLater(() -> {
            JButton button = this.refreshButton;
            button.setPreferredSize(new Dimension(400, 40));
            button.setText("Refresh");
            button.setBackground(Color.gray);
            toggleRefreshButton(true);
        });
    }

    /**
     * Shows the locked look of the refresh button.
     */
    public void disableRefreshButton() {
        SwingUtilities.invokeLater(() -> {
            JButton button = this.refreshButton;
            button.setText("Fetching new data...");
            toggleRefreshButton(false);
        });
    }

    /*
    Creates the top panels contents and returns the top panel.
     */
    private JMenuBar setMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.menu = new JMenu("categories");
        menuBar.add(this.menu);
        return menuBar;
    }

    /*
    Creates the bottom panels contents and returns the bottom panel.
     */
    private JPanel setPanelBottom() {
        JPanel panel = new JPanel();
        refreshButton = new JButton();
        defaultRefreshButton();
        refreshButton.setActionCommand("refreshButton");
        panel.setBackground(Color.GRAY);
        panel.add(refreshButton);
        return panel;
    }

    /*
    Creates table model with the names of each column,
    and adds it to a scrollPane. The scrollPane is later returned.
     */
    private JScrollPane setTable() {
        this.tableModel = new DefaultTableModel() {
            String[] columnNames = {
                    "Episode", "Start Time (CET)", "End Time (CET)"
            };
            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public String getColumnName(int index) {
                return columnNames[index];
            }
        };

        this.table = new JTable(this.tableModel);
        this.table.setFillsViewportHeight(true);

        return new JScrollPane(this.table);
    }

    /*
    Enables or disables the refresh button depending on what the parameter says.
     */
    private void toggleRefreshButton(boolean clickable) {
        this.refreshButton.setEnabled(clickable);
    }
}

