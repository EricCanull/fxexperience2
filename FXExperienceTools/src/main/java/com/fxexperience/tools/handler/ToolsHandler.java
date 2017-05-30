
package com.fxexperience.tools.handler;

import javafx.scene.Node;

public interface ToolsHandler {
    
    // This method will allow the injection of the tool
    public void setParentTool(Node tool);
    
    public String getCodeOutput();
    
    public void startAnimations();
    public void stopAnimations();
}
