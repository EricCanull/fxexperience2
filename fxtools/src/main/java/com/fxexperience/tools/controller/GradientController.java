/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.tools.controller;


import com.paintpicker.scene.control.gradientpicker.GradientControl;
import com.paintpicker.scene.control.picker.PaintPicker;
import com.paintpicker.scene.control.picker.mode.Mode;
import com.paintpicker.utils.ColorEncoder;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GradientController extends GridPane {

    @FXML private Rectangle gradientSquare;
    @FXML private Button fxButton;
    @FXML private Circle gradientCircle;
    @FXML private TextArea gradientCSSText;
    @FXML private GridPane gridPane;
    
   private PaintPicker gradientPicker;

    public GradientController() {
        initialize();
    }

    /**
     * Initializes the controller class.
     */
    private void initialize() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GradientController.class.getResource("/fxml/FXMLGradientPanel.fxml")); //NOI18N
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(GradientController.class.getName()).log(Level.SEVERE, null, ex);
        }

        gradientPicker = new PaintPicker(Color.web("#111111"), Mode.GRADIENT);
        gradientPicker.setPrefWidth(200);
        GridPane.setConstraints(gradientPicker, 2, 3);
        gridPane.getChildren().add(gradientPicker);

        final ChangeListener<Paint> onPaintChanged = ((ov, oldValue, newValue) -> updateGradientCSS());
        gradientPicker.valueProperty().addListener(onPaintChanged);
    }
    
     public void updateGradientCSS() {
         String style;
         if (GradientControl.getGradientType().equals(GradientControl.GradientType.LINEAR)
                 || GradientControl.getGradientType().equals(GradientControl.GradientType.RADIAL)) {
             style = GradientControl.GRADIENT_CSS.get();
         } else {
             style = ColorEncoder.encodeColor((Color) gradientPicker.getValue());
         }

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
