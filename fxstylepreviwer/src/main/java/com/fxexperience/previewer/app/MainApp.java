package com.fxexperience.previewer.app;

import com.fxexperience.previewer.controller.PreviewController;
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
        PreviewController animationController = new PreviewController();
        Scene scene = new Scene(animationController);

        stage.setScene(scene);
        stage.show();
    }

    /* @param args the command line arguments */
    public static void main(String[] args) {
        launch(args);
    }
}
