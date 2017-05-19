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
import com.fxexperience.tools.controller.MainController;
import com.fxexperience.tools.handler.ViewHandler;
import java.util.ResourceBundle;

public enum WindowFactory {

    MAIN {
        @Override
        public AbstractWindow createWindow(ViewHandler viewHandler, ResourceBundle bundle) {
            final AbstractController controller = new MainController(viewHandler);
            return new MainWindow(controller, bundle);
        }
    };

    public abstract AbstractWindow createWindow(ViewHandler viewHandler, ResourceBundle bundle);
}
