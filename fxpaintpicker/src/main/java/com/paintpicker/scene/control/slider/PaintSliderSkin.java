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


import com.sun.javafx.scene.control.skin.SliderSkin;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PaintSliderSkin extends SliderSkin {
    private final StackPane track;
    private final StackPane thumb;

    public PaintSliderSkin(PaintSlider slider) {
        super(slider);
    
        track = (StackPane) getSkinnable().lookup(".track");
        thumb = (StackPane) getSkinnable().lookup(".thumb");

        thumb.getStyleClass().add("color-thumb");

        track.getStyleClass().add("color-track");

        track.setBackground(new Background(
                            new BackgroundFill(Color.web("#CCCCCC"),
                            new CornerRadii(5), Insets.EMPTY)));

        Rectangle alphaRect = new Rectangle();
        alphaRect.setArcHeight(15);
        alphaRect.setArcWidth(15);

        ImageView alphaImageView = new ImageView(
                                   new Image("/images/chequers_long_light.png"));

        alphaRect.widthProperty().bind(track.widthProperty());
        alphaRect.heightProperty().bind(track.heightProperty());
        alphaRect.layoutXProperty().bind(track.layoutXProperty().subtract(0));
        alphaRect.layoutYProperty().bind(track.layoutYProperty().subtract(0));

        alphaImageView.setClip(alphaRect);

        getChildren().clear();
        getChildren().addAll(alphaImageView, track, thumb);
        
        track.backgroundProperty().bind(Bindings.createObjectBinding(() -> 
                new Background(
                new BackgroundFill(slider.trackFillProperty().get(),
                new CornerRadii(5), Insets.EMPTY)), slider.trackFillProperty()));
    }

    @Override
    protected void handleControlPropertyChanged(String p) {
        super.handleControlPropertyChanged(p);

    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        
         track.setPrefSize(132, 8);
    }
}

