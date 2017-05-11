/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.tools.application;

import com.fxexperience.tools.handler.ViewHandler;
import com.fxexperience.tools.controllers.styler.StylerController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.ObjectBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Eric Canull
 */
public class MainController extends AbstractController implements Initializable {
    
    private ToggleGroup toggleGroup;
   
    @FXML private AnchorPane menuPane;
    @FXML private StackPane mainContent;
   
    @FXML private ToggleButton stylerToggle;
    @FXML private ToggleButton splineToggle;
    @FXML private ToggleButton derivedColorToggle;

    public MainController(ViewHandler viewHandler) {
        super(viewHandler);
     
    }
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(stylerToggle, splineToggle, derivedColorToggle);
        toggleGroup.getToggles().forEach((t) -> setIconBinding((ToggleButton) t));
        toggleGroup.selectToggle(stylerToggle);
//         setScreen("FXMLStylerPanel.fxml");
//      
          
        prepareSlideMenuAnimation();     
    }

    public void setScreen(String id) {

        try {
            Parent splitPane = FXMLLoader.load(StylerController.class.getResource(id));
            mainContent.getChildren().add(splitPane);

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void prepareSlideMenuAnimation() {

        TranslateTransition openNav = new TranslateTransition(new Duration(350), menuPane);
        openNav.setToY(0);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), menuPane);

        menuPane.setOnMouseEntered((MouseEvent event) -> {
            if (menuPane.getTranslateY() != 0) {
                openNav.play();
            }
        });

        menuPane.setOnMouseExited((MouseEvent event) -> {
            if (menuPane.getTranslateY() == 0) {
                closeNav.setToY(-100);
                closeNav.play();
            }
        });
    }

    @FXML
    private void stylerToggleAction(ActionEvent event) {
        setScreen("FXMLStylerPanel.fxml");  
    }

    @FXML
    private void splineToggleAction(ActionEvent event) {
    }

    @FXML
    private void derivedToggleAction(ContextMenuEvent event) {
    }

    @FXML
    private void derivedToggleAction(ActionEvent event) {
    }
}
