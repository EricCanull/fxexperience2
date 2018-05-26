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
import javafx.scene.control.ComboBox;

/**
 * 
 * @author andje22
 */
public class DemoControl implements Initializable {
       
    @FXML private StackPane rootPane;
    @FXML private HBox menuBar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        PaintPicker paintPicker = new PaintPicker(Color.web("#1A4C9C"), Mode.SINGLE);


        menuBar.widthProperty().addListener((observable, oldValue, newValue) -> {
            if ((double) newValue > 0) {
                paintPicker.setPrefWidth((double) newValue);
            }
        });

        menuBar.getChildren().add(paintPicker);

        rootPane.backgroundProperty().bind(Bindings.createObjectBinding(() -> 
                new Background(new BackgroundFill(paintPicker.getValue(),
                CornerRadii.EMPTY, Insets.EMPTY)),
                paintPicker.valueProperty()));
    }
}
