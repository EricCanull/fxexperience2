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

import com.paintpicker.scene.control.picker.skins.PaintPickerSkin;
import com.paintpicker.scene.control.picker.mode.Mode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 */
public class PaintPicker extends ComboBoxBase<Paint> {

    /**
     * The custom colors added to the Color Palette by the user.
     */
    private final ObservableList<Paint> customColors = FXCollections.observableArrayList();

    /**
     * Gets the list of custom colors added to the Color Palette by the user.
     *
     * @return
     */
    public final ObservableList<Paint> getCustomColors() {
        return customColors;
    }

    private final Mode mode;

    /**
     * Creates a default ColorPicker instance with a selected color set to white.
     */
    public PaintPicker() {
        this(Color.WHITE, Mode.SINGLE);
    }

    /**
     * Creates a ColorPicker instance and sets the selected paint to the given paint.
     *
     * @param paint to be set as the currently selected paint of the ColorPicker.
     * @param mode
     */
    public PaintPicker(Paint paint, Mode mode) {
        setValue(paint);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.mode = mode;
    }

    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     * @return 
     **************************************************************************/
    public Mode getMode() {
        return mode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new PaintPickerSkin(this);
    }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     * @return
     **************************************************************************/
    private static final String DEFAULT_STYLE_CLASS = "color-picker";

    /**
     * The style class to specify a Button like appearance of ColorPicker control.
     */
    public static final String STYLE_CLASS_BUTTON = "button";

    /**
     * The style class to specify a SplitMenuButton like appearance of ColorPicker control.
     */
    public static final String STYLE_CLASS_SPLIT_BUTTON = "split-button";

    /**
     * The style class to specify a default mode.
     */
    public static final String DEFAULT_MODE = "default-mode";

    /**
     * The style class to specify a gradient mode.
     */
    public static final String GRADIENT_MODE = "gradient-mode";


}

