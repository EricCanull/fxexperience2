/*
 * Permissions of this copy-left license are conditioned on making available 
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.application;

import com.fxexperience.tools.handler.impl.AppViewHandler;
import static com.fxexperience.tools.util.AppPaths.RESOURCE_BUNDLE;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Paweł Gawędzki 
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
          new AppViewHandler(primaryStage, ResourceBundle.getBundle(RESOURCE_BUNDLE, Locale.ENGLISH))
                .launchMainWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
