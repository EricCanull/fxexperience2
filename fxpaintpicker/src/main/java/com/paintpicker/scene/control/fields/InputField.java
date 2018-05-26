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

package com.paintpicker.scene.control.fields;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;

/**
 *
 */
public class InputField extends Control {
    /**
     * The default value for {@link #prefColumnCount}.
     */
    public static final int DEFAULT_PREF_COLUMN_COUNT = 12;

    /**
     * Indicates whether this InputField can be edited by the user. If true, the
     * "readonly" pseudo class will be false, but if false, the "readonly"
     * pseudo class will be true.
     */
    private BooleanProperty editable = new SimpleBooleanProperty(this, "editable", true);
    public final boolean isEditable() { return editable.getValue(); }
    public final void setEditable(boolean value) { editable.setValue(value); }
    public final BooleanProperty editableProperty() { return editable; }

    /**
     * The {@code InputField}'s prompt text to display, or
     * <tt>null</tt> if no prompt text is displayed.
     */
    private StringProperty promptText = new StringPropertyBase("") {
        @Override protected void invalidated() {
            // Strip out newlines
            String txt = get();
            if (txt != null && txt.contains("\n")) {
                txt = txt.replace("\n", "");
                set(txt);
            }
        }

        @Override public Object getBean() { return InputField.this; }
        @Override public String getName() { return "promptText"; }
    };
    public final StringProperty promptTextProperty() { return promptText; }
    public final String getPromptText() { return promptText.get(); }
    public final void setPromptText(String value) { promptText.set(value); }


    /**
     * The preferred number of text columns. This is used for
     * calculating the {@code InputField}'s preferred width.
     */
    private IntegerProperty prefColumnCount = new IntegerPropertyBase(DEFAULT_PREF_COLUMN_COUNT) {
        @Override public void set(int value) {
            if (value < 0) {
                throw new IllegalArgumentException("value cannot be negative.");
            }

            super.set(value);
        }

        @Override public Object getBean() { return InputField.this; }
        @Override public String getName() { return "prefColumnCount"; }
    };
    public final IntegerProperty prefColumnCountProperty() { return prefColumnCount; }
    public final int getPrefColumnCount() { return prefColumnCount.getValue(); }
    public final void setPrefColumnCount(int value) { prefColumnCount.setValue(value); }

    /**
     * The action handler associated with this InputField, or
     * <tt>null</tt> if no action handler is assigned.
     *
     * The action handler is normally called when the user types the ENTER key.
     */
    private final ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        @Override protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

        @Override public Object getBean() { return InputField.this; }
        @Override public String getName() { return "onAction"; }
    };
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() { return onAction; }
    public final EventHandler<ActionEvent> getOnAction() { return onActionProperty().get(); }
    public final void setOnAction(EventHandler<ActionEvent> value) { onActionProperty().set(value); }

    /**
     * Creates a new InputField. The style class is set to "money-field".
     */
    public InputField() {
        getStyleClass().setAll("input-field");
    }

    @Override public String getUserAgentStylesheet() {
        return getClass().getResource("/styles/input-field.css").toExternalForm();
    }
}