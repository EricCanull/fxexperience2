package com.fxexperience.javafx.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/FXMLDemoPanel.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /* @param args the command line arguments */
    public static void main(String[] args) {
        launch(args);
    }
}
