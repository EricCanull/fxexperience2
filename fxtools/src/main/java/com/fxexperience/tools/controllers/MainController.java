/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.controllers;

import com.fxexperience.javafx.controller.AnimationController;
import com.fxexperience.tools.handler.ViewHandler;
import com.fxexperience.tools.util.Screen;
import com.fxexperience.tools.util.SwitchScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public final class MainController extends AbstractController implements Initializable {
    
    @FXML
    private StackPane rootContainer;
    
    private SwitchScreen switchScreen;

    public MainController(ViewHandler viewHandler) {
       super(viewHandler);
    }

    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        switchScreen = new SwitchScreen(rootContainer);
        switchScreen.putScreen(Screen.CSS.ordinal(), new StyleController());
        switchScreen.putScreen(Screen.SPLINE.ordinal(), new SplineController());
        switchScreen.putScreen(Screen.DERIVATION.ordinal(), new DerivationController());
        switchScreen.putScreen(Screen.GRADIENT.ordinal(), new GradientController());
        switchScreen.putScreen(Screen.ANIMATION.ordinal(), new AnimationController());
        switchScreen.setDefaultScreen(switchScreen.getScreens().get(0));
    }

    @FXML
    private void styleToggleAction(ActionEvent event) {
        ToggleButton tog = ((ToggleButton) event.getSource());
       
        // prevent setting the same tool twice
        if (!tog.isSelected()) {
            tog.setSelected(true);
            return;
        }
        switchScreen.change(Screen.CSS);
    }

    @FXML private void splineToggleAction(ActionEvent event) {
       ToggleButton tog = ((ToggleButton) event.getSource());
       
        // prevent setting the same tool twice
        if (!tog.isSelected()) {
            tog.setSelected(true);
            return;
        } switchScreen.change(Screen.SPLINE);
    }

    @FXML private void derivedToggleAction(ActionEvent event) {
       ToggleButton tog = ((ToggleButton) event.getSource());
       
        // prevent setting the same tool twice
        if (!tog.isSelected()) {
            tog.setSelected(true);
            return;
        } switchScreen.change(Screen.DERIVATION);
    }
    
    @FXML private void gradientToggleAction(ActionEvent event) {
        ToggleButton tog = ((ToggleButton) event.getSource());
       
        // prevent setting the same tool twice
        if (!tog.isSelected()) {
            tog.setSelected(true);
            return;
        } switchScreen.change(Screen.GRADIENT);
    }

    @FXML private void animationToggleAction(ActionEvent event) {
        ToggleButton tog = ((ToggleButton) event.getSource());
       
        // prevent setting the same tool twice
        if (!tog.isSelected()) {
            tog.setSelected(true);
            return;
        } switchScreen.change(Screen.ANIMATION);
    }
}
