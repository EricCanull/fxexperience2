/*
 * Permissions of this Copyleft license are conditioned on making available 
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;


public class PreviewPanelController implements Initializable {

    @FXML private GridPane contentPanel;
    @FXML private ChoiceBox<?> choiceBox;
    @FXML private ComboBox<?> comboBox;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // tweek preview content panel
        choiceBox.getSelectionModel().select(0);
        comboBox.getSelectionModel().select(0);
    }

    public void setStyle(String css) {
        contentPanel.setStyle(css);
    }
}
