/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.tools.main;

import com.fxexperience.tools.gui.menubar.MenubarController;
import com.fxexperience.tools.gui.styler.StylerMainController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Eric Canull
 */
public class MainController implements Initializable {

    @FXML private AnchorPane menuPane;
    @FXML private StackPane mainContent;
    @FXML private MenubarController menubarController;
    

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
      //  prepareSlideMenuAnimation();
       
        menubarController.init(this);
        
       
       
    }

    public void setScreen(String id) {
       
        try {
            
            SplitPane splitPane = FXMLLoader.load(StylerMainController.class.getResource("FXMLStylerMain.fxml"));
            mainContent.getChildren().add(splitPane);

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
}
