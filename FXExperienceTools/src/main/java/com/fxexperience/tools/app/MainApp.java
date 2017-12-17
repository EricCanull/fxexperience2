package com.fxexperience.tools.app;
/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */

import com.fxexperience.tools.handler.AppViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.fxexperience.tools.util.AppPaths.RESOURCE_BUNDLE;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        new AppViewHandler(stage, ResourceBundle.getBundle(RESOURCE_BUNDLE, Locale.getDefault()))
                .launchMainWindow();
    }

    /* @param args the command line arguments */
    public static void main(String... args) {
        launch(args);
    }
}
