/*
 * Permissions of this copy-left license are conditioned on making available 
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.controller.impl;

import com.fxexperience.tools.util.Tool;
import com.fxexperience.tools.util.AnimatedAction;
import com.fxexperience.tools.util.AppPaths;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Jasper Potts, Eric Canull
 */
public class MainController implements Initializable {
   
    private static final Interpolator INTERPOLATOR = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);
    private ToggleGroup toggleGroup;
    private StackPane currentPane, sparePane;
     
    final SimpleDoubleProperty width = new SimpleDoubleProperty(85);
    private int currentToolIndex = 0;
    private int nextToolIndex = 0;
    
    private Timeline timeline;
    private Tool nextTool;
     
    private Tool[] tools;
   
  
    @FXML private BorderPane root;
    @FXML private StackPane rootContainer;
    @FXML private VBox menuPane;
   
    @FXML private ToggleButton stylerToggle;
    @FXML private ToggleButton splineToggle;
    @FXML private ToggleButton derivedColorToggle;

  
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        initToggleGroup();
        
        initTools();
        
        prepareNavigationBarAnimation();
        
        currentPane = new StackPane();
        currentPane.getChildren().add(tools[0].getContent());
        sparePane = new StackPane();
        sparePane.setVisible(false);

        rootContainer.getChildren().addAll(currentPane, sparePane);
    }

    private void initTools() {
        try {
            tools = new Tool[] {
                new Tool("Styler", (Parent) FXMLLoader.load(StylerController.class.getResource(
                AppPaths.FXML_PATH + "FXMLStylerPanel.fxml")), 0),
                new Tool("Animation Spline Editor", new SplineEditor(), 1),
                new Tool("Derived Color Calculator", (Parent) FXMLLoader.load(DerivationController.class.getResource(
                AppPaths.FXML_PATH + "FXMLDerivationPanel.fxml")), 2)
            };
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   private void initToggleGroup() {
        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(stylerToggle, splineToggle, derivedColorToggle);
        toggleGroup.getToggles().forEach((t) -> setIconBinding((ToggleButton) t));
        toggleGroup.selectToggle(stylerToggle);
    }
   
    private void setIconBinding(ToggleButton toggle) {
        ImageView icon = (ImageView) toggle.getGraphic();
        icon.effectProperty().bind(new ObjectBinding<Effect>() {
            { bind(toggle.selectedProperty()); }

            @Override
            protected Effect computeValue() {
                return toggle.isSelected() ? null : new ColorAdjust(0, -1, 0, 0);
            }
        });
    }

    private void prepareNavigationBarAnimation() {
//        
//        final double expandedWidth = 85;
//
//       // apply the animations when the button is pressed.
//      navPane.setOnMouseClicked((MouseEvent event) -> {
//          // create an animation to hide sidebar.
//          final Animation hideSidebar = new Transition() {
//              { setCycleDuration(Duration.millis(250)); }
//              @Override
//              protected void interpolate(double frac) {
//                  final double curWidth = expandedWidth * (1.0 - frac);
//                  menuPane.setPrefWidth(curWidth);
//                  
//                  menuPane.setTranslateX(-expandedWidth + curWidth);
//                  navPane.setTranslateX(-expandedWidth + curWidth);
//              }
//          };
//          hideSidebar.onFinishedProperty().set((EventHandler<ActionEvent>) (ActionEvent actionEvent1) -> {
//              menuPane.setVisible(false);
//               navPane.setPrefWidth(15);
////              controlButton.setText(">");
////              controlButton.getStyleClass().remove("hide-left");
////              controlButton.getStyleClass().add("show-right");
//          });
//  
//          // create an animation to show a sidebar.
//          final Animation showSidebar = new Transition() {
//              { setCycleDuration(Duration.millis(250)); }
//              @Override
//              protected void interpolate(double frac) {
//                  
//                  final double curWidth = expandedWidth * frac;
//                  menuPane.setPrefWidth(curWidth);
//                  navPane.setPrefWidth(curWidth);
//                  menuPane.setTranslateX(-expandedWidth + curWidth);
//                  navPane.setTranslateX(-expandedWidth + curWidth);
//                  System.out.println(curWidth);
//              }
//          };
//          showSidebar.onFinishedProperty().set((EventHandler<ActionEvent>) (ActionEvent actionEvent1) -> {
////              controlButton.setText("<");
////              controlButton.getStyleClass().add("hide-left");
////              controlButton.getStyleClass().remove("show-right");
//          });
//  
//          if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
//              if (menuPane.isVisible()) {
//                  hideSidebar.play();
//              } else {
//                  menuPane.setVisible(true);
//                  showSidebar.play();
//              }
//          }
//      });
//    }

//        menuPane.setOnMouseEntered((MouseEvent event) -> {
//
//            System.out.println(menuPane.isVisible());
//            // create an animation to hide sidebar.
//            final Animation showSidebar = new Transition() {
//                {
//                    setCycleDuration(Duration.millis(250));
//                }
//
//                @Override
//                protected void interpolate(double frac) {
//
//                    final double curWidth = expandedWidth * frac;
//                    menuPane.setPrefWidth(curWidth + 75);
//                    menuPane.setTranslateX(-expandedWidth + curWidth);
//                }
//            };
//
//            // menuPane.setVisible(true);
//            showSidebar.play();
//
//        });
        TranslateTransition openNav = new TranslateTransition(new Duration(350), menuPane);
        openNav.setToX(0);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), menuPane);

//        menuPane.setOnMouseEntered((MouseEvent event) -> {
//
//            if (menuPane.getTranslateX() < 0) {
//               
//                openNav.play();
//                
//               //rootContainer.setTranslateX(0);
//            }
//        });
//
//        menuPane.setOnMouseExited((MouseEvent event) -> {
//
//            if (menuPane.getTranslateX() == 0) {
//                closeNav.setToX(-95);
//                closeNav.play();
//               
//               // root.setMaxWidth(root.getWidth()-95);
//                 //rootContainer.setTranslateX(-95);
//                 
//                
//
//            }
//        });
   }
    
     public void switchTool(Tool newTool) {
         
        // check if existing animation running
        if (timeline != null) {
            nextTool = newTool;
            nextToolIndex = newTool.getIndex();
            timeline.setRate(4);
            return;
        } else {
            nextTool = null;
            nextToolIndex = -1;
        }
        
        // stop any animations
        if (tools[currentToolIndex].getContent() instanceof AnimatedAction) {
            ((AnimatedAction) tools[currentToolIndex].getContent()).stopAnimations();
        }
        
        // load new content
        sparePane.getChildren().setAll(newTool.getContent());
        sparePane.setCache(true);
        currentPane.setCache(true);
        // wait one pulse then animate
        Platform.runLater(() -> {
            // animate switch
            if (newTool.getIndex() > currentToolIndex) { // animate from bottom
                currentToolIndex = newTool.getIndex();
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

            } else { // from top
                currentToolIndex = newTool.getIndex();
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
        // start any animations
        if (tools[currentToolIndex].getContent() instanceof AnimatedAction) {
            ((AnimatedAction) tools[currentToolIndex].getContent()).startAnimations();
        }
        // check if we have a animation waiting
        if (nextTool != null) {
            switchTool(nextTool);
        }
    };

    @FXML
    private void stylerToggleAction(ActionEvent event) {
        if (tools[0].getIndex() == currentToolIndex) {
            stylerToggle.setSelected(true);
            return;
        }
        switchTool(tools[0]);
    }

    @FXML
    private void splineToggleAction(ActionEvent event) {
        if (tools[1].getIndex() == currentToolIndex) {
            splineToggle.setSelected(true);
            return;
        }
        switchTool(tools[1]);
    }

    @FXML
    private void derivedToggleAction(ActionEvent event) {
        if (tools[2].getIndex() == currentToolIndex) {
            derivedColorToggle.setSelected(true);
            return;
        }
        switchTool(tools[2]);
    }

    @FXML
    private void closeButtonAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
