package Main;

import Main.Controller.Controller;
import Main.Model.Model;
import Main.View.View;
import javax.swing.*;

/**
 * <h1>Runs a radio schedule application</h1>
 *
 * Initializes a controller object which runs a radio schedule application
 * with it's own GIU. The application is invoked on the EDT.
 *
 * The data is supplied via the SverigesRadio API
 * https://sverigesradio.se/api/documentation/v2/metoder/kanaler.html
 *
 * @author Ludwig Fallstrom (dv20lfm@cs.umu.se)
 * @version 2.0
 * @since 2022-01-13
 */
public class Radio {

    /**
     * Initializes the Controller object on the EDT.
     *
     * <p>Initializes the Model, View, Controller objects.</p>
     *
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new Controller(new Model(), new View()));

    }
}

