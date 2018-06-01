package com.paintpicker.app;

import com.paintpicker.scene.control.picker.PaintPicker;
import com.paintpicker.scene.control.picker.mode.Mode;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 
 * @author andje22
 */
public class DemoControl implements Initializable {
       
    @FXML private StackPane rootPane;
    @FXML private HBox menuBar, menuBar1;
    @FXML private VBox vbox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        PaintPicker gradientPicker = new PaintPicker(Color.web("#1A4C9C"), Mode.GRADIENT);
        gradientPicker.setPrefWidth(150);
//        gradientPicker.focusedProperty().addListener((observable) -> {
//            rootPane.backgroundProperty().re;
//        });
        PaintPicker paintPicker = new PaintPicker();
        paintPicker.setPrefWidth(150);
                
        menuBar.getChildren().add(gradientPicker);
        menuBar1.getChildren().add(paintPicker);

        rootPane.backgroundProperty().bind(Bindings.createObjectBinding(() -> 
                new Background(new BackgroundFill(gradientPicker.getValue(),
                CornerRadii.EMPTY, Insets.EMPTY)),
                gradientPicker.valueProperty()));
    }
}
