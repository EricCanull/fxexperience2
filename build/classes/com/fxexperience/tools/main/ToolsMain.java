package com.fxexperience.tools.main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Eric Canull
 */
public class ToolsMain extends Application {
    
    public Parent createContent() throws IOException {
       Parent parent = FXMLLoader.load(MainController.class.getResource("FXMLMainController.fxml"));
        return parent;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Scene scene = new Scene(createContent());
            scene.getStylesheets().add(ToolsMain.class.getResource("Tools.css").toString());
            primaryStage.setTitle("FXExperience-Tools");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(ToolsMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
///**
// * @author Jasper Potts
// */
//public class ToolsMain extends Application {
//
//    private static final Interpolator INTERPOLATOR = Interpolator.SPLINE(0.4829, 0.5709, 0.6803, 0.9928);
//    private static final double TOOLBAR_WIDTH = 80;
//    private StackPane root, currentPane, sparePane;
//    private VBox toolBar;
//    private int currentToolIndex = 0;
//    private Timeline timeline;
//    private Tool nextTool = null;
//    private int nextToolIndex;
//    private final DoubleProperty arrowH = new SimpleDoubleProperty(200);
//
//    private Tool[] tools;
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws IOException {
//
//        tools = new Tool[]{
//            new Tool(
//            "Caspian Styler",
//            (Parent) FXMLLoader.load(StylerMainController.class.getResource("FXMLStylerMain.fxml")),
//            new Image(ToolsMain.class.getResourceAsStream("/images/caspianstyler-icon.png"))
//               
//            ),
//            new Tool(
//            "Animation Spline Editor",
//            new SplineEditor(),
//            new Image(ToolsMain.class.getResourceAsStream("/images/spline-editor-icon.png"))
//            ),
//            new Tool(
//            "Derived Color Calculator",
//            (Parent) FXMLLoader.load(DerivationCalcContent.class.getResource("DerivationCalcContent.fxml")),
//            new Image(ToolsMain.class.getResourceAsStream("/images/derive-color-icon.png"))
//            )
//        };
//
//        //create root node
//       root = new StackPane() {
//            
//           @Override
//            protected void layoutChildren() {
//                double w = getWidth();
//                double h = getHeight();
//                toolBar.resizeRelocate(0, 0, TOOLBAR_WIDTH, h);
//                currentPane.relocate(TOOLBAR_WIDTH - 10, 0);
//                currentPane.resize(w - TOOLBAR_WIDTH + 10, h);
//                sparePane.relocate(TOOLBAR_WIDTH - 10, 0);
//                sparePane.resize(w - TOOLBAR_WIDTH + 10, h);
//                Node currentToolButton = toolBar.getChildren().get(currentToolIndex);
//                arrowH.set(currentToolButton.getBoundsInParent().getMinY() + (currentToolButton.getLayoutBounds().getHeight() / 2));
//            }
//        };
//
//        // create toolbar background path
//        Path toolBarBackground = createToolBarPath(null, Color.web("#606060"));
//
//        // create toolbar
//        toolBar = new VBox(0);
//        toolBar.setId("ToolsToolbar");
//        toolBar.setClip(createToolBarPath(Color.BLACK, null));
//        ToggleGroup toggleGroup = new ToggleGroup();
//        for (int i = 0; i < tools.length; i++) {
//            final int index = i;
//            final Tool tool = tools[i];
//            final ToggleButton button = new ToggleButton(tool.getName().replace(' ', '\n'));
//            ImageView icon = new ImageView(tool.getIcon());
//            icon.effectProperty().bind(new ObjectBinding<Effect>() {
//                {
//                    bind(button.selectedProperty());
//                }
//
//                @Override
//                protected Effect computeValue() {
//                    return button.isSelected() ? null : new ColorAdjust(0, -1, 0, 0);
//                }
//            });
//            button.setGraphic(icon);
//            button.setContentDisplay(ContentDisplay.TOP);
//            if (i == 0) {
//                button.setSelected(true);
//            }
//            button.setMaxWidth(Double.MAX_VALUE);
//            button.setAlignment(Pos.CENTER);
//            button.setTextAlignment(TextAlignment.CENTER);
//            button.setToggleGroup(toggleGroup);
//            button.setOnAction((ActionEvent t) -> {
//                switchTool(tool, index);
//            });
//            toolBar.getChildren().add(button);
//        }
//
//        currentPane = new StackPane();
//        currentPane.getChildren().setAll(tools[0].getContent());
//        sparePane = new StackPane();
//        sparePane.setVisible(false);
//
//        root.getChildren().addAll(currentPane, sparePane, toolBarBackground, toolBar);
//
//        primaryStage.setTitle("FX Experience Tools");
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add(ToolsMain.class.getResource("Tools.css").toString());
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    private Path createToolBarPath(Paint fill, Paint stroke) {
//        Path toolBarBackground = new Path();
//        toolBarBackground.setFill(fill);
//        toolBarBackground.setStroke(stroke);
//        toolBarBackground.setStrokeType(StrokeType.OUTSIDE);
//        LineTo arrowTop = new LineTo(TOOLBAR_WIDTH, 0);
//        arrowTop.yProperty().bind(arrowH.add(-8));
//        LineTo arrowTip = new LineTo(TOOLBAR_WIDTH - 10, 0);
//        arrowTip.yProperty().bind(arrowH);
//        LineTo arrowBottom = new LineTo(TOOLBAR_WIDTH, 0);
//        arrowBottom.yProperty().bind(arrowH.add(8));
//        LineTo bottomRight = new LineTo(TOOLBAR_WIDTH, 0);
//        bottomRight.yProperty().bind(root.heightProperty());
//        LineTo bottomLeft = new LineTo(0, 0);
//        bottomLeft.yProperty().bind(root.heightProperty());
//        toolBarBackground.getElements().addAll(
//                new MoveTo(0, 0),
//                new LineTo(TOOLBAR_WIDTH, 0),
//                arrowTop, arrowTip, arrowBottom,
//                bottomRight, bottomLeft,
//                new ClosePath()
//        );
//        return toolBarBackground;
//    }
//
//    private void switchTool(Tool newTool, final int toolIndex) {
//        // check if existing animation running
//        if (timeline != null) {
//            nextTool = newTool;
//            nextToolIndex = toolIndex;
//            timeline.setRate(4);
//            return;
//        } else {
//            nextTool = null;
//            nextToolIndex = -1;
//        }
//        // stop any animations
//        if (tools[currentToolIndex].getContent() instanceof AnimatedPanel) {
//            ((AnimatedPanel) tools[currentToolIndex].getContent()).stopAnimations();
//        }
//        // load new content
//        sparePane.getChildren().setAll(newTool.getContent());
//        sparePane.setCache(true);
//        currentPane.setCache(true);
//        // wait one pulse then animate
//        Platform.runLater(() -> {
//            // animate switch
//            if (toolIndex > currentToolIndex) { // animate from bottom
//                currentToolIndex = toolIndex;
//                sparePane.setTranslateY(root.getHeight());
//                sparePane.setVisible(true);
//                timeline = new Timeline(
//                        new KeyFrame(Duration.millis(0),
//                                new KeyValue(currentPane.translateYProperty(), 0, INTERPOLATOR),
//                                new KeyValue(sparePane.translateYProperty(), root.getHeight(), INTERPOLATOR)),
//                        new KeyFrame(Duration.millis(800),
//                                animationEndEventHandler,
//                                new KeyValue(currentPane.translateYProperty(), -root.getHeight(), INTERPOLATOR),
//                                new KeyValue(sparePane.translateYProperty(), 0, INTERPOLATOR)));
//                timeline.play();
//
//            } else { // from top
//                currentToolIndex = toolIndex;
//                sparePane.setTranslateY(-root.getHeight());
//                sparePane.setVisible(true);
//                timeline = new Timeline(
//                        new KeyFrame(Duration.millis(0),
//                                new KeyValue(currentPane.translateYProperty(), 0, INTERPOLATOR),
//                                new KeyValue(sparePane.translateYProperty(), -root.getHeight(), INTERPOLATOR)),
//                        new KeyFrame(Duration.millis(800),
//                                animationEndEventHandler,
//                                new KeyValue(currentPane.translateYProperty(), root.getHeight(), INTERPOLATOR),
//                                new KeyValue(sparePane.translateYProperty(), 0, INTERPOLATOR)));
//                timeline.play();
//            }
//        });
//    }
//
//    private final EventHandler<ActionEvent> animationEndEventHandler = new EventHandler<ActionEvent>() {
//        @Override
//        public void handle(ActionEvent t) {
//            // switch panes
//            StackPane temp = currentPane;
//            currentPane = sparePane;
//            sparePane = temp;
//            // cleanup
//            timeline = null;
//            currentPane.setTranslateY(0);
//            sparePane.setCache(false);
//            currentPane.setCache(false);
//            sparePane.setVisible(false);
//            sparePane.getChildren().clear();
//            // start any animations
//            if (tools[currentToolIndex].getContent() instanceof AnimatedPanel) {
//                ((AnimatedPanel) tools[currentToolIndex].getContent()).startAnimations();
//            }
//            // check if we have a animation waiting
//            if (nextTool != null) {
//                switchTool(nextTool, nextToolIndex);
//            }
//        }
//    };
//}

//    public static String screen1ID = "main";
//    public static String screen1Styler = "/com/fxexperience/tools/styler/mainpanel/FXMLStylerMain.fxml";
//    public static String screen2ID = "screen2";
//    public static String screen2Spline = "FXMLSplineEditor.fxml";
//    public static String screen3ID = "screen3";
//    public static String screen3Derived = "/com/fxexperience/tools/derivationcalc/DerivationCalcContent.fxml";
//
//    @Override
//    public void start(Stage primaryStage) {
//
//        ScreensController screenController = new ScreensController();
//        screenController.loadScreen(ToolsMain.screen1ID, ToolsMain.screen1Styler);
//      //  mainContainer.loadScreen(ToolsMain.screen2ID, ToolsMain.screen2Spline);
//        screenController.loadScreen(ToolsMain.screen3ID, ToolsMain.screen3Derived);
//
//        screenController.setScreen(ToolsMain.screen1ID);
//
//        Group root = new Group();
//        root.getChildren().addAll(screenController);
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add(ToolsMain.class.getResource("Tools.css").toString());
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
