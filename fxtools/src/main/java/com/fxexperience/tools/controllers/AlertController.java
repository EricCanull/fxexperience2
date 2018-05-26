package com.fxexperience.tools.controllers;
/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger
 * work using the licensed work through interfaces provided by the licensed
 * work may be distributed under different terms and without source code
 * for the larger work.
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlertController extends AnchorPane {

    protected double panelWidth;

    @FXML private AnchorPane statusDialog;
    @FXML private Label notification_label;
    @FXML private Boolean displayActive;


    public AlertController(String text) {
        initialize(text);
    }

    /**
     * Private
     */
    private void initialize(String text) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AlertController.class.getResource("/fxml/FXMLStatusDialog.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(AlertController.class.getName()).log(Level.SEVERE, null, ex);
        }

        notification_label.setText(text);
    }

    private void displayStatusAlert(String textMessage, StackPane rootContainer) {
//        double prefWidth = rootContainer.getLayoutBounds().getWidth();
//
//        AlertController alert = new AlertController(textMessage);
//        alert.setOpacity(0);
//
//        alert.setPanelWidth(prefWidth, currentToolIndex);
//
//        rootContainer.layoutXProperty().addListener((observable, oldValue, newValue) ->
//                alert.setPanelWidth(newValue.doubleValue(), currentToolIndex));
//
//        alert.setTranslateY(rootContainer.getLayoutY()+alert.getPrefHeight());
//
//        AnchorPane.setTopAnchor(alert, 0d);
//        rootAnchorPane.getChildren().add(alert);
//
//        new FadeInDownBigTransition(alert).play();
//        removeAlert(alert);
    }

    public void setDisplayActive(boolean showAlert) {
        this.displayActive = showAlert;
    }

    public boolean getDisplayActive() {
        return this.displayActive;
    }

    public void setStatusDialog(String newMessage) {
        this.notification_label.setText(newMessage);
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
                case 2:
                    setPrefWidth(panelWidth - 360d);
                    break;
                default:
                    setPrefWidth(panelWidth);
                    break;
            }
        }
    }
}

