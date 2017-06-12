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

import com.fxexperience.tools.handler.ToolsHandler;
import javafx.animation.*;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplinePanelController implements Initializable, ToolsHandler {

    protected Node splinePanelController;
    
    @FXML private GridPane gridPane;
    @FXML private TextField codeTextField;  
    @FXML private Rectangle fadeSquare;
    @FXML private Circle linearCircle;
    @FXML private Circle scaleCircle;
    @FXML private Rectangle rotateRectangle;
    
    private Timeline timeline;
    private SplineEditor SplineEditor;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SplineEditor = new SplineEditor();
       
        GridPane.setConstraints(SplineEditor, 0, 0, 1, 10,
                HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
        gridPane.add(SplineEditor, 0, 0);

        codeTextField.textProperty().bind(new StringBinding() {
            {
                bind(SplineEditor.controlPoint1xProperty(),
                        SplineEditor.controlPoint1yProperty(),
                        SplineEditor.controlPoint2xProperty(),
                        SplineEditor.controlPoint2yProperty());
            }

            @Override
            protected String computeValue() {
                return String.format("Interpolator.SPLINE(%.4f, %.4f, %.4f, %.4f);",
                        SplineEditor.getControlPoint1x(),
                        SplineEditor.getControlPoint1y(),
                        SplineEditor.getControlPoint2x(),
                        SplineEditor.getControlPoint2y());
            }
        });
        
        // create animation updater
        ChangeListener<Number> animUpdater = (ObservableValue<? extends Number> ov, Number t, Number t1) -> updateAnimation();
        
        SplineEditor.controlPoint1xProperty().addListener(animUpdater);
        SplineEditor.controlPoint1yProperty().addListener(animUpdater);
        SplineEditor.controlPoint2xProperty().addListener(animUpdater);
        SplineEditor.controlPoint2yProperty().addListener(animUpdater);
    }

    @Override
    public void startAnimations() {
        updateAnimation();
    }

    @Override
    public void stopAnimations() {
        if (timeline != null) {
            PauseTransition pauseTransition = new PauseTransition();
            pauseTransition.setDuration(Duration.seconds(1.5));
            pauseTransition.play();
            pauseTransition.setOnFinished((ActionEvent t) -> {
                timeline.stop();
                scaleCircle.setScaleX(0d);
                rotateRectangle.setRotate(0d);
                fadeSquare.setOpacity(1);
                linearCircle.translateXProperty().setValue(0d);
            });
            timeline.stop();
        }
    }

    private void updateAnimation() {
        Interpolator spline = Interpolator.SPLINE(SplineEditor.getControlPoint1x(),
                SplineEditor.getControlPoint1y(),
                SplineEditor.getControlPoint2x(),
                SplineEditor.getControlPoint2y());
        
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().addAll(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(scaleCircle.scaleXProperty(), 0d, spline),
                        new KeyValue(scaleCircle.scaleYProperty(), 0d, spline),
                        new KeyValue(rotateRectangle.rotateProperty(), 0d, spline),
                        new KeyValue(fadeSquare.opacityProperty(), 0d, spline),
                        new KeyValue(linearCircle.translateXProperty(), 0d, spline)                                           
                ),
                new KeyFrame(
                        Duration.seconds(5),
                        new KeyValue(scaleCircle.scaleXProperty(), 1d, spline),
                        new KeyValue(scaleCircle.scaleYProperty(), 1d, spline),
                        new KeyValue(rotateRectangle.rotateProperty(), 360d, spline),
                        new KeyValue(rotateRectangle.rotateProperty(), 360d, spline),
                        new KeyValue(fadeSquare.opacityProperty(), 1d, spline),
                        new KeyValue(linearCircle.translateXProperty(), 170d, spline)
                )
        );
        timeline.play();
    }

    @Override
    public String getCodeOutput() {
       return codeTextField.getText();
    }

    @Override
    public void setParentTool(Node parentTool) {
       splinePanelController = parentTool;
    }
}
   