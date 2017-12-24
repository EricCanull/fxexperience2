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

import com.fxexperience.javafx.fxanimations.FadeInDownBigTransition;
import com.fxexperience.tools.handler.ViewHandler;
import com.fxexperience.tools.util.AppPaths;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public final class MainController extends AbstractController implements Initializable {

    public enum Tool {
        CSS, SPLINE, DERIVATION, GRADIENT, ANIMATION
    }

    // Custom interpolator for the slide animation transition
    private static final Interpolator INTERPOLATOR = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);

    private final DoubleProperty arrowHeight = new SimpleDoubleProperty(52);

    // Holds the tools to be displayed
    private final HashMap<Integer, Node> tools = new HashMap<>();

    @FXML private StackPane toolBar;
    @FXML private ToggleButton stylerToggle;
    @FXML private ToggleButton splineToggle;
    @FXML private ToggleButton derivedColorToggle;
    @FXML private ToggleButton gradientBuilderToggle;
    @FXML private ToggleButton animationToggle;

    @FXML private BorderPane rootBorderPane;
    @FXML private AnchorPane rootAnchorPane;
    @FXML private StackPane rootContainer;
    @FXML private CheckMenuItem themeMenuItem;

    private StyleController StyleController;
    private SplineController splineController;
    private DerivationController derivationController;
    private GradientController gradientController;
    private AnimationController animationController;

    // Containers for the tools for slide animation
    private StackPane currentPane, sparePane;

    private Timeline timeline;

    private int currentToolIndex;
    private Tool nextTool;

    public MainController(ViewHandler viewHandler) {
       super(viewHandler);
    }

    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadToggleGroup();
        loadTools();
        loadSelectedToolArrow();
    }

    // Creates toggle group to bind color icon effect
    private void loadToggleGroup() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(stylerToggle, splineToggle, derivedColorToggle,
                gradientBuilderToggle, animationToggle);
        toggleGroup.getToggles().forEach((t) -> setIconBinding((ToggleButton) t));
        toggleGroup.selectToggle(stylerToggle);
    }

    // Adjusts the color of the toogle icons upon selection
    private void setIconBinding(ToggleButton toggle) {
        ImageView icon = (ImageView) toggle.getGraphic();
        icon.effectProperty().bind(new ObjectBinding<Effect>() {
            {
                bind(toggle.selectedProperty());
            }

            @Override
            protected Effect computeValue() {
                return toggle.isSelected() ? null : new ColorAdjust(0, -1, 0, 0);
            }
        });
    }

    private void loadTools() {
        StyleController = new StyleController();
        splineController = new SplineController();
        derivationController = new DerivationController();
        gradientController = new GradientController();
        animationController = new AnimationController();

        tools.put(Tool.CSS.ordinal(), StyleController);
        tools.put(Tool.SPLINE.ordinal(), splineController);
        tools.put(Tool.DERIVATION.ordinal(), derivationController);
        tools.put(Tool.GRADIENT.ordinal(), gradientController);
        tools.put(Tool.ANIMATION.ordinal(), animationController);

        currentPane = new StackPane();
        sparePane = new StackPane();
        sparePane.setVisible(false);

        currentPane.getChildren().add(StyleController);
        rootContainer.getChildren().addAll(currentPane, sparePane);
        currentToolIndex = Tool.CSS.ordinal();
    }

    private void loadSelectedToolArrow() {
        // create toolbar background path
        toolBar.setClip(createToolBarPath(Color.web("#2c2f33"), null));
        Path toolBarBackground = createToolBarPath(null, Color.web("#2c2f33"));
        toolBar.getChildren().add(toolBarBackground);
    }

    // Displays a new tool and applies the slide transitions
    private void setTool(Tool tool) {

        // check if existing animation running
        if (timeline != null) {
            nextTool = tool;
            timeline.setRate(4);
            return;
        } else {
            nextTool = null;
        }

        // stop spline tool animations
        if (tools.get(currentToolIndex) instanceof SplineController) {
           splineController.stopAnimations();
        }

        // load new content
        sparePane.getChildren().setAll(tools.get(tool.ordinal()));
        sparePane.setCache(true);
        currentPane.setCache(true);

        // wait one pulse then animate
        Platform.runLater(() -> {
            // animate switch
            if (tool.ordinal() > currentToolIndex) { // animate from bottom
                currentToolIndex = tool.ordinal();
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
                currentToolIndex = tool.ordinal();
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
        StackPane temp = currentPane;
        currentPane = sparePane;
        sparePane = temp;

        // cleanup
        timeline = null;
        currentPane.setTranslateY(0);
        sparePane.setCache(false);
        currentPane.setCache(false);
        sparePane.setVisible(false);
        sparePane.getChildren().clear();

        // start spline tool animations
        if (tools.get(currentToolIndex) instanceof SplineController) {
           splineController.startAnimations();
        }

        // Check if we have a animation waiting
        if (nextTool != null) {
           setTool(nextTool);
        }
    };

    private void setArrow(Node toggleButton) {
        double minY = toggleButton.getBoundsInParent().getMinY();
        double centerY = toggleButton.getLayoutBounds().getHeight() / 2.0;
        arrowHeight.set(minY + centerY);
    }

    private Path createToolBarPath(Paint fill, Paint stroke) {
        final double toolbarWidth = 90;
        final Path toolPath = new Path();

        toolPath.setFill(fill);
        toolPath.setStroke(stroke);
        toolPath.setStrokeType(StrokeType.CENTERED);
        LineTo arrowTop = new LineTo(toolbarWidth,0);
        arrowTop.yProperty().bind(arrowHeight.add(-8));
        LineTo arrowTip = new LineTo(toolbarWidth-9,0);
        arrowTip.yProperty().bind(arrowHeight);
        LineTo arrowBottom = new LineTo(toolbarWidth,0);
        arrowBottom.yProperty().bind(arrowHeight.add(8));
        LineTo bottomRight = new LineTo(toolbarWidth,0);
        bottomRight.yProperty().bind(toolBar.heightProperty());
        LineTo bottomLeft = new LineTo(0,0);
        bottomLeft.yProperty().bind(toolBar.heightProperty());
        toolPath.getElements().addAll(
                new MoveTo(0,0),
                new LineTo(toolbarWidth,0),
                arrowTop, arrowTip, arrowBottom,
                bottomRight, bottomLeft,
                new ClosePath()
        );
        return toolPath;
    }

    private void displayStatusAlert(String textMessage) {
        double prefWidth = rootContainer.getLayoutBounds().getWidth();

        AlertController alert = new AlertController(textMessage);
        alert.setOpacity(0);

       alert.setPanelWidth(prefWidth, currentToolIndex);

       rootContainer.layoutXProperty().addListener((observable, oldValue, newValue) ->
               alert.setPanelWidth(newValue.doubleValue(), currentToolIndex));

        alert.setTranslateY(rootContainer.getLayoutY()+alert.getPrefHeight());

        AnchorPane.setTopAnchor(alert, 0d);
        rootAnchorPane.getChildren().add(alert);

        new FadeInDownBigTransition(alert).play();
        removeAlert(alert);
    }

    private void removeAlert(Node alert) {
        PauseTransition pauseTransition = new PauseTransition();
        pauseTransition.setDuration(Duration.seconds(4.5));
        pauseTransition.play();
        pauseTransition.setOnFinished((ActionEvent t) -> rootAnchorPane.getChildren().remove(alert));
    }

    private void loadStyle(boolean isDarkThemeSelected) {
        String mainControllerCSS = isDarkThemeSelected
                ? getClass().getResource(AppPaths.STYLE_PATH + "main_dark.css").toExternalForm()
                : getClass().getResource(AppPaths.STYLE_PATH + "main_light.css").toExternalForm();
        String styleControllerCSS = isDarkThemeSelected
                ? getClass().getResource(AppPaths.STYLE_PATH + "style_panel_dark.css").toExternalForm()
                : getClass().getResource(AppPaths.STYLE_PATH + "style_panel_light.css").toExternalForm();

        rootBorderPane.getStylesheets().clear();
        rootBorderPane.getStylesheets().add(mainControllerCSS);
        StyleController.getStylesheets().clear();
        StyleController.getStylesheets().add(styleControllerCSS);
    }

    @FXML private void stylerToggleAction(ActionEvent event) {
        // Prevent setting the same tool twice
        if (!stylerToggle.isSelected()) {
            stylerToggle.setSelected(true);
        } else {
            setTool(Tool.CSS);
            setArrow(stylerToggle);
        }
    }

    @FXML private void splineToggleAction(ActionEvent event) {
        // Prevent setting the same tool twice
        if (!splineToggle.isSelected()) {
            splineToggle.setSelected(true);
        } else {
            setTool(Tool.SPLINE);
            setArrow(splineToggle);
        }
    }

    @FXML private void derivedToggleAction(ActionEvent event) {
        // Prevent setting the same tool twice
        if (!derivedColorToggle.isSelected()) {
            derivedColorToggle.setSelected(true);
        } else {
            setTool(Tool.DERIVATION);
            setArrow(derivedColorToggle);
        }
    }
    @FXML private void gradientToggleAction(ActionEvent event) {
        // Prevent setting the same tool twice
        if (!gradientBuilderToggle.isSelected()) {
            gradientBuilderToggle.setSelected(true);
        } else {
            setTool(Tool.GRADIENT);
            setArrow(gradientBuilderToggle);
        }
    }

    @FXML private void animationToggleAction(ActionEvent event) {
        // Prevent setting the same tool twice
        if (!animationToggle.isSelected()) {
            animationToggle.setSelected(true);
        } else {
            setTool(Tool.ANIMATION);
            setArrow(animationToggle);
        }
    }

    @FXML private void setThemeAction() {
        loadStyle(themeMenuItem.isSelected());
    }

    @FXML private void closeButtonAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
