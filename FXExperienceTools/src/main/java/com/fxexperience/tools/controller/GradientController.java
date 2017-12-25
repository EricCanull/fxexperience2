/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.tools.controller;

import com.fxexperience.javafx.scene.control.paintpicker.PaintPicker;
import com.fxexperience.javafx.scene.control.popup.ColorPopupEditor;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GradientController extends SplitPane {

    @FXML private Rectangle gradientSquare;
    @FXML private Button fxButton;
    @FXML private Circle gradientCircle;
    @FXML private TextArea gradientCSSText;
    @FXML private GridPane gridPane;
    
    private ColorPopupEditor gradientPicker;

    public GradientController() {

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GradientController.class.getResource("/fxml/FXMLGradientPanel.fxml")); //NOI18N
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
            initialize();

        } catch (IOException ex) {
            Logger.getLogger(GradientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    public void initialize() {

        gradientPicker = new ColorPopupEditor(PaintPicker.Mode.COLOR, Color.web("#111111"));
        gradientPicker.setPrefWidth(225);
        gradientPicker.setPadding(new Insets(0, 0, 0, 20));
        GridPane.setConstraints(gradientPicker, 3, 6);
        gridPane.getChildren().add(gradientPicker);

        final ChangeListener<Paint> onPaintChanged = ((ov, oldValue, newValue) -> updateGradientCSS());
        gradientPicker.getRectangle().fillProperty().addListener(onPaintChanged);
    }
    
     public void updateGradientCSS() {
        String style = gradientPicker.getGradientString();

        setGradientStyles(style);
        gradientCSSText.setText(style);
    }
     
    private void setGradientStyles(String style){
        this.gradientSquare.setStyle("-fx-fill: " + style);
        this.gradientCircle.setStyle("-fx-fill: " + style);
        this.fxButton.setStyle("-fx-background-color: " + style);
    }

    
    public String getCodeOutput() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void startAnimations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void stopAnimations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
