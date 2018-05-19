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
package com.paintpicker.scene.control.gradientpicker;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * 
 */
public class GradientDialog {

    private final Scene customScene;
    private final Stage stage = new Stage();

    public GradientDialog(Window owner, GradientControl node) {

        if (owner != null) {
            stage.initOwner(owner);
        }

        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.addEventHandler(KeyEvent.ANY, keyEventListener);
       
        
        stage.getOwner().widthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                stage.setWidth(newValue.doubleValue());
            }
        });
     
        stage.getOwner().xProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                stage.setX(newValue.doubleValue());
            }
        });
        stage.getOwner().yProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                stage.setY(newValue.doubleValue() + stage.getOwner().getHeight());
            }
        });
        
        customScene = new Scene((Parent) node);
        customScene.setFill(Color.web("#31363B"));
                
        stage.setScene(customScene);
    }

    public void hide() {
        stage.hide();
    }

    public boolean isShowing() {
        return stage.isShowing();
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
        Window window = stage.getOwner();
        double x = window.getX();
        double y = window.getHeight() + window.getY();
        double width = window.getWidth();
        stage.setWidth(width);
        window.setX(x);
        window.setY(y-window.getHeight());
      
        stage.setX(x);
        stage.setY(y);
    }

    private final EventHandler<KeyEvent> keyEventListener = e -> {
        if (e.getCode().equals(KeyCode.ESCAPE)) {
            stage.close();
        }
    };
}
