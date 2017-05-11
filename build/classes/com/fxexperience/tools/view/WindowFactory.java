/**
 * Permissions of this copy-left license are conditioned on making available 
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.view;

import com.fxexperience.tools.controller.AbstractController;
import com.fxexperience.tools.controller.impl.MainController;
import com.fxexperience.tools.handler.ViewHandler;
import com.fxexperience.tools.view.impl.MainWindow;
import com.fxexperience.tools.view.window.AbstractWindow;
import java.util.ResourceBundle;

/**
 * 
 * @author Paweł Gawędzki
 */
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
