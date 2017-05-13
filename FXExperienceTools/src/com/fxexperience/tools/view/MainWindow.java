package com.fxexperience.tools.view;
/*
 * Permissions of this copy-left license are conditioned on making available 
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */

import com.fxexperience.tools.controller.AbstractController;
import java.util.ResourceBundle;

public class MainWindow extends AbstractWindow {

    public MainWindow(AbstractController controller, ResourceBundle bundle) {
        super(controller, bundle);
    }

    @Override
    protected String iconFileName() {
        return "fxexperience-32.png";
    }

    @Override
    protected String fxmlFileName() {
        return "FXMLMainPanel.fxml";
    }

    @Override
    public String titleBundleKey() {
        return "main.title";
    }
}