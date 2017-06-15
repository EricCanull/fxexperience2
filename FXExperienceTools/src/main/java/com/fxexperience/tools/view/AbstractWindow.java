/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */

package com.fxexperience.tools.view;

import com.fxexperience.tools.controller.AbstractController;
import com.fxexperience.tools.util.AppPaths;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public abstract class AbstractWindow {

	private final AbstractController controller;
	private final ResourceBundle bundle;

	public AbstractWindow(AbstractController controller, ResourceBundle bundle) {
		this.controller = controller;
		this.bundle = bundle;
	}

	public Parent root() throws IOException {
		FXMLLoader loader = new FXMLLoader(url(), bundle);
		loader.setController(controller);
		return loader.load();
	}

	private URL url() {
		return getClass().getClassLoader().getResource("fxml/FXMLMainPanel.fxml");
	}

	public String iconFilePath() {
		return AppPaths.IMG_PATH + iconFileName();
	}
        
	public boolean nonresizable() {
            return false;
        }

	public boolean resizable() {
		return true;
	}

	protected abstract String iconFileName();
	protected abstract String fxmlFileName();
	public abstract String titleBundleKey();

}