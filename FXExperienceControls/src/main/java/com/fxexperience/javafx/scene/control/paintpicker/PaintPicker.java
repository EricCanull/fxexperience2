package com.fxexperience.javafx.scene.control.paintpicker;

/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
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


import java.io.IOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 * Paint editor control.
 */
public class PaintPicker {

    public enum Mode {

        COLOR, LINEAR, RADIAL, SINGLE
    }
    private Pane pickerPane;
    private final PaintPickerController controller;

    public PaintPicker(Mode mode) {        
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PaintPicker.class.getResource("/fxml/FXMLPaintPicker.fxml")); //NOI18N

        try {
            // Loading
            final Object rootObject = loader.load();
            assert rootObject instanceof Node;
            final Pane rootNode = (Pane) rootObject;
            this.pickerPane = rootNode;


            // Retrieving the controller
            final Object ctl = loader.getController();
            assert ctl instanceof PaintPickerController;
            this.controller = (PaintPickerController) ctl;
            if(mode.equals(Mode.SINGLE)) {
                ((PaintPickerController) ctl).setSingleMode(mode);
            }
           //this.controller.setDelegate(delegate);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }



    public final void setPaintProperty(Paint value) {
        // Update model
        controller.setPaintProperty(value);
        // Update UI
        controller.updateUI(value);
    }

    public Pane getPickerPane() {
        return pickerPane;
    }

    public ObjectProperty<Paint> getPaintProperty() {
        return controller.getPaintProperty();
    }

    public Paint getPaint() {
        return controller.getPaint();
    }
    
    public final ReadOnlyBooleanProperty liveUpdateProperty() {
        return controller.liveUpdateProperty();
    }

    public boolean isLiveUpdate() {
        return controller.isLiveUpdate();
    }

    public static interface Delegate {
        public void handleError(String warningKey, Object... arguments);
    }

}
