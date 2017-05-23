/*
 * Permissions of this copy-left license are conditioned on making available 
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.util;

import com.fxexperience.tools.controller.StylerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Tool {
    
    private FXMLLoader loader;
    private final String name;
    private final Parent content;
    private final int index;

    public Tool(String name, Parent content, int index) {
        this.name = name;
        this.content = content;
        this.index = index;
        
    }
    
    public FXMLLoader getLoader(String fxml) {
        loader = new FXMLLoader(StylerController.class.getResource(
                    AppPaths.FXML_PATH + fxml));
        return loader;
    }
            

    public Parent getContent() {
        return content;
    }

    public String getName() {
        return name;
    }
    
    public int getIndex() {
        return index;
    }

}
