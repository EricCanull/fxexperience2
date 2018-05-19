/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.paintpicker.scene.control.slider;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.paint.*;

public class PaintSlider extends Slider {
    
    /**
     * {@inheritDoc}
     */
    public PaintSlider() {
        super(0, 100, 50);
        initialize();
    }

    /**
     * {@inheritDoc}
     */
    public PaintSlider(double min, double max, double value) {
        super(min, max, value);
        initialize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new PaintSliderSkin(this);
    }

    private void initialize() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * the track color property of Slider.
     */
    private final ObjectProperty<Paint> trackFill = new SimpleObjectProperty<>(Color.BLUE);

    public final ObjectProperty<Paint> trackFillProperty() {
        return this.trackFill;
    }

    /***************************************************************************
     *                                                                         *
     * Convenience methods for painting the track using gradients              *
     *                                                                         *
     **************************************************************************/

    /**
     * This method sets the track to a rainbow gradient with the given saturation and brightness.
     * This is useful for choosing a hueProperty in the context of the current saturation
     * and brightness settings.
     *
     * @param saturation
     * @param brightness
     * @param alpha
     */
    public void setGradientForHueWithSaturation(double saturation, double brightness, double alpha) {
        Stop[] stops = new Stop[40];
        double offset;
        for (int i = 0; i < 40; i++) {
            offset = 1 - (1.0 / 40) * i;
            int hue = (int) (((40 - i) / (double) 40) * 360);
            stops[i] = new Stop(offset, Color.hsb(hue, saturation, brightness, alpha));
        }
        trackFill.set(new LinearGradient(0f, 0f, 1f, 1f, true, CycleMethod.NO_CYCLE, stops));
    }

    /**
     * This method sets the track to a gradient varying from grey to fully saturated with the hueProperty
     * and brightness provided.
     * This is useful for choosing a saturation value in the context of the current hueProperty
     * and brightness settings.
     * @param hue
     * @param brightness
     * @param alpha
     */
    public void setGradientForSaturationWithHue(double hue, double brightness, double alpha) {
        trackFill.set(new LinearGradient(0, 0, 1, 0,
                true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.hsb(hue, 0, brightness, alpha)),
                new Stop(1, Color.hsb(hue, 1, brightness, alpha))));
    }

    /**
     * This method sets the track to a gradient varying from black to full brightness with the hueProperty
     * and saturation provided.
     * This is useful for choosing a brightness value in the context of the current hueProperty
     * and saturation settings.
     *
     * @param hue
     * @param saturation
     * @param alpha
     */
    public void setGradientForBrightnessWithHue(double hue, double saturation, double alpha) {
        trackFill.set(new LinearGradient(0, 0, 1, 0,
                true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.hsb(hue, saturation, 0, alpha)),
                new Stop(1, Color.hsb(hue, saturation, 1, alpha))));
    }

    /**
     * This method sets the track to a gradient with varying redProperty for the given greenProperty
     * & blueProperty values.
     * 
     * @param green
     * @param blue
     * @param alpha
     */
    public void setGradientForRedWithGreen(int green, int blue, double alpha) {
        trackFill.set(new LinearGradient(0, 0, 1, 0,
                true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(0, green, blue, alpha)),
                new Stop(1, Color.rgb(255, green, blue, alpha))));
    }

    /**
     * This method sets the track to a gradient with varying greenProperty for the given redProperty
     * & blueProperty values.
     *
     * @param red
     * @param blue
     * @param alpha
     */
    public void setGradientForGreenWithRed(int red, int blue, double alpha) {
        trackFill.set(new LinearGradient(0, 0, 1, 0,
                true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(red, 0, blue, alpha)),
                new Stop(1, Color.rgb(red, 255, blue, alpha))));
    }

    /**
     * This method sets the track to a gradient with varying blueProperty for the given redProperty
     * & greenProperty values.
     *
     * @param red
     * @param green
     * @param alpha
     */
    public void setGradientForBlueWithRed(int red, int green, double alpha) {
        trackFill.set(new LinearGradient(0, 0, 1, 0,
                true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(red, green, 0, alpha)),
                new Stop(1, Color.rgb(red, green, 255, alpha))));
    }

    /**
     * This method sets the track to a gradient with varying alphaProperty values.
     *
     * @param color
     */
    public void setGradientForAlphaWithCurrentColor(Color color) {
        trackFill.set(new LinearGradient(0, 0, 1, 0,
                true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(0, 0, 0, 0)),
                new Stop(1, Color.hsb(color.getHue(), color.getSaturation(), color.getBrightness()))));
    }
    
    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    /**
     * Initialize the style class to 'paint-slider'.
     * <p>
     * This is the selector class from which CSS can be used to style
     * this control.
     */
    private static final String DEFAULT_STYLE_CLASS = "paint-slider";
}