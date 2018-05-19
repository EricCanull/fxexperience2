/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.controller;

import com.fxexperience.javafx.controller.AnimationController;
import com.fxexperience.tools.handler.ViewHandler;
import com.fxexperience.tools.util.AppPaths;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public final class MainController extends AbstractController implements Initializable {

    public enum ToolController {
        CSS, SPLINE, DERIVATION, GRADIENT, ANIMATION
    }

    // Custom interpolator for the slide animation transition
    private static final Interpolator INTERPOLATOR = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);

    // Holds the tools to be displayed
    private final HashMap<Integer, Node> tools = new HashMap<>();

    @FXML private ToggleButton styleToggle, splineToggle, derivedColorToggle;
    @FXML private ToggleButton gradientBuilderToggle, animationToggle;

    @FXML private BorderPane rootBorderPane;
    @FXML private StackPane rootContainer;

    private StyleController StyleController;
    private SplineController splineController;
    private DerivationController derivationController;
    private GradientController gradientController;
    private AnimationController animationController;

    // Containers for the tools for slide animation
    private StackPane currentPane, sparePane;

    private Timeline timeline;

    private SimpleIntegerProperty currentToolIndex =
            new SimpleIntegerProperty(this, "currentToolIndex");
    
    private ToolController nextToolController;

    public MainController(ViewHandler viewHandler) {
       super(viewHandler);
    }

    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTools();
    }

    private void initializeTools() {
        StyleController = new StyleController();
        splineController = new SplineController();
        derivationController = new DerivationController();
        gradientController = new GradientController();
        animationController = new AnimationController();

        tools.put(ToolController.CSS.ordinal(), StyleController);
        tools.put(ToolController.SPLINE.ordinal(), splineController);
        tools.put(ToolController.DERIVATION.ordinal(), derivationController);
        tools.put(ToolController.GRADIENT.ordinal(), gradientController);
        tools.put(ToolController.ANIMATION.ordinal(), animationController);

        currentPane = new StackPane();
        sparePane = new StackPane();
        sparePane.setVisible(false);

        currentPane.getChildren().add(StyleController);
        rootContainer.getChildren().addAll(currentPane, sparePane);
        currentToolIndex.set(ToolController.CSS.ordinal());
    }

    // Displays a new toolController and applies the slide transitions
    private void setTool(ToolController toolController) {

        // check if existing animation running
        if (timeline != null) {
            nextToolController = toolController;
            timeline.setRate(5);
            return;
        } else {
            nextToolController = null;
        }

        // stop spline toolController animations
        if (tools.get(currentToolIndex.get()) instanceof SplineController) {
           splineController.stopAnimations();
        }

        // load new content
        sparePane.getChildren().setAll(tools.get(toolController.ordinal()));
        sparePane.setCache(true);
        currentPane.setCache(true);

        // wait one pulse then animate
        Platform.runLater(() -> {
            // animate switch
            if (toolController.ordinal() > currentToolIndex.get()) { // animate from bottom
                currentToolIndex.set(toolController.ordinal());
                sparePane.setTranslateY(rootContainer.getHeight());
                sparePane.setVisible(true);
                timeline = new Timeline(
                        new KeyFrame(Duration.millis(0),
                                new KeyValue(currentPane.translateYProperty(), 0, INTERPOLATOR),
                                new KeyValue(sparePane.translateYProperty(), rootContainer.getHeight(), INTERPOLATOR)),
                        new KeyFrame(Duration.millis(800),
                                animationEndEventHandler,
                                new KeyValue(currentPane.translateYProperty(), -rootContainer.getHeight(), INTERPOLATOR),
                                new KeyValue(sparePane.translateYProperty(), 0, INTERPOLATOR)));
                timeline.play();

            } else { // animate from top
                currentToolIndex.set(toolController.ordinal());
                sparePane.setTranslateY(-rootContainer.getHeight());
                sparePane.setVisible(true);
                timeline = new Timeline(
                        new KeyFrame(Duration.millis(0),
                                new KeyValue(currentPane.translateYProperty(), 0, INTERPOLATOR),
                                new KeyValue(sparePane.translateYProperty(), -rootContainer.getHeight(), INTERPOLATOR)),
                        new KeyFrame(Duration.millis(800),
                                animationEndEventHandler,
                                new KeyValue(currentPane.translateYProperty(), rootContainer.getHeight(), INTERPOLATOR),
                                new KeyValue(sparePane.translateYProperty(), 0, INTERPOLATOR)));
                timeline.play();
            }
        });
    }

    private final EventHandler<ActionEvent> animationEndEventHandler = (ActionEvent t) -> {

        // switch panes
        StackPane tempPane = currentPane;
        currentPane = sparePane;
        sparePane = tempPane;

        // cleanup
        timeline = null;
        currentPane.setTranslateY(0);
        sparePane.setCache(false);
        currentPane.setCache(false);
        sparePane.setVisible(false);
        sparePane.getChildren().clear();

        // start spline tool animations
        if (tools.get(currentToolIndex.get()) instanceof SplineController) {
           splineController.startAnimations();
        }

        // Check if we have a animation waiting
        if (nextToolController != null) {
           setTool(nextToolController);
        }
    };    

    private void loadStyle(boolean isDarkThemeSelected) {
        String mainControllerCSS = isDarkThemeSelected
                ? getClass().getResource(AppPaths.STYLE_PATH + "main.css").toExternalForm()
                : getClass().getResource(AppPaths.STYLE_PATH + "main_light.css").toExternalForm();
        String styleControllerCSS = isDarkThemeSelected
                ? getClass().getResource(AppPaths.STYLE_PATH + "main.css").toExternalForm()
                : getClass().getResource(AppPaths.STYLE_PATH + "style_panel_light.css").toExternalForm();

        rootBorderPane.getStylesheets().clear();
        rootBorderPane.getStylesheets().add(mainControllerCSS);
        StyleController.getStylesheets().clear();
        StyleController.getStylesheets().add(styleControllerCSS);
    }

    @FXML private void styleToggleAction(ActionEvent event) {
        // prevent setting the same tool twice
        if (!styleToggle.isSelected()) {
            styleToggle.setSelected(true);
            return;
        } setTool(ToolController.CSS);
    }

    @FXML private void splineToggleAction(ActionEvent event) {
        // prevent setting the same tool twice
        if (!splineToggle.isSelected()) {
            splineToggle.setSelected(true);
            return;
        } setTool(ToolController.SPLINE);
    }

    @FXML private void derivedToggleAction(ActionEvent event) {
        // prevent setting the same tool twice
        if (!derivedColorToggle.isSelected()) {
            derivedColorToggle.setSelected(true);
            return;
        } setTool(ToolController.DERIVATION);
    }
    
    @FXML private void gradientToggleAction(ActionEvent event) {
        // prevent setting the same tool twice
        if (!gradientBuilderToggle.isSelected()) {
            gradientBuilderToggle.setSelected(true);
            return;
        } setTool(ToolController.GRADIENT);
    }

    @FXML private void animationToggleAction(ActionEvent event) {
        // prevent setting the same tool twice
        if (!animationToggle.isSelected()) {
            animationToggle.setSelected(true);
            return;
        } setTool(ToolController.ANIMATION);
    }

    @FXML private void setThemeAction() {
        // loadStyle(themeMenuItem.isSelected());
    }

    @FXML private void closeButtonAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}