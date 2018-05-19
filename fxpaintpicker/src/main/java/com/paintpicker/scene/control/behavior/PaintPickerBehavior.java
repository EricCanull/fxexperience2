/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.paintpicker.scene.control.behavior;

import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

import java.util.ArrayList;
import java.util.List;

import com.paintpicker.scene.control.picker.PaintPicker;
import com.paintpicker.scene.control.picker.skins.PaintPickerSkin;
import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.behavior.KeyBinding;

import javafx.scene.paint.Paint;

/**
 * 
 * @author 
 */

public class PaintPickerBehavior extends ComboBoxBaseBehavior<Paint> {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     *
     * @param paintPicker
     */
    public PaintPickerBehavior(final PaintPicker paintPicker) {
        super(paintPicker, COLOR_PICKER_BINDINGS);
    }

    /***************************************************************************
     *                                                                         *
     * Key event handling                                                      *
     *                                                                         *
     **************************************************************************/

    /**
     * Opens the Color Picker Palette.
     */
    protected static final String OPEN_ACTION = "Open";

    /**
     * Closes the Color Picker Palette.
     */
    protected static final String CLOSE_ACTION = "Close";

    /**
     *
     */
    protected static final List<KeyBinding> COLOR_PICKER_BINDINGS = new ArrayList<KeyBinding>();

    static {
        COLOR_PICKER_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
        COLOR_PICKER_BINDINGS.add(new KeyBinding(ESCAPE, KEY_PRESSED, CLOSE_ACTION));
        COLOR_PICKER_BINDINGS.add(new KeyBinding(SPACE, KEY_PRESSED, OPEN_ACTION));
        COLOR_PICKER_BINDINGS.add(new KeyBinding(ENTER, KEY_PRESSED, OPEN_ACTION));

    }

    @Override protected void callAction(String name) {
        if (null == name) super.callAction(name); else switch (name) {
            case OPEN_ACTION:
                show();
                break;
            case CLOSE_ACTION:
                hide();
                break;
            default:
                super.callAction(name);
                break;
        }
    }

     /**************************************************************************
     *                                                                        *
     * Mouse Events                                                           *
     *                                                                        *
     *************************************************************************/
    
    @Override public void onAutoHide() {
        // when we click on some non  interactive part of the
        // Color Palette - we do not want to hide.
        PaintPicker paintPicker = (PaintPicker)getControl();
        PaintPickerSkin cpSkin = (PaintPickerSkin)paintPicker.getSkin();
        cpSkin.syncWithAutoUpdate();
        // if the ColorPicker is no longer showing, then invoke the super method
        // to keep its show/hide state in sync.
        if (!paintPicker.isShowing()) super.onAutoHide();
    }
}
