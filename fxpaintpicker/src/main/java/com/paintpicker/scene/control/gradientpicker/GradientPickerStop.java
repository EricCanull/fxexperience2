package com.paintpicker.scene.control.gradientpicker;

/*
 * Copyright (c) 2012, 2015, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import com.paintpicker.scene.control.picker.CustomPaintControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for the gradient editor stop.
 */
public class GradientPickerStop extends VBox {

    @FXML private Rectangle chip_rect;
    @FXML private ImageView indicator_image;
    @FXML private TextField offset_textfield;
    @FXML private ContextMenu context_menu;
    @FXML private Button stop_button;

    private final double min;
    private final double max;
    private double offset;
    private Color color;
    private boolean isSelected;
    private double origX;
    private double startDragX;
    private double thumbWidth;
    private final double edgeMargin = 2.0;
    private final GradientControl gradientPicker;

    /*
     * Clamp value to be between min and max.
     */
    private static double clamp(double min, double value, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public GradientPickerStop(GradientControl ge, double mini, double maxi, double val, Color c) {
        gradientPicker = ge;
        min = mini;
        max = maxi;
        offset = val;
        color = c;
        initialize();
    }

    public void setOffset(double val) {
        offset = clamp(min, val, max);
        valueToPixels();
    }

    public double getOffset() {
        return offset;
    }

    public void setColor(Color c) {
        color = c;
        chip_rect.setFill(c);
    }

    public Color getColor() {
        return color;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if (selected) {
            indicator_image.setVisible(true);
        } else {
            indicator_image.setVisible(false);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    private void initialize() {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GradientPickerStop.class.getResource("/fxml/FXMLGradientPickerStop.fxml")); //NOI18N
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(GradientControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        assert offset_textfield != null;
        assert chip_rect != null;
       
        context_menu.setStyle(
                  "-fx-opacity: 0.8;"
                + "-fx-padding: -1; "
                + "-fx-insets: -1; "
                + "-fx-background-radius: 10; "
                + "-fx-pref-height: 35px;");
    
        offset_textfield.setText("" + offset); //NOI18N
        

        chip_rect.setFill(color);
        gradientPicker.setSelectedStop(this);

       stop_button.setOnAction((e -> e.consume()));

        // when we detect a width change, we know node layout is resolved so we position stop in track
        widthProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.doubleValue() > 0) {
                thumbWidth = newValue.doubleValue();
                valueToPixels();
            }
        });
    }

    @FXML
    void stopAction(ActionEvent event) {
        double val = Double.valueOf(offset_textfield.getText());
        setOffset(val);
        showHUD();
        // Called when moving a gradient stop :
        // - update gradient preview accordingly
        // - update model
        final CustomPaintControl paintPicker
                = gradientPicker.getCustomPaintControl();
        final Paint value = gradientPicker.getPaint();
        gradientPicker.updatePreviewRect(value);
        // Update model
       // paintPicker.outputPaintProperty.set(value);
    }

    @FXML
    void thumbKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.DELETE) {
            gradientPicker.removeStop(this);
            // Called when removing a gradient stop :
            // - update gradient preview accordingly
            // - update model
            final CustomPaintControl paintPicker
                    = gradientPicker.getCustomPaintControl();
            final Paint value = gradientPicker.getPaint();
            gradientPicker.updatePreviewRect(value);
            // Update model
            paintPicker.customColorProperty().set(this.getColor());
          //  paintPicker.outputPaintProperty.set(getColor());
            e.consume();
        }
    }

    @FXML
    void thumbMousePressed(MouseEvent event) {
        gradientPicker.setSelectedStop(this);
        startDragX = event.getSceneX();
        origX = getLayoutX();
        toFront(); // make sure this stop is in highest z-order
        pixelsToValue();
        // Called when selecting a gradient stop :
        // - update color preview accordingly
        // - do not update the model
        final CustomPaintControl paintPicker
                = gradientPicker.getCustomPaintControl();
        paintPicker.customColorProperty().set(this.getColor());
        //final ColorPicker colorPicker = paintPicker.getColorPicker();
        //colorPicker.updateUI(color);
        stop_button.requestFocus();
    }

    @FXML
    void thumbMouseReleased() {
      //  pixelsToValue();
    }

    @FXML
    void thumbMouseDragged(MouseEvent event) {
        double dragValue = event.getSceneX() - startDragX;
        double deltaX = origX + dragValue;
        double trackWidth = getParent().getBoundsInLocal().getWidth();
        final Double newX = clamp(edgeMargin, deltaX, (trackWidth - (getWidth() + edgeMargin)));
        setLayoutX(newX);
        showHUD();
        pixelsToValue();
        // Called when moving a gradient stop :
        // - update gradient preview accordingly
        // - update model
        final CustomPaintControl paintPicker
                = gradientPicker.getCustomPaintControl();
        final Paint value = gradientPicker.getPaint();
        
        gradientPicker.updatePreviewRect(value);
        // Update model
        paintPicker.setCustomPaint(value);
    }

    private void showHUD() {
        offset_textfield.setText(String.format("%.2f", offset));
        context_menu.show(this, Side.BOTTOM, 0, 5); // better way to center?
    }

    private void valueToPixels() {
        double stopValue = clamp(min, offset, max);
        double availablePixels = getParent().getLayoutBounds().getWidth() - (thumbWidth + edgeMargin);
        double range = max - min;
        double pixelPosition = ((availablePixels / range) * stopValue);
        setLayoutX(pixelPosition);
    }

    private void pixelsToValue() {
        double range = max - min;
        double availablePixels = getParent().getLayoutBounds().getWidth() - (thumbWidth + edgeMargin);
        setOffset(min + (getLayoutX() * (range / availablePixels)));
    }
}
