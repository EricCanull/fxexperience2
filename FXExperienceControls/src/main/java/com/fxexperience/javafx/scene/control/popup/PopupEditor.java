/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.javafx.scene.control.popup;

import com.fxexperience.javafx.scene.control.paintpicker.PaintPicker;
import com.fxexperience.javafx.scene.control.paintpicker.PaintPicker.Mode;
import com.fxexperience.javafx.util.encoders.ColorEncoder;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class PopupEditor extends MenuButton {
    @FXML private StackPane editorHost;
    @FXML private Rectangle rectangle;

    private boolean initialized = false;

    private PaintPicker paintPicker;

    public PopupEditor(Mode mode, Object startColor) {
        initialize(mode, startColor);

    }

    private void initialize(Mode mode, Object startColor) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/FXMLPopupEditor.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        initializePopup(mode, startColor);
    }

    private void initializePopup(Mode mode, Object value) {
       final Color startColor = (Color) value;

        paintPicker = new PaintPicker(mode);
        paintPicker.setPaintProperty(startColor);
        setRectangleFill(startColor);
        setTextField(startColor);


        this.showingProperty().addListener((ov, previousVal, newVal) -> {
            if (newVal) {
                if (!initialized) {
                    paintPicker = new PaintPicker(mode);
                    paintPicker.setPaintProperty(startColor);
                    editorHost.getChildren().add(paintPicker.getPickerPane());
                    initialized = true;
                }

                paintPicker.paintProperty().addListener(paintChangeListener);
            }
        });
    }

    private final ChangeListener<Paint> paintChangeListener = (ov, oldValue, newValue) -> {
        if (newValue == null) {
            return;
        }
        assert newValue instanceof Paint;
        if (newValue instanceof LinearGradient
                || newValue instanceof RadialGradient
                || newValue instanceof ImagePattern) {
            setRectangleFill(newValue);
            textProperty().set(newValue.getClass().getSimpleName());
            return;
        }
        assert newValue instanceof Color;

        setRectangleFill(newValue);
        setTextField(newValue);
    };

    public final synchronized String getColorString() {
        return getColorString(getPaintProperty());
    }

    private synchronized String getColorString(Object value) {
        if (value == null) {
            return null;
        }

        assert value instanceof Color;

        return ColorEncoder.encodeColor((Color) value).toUpperCase();
    }

    public final synchronized String getGradientString() {
        return getGradientString(getPaintProperty());
    }

    private synchronized String getGradientString(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof LinearGradient) {
            return ColorEncoder.encodeLinearToCSS(value);
        } else if (value instanceof  RadialGradient){
            return ColorEncoder.encodeRadialToCSS(value);
        } else {
            return ColorEncoder.encodeColor((Color) value).toUpperCase();
        }

    }

    private void setRectangleFill(Object value) {
        this.rectangle.fillProperty().set((Paint) value);
    }

    private void setTextField(Object value) {
        textProperty().setValue(getColorString(value));
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Paint getPaintProperty() {
        return paintPicker.getPaintProperty();
    }

}
