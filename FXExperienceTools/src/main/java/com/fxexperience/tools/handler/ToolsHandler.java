
package com.fxexperience.tools.handler;

import javafx.scene.Node;

public interface ToolsHandler {
    
    // This method will allow the injection of the tool
    void setParentTool(Node tool);
    
    String getCodeOutput();
    
    void startAnimations();
    void stopAnimations();
}
