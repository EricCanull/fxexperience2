/*
 * Copyright (c) 2014, Oracle and/or its affiliates.
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

package com.paintpicker.scene.control.fields.skins;


import com.paintpicker.scene.control.fields.DoubleField;
import com.paintpicker.scene.control.fields.skins.InputFieldSkin;
import com.sun.istack.internal.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;

import java.util.logging.Level;

/**
 */
public class DoubleFieldSkin extends InputFieldSkin {
    private final InvalidationListener doubleFieldValueListener;

    /**
     * Create a new DoubleFieldSkin.
     * @param control The DoubleField
     */
    public DoubleFieldSkin(final DoubleField control) {
        super(control);

        // Whenever the value changes on the control, we need to update the text
        // in the TextField. The only time this is not the case is when the update
        // to the control happened as a result of an update in the text textField.
        control.valueProperty().addListener(doubleFieldValueListener = (Observable observable) -> {
            updateText();
        });
    }

    @Override public DoubleField getSkinnable() {
        return (DoubleField) control;
    }

    @Override public Node getNode() {
        return getTextField();
    }

    /**
     * Called by a Skinnable when the Skin is replaced on the Skinnable. This method
     * allows a Skin to implement any logic necessary to clean up itself after
     * the Skin is no longer needed. It may be used to release native resources.
     * The methods {@link #getSkinnable()} and {@link #getNode()}
     * should return null following a call to dispose. Calling dispose twice
     * has no effect.
     */
    @Override
    public void dispose() {
        ((DoubleField) control).valueProperty().removeListener(doubleFieldValueListener);
        super.dispose();
    }

    @Override
    protected boolean accept(String text) {
        if (text.length() == 0) return true;
        if (text.matches("[0-9.]*")) {
            try {
                Double.parseDouble(text);
                return true;
            } catch (NumberFormatException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return false;
    }

    @Override
    protected void updateText() {
        getTextField().setText("" + ((DoubleField) control).getValue());
    }

    @Override
    protected void updateValue() {
        double value = ((DoubleField) control).getValue();
        double newValue;
        String text = getTextField().getText() == null ? "" : getTextField().getText().trim();
        try {
            newValue = Double.parseDouble(text);
            if (newValue != value) {
                ((DoubleField) control).setValue(newValue);
            }
        } catch (NumberFormatException ex) {
            // Empty string most likely
            ((DoubleField) control).setValue(0);
            Platform.runLater(() -> {
                getTextField().positionCaret(1);
            });
        }
    }
}
