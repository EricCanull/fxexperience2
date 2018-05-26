/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.previewer.controller;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author eric
 */
public class PreviewScene {
   
    private final Stage stage = new Stage();
    
    public PreviewScene(Window owner) {
        
        PreviewController previewController = new PreviewController();
        Scene customScene = new Scene(previewController);

        if (owner != null) {
            stage.initOwner(owner);
        }

        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
    //    stage.addEventHandler(KeyEvent.ANY, keyEventListener);
       
        
        stage.getOwner().widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                stage.setWidth(newValue.doubleValue());
            }
        });
     
        stage.getOwner().xProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                stage.setX(newValue.doubleValue());
            }
        });
        stage.getOwner().yProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                stage.setY(newValue.doubleValue() + stage.getOwner().getHeight());
            }
        });
        
        
        customScene.setFill(Color.web("#31363B"));
                
        stage.setScene(customScene);
    }
    
}
