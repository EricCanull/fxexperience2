/*
 * Copyright (c) 2012, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.paintpicker.scene.control.picker;

import com.paintpicker.scene.control.fields.IntegerField;
import com.paintpicker.scene.control.fields.WebColorField;
import com.paintpicker.scene.control.gradientpicker.GradientControl;
import com.paintpicker.scene.control.gradientpicker.GradientDialog;
import com.paintpicker.scene.control.gradientpicker.GradientPickerStop;
import com.paintpicker.scene.control.picker.mode.Mode;
import com.paintpicker.scene.control.slider.PaintSlider;
import com.paintpicker.utils.ScreenUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 *
 */
public class CustomPaintControl extends AnchorPane {
    
    @FXML private GridPane colorPickerGrid;
    @FXML private IntegerField hueTextField, satTextField, brightTextField,
                               redTextField, greenTextField, blueTextField, 
                               alphaTextField;
    @FXML private WebColorField hexTextField;
    @FXML private StackPane colorRect, alphaPane;
    @FXML private Pane hueBar, hueRect, hueRectOverlayOne, hueRectOverlayTwo;
    @FXML private Region hueBarHandle, circleHandle;
    @FXML private Region previousColorRect, currentColorRect;
    @FXML private Button gradientButton;
    
    Runnable onSave;
    private Runnable onSelect;
    private Runnable onCancel;

    private final Scene customScene;
    private final Stage stage = new Stage();

    private PaintSlider[] sliders = new PaintSlider[7];

    private GradientDialog gradientDialog = null;
    private GradientControl gradientPicker = null;

    public CustomPaintControl(PopupControl owner, Mode mode) {

        if (owner != null) {
            stage.initOwner(owner);
        }
        initialize();

        stage.setTitle("Custom Paints");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(true);
        stage.addEventHandler(KeyEvent.ANY, keyEventListener);

        customScene = new Scene(this);

        stage.setScene(customScene);

        stage.setOnCloseRequest((WindowEvent e) -> {
            if (onSave != null) {
                onSave.run();
            }
        });

        if (mode.equals(Mode.GRADIENT)) {
            gradientButton.setVisible(true);
            createGradientDialog();
        }
    }

    private void initialize() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(CustomPaintControl.class.getResource("/fxml/FXMLCustomPaintControl.fxml"));
            loader.setController(CustomPaintControl.this);
            loader.setRoot(CustomPaintControl.this);
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(CustomPaintControl.class.getName()).log(Level.SEVERE, null, ex);
        }

        IntStream.range(0, sliders.length).forEachOrdered(index -> {
            sliders[index] = new PaintSlider();
            sliders[index].getStyleClass().add("controls-paint-slider");

            int row = index > 2 ? index + 1 : index; // skips row 4
            GridPane.setConstraints(sliders[index], 6, row, 6, 1,
                    HPos.LEFT, VPos.CENTER, Priority.NEVER, Priority.ALWAYS);
            colorPickerGrid.getChildren().add(sliders[index]);
        });

        bindControlsValue(0, 360, hue);
        bindControlsValue(1, 100, sat);
        bindControlsValue(2, 100, bright);
        bindControlsValue(3, 255, red);
        bindControlsValue(4, 255, green);
        bindControlsValue(5, 255, blue);
        bindControlsValue(6, 100, alpha);

        hueTextField.setMaxValue(360);
        satTextField.setMaxValue(100);
        brightTextField.setMaxValue(100);
        greenTextField.setMaxValue(255);
        redTextField.setMaxValue(255);
        blueTextField.setMaxValue(255);
        alphaTextField.setMaxValue(100);

        hueTextField.valueProperty().bindBidirectional(sliders[0].valueProperty());
        satTextField.valueProperty().bindBidirectional(sliders[1].valueProperty());
        brightTextField.valueProperty().bindBidirectional(sliders[2].valueProperty());
        redTextField.valueProperty().bindBidirectional(sliders[3].valueProperty());
        greenTextField.valueProperty().bindBidirectional(sliders[4].valueProperty());
        blueTextField.valueProperty().bindBidirectional(sliders[5].valueProperty());
        alphaTextField.valueProperty().bindBidirectional(sliders[6].valueProperty());
        hexTextField.valueProperty().bindBidirectional(customColorProperty);
        hexTextField.setEditable(true);

        customColorProperty.addListener(observable -> colorChanged());
        
        hueRect.backgroundProperty().bind(Bindings.createObjectBinding(()->
                new Background(
                new BackgroundFill(Color.hsb(hue.getValue(), 1.0, 1.0),
                        CornerRadii.EMPTY, Insets.EMPTY)), hue));

        hueRectOverlayOne.setBackground(
                new Background(
                new BackgroundFill(
                new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 255, 255, 1)),
                new Stop(1, Color.rgb(255, 255, 255, 0))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        EventHandler<MouseEvent> rectMouseHandler = event -> {
            final double x = event.getX();
            final double y = event.getY();
            sat.set(clamp(x / colorRect.getWidth()) * 100);
            bright.set(100 - (clamp(y / colorRect.getHeight()) * 100));
        };

        hueRectOverlayTwo.setBackground(
                new Background(
                new BackgroundFill(
                new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(0, 0, 0, 0)),
                new Stop(1, Color.rgb(0, 0, 0, 1))),
                CornerRadii.EMPTY, Insets.EMPTY)));
        hueRectOverlayTwo.setOnMouseDragged(rectMouseHandler);
        hueRectOverlayTwo.setOnMousePressed(rectMouseHandler);

        // hueProperty bar
        hueBar.setBackground(
                new Background(
                new BackgroundFill(setHueGradient(), CornerRadii.EMPTY, Insets.EMPTY)));

        circleHandle.layoutXProperty().bind(sat.divide(100).multiply(colorRect.widthProperty()));
        circleHandle.layoutYProperty().bind(Bindings.subtract(1, bright.divide(100)).multiply(colorRect.heightProperty()));
        hueBarHandle.layoutYProperty().bind(hue.divide(360).multiply(hueBar.heightProperty()));
        alphaPane.opacityProperty().bind(alpha.divide(100));

        EventHandler<MouseEvent> barMouseHandler = event -> {
            final double y = event.getY();
            hue.set(clamp(y / hueBar.getHeight()) * 360);
        };

        hueBar.setOnMouseDragged(barMouseHandler);
        hueBar.setOnMousePressed(barMouseHandler);
        
        currentColorRect.backgroundProperty().bind(Bindings.createObjectBinding(()->
                new Background(new BackgroundFill(currentColorProperty.get(),
                        CornerRadii.EMPTY, Insets.EMPTY)), currentColorProperty));

        previousColorRect.backgroundProperty().bind(Bindings.createObjectBinding(()->
                new Background(new BackgroundFill(customColorProperty.get(),
                        CornerRadii.EMPTY, Insets.EMPTY)), customColorProperty));
    }

    public void show() {
        if (stage.getOwner() != null) {
            // Workaround of RT-29871: Instead of just invoking fixPosition()
            // here need to use listener that fixes dialog position once both
            // width and height are determined
            stage.widthProperty().addListener(positionAdjuster);
            stage.heightProperty().addListener(positionAdjuster);
            positionAdjuster.invalidated(null);
        }
        if (stage.getScene() == null) {
            stage.setScene(customScene);
        }
        updateValues();
        stage.show();
    }

    private InvalidationListener positionAdjuster = new InvalidationListener() {
        @Override
        public void invalidated(Observable ignored) {
            if (Double.isNaN(stage.getWidth()) || Double.isNaN(stage.getHeight())) {
                return;
            }
            stage.widthProperty().removeListener(positionAdjuster);
            stage.heightProperty().removeListener(positionAdjuster);
            fixPosition();
        }
    };

    private void fixPosition() {
        Window w = stage.getOwner();
        
        Screen s = ScreenUtil.getScreen(w);
        Rectangle2D sb = s.getBounds();
        double xR = w.getX() + w.getWidth();
        double xL = w.getX() - stage.getWidth();
        double x, y;
        if (sb.getMaxX() >= xR + stage.getWidth()) {
            x = xR;
        } else if (sb.getMinX() <= xL) {
            x = xL;
        } else {
            x = Math.max(sb.getMinX(), sb.getMaxX() - stage.getWidth());
        }
        y = Math.max(sb.getMinY(), Math.min(sb.getMaxY() - stage.getHeight(), w.getY()));
        stage.setX(x);
        stage.setY(y);
    }

    public ReadOnlyBooleanProperty showingProperty() {
        return stage.showingProperty();
    }

    public boolean isShowing() {
        return stage.isShowing();
    }
    
    private final ObjectProperty<Paint> customColorProperty = new SimpleObjectProperty<>(Color.BLACK);

    public ObjectProperty<Paint> customColorProperty() {
        return customColorProperty;
    }
    
    public Color getCustomColor() {
        return (Color) customColorProperty.get();
    }

    public void setCustomColor(Color color) {
        customColorProperty.set(color);
    }
    
    private final ObjectProperty<Paint> currentColorProperty = new SimpleObjectProperty<>(Color.WHITE);
    
    public ObjectProperty<Paint> currentColorProperty() {
        return currentColorProperty;
    }
    
    public Color getCurrentColor() {
        return (Color) currentColorProperty.get();
    }

    public void setCurrentColor(Color color) {
        currentColorProperty.set(color);
    }
    
    private final ObjectProperty<Paint> customPaintProperty = new SimpleObjectProperty<>(Color.WHITE);

    public ObjectProperty<Paint> customPaintProperty() {
        return customPaintProperty;
    }
    
    public Paint getCustomPaint() {
        return customPaintProperty.get();
    }

    public void setCustomPaint(Paint paint) {
        customPaintProperty.set(paint);
    }

    private final EventHandler<KeyEvent> keyEventListener = e -> {
        if (e.getCode().equals(KeyCode.ESCAPE)) {
            stage.close();
        }
    };

    public void setOnHidden(EventHandler<WindowEvent> onHidden) {
        stage.setOnHidden(onHidden);
    }

    public Stage getDialog() {
        return stage;
    }

    public void hide() {
        if (stage.getOwner() != null) {
            stage.hide();
        }
    }

    /**
     *
     * @return
     */
    public Runnable getOnSave() {
        return onSave;
    }

    /**
     *
     * @return
     */
    public Runnable getOnCancel() {
        return onCancel;
    }

    /**
     *
     * @param onCancel
     */
    public void setOnCancel(Runnable onCancel) {
        this.onCancel = onCancel;
    }

    /**
     *
     * @param onSave
     */
    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    /**
     *
     * @return
     */
    public Runnable getOnUse() {
        return onSelect;
    }

    /**
     *
     * @param onUse
     */
    public void setOnUse(Runnable onUse) {
        this.onSelect = onUse;
    }

    @Override
    public void layoutChildren() {
        super.layoutChildren();

        circleHandle.setManaged(false);
        circleHandle.autosize();
    }

    private void createGradientDialog() {
        if (gradientPicker == null) {
            gradientPicker = new GradientControl(this);
            
        }
        gradientDialog = new GradientDialog(this.getScene().getWindow(),
                gradientPicker);
    }

    private void bindControlsValue(int index, int maxValue, Property<Number> prop) {
        sliders[index].valueProperty().bindBidirectional(prop);
        sliders[index].setMax(maxValue);
    }

    private boolean changeIsLocal = false;

    private final DoubleProperty hue = new SimpleDoubleProperty(-1) {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                updateHSBColor();
                changeIsLocal = false;
            }
        }
    };
    private final DoubleProperty sat = new SimpleDoubleProperty(100) {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                updateHSBColor();
                changeIsLocal = false;
            }
        }
    };
    private final DoubleProperty bright = new SimpleDoubleProperty(100) {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                updateHSBColor();
                changeIsLocal = false;
            }
        }
    };
    private final IntegerProperty red = new SimpleIntegerProperty(-1) {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                updateRGBAColor();
                changeIsLocal = false;
            }
        }
    };

    private final IntegerProperty green = new SimpleIntegerProperty(-1) {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                updateRGBAColor();
                changeIsLocal = false;
            }
        }
    };

    private final IntegerProperty blue = new SimpleIntegerProperty(-1) {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                updateRGBAColor();
                changeIsLocal = false;
            }
        }
    };

    private final DoubleProperty alpha = new SimpleDoubleProperty(100) {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                updateRGBAColor();
                changeIsLocal = false;
            }
        }
    };

    private void updateRGBAColor() {
        Color newColor = Color.rgb(
                red.get(),
                green.get(),
                blue.get(),
                clamp(alpha.divide(100).get()));
        hue.set(newColor.getHue());
        sat.set(newColor.getSaturation() * 100);
        bright.set(newColor.getBrightness() * 100);
        alpha.set(newColor.getOpacity() * 100);
        customColorProperty.set(newColor);
        updateSlidersTrackColors();
        updateSelectedGradientStop(newColor);
    }

    private void updateHSBColor() {
        Color newColor = Color.hsb(
                hue.get(),
                clamp(sat.get() / 100),
                clamp(bright.get() / 100),
                clamp(alpha.get() / 100));
        red.set(doubleToInt(newColor.getRed()));
        green.set(doubleToInt(newColor.getGreen()));
        blue.set(doubleToInt(newColor.getBlue()));
        alpha.set(newColor.getOpacity() * 100);
        customColorProperty.set(newColor);
        updateSlidersTrackColors();
        updateSelectedGradientStop(newColor);
    }

    private void updateSlidersTrackColors() {
        double hueDouble    = hue.get();
        double satDouble    = sat.get() / 100;
        double brightDouble = bright.get() / 100;
        int redInt          = red.get();
        int greenInt        = green.get();
        int blueInt         = blue.get();
        double alphaDouble  = alpha.get() / 100;

        final Color newColor = Color.rgb(redInt, greenInt, blueInt, alphaDouble);
        sliders[0].setGradientForHueWithSaturation(satDouble, brightDouble, alphaDouble);
        sliders[1].setGradientForSaturationWithHue(hueDouble, brightDouble, alphaDouble);
        sliders[2].setGradientForBrightnessWithHue(hueDouble, satDouble, alphaDouble);
        sliders[3].setGradientForRedWithGreen(greenInt, blueInt, alphaDouble);
        sliders[4].setGradientForGreenWithRed(blueInt, redInt, alphaDouble);
        sliders[5].setGradientForBlueWithRed(greenInt, redInt, alphaDouble);
        sliders[6].setGradientForAlphaWithCurrentColor(newColor);
    }

    /**
     * Update hueProperty, satProperty, brightProperty, color, redProperty,
     * greenProperty and blueProperty automatically every time the values
     * change.
     */
    private void updateSelectedGradientStop(Color newColor) {
        if (gradientDialog != null) {
            if (gradientDialog.isShowing()) {
                GradientPickerStop gradientPickerStop = gradientPicker.getSelectedStop();
                if (gradientPickerStop != null) {
                    gradientPickerStop.setColor(newColor);
                    // Update gradient preview
                    gradientPicker.updatePreviewRect(gradientPicker.getPaint());
                    setCustomPaint(gradientPicker.getPaint());
                }
            }
        }
    }

    private void colorChanged() {
        if (!changeIsLocal) {
            changeIsLocal = true;
            hue.set(getCustomColor().getHue());
            sat.set(getCustomColor().getSaturation() * 100);
            bright.set(getCustomColor().getBrightness() * 100);
            red.set(doubleToInt(getCustomColor().getRed()));
            green.set(doubleToInt(getCustomColor().getGreen()));
            blue.set(doubleToInt(getCustomColor().getBlue()));
            alpha.set(getCustomColor().getOpacity() * 100);
            updateSlidersTrackColors();
            changeIsLocal = false;
        }
    }

    /**
     * Updates hueProperty, satProperty, brightProperty, color, redProperty,
     * greenProperty and blueProperty and slider background colors whenever the
     * show() method for this dialog gets called.
     */
    protected void updateValues() {
        if (getCurrentColor() == null) {
            setCurrentColor(Color.TRANSPARENT);
        }
        changeIsLocal = true;
        //Initialize hue, sat, bright, color, red, green and blue
        hue.set(getCustomColor().getHue());
        sat.set(getCustomColor().getSaturation() * 100);
        bright.set(getCustomColor().getBrightness() * 100);
        alpha.set(getCustomColor().getOpacity() * 100);
        setCustomColor(Color.hsb(hue.get(),
                clamp(sat.get() / 100),
                clamp(bright.get() / 100),
                clamp(alpha.get() / 100)));
        red.set(doubleToInt(getCustomColor().getRed()));
        green.set(doubleToInt(getCustomColor().getGreen()));
        blue.set(doubleToInt(getCustomColor().getBlue()));
        updateSlidersTrackColors();
        changeIsLocal = false;
    }
    
    public boolean isGradientShowing() {
        return gradientDialog.isShowing();
    }

    /**
     * @param e
     */
    @FXML
    private void onGradientButtonAction(ActionEvent e) {
        if (gradientDialog.isShowing() == false) {
            gradientDialog.show();    
            updateSelectedGradientStop(getCustomColor());
             gradientPicker.updatePreviewRect(gradientPicker.getPaint());
             
        } else {
            gradientDialog.hide();
        }
    }

    /**
     * @param event
     */
    @FXML
    private void onSaveButtonAction(ActionEvent event) {
        if (onSave != null) {
            onSave.run();
        }
        hide();
    }

    /**
     *
     * @param event
     */
    @FXML
    private void onCancelButtonAction(ActionEvent event) {
        customColorProperty.set(getCurrentColor());
        if (onCancel != null) {
            onCancel.run();
        }
        hide();
    }

    private void setMode(Paint value) {
        if (value instanceof Color) {

        } else if (value instanceof LinearGradient) {
//            // make sure that a second click doesn't deselect the button
//            if (linearToggleButton.isSelected() == false) {
//                linearToggleButton.setSelected(true);
//            }
//            if (!root_vbox.getChildren().contains(gradientPicker)) {
//                root_vbox.getChildren().add(gradientPicker);
//            }
        } else if (value instanceof RadialGradient) {
//            // make sure that a second click doesn't deselect the button
//            if (radialToggleButton.isSelected() == false) {
//                radialToggleButton.setSelected(true);
//            }
        }
    }

    /**
     * @param value
     * @return
     */
    private static double clamp(double value) {
        return value < 0 ? 0 : value > 1 ? 1 : value;
    }

    private static LinearGradient setHueGradient() {
        Stop[] stops = new Stop[255];
        double offset;

        for (int y = 0; y < 255; y++) {
            offset = 1 - (1.0 / 255) * y;
            int h = (int) ((y / (double) 255) * 360);
            stops[y] = new Stop(offset, Color.hsb(h, 1.0, 1.0));
        }
        return new LinearGradient(0f, 1f, 0f, 0f, true, CycleMethod.NO_CYCLE, stops);
    }

    /**
     * @param value
     * @return
     */
    private static int doubleToInt(double value) {
        return (int) (value * 255 + 0.5); // Adding 0.5 for rounding only
    }
}
