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
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeType;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MainController extends AbstractController implements Initializable {
    private DoubleProperty arrowHeight = new SimpleDoubleProperty(50);
    private static double TOOLBAR_WIDTH = 90;

    @FXML private StackPane toolBar;

    @FXML private BorderPane rootBorderPane;
    @FXML private AnchorPane rootAnchorPane;
    @FXML private StackPane rootContainer;

    @FXML private ToggleButton stylerToggle;
    @FXML private ToggleButton splineToggle;
    @FXML private ToggleButton derivedColorToggle;
    @FXML private CheckMenuItem themeMenuItem;

    private ToolsController toolsController;

    public MainController(ViewHandler viewHandler) {
        super(viewHandler);
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        toolsController = new ToolsController(rootContainer);

        initToggleGroup();

        // create toolbar background path

        toolBar.setClip(createToolBarPath(Color.BLACK, null));
        Path toolBarBackground = createToolBarPath(null,Color.web("#606060"));
        toolBar.getChildren().add(toolBarBackground);


    }

    // Creates toggle group to bind color icon effect
    private void initToggleGroup() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(stylerToggle, splineToggle, derivedColorToggle);
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


    private void displayStatusAlert(String textMessage) {
        StatusAlertController alert = new StatusAlertController(textMessage);
        alert.setOpacity(0);

        double prefWidth = toolsController.getCurrentToolIndex() == 0
                ? rootContainer.getLayoutBounds().getWidth() - 350d
                : rootContainer.getLayoutBounds().getWidth();

        alert.setPrefWidth(prefWidth);

        alert.setTranslateY(rootContainer.getHeight()+alert.getPrefHeight());
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
        String stylerControllerCSS = isDarkThemeSelected
                ? getClass().getResource(AppPaths.STYLE_PATH + "styler_dark.css").toExternalForm()
                : getClass().getResource(AppPaths.STYLE_PATH + "styler_light.css").toExternalForm();

        rootBorderPane.getStylesheets().clear();
        rootBorderPane.getStylesheets().add(mainControllerCSS);
        toolsController.getStylerController().getRootSplitPane().getStylesheets().clear();
        toolsController.getStylerController().getRootSplitPane().getStylesheets().add(stylerControllerCSS);
    }

    private void setArrowSelected(Node toggleButton) {
        double minY = toggleButton.getBoundsInParent().getMinY();
        double centerY = toggleButton.getLayoutBounds().getHeight() / 2.0;
        arrowHeight.set(minY + centerY);
    }

    private Path createToolBarPath(Paint fill, Paint stroke) {
        Path toolPath = new Path();
        toolPath.setFill(fill);
        toolPath.setStroke(stroke);
        toolPath.setStrokeType(StrokeType.CENTERED);
        LineTo arrowTop = new LineTo(TOOLBAR_WIDTH,0);
        arrowTop.yProperty().bind(arrowHeight.add(-8));
        LineTo arrowTip = new LineTo(TOOLBAR_WIDTH-10,0);
        arrowTip.yProperty().bind(arrowHeight);
        LineTo arrowBottom = new LineTo(TOOLBAR_WIDTH,0);
        arrowBottom.yProperty().bind(arrowHeight.add(8));
        LineTo bottomRight = new LineTo(TOOLBAR_WIDTH,0);
        bottomRight.yProperty().bind(rootContainer.heightProperty());
        LineTo bottomLeft = new LineTo(0,0);
        bottomLeft.yProperty().bind(rootContainer.heightProperty());
        toolPath.getElements().addAll(
                new MoveTo(0,0),
                new LineTo(TOOLBAR_WIDTH,0),
                arrowTop, arrowTip, arrowBottom,
                bottomRight, bottomLeft,
                new ClosePath()
        );
        return toolPath;
    }

    @FXML
    private void setThemeAction() {
        loadStyle(themeMenuItem.isSelected());
    }

    @FXML
    private void stylerToggleAction(ActionEvent event) {
        // Prevent setting the same tool twice
        if (stylerToggle.isSelected()) {
            toolsController.setTool(AppPaths.STYLER_ID);
            setArrowSelected(stylerToggle);
        } else { // tool is already active
            stylerToggle.setSelected(true);
        }
    }

    @FXML
    private void splineToggleAction(ActionEvent event) {
        // Prevent setting the same tool twice
        if (splineToggle.isSelected()) {
            toolsController.setTool(AppPaths.SPLINE_ID);
            setArrowSelected(splineToggle);
        } else { // tool is already active
            splineToggle.setSelected(true);
        }
    }

    @FXML
    private void derivedToggleAction(ActionEvent event) {
        // Prevent setting the same tool twice
        if (derivedColorToggle.isSelected()) {
            toolsController.setTool(AppPaths.DERIVED_ID);
           setArrowSelected(derivedColorToggle);
        } else { // tool is already active
            derivedColorToggle.setSelected(true);
        }
    }

    @FXML
    private void copyButtonAction(ActionEvent event) {
        Clipboard.getSystemClipboard().setContent(
                Collections.singletonMap(DataFormat.PLAIN_TEXT, toolsController.getStylerController().getCodeOutput()));
        displayStatusAlert("Code has been copied to the clipboard.");
    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        if (stylerToggle.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(rootContainer.getScene().getWindow());
            if (file != null && !file.exists() && file.getParentFile().isDirectory()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(toolsController.getStylerController().getCodeOutput());
                    displayStatusAlert("Code saved to " + file.getAbsolutePath());
                    writer.flush();
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void closeButtonAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
