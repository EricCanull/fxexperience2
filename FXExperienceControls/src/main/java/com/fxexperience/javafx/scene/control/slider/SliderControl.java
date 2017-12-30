package com.fxexperience.javafx.scene.control.slider;

import com.fxexperience.javafx.util.encoders.ColorEncoder;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SliderControl extends HBox {

    @FXML private StackPane thumb;
    @FXML private Region track_container;
    @FXML private Region alpha_region;

    private double thumbWidth;

    public SliderControl(double min, double max, double value) {
        setMin(min);
        setMax(max);
        setValue(value);
        initialize();

        valueToPixels();
    }

    private void initialize() {

        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SliderControl.class.getResource("/fxml/FXMLSliderControl.fxml")); //NOI18N
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SliderControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        // when we detect a width change, we know node layout is resolved so we position stop in track
        thumb.widthProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.doubleValue() > 0) {
             thumbWidth = newValue.doubleValue();
            }
        });
    }

    /**
     * The maximum value represented by this Slider.
     */
    private Double max;

    public final void setMax(double value) { max = value;  }

    public final double getMax() {
        return max == null ? 100 : max;
    }

    /**
     * The minimum value represented by this Slider.
     */
    private Double min;

    public final void setMin(double value) { min = value; }

    public final double getMin() {
        return min == null ? 0 : min;
    }

    private SimpleDoubleProperty valueProperty;

    public final void setValue(double value) {
        if (valueProperty == null) {
            valueProperty = new SimpleDoubleProperty(0);
        }
        valueProperty.setValue(value);
    }

    public final double getValue() {
        return valueProperty.getValue() == null ? 0 : valueProperty.get();
    }

    public DoubleProperty getValueProperty() { return valueProperty; }

    public void moveThumb(MouseEvent event) {
        double deltaX = event.getSceneX() - thumbWidth * 2;
        double trackWidth = track_container.getWidth() - thumbWidth;
        final Double newX = ColorEncoder.clamp(0, deltaX, trackWidth);
        thumb.setLayoutX(newX);
        pixelsToValue();
    }

    private void valueToPixels() {
        double stopValue = ColorEncoder.clamp(getMin(), getValue(), getMax());
        double availablePixels = track_container.getPrefWidth();
        double range = getMax() - getMin();
        double pixelPosition = ((availablePixels / range) * stopValue);
        thumb.setLayoutX(pixelPosition);
    }

    private void pixelsToValue() {
        double range = getMax() - getMin();
        double availablePixels = track_container.getWidth() - thumbWidth;
        setValue(getMin() + (thumb.getLayoutX() * (range / availablePixels)));
    }

    private String makeHueSliderCSS() {
        final StringBuilder sb = new StringBuilder();
        sb.append("-fx-background-color: linear-gradient(to right "); //NOI18N
        for (int i = 0; i < 12; i++) { // max 12 gradient stops
            sb.append(", hsb("); //NOI18N
            sb.append(i * (360 / 11));
            sb.append(", 100%, 100%)"); //NOI18N
        }
        sb.append(");"); //NOI18N
        return sb.toString();
    }

    public void setAlphaSliderCSS() { track_container.setId("Alpha-Region"); }

    public void setAlphaChipCSS(String css) {
        track_container.setStyle(css);
    }

    public void setHueSliderCSS() { track_container.setStyle(makeHueSliderCSS()); }

    @FXML void onMouseDragged(MouseEvent event) { moveThumb(event); }

    @FXML void onMousePressed(MouseEvent event) { moveThumb(event); }
}
