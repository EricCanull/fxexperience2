package com.fxexperience.tools.application;


import com.fxexperience.tools.handler.impl.AppViewHandler;
import static com.fxexperience.tools.util.AppPaths.RESOURCE_BUNDLE;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Eric Canull
 */
public class ToolsApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
          new AppViewHandler(primaryStage, ResourceBundle.getBundle(RESOURCE_BUNDLE, Locale.getDefault()))
                .launchMainWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
