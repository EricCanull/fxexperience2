/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.tools.gui.menubar;

import com.fxexperience.tools.application.MainController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.ObjectBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Eric Canull
 */
public class MenubarController {
   
    private MainController mainController;
    private ToggleGroup toggleGroup;
     
    @FXML private ToggleButton stylerToggle;
    @FXML private ToggleButton splineToggle;
    @FXML private ToggleButton derivedColorToggle;
    @FXML private AnchorPane rootMenubar;

    public void initialize(MainController mainController) {
       toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(stylerToggle, splineToggle, derivedColorToggle);
        toggleGroup.getToggles().forEach((t) -> setIconBinding((ToggleButton) t));
        this.mainController = mainController;

    }

    private void setIconBinding(ToggleButton toggle) {
        ImageView icon = (ImageView) toggle.getGraphic();
        icon.effectProperty().bind(new ObjectBinding<Effect>() {
            { bind(toggle.selectedProperty()); }

            @Override
            protected Effect computeValue() {
                return toggle.isSelected() ? null : new ColorAdjust(0, -1, 0, 0);
            }
        });
    }
    
    
    @FXML
    private void stylerToggleAction(ActionEvent event) {
        // myController.setScreen(ToolsMain.screen1ID);

    }

    @FXML
    private void splineToggleAction(ActionEvent event) {
        // myController.setScreen(ToolsMain.screen2ID);
    }

    @FXML
    private void derivedToggleAction(ActionEvent event) {
        // myController.setScreen(ToolsMain.screen3ID);
    }
}
