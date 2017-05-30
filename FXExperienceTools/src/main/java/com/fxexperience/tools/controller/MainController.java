/*
 * Permissions of this Copyleft license are conditioned on making available 
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.controller;

import com.fxexperience.tools.handler.ToolsHandler;
import com.fxexperience.tools.handler.ViewHandler;
import com.fxexperience.tools.util.AppPaths;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class MainController extends AbstractController implements Initializable {

    private static final Interpolator INTERPOLATOR = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);

    //Holds the screens to be displayed
    private final HashMap<Integer, Node> screens = new HashMap<>();

    private StackPane currentPane, sparePane;

    private StylerController stylerController;

    private int currentToolIndex = 0;
    private Timeline timeline;
    private Node nextTool;

    @FXML private StackPane rootContainer;

    @FXML private ToggleButton stylerToggle;
    @FXML private ToggleButton splineToggle;
    @FXML private ToggleButton derivedColorToggle;

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

        loadScreen(AppPaths.STYLER_ID, AppPaths.STYLER_FXML_PATH);
        loadScreen(AppPaths.SPLINE_ID, AppPaths.SPLINE_FXML_PATH);
        loadScreen(AppPaths.DERIVED_ID, AppPaths.DERIVED_FXML_PATH);

        initToggleGroup();

        currentPane = new StackPane();
        currentPane.getChildren().add(screens.get(AppPaths.STYLER_ID));
        sparePane = new StackPane();
        sparePane.setVisible(false);

        rootContainer.getChildren().addAll(currentPane, sparePane);
    }
    
    //Add the screen to the collection
    public void addScreen(int id, Node screen) {
        screens.put(id, screen);
    }

    //Returns the Node with the appropriate name
    public Node getScreen(int id) {
        return screens.get(id);
    }

    //Loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    public boolean loadScreen(int id, String fxml) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(fxml));
            Parent loadScreen = (Parent) myLoader.load();
            ToolsHandler toolsHandler = ((ToolsHandler) myLoader.getController());
            toolsHandler.setScreenParent(rootContainer);
            addScreen(id, loadScreen);
          
            if(id == AppPaths.STYLER_ID) {
                stylerController = (StylerController) toolsHandler;
            }
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void initToggleGroup() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(stylerToggle, splineToggle, derivedColorToggle);
        toggleGroup.getToggles().forEach((t) -> setIconBinding((ToggleButton) t));
        toggleGroup.selectToggle(stylerToggle);
    }

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

    public void setScreen(Integer id) {

        // check if existing animation running
        if (timeline != null) {
            nextTool = screens.get(id);

            timeline.setRate(4);
            return;
        } else {
            nextTool = null;
        }

        // load new content
        sparePane.getChildren().setAll(screens.get(id));
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

            } else { // from top
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

//        // start any animations
//        if (tools[currentToolIndex].getContent() instanceof AnimatedAction) {
//            ((AnimatedAction) tools[currentToolIndex].getContent()).startAnimations();
//        }
        // check if we have a animation waiting
        if (nextTool != null) {
            // switchTool(id);
        }
    };

    @FXML
    private void stylerToggleAction(ActionEvent event) {
        if (stylerToggle.isSelected()) {
            setScreen(AppPaths.STYLER_ID);
        } else {
            stylerToggle.setSelected(true);
        }
    }

    @FXML
    private void splineToggleAction(ActionEvent event) {
        if (splineToggle.isSelected()) {
            setScreen(AppPaths.SPLINE_ID);
        } else {
            splineToggle.setSelected(true);
        }
    }

    @FXML
    private void derivedToggleAction(ActionEvent event) {
        if (derivedColorToggle.isSelected()) {
            setScreen(AppPaths.DERIVED_ID);
        } else {
            derivedColorToggle.setSelected(true);
        }
    }

    @FXML
    private void copyButtonAction(ActionEvent event) {
        Clipboard.getSystemClipboard().setContent(
                Collections.singletonMap(DataFormat.PLAIN_TEXT, stylerController.getCodeOutput()));

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Code has been copied to the clipboard.", ButtonType.OK);
        alert.getDialogPane().setId("Code-dialog");
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add(AppPaths.STYLE_PATH + "dialog.css");
        alert.showAndWait();

    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        if (stylerToggle.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(rootContainer.getScene().getWindow());
            if (file != null && !file.exists() && file.getParentFile().isDirectory()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(stylerController.getCodeOutput());
                    writer.flush();
                } catch (IOException ex) {
                    Logger.getLogger(StylerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Code has been saved.", ButtonType.OK);
            alert.getDialogPane().setId("Code-dialog");
            alert.setHeaderText(null);
            alert.getDialogPane().getStylesheets().add(AppPaths.STYLE_PATH + "dialog.css");
            alert.showAndWait();
        }
    }

    @FXML
    private void closeButtonAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
