package com.fxexperience.tools.handler;
/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger
 * work using the licensed work through interfaces provided by the licensed
 * work may be distributed under different terms and without source code
 * for the larger work.
 */
import javafx.scene.Node;

public interface ToolsHandler {

    // This method will allow the injection of the tool
    void setParentTool(Node tool);

    String getCodeOutput();

    void startAnimations();

    void stopAnimations();
}
