package com.fxexperience.javafx.app;

import com.fxexperience.javafx.control.fontpicker.FontPickerController;
import com.fxexperience.javafx.controller.AnimationController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/*
 * Simple Demo application for the Canned animation transitions in
 * FXExperience Controls
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        
        FontPickerController fontPicker = new FontPickerController();
      //  AnimationController animationController = new AnimationController();
        Scene scene = new Scene(fontPicker);

        stage.setScene(scene);
        stage.show();
    }

    /* @param args the command line arguments */
    public static void main(String[] args) {
        launch(args);
    }
}
