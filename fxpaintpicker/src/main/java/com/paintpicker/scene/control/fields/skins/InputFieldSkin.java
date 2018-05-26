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

import com.paintpicker.scene.control.fields.InputField;
import com.sun.javafx.event.EventDispatchChainImpl;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventDispatchChain;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;

/**
 */
public abstract class InputFieldSkin implements Skin<InputField> {
    /**
     * The {@code Control} that is referencing this Skin. There is a
     * one-to-one relationship between a {@code Skin} and a {@code Control}.
     * When a {@code Skin} is set on a {@code Control}, this variable is
     * automatically updated.
     */
    protected InputField control;

    /**
     * This textField is used to represent the InputField.
     */
    private InnerTextField textField;

    private InvalidationListener InputFieldFocusListener;
    private InvalidationListener InputFieldStyleClassListener;

    /**
     * Create a new InputFieldSkin.
     * @param control The InputField
     */
    public InputFieldSkin(final InputField control) {
        this.control = control;

        // Create the TextField that we are going to use to represent this InputFieldSkin.
        // The textField restricts input so that only valid digits that contribute to the
        // Money can be input.
        textField = new InnerTextField() {
            @Override public void replaceText(int start, int end, String text) {
                String t = textField.getText() == null ? "" : textField.getText();
                t = t.substring(0, start) + text + t.substring(end);
                if (accept(t)) {
                    super.replaceText(start, end, text);
                }
            }

            @Override public void replaceSelection(String text) {
                String t = textField.getText() == null ? "" : textField.getText();
                int start = Math.min(textField.getAnchor(), textField.getCaretPosition());
                int end = Math.max(textField.getAnchor(), textField.getCaretPosition());
                t = t.substring(0, start) + text + t.substring(end);
                if (accept(t)) {
                    super.replaceSelection(text);
                }
            }
        };

        textField.setId("input-text-field");
        textField.getStyleClass().setAll(control.getStyleClass());
        control.getStyleClass().addListener(InputFieldStyleClassListener = (Observable observable) -> {
            textField.getStyleClass().setAll(control.getStyleClass());
        });

//        // Align the text to the right
//        textField.setAlignment(Pos.BASELINE_RIGHT);
        textField.promptTextProperty().bind(control.promptTextProperty());
        textField.editableProperty().bind(control.editableProperty());
        textField.prefColumnCountProperty().bind(control.prefColumnCountProperty());

        // Whenever the text of the textField changes, we may need to
        // update the value.
        textField.textProperty().addListener((Observable observable) -> {
            updateValue();
        });

        // Right now there is some funny business regarding focus in JavaFX. So
        // we will just make sure the TextField gets focus whenever somebody tries
        // to give it to the InputField. This isn't right, but we need to fix
        // this in JavaFX, I don't think I can hack around it
        textField.setFocusTraversable(false);
        control.focusedProperty().addListener(InputFieldFocusListener = (Observable observable) -> {
            textField.handleFocus(control.isFocused());
        });

        control.addEventFilter(InputEvent.ANY, (InputEvent t) -> {
            if (textField == null) return;
            textField.fireEvent(t);
        });

        textField.setOnAction((ActionEvent actionEvent) -> {
            // Because TextFieldBehavior fires an action event on the parent of the TextField
            // (maybe a misfeature?) I don't need to do this. But I think this is
            // a bug, because having to add an empty event handler to get an
            // event on the control is odd to say the least!
//                control.fireEvent(new ActionEvent(textField, textField));
        });

        updateText();
    }

    @Override public InputField getSkinnable() {
        return control;
    }

    @Override public Node getNode() {
        return textField;
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
        control.getStyleClass().removeListener(InputFieldStyleClassListener);
        control.focusedProperty().removeListener(InputFieldFocusListener);
        textField = null;
    }

    protected abstract boolean accept(String text);
    protected abstract void updateText();
    protected abstract void updateValue();

    protected TextField getTextField() {
        return textField;
    }

    private class InnerTextField extends TextField {
        public void handleFocus(boolean b) {
            setFocused(b);
        }

        @Override public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
            EventDispatchChain chain = new EventDispatchChainImpl();
            chain.append(textField.getEventDispatcher());
            return chain;
        }
   }
}
