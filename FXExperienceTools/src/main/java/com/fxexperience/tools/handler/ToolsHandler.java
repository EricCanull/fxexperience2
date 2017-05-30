
package com.fxexperience.tools.handler;

import javafx.scene.Node;

public interface ToolsHandler {
    
    //This method will allow the injection of the Parent ScreenPane
    public void setScreenParent(Node screenParent);
    
    public String getCodeOutput();
    
    public void startAnimations();
    public void stopAnimations();
}
