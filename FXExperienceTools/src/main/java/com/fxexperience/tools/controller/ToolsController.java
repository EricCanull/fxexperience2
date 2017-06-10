package com.fxexperience.tools.controller;
/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger
 * work using the licensed work through interfaces provided by the licensed
 * work may be distributed under different terms and without source code
 * for the larger work.
 */

import com.fxexperience.tools.handler.ToolsHandler;
import com.fxexperience.tools.util.AppPaths;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToolsController {

    // Custom interpolator for the slide animation transition
    private static final Interpolator INTERPOLATOR = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);

    // Holds the tools to be displayed
    private final HashMap<Integer, Node> tools = new HashMap<>();

    // Containers for the tools for slide animation
    private StackPane currentPane, sparePane;

    private StylerController stylerController;

    private StackPane rootContainer;

    private int currentToolIndex = 0;
    private Timeline timeline;
    private Node nextTool;

    public ToolsController(StackPane rootContainer) {
        this.rootContainer = rootContainer;
        initializeTools();
    }

    public void initializeTools() {
        loadTool(AppPaths.STYLER_ID, AppPaths.STYLER_FXML_PATH);
        loadTool(AppPaths.SPLINE_ID, AppPaths.SPLINE_FXML_PATH);
        loadTool(AppPaths.DERIVED_ID, AppPaths.DERIVED_FXML_PATH);

        currentPane = new StackPane();
        currentPane.getChildren().add(tools.get(AppPaths.STYLER_ID));
        sparePane = new StackPane();
        sparePane.setVisible(false);

        rootContainer.getChildren().addAll(currentPane, sparePane);

    }

    // Add the tool to the collection
    private void addTool(int id, Node screen) {
        tools.put(id, screen);
    }

    // Returns the Node with the appropriate name
    private Node getTool(int id) {
        return tools.get(id);
    }

    // Loads the fxml file, add the tool to the screens collection and
    // finally injects the screenPane to the controller.
    private boolean loadTool(int id, String fxml) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(fxml));
            Parent loadScreen = myLoader.load();
            ToolsHandler toolsHandler = myLoader.getController();
            toolsHandler.setParentTool(rootContainer);
            addTool(id, loadScreen);

            if (id == AppPaths.STYLER_ID) {
                stylerController = (StylerController) toolsHandler;

            }
            return true;
        } catch (IOException e) {
            Logger.getLogger(ToolsController.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    // Displays a new tool and applies the slide transitions
    public void setTool(int id) {

        // check if existing animation running
        if (timeline != null) {
            nextTool = tools.get(id);
            timeline.setRate(4);
            return;
        } else {
            nextTool = null;
        }

        // load new content
        sparePane.getChildren().setAll(tools.get(id));
        sparePane.setCache(true);
        currentPane.setCache(true);

        // wait one pulse then animate
        Platform.runLater(() -> {
            // animate switch
            if (id > currentToolIndex) { // animate from bottom
                currentToolIndex = id;
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
                currentToolIndex = id;
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

        // Attempt to turn off animations in the spline tool
        // start any animations
//        if (tools.get(currentToolIndex) instanceof AnimatedAction) {
//            ((AnimatedAction) tools[currentToolIndex].getContent()).startAnimations();
//        }
        // check if we have a animation waiting
        if (nextTool != null) {
            // switchTool(id);
        }
    };

    public int getCurrentToolIndex() {
        return currentToolIndex;
    }

    public StylerController getStylerController() {
        return stylerController;
    }
}