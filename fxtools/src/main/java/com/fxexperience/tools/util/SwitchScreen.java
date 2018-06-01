/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.tools.util;

import com.fxexperience.tools.controllers.SplineController;
import java.util.HashMap;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author eric
 */
public class SwitchScreen {
    
    // Custom interpolator for the slide animation transition
    private static final Interpolator INTERPOLATOR = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);
    
//    private final ObservableMap<Integer, Node> screeny = FXCollections.observableHashMap();
    
    // Holds the tools to be displayed
    private final HashMap<Integer, Node> screens = new HashMap<>();
   
    // Containers for the tools for slide animation
    private final StackPane rootContainer;
    private StackPane currentPane, sparePane;

    private Timeline timeline;

    private SimpleIntegerProperty screenIndex =
            new SimpleIntegerProperty(this, "currentToolIndex");
    
    private Screen nextScreen;
    
    public SwitchScreen(StackPane rootContainer) {
        this.rootContainer = rootContainer;
        currentPane = new StackPane();
        sparePane = new StackPane();
    }
    
    public void setDefaultScreen(Node node) {
        sparePane.setVisible(false);
        currentPane.getChildren().add(node);
        rootContainer.getChildren().addAll(currentPane, sparePane);
        screenIndex.set(0);
    }

     // Displays a new toolController and applies the slide transitions
    public void change(Screen screen) {

        // check if existing animation running
        if (timeline != null) {
            nextScreen = screen;
            timeline.setRate(5);
            return;
        } else {
            nextScreen = null;
        }

        // stop spline toolController animations
        if (screens.get(screenIndex.get()) instanceof SplineController) {
           ((SplineController)screens.get(screenIndex.get())).stopAnimations();
        }

        // load new content
        sparePane.getChildren().setAll(screens.get(screen.ordinal()));
        sparePane.setCache(true);
        currentPane.setCache(true);

        // wait one pulse then animate
        Platform.runLater(() -> {
            // animate switch
            if (screen.ordinal() > screenIndex.get()) { // animate from bottom
                screenIndex.set(screen.ordinal());
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
                screenIndex.set(screen.ordinal());
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

        // Check if we have animation waiting
        if (nextScreen != null) {
           change(nextScreen);
        }
        
        // start spline tool animations
        if (screens.get(screenIndex.get()) instanceof SplineController) {
            ((SplineController)screens.get(screenIndex.get())).startAnimations();
        }
    };  
    
    public void putScreen(Integer index, Node node) {
        screens.put(index, node);
    }
    
//    public void setScreens(ObservableMap<Integer, Node> map) {
//        screeny.putAll(map);
//    }
    
    public HashMap<Integer, Node> getScreens() {
        return screens;
    }
}
