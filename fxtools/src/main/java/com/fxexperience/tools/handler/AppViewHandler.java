/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.handler;

import com.fxexperience.tools.view.AbstractWindow;
import com.fxexperience.tools.view.WindowFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class AppViewHandler implements ViewHandler {
 
    private final Stage primaryStage;
    private final ResourceBundle bundle;

    public AppViewHandler(Stage primaryStage, ResourceBundle bundle) {
        this.primaryStage = primaryStage;
        this.bundle = bundle;
    }

    @Override
    public void launchMainWindow() throws IOException {
        buildAndShowScene(primaryStage, WindowFactory.MAIN.createWindow(this, bundle));
    }

    private void buildAndShowScene(Stage stage, AbstractWindow window) throws IOException {
        stage.getIcons().add(new Image(window.iconFilePath()));
        stage.setTitle(bundle.getString(window.titleBundleKey()));
        stage.setResizable(window.resizable());
        stage.setScene(new Scene(window.root()));
        stage.show();
    }
}