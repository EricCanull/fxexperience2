package com.fxexperience.tools.controller;
/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger
 * work using the licensed work through interfaces provided by the licensed
 * work may be distributed under different terms and without source code
 * for the larger work.
 */

import com.fxexperience.tools.util.AppPaths;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;


import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatusAlertController extends AnchorPane {

    protected double panelWidth;

    @FXML private AnchorPane statusDialog;
    @FXML private Label statusLabel;


    public StatusAlertController(String text) {
        initialize(text);
    }

    /**
     * Private
     */
    private void initialize(String text) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StatusAlertController.class.getResource("/fxml/FXMLStatusDialog.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(StatusAlertController.class.getName()).log(Level.SEVERE, null, ex);
        }

        statusLabel.setText(text);
    }

    public void setPanelWidth(double panelWidth, int toolIndex) {
        this.panelWidth = panelWidth;

        if (this != null) {
            switch (toolIndex) {
                case 0:
                    setPrefWidth(panelWidth - 350d);
                    break;
                case 1:
                    setPrefWidth(panelWidth);
                    break;
                default:
                    setPrefWidth(panelWidth);
                    break;
            }
        }
    }
}

