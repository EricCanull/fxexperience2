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

import javafx.scene.Parent;

public class Tool {
    private final String name;
    private final Parent content;
    private final int index;

    public Tool(String name, Parent content, int index) {
        this.name = name;
        this.content = content;
        this.index = index;
        
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
