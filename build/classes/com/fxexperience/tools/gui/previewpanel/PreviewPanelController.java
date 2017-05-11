/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.tools.gui.previewpanel;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

/**
 * Preview Controller class
 *
 * @author ericc
 */
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
