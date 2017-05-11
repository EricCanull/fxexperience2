package com.fxexperience.tools.view.impl;

import com.fxexperience.tools.application.AbstractController;
import com.fxexperience.tools.view.window.AbstractWindow;
import java.util.ResourceBundle;

public class StylerWindow extends AbstractWindow {

    public StylerWindow(AbstractController controller, ResourceBundle bundle) {
        super(controller, bundle);
    }

    @Override
    protected String iconFileName() {
        return "startIcon.png";
    }

    @Override
    protected String fxmlFileName() {
        return "FXMLMainControler.fxml";
    }

    @Override
    public String titleBundleKey() {
        return "main.title";
    }
}
