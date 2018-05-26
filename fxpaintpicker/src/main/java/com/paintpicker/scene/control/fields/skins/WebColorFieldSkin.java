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

import com.paintpicker.scene.control.fields.WebColorField;
import com.paintpicker.utils.ColorEncoder;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;

/**
 */
public class WebColorFieldSkin extends InputFieldSkin {
    private final InvalidationListener integerFieldValueListener;

    /**
     * Create a new IntegerFieldSkin.
     * @param control The IntegerField
     */
    public WebColorFieldSkin(final WebColorField control) {
        super(control);

        // Whenever the value changes on the control, we need to update the text
        // in the TextField. The only time this is not the case is when the update
        // to the control happened as a result of an update in the text textField.
        control.valueProperty().addListener(integerFieldValueListener = (Observable observable) -> {
            updateText();
        });
    }

    @Override public WebColorField getSkinnable() {
        return (WebColorField) control;
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
    @Override public void dispose() {
        ((WebColorField) control).valueProperty().removeListener(integerFieldValueListener);
        super.dispose();
    }

    // Useless method ?
    @Override
    protected boolean accept(String text) {
        if (text.length() == 0) return true;
        return true;
       // return text.matches("#[A-F0-9]{6}") || text.matches("#[A-F0-9]{8}");
    }

    @Override
    protected void updateText() {
        Paint paint = (Color) ((WebColorField) control).getValue();
        if (paint instanceof LinearGradient) {
            getTextField().setText("Linear Gradient");
           // getTextField().setText(ColorEncoder.encodeLinearToCSS(paint));
        } else if (paint instanceof RadialGradient) {
             getTextField().setText("Radial Gradient");
           // getTextField().setText(ColorEncoder.encodeRadialToCSS(paint));
        } else {
            Color color = (Color) paint;
            if (color == null) {
                color = Color.BLACK;
            }
            getTextField().setText(ColorEncoder.encodeColor(color));
        }
    }

    @Override
    protected void updateValue() {
        if (((WebColorField) control).getValue() instanceof LinearGradient
                || ((WebColorField) control).getValue() instanceof RadialGradient) {
            return;
        }
        Color value = (Color) ((WebColorField) control).getValue();
        String text = getTextField().getText() == null ? "" : getTextField().getText().trim().toUpperCase();
        if (text.matches("#[A-F0-9]{6}") || text.matches("#[A-F0-9]{8}")) {
            try {
                Color newValue = Color.web(text);
                if (!newValue.equals(value)) {
                    ((WebColorField) control).setValue(newValue);
                }
            } catch (java.lang.IllegalArgumentException ex) {
                System.out.println("Failed to parse [" + text + "]");
            }
        }
    }
}
