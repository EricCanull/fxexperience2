/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PreviewController extends VBox {

    @FXML private ChoiceBox<?> choiceBox;
    @FXML private ComboBox<?> comboBox;
    @FXML private TextField searchTextField;
    @FXML private Button goButton;
    @FXML private ListView<String> stringValuesListView;
    @FXML private ListView<Integer> integerValuesListView;


    public PreviewController() {
        initialize();
    }

    /**
     * Private
     */
    private void initialize() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PreviewController.class.getResource("/fxml/FXMLPreviewPanel.fxml")); //NOI18N
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(PreviewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        setMenuBoxPresets();

        ObservableList<String> values = FXCollections.observableArrayList("Alpha","Beta", "Gamma");
        stringValuesListView.setItems(values);
    }

    private void setMenuBoxPresets() {
        // set presets for menu-boxes
        choiceBox.getSelectionModel().select(0);
        comboBox.getSelectionModel().select(0);
    }

    public void setPreviewPanelStyle(String code) {
        setStyle(code);
    }
}
