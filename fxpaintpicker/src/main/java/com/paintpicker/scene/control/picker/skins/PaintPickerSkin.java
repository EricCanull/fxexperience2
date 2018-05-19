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

package com.paintpicker.scene.control.picker.skins;
import static javafx.scene.paint.Color.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.paintpicker.scene.control.behavior.PaintPickerBehavior;
import com.paintpicker.scene.control.picker.PaintPalette;
import com.paintpicker.scene.control.picker.PaintPicker;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import com.sun.javafx.scene.control.skin.ComboBoxBaseSkin;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Rectangle;

/**
 *
 */
public class PaintPickerSkin extends ComboBoxPopupControl<Paint> {

    private Label displayNode;
    private StackPane pickerColorBox;
    private final Rectangle colorRect;
    private PaintPalette popupContent;

    StyleableProperty<Boolean> colorLabelVisible = new StyleableBooleanProperty(true) {
        @Override public void invalidated() {
            if (displayNode != null) {
                if (colorLabelVisible.getValue()) {
                    displayNode.setText(colorDisplayName((Color) getSkinnable().getValue()));
                } else {
                    displayNode.setText("");
                }
            }
        }
        @Override public Object getBean() {
            return PaintPickerSkin.this;
        }
        @Override public String getName() {
            return "colorLabelVisible";
        }
        @Override public CssMetaData<PaintPicker,Boolean> getCssMetaData() {
            return StyleableProperties.COLOR_LABEL_VISIBLE.get();
        }
    };
    
    // --- Editor
    private TextField textField;
    /**
     * The editor for the ComboBox. The editor is null if the ComboBox is not
     * {@link #editableProperty() editable}.
     * @since JavaFX 2.2
     */
    private ReadOnlyObjectWrapper<TextField> editor;
    @Override
    public final TextField getEditor() {
        return editorProperty().get();
    }
    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (editor == null) {
            editor = new ReadOnlyObjectWrapper<>(this, "editor");
            textField = new ComboBoxListViewSkin.FakeFocusTextField();
            editor.set(textField);
        }
        return editor.getReadOnlyProperty();
    }
    
    public StringProperty imageUrlProperty() { return imageUrl; }
    private final StyleableStringProperty imageUrl = new StyleableStringProperty() {
        @Override public void applyStyle(StyleOrigin origin, String v) {
            super.applyStyle(origin, v);
            if (v == null) {
                // remove old image view
                if (pickerColorBox.getChildren().size() == 2) pickerColorBox.getChildren().remove(1);
            } else {
                if (pickerColorBox.getChildren().size() == 2) {
                    ImageView imageView = (ImageView)pickerColorBox.getChildren().get(1);
                    imageView.setImage(StyleManager.getInstance().getCachedImage(v));
                } else {
                    pickerColorBox.getChildren().add(new ImageView(StyleManager.getInstance().getCachedImage(v)));
                }
            }
        }
        @Override public Object getBean() {
            return PaintPickerSkin.this;
        }
        @Override public String getName() {
            return "imageUrl";
        }
        @Override public CssMetaData<PaintPicker, String> getCssMetaData() {
            return StyleableProperties.GRAPHIC;
        }
    };
    private final StyleableDoubleProperty colorRectWidth =  new StyleableDoubleProperty(12) {
        @Override protected void invalidated() {
            if(pickerColorBox!=null) pickerColorBox.requestLayout();
        }
        @Override public CssMetaData<PaintPicker,Number> getCssMetaData() {
            return StyleableProperties.COLOR_RECT_WIDTH;
        }
        @Override public Object getBean() {
            return PaintPickerSkin.this;
        }
        @Override public String getName() {
            return "colorRectWidth";
        }
    };
    private final StyleableDoubleProperty colorRectHeight =  new StyleableDoubleProperty(12) {
        @Override protected void invalidated() {
            if(pickerColorBox!=null) pickerColorBox.requestLayout();
        }
        @Override public CssMetaData<PaintPicker,Number> getCssMetaData() {
            return StyleableProperties.COLOR_RECT_HEIGHT;
        }
        @Override public Object getBean() {
            return PaintPickerSkin.this;
        }
        @Override public String getName() {
            return "colorRectHeight";
        }
    };
   
    private final StyleableDoubleProperty colorRectX =  new StyleableDoubleProperty(0) {
        @Override protected void invalidated() {
            if(pickerColorBox!=null) pickerColorBox.requestLayout();
        }
        @Override public CssMetaData<PaintPicker,Number> getCssMetaData() {
            return StyleableProperties.COLOR_RECT_X;
        }
        @Override public Object getBean() {
            return PaintPickerSkin.this;
        }
        @Override public String getName() {
            return "colorRectX";
        }
    };
    private final StyleableDoubleProperty colorRectY =  new StyleableDoubleProperty(0) {
        @Override protected void invalidated() {
            if(pickerColorBox!=null) pickerColorBox.requestLayout();
        }
        @Override public CssMetaData<PaintPicker,Number> getCssMetaData() {
            return StyleableProperties.COLOR_RECT_Y;
        }
        @Override public Object getBean() {
            return PaintPickerSkin.this;
        }
        @Override public String getName() {
            return "colorRectY";
        }
    };

    public PaintPickerSkin(final PaintPicker paintPicker) {
        super(paintPicker, new PaintPickerBehavior(paintPicker));
        updateComboBoxMode();
        registerChangeListener(paintPicker.valueProperty(), "VALUE");
        this.getSkinnable().setEditable(true);
       
        textField = getEditor();
        textField.setText("Default");
        textField.setManaged(false);
        
       

        // create displayNode
        displayNode = new Label();
        displayNode.getStyleClass().add("color-picker-label");
        displayNode.setManaged(false);

        // label graphic
        pickerColorBox = new PickerColorBox();
        pickerColorBox.getStyleClass().add("picker-color");
        colorRect = new Rectangle(12, 12);
        colorRect.getStyleClass().add("picker-color-rect");

        updateColor();

        pickerColorBox.getChildren().add(colorRect);
        displayNode.setGraphic(pickerColorBox);
    }


    @Override protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (!colorLabelVisible.getValue()) {
            return super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        }
        String displayNodeText = displayNode.getText();
        double width = 0;
        for (String name : COLOR_NAME_MAP.values()) {
            displayNode.setText(name);
            width = Math.max(width, super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset));
        }
        displayNode.setText(formatHexString(Color.BLACK)); // #000000
        width = Math.max(width, super.computePrefWidth(height, topInset, rightInset, bottomInset, leftInset));
        displayNode.setText(displayNodeText);
        return width;
    }

    private void updateComboBoxMode() {
        List<String> styleClass = getSkinnable().getStyleClass();
        if (styleClass.contains(PaintPicker.STYLE_CLASS_BUTTON)) {
          // pickerColorBox.setMode(Mode.BUTTON);
        } else if (styleClass.contains(PaintPicker.STYLE_CLASS_SPLIT_BUTTON)) {
         //   setMode(Mode.SPLITBUTTON);
        }
    }

    private static final Map<Paint, String> COLOR_NAME_MAP = new HashMap<>(24);
    private static final Map<Paint, String> CSS_NAME_MAP = new HashMap<>(139);
    static {
        // Translatable display names for the most common colors
        COLOR_NAME_MAP.put(TRANSPARENT, getString("colorName.transparent"));
        COLOR_NAME_MAP.put(BLACK,       getString("colorName.black"));
        COLOR_NAME_MAP.put(BLUE,        getString("colorName.blue"));
        COLOR_NAME_MAP.put(CYAN,        getString("colorName.cyan"));
        COLOR_NAME_MAP.put(DARKBLUE,    getString("colorName.darkblue"));
        COLOR_NAME_MAP.put(DARKCYAN,    getString("colorName.darkcyan"));
        COLOR_NAME_MAP.put(DARKGRAY,    getString("colorName.darkgray"));
        COLOR_NAME_MAP.put(DARKGREEN,   getString("colorName.darkgreen"));
        COLOR_NAME_MAP.put(DARKMAGENTA, getString("colorName.darkmagenta"));
        COLOR_NAME_MAP.put(DARKRED,     getString("colorName.darkred"));
        COLOR_NAME_MAP.put(GRAY,        getString("colorName.gray"));
        COLOR_NAME_MAP.put(GREEN,       getString("colorName.green"));
        COLOR_NAME_MAP.put(LIGHTBLUE,   getString("colorName.lightblue"));
        COLOR_NAME_MAP.put(LIGHTCYAN,   getString("colorName.lightcyan"));
        COLOR_NAME_MAP.put(LIGHTGRAY,   getString("colorName.lightgray"));
        COLOR_NAME_MAP.put(LIGHTGREEN,  getString("colorName.lightgreen"));
        COLOR_NAME_MAP.put(LIGHTYELLOW, getString("colorName.lightyellow"));
        COLOR_NAME_MAP.put(MAGENTA,     getString("colorName.magenta"));
        COLOR_NAME_MAP.put(MEDIUMBLUE,  getString("colorName.mediumblue"));
        COLOR_NAME_MAP.put(ORANGE,      getString("colorName.orange"));
        COLOR_NAME_MAP.put(PINK,        getString("colorName.pink"));
        COLOR_NAME_MAP.put(RED,         getString("colorName.red"));
        COLOR_NAME_MAP.put(WHITE,       getString("colorName.white"));
        COLOR_NAME_MAP.put(YELLOW,      getString("colorName.yellow"));

        // CSS names.
        // Note that synonyms (such as "grey") have been removed here,
        // since a color can be presented with only one name in this
        // skin. If a reverse map is created for parsing names in the
        // future, then the synonyms should be included there. For a
        // full list of CSS names, see Color.java.
        CSS_NAME_MAP.put(ALICEBLUE,            "aliceblue");
        CSS_NAME_MAP.put(ANTIQUEWHITE,         "antiquewhite");
        CSS_NAME_MAP.put(AQUAMARINE,           "aquamarine");
        CSS_NAME_MAP.put(AZURE,                "azure");
        CSS_NAME_MAP.put(BEIGE,                "beige");
        CSS_NAME_MAP.put(BISQUE,               "bisque");
        CSS_NAME_MAP.put(BLACK,                "black");
        CSS_NAME_MAP.put(BLANCHEDALMOND,       "blanchedalmond");
        CSS_NAME_MAP.put(BLUE,                 "blue");
        CSS_NAME_MAP.put(BLUEVIOLET,           "blueviolet");
        CSS_NAME_MAP.put(BROWN,                "brown");
        CSS_NAME_MAP.put(BURLYWOOD,            "burlywood");
        CSS_NAME_MAP.put(CADETBLUE,            "cadetblue");
        CSS_NAME_MAP.put(CHARTREUSE,           "chartreuse");
        CSS_NAME_MAP.put(CHOCOLATE,            "chocolate");
        CSS_NAME_MAP.put(CORAL,                "coral");
        CSS_NAME_MAP.put(CORNFLOWERBLUE,       "cornflowerblue");
        CSS_NAME_MAP.put(CORNSILK,             "cornsilk");
        CSS_NAME_MAP.put(CRIMSON,              "crimson");
        CSS_NAME_MAP.put(CYAN,                 "cyan");
        CSS_NAME_MAP.put(DARKBLUE,             "darkblue");
        CSS_NAME_MAP.put(DARKCYAN,             "darkcyan");
        CSS_NAME_MAP.put(DARKGOLDENROD,        "darkgoldenrod");
        CSS_NAME_MAP.put(DARKGRAY,             "darkgray");
        CSS_NAME_MAP.put(DARKGREEN,            "darkgreen");
        CSS_NAME_MAP.put(DARKKHAKI,            "darkkhaki");
        CSS_NAME_MAP.put(DARKMAGENTA,          "darkmagenta");
        CSS_NAME_MAP.put(DARKOLIVEGREEN,       "darkolivegreen");
        CSS_NAME_MAP.put(DARKORANGE,           "darkorange");
        CSS_NAME_MAP.put(DARKORCHID,           "darkorchid");
        CSS_NAME_MAP.put(DARKRED,              "darkred");
        CSS_NAME_MAP.put(DARKSALMON,           "darksalmon");
        CSS_NAME_MAP.put(DARKSEAGREEN,         "darkseagreen");
        CSS_NAME_MAP.put(DARKSLATEBLUE,        "darkslateblue");
        CSS_NAME_MAP.put(DARKSLATEGRAY,        "darkslategray");
        CSS_NAME_MAP.put(DARKTURQUOISE,        "darkturquoise");
        CSS_NAME_MAP.put(DARKVIOLET,           "darkviolet");
        CSS_NAME_MAP.put(DEEPPINK,             "deeppink");
        CSS_NAME_MAP.put(DEEPSKYBLUE,          "deepskyblue");
        CSS_NAME_MAP.put(DIMGRAY,              "dimgray");
        CSS_NAME_MAP.put(DODGERBLUE,           "dodgerblue");
        CSS_NAME_MAP.put(FIREBRICK,            "firebrick");
        CSS_NAME_MAP.put(FLORALWHITE,          "floralwhite");
        CSS_NAME_MAP.put(FORESTGREEN,          "forestgreen");
        CSS_NAME_MAP.put(GAINSBORO,            "gainsboro");
        CSS_NAME_MAP.put(GHOSTWHITE,           "ghostwhite");
        CSS_NAME_MAP.put(GOLD,                 "gold");
        CSS_NAME_MAP.put(GOLDENROD,            "goldenrod");
        CSS_NAME_MAP.put(GRAY,                 "gray");
        CSS_NAME_MAP.put(GREEN,                "green");
        CSS_NAME_MAP.put(GREENYELLOW,          "greenyellow");
        CSS_NAME_MAP.put(HONEYDEW,             "honeydew");
        CSS_NAME_MAP.put(HOTPINK,              "hotpink");
        CSS_NAME_MAP.put(INDIANRED,            "indianred");
        CSS_NAME_MAP.put(INDIGO,               "indigo");
        CSS_NAME_MAP.put(IVORY,                "ivory");
        CSS_NAME_MAP.put(KHAKI,                "khaki");
        CSS_NAME_MAP.put(LAVENDER,             "lavender");
        CSS_NAME_MAP.put(LAVENDERBLUSH,        "lavenderblush");
        CSS_NAME_MAP.put(LAWNGREEN,            "lawngreen");
        CSS_NAME_MAP.put(LEMONCHIFFON,         "lemonchiffon");
        CSS_NAME_MAP.put(LIGHTBLUE,            "lightblue");
        CSS_NAME_MAP.put(LIGHTCORAL,           "lightcoral");
        CSS_NAME_MAP.put(LIGHTCYAN,            "lightcyan");
        CSS_NAME_MAP.put(LIGHTGOLDENRODYELLOW, "lightgoldenrodyellow");
        CSS_NAME_MAP.put(LIGHTGRAY,            "lightgray");
        CSS_NAME_MAP.put(LIGHTGREEN,           "lightgreen");
        CSS_NAME_MAP.put(LIGHTPINK,            "lightpink");
        CSS_NAME_MAP.put(LIGHTSALMON,          "lightsalmon");
        CSS_NAME_MAP.put(LIGHTSEAGREEN,        "lightseagreen");
        CSS_NAME_MAP.put(LIGHTSKYBLUE,         "lightskyblue");
        CSS_NAME_MAP.put(LIGHTSLATEGRAY,       "lightslategray");
        CSS_NAME_MAP.put(LIGHTSTEELBLUE,       "lightsteelblue");
        CSS_NAME_MAP.put(LIGHTYELLOW,          "lightyellow");
        CSS_NAME_MAP.put(LIME,                 "lime");
        CSS_NAME_MAP.put(LIMEGREEN,            "limegreen");
        CSS_NAME_MAP.put(LINEN,                "linen");
        CSS_NAME_MAP.put(MAGENTA,              "magenta");
        CSS_NAME_MAP.put(MAROON,               "maroon");
        CSS_NAME_MAP.put(MEDIUMAQUAMARINE,     "mediumaquamarine");
        CSS_NAME_MAP.put(MEDIUMBLUE,           "mediumblue");
        CSS_NAME_MAP.put(MEDIUMORCHID,         "mediumorchid");
        CSS_NAME_MAP.put(MEDIUMPURPLE,         "mediumpurple");
        CSS_NAME_MAP.put(MEDIUMSEAGREEN,       "mediumseagreen");
        CSS_NAME_MAP.put(MEDIUMSLATEBLUE,      "mediumslateblue");
        CSS_NAME_MAP.put(MEDIUMSPRINGGREEN,    "mediumspringgreen");
        CSS_NAME_MAP.put(MEDIUMTURQUOISE,      "mediumturquoise");
        CSS_NAME_MAP.put(MEDIUMVIOLETRED,      "mediumvioletred");
        CSS_NAME_MAP.put(MIDNIGHTBLUE,         "midnightblue");
        CSS_NAME_MAP.put(MINTCREAM,            "mintcream");
        CSS_NAME_MAP.put(MISTYROSE,            "mistyrose");
        CSS_NAME_MAP.put(MOCCASIN,             "moccasin");
        CSS_NAME_MAP.put(NAVAJOWHITE,          "navajowhite");
        CSS_NAME_MAP.put(NAVY,                 "navy");
        CSS_NAME_MAP.put(OLDLACE,              "oldlace");
        CSS_NAME_MAP.put(OLIVE,                "olive");
        CSS_NAME_MAP.put(OLIVEDRAB,            "olivedrab");
        CSS_NAME_MAP.put(ORANGE,               "orange");
        CSS_NAME_MAP.put(ORANGERED,            "orangered");
        CSS_NAME_MAP.put(ORCHID,               "orchid");
        CSS_NAME_MAP.put(PALEGOLDENROD,        "palegoldenrod");
        CSS_NAME_MAP.put(PALEGREEN,            "palegreen");
        CSS_NAME_MAP.put(PALETURQUOISE,        "paleturquoise");
        CSS_NAME_MAP.put(PALEVIOLETRED,        "palevioletred");
        CSS_NAME_MAP.put(PAPAYAWHIP,           "papayawhip");
        CSS_NAME_MAP.put(PEACHPUFF,            "peachpuff");
        CSS_NAME_MAP.put(PERU,                 "peru");
        CSS_NAME_MAP.put(PINK,                 "pink");
        CSS_NAME_MAP.put(PLUM,                 "plum");
        CSS_NAME_MAP.put(POWDERBLUE,           "powderblue");
        CSS_NAME_MAP.put(PURPLE,               "purple");
        CSS_NAME_MAP.put(RED,                  "red");
        CSS_NAME_MAP.put(ROSYBROWN,            "rosybrown");
        CSS_NAME_MAP.put(ROYALBLUE,            "royalblue");
        CSS_NAME_MAP.put(SADDLEBROWN,          "saddlebrown");
        CSS_NAME_MAP.put(SALMON,               "salmon");
        CSS_NAME_MAP.put(SANDYBROWN,           "sandybrown");
        CSS_NAME_MAP.put(SEAGREEN,             "seagreen");
        CSS_NAME_MAP.put(SEASHELL,             "seashell");
        CSS_NAME_MAP.put(SIENNA,               "sienna");
        CSS_NAME_MAP.put(SILVER,               "silver");
        CSS_NAME_MAP.put(SKYBLUE,              "skyblue");
        CSS_NAME_MAP.put(SLATEBLUE,            "slateblue");
        CSS_NAME_MAP.put(SLATEGRAY,            "slategray");
        CSS_NAME_MAP.put(SNOW,                 "snow");
        CSS_NAME_MAP.put(SPRINGGREEN,          "springgreen");
        CSS_NAME_MAP.put(STEELBLUE,            "steelblue");
        CSS_NAME_MAP.put(TAN,                  "tan");
        CSS_NAME_MAP.put(TEAL,                 "teal");
        CSS_NAME_MAP.put(THISTLE,              "thistle");
        CSS_NAME_MAP.put(TOMATO,               "tomato");
        CSS_NAME_MAP.put(TRANSPARENT,          "transparent");
        CSS_NAME_MAP.put(TURQUOISE,            "turquoise");
        CSS_NAME_MAP.put(VIOLET,               "violet");
        CSS_NAME_MAP.put(WHEAT,                "wheat");
        CSS_NAME_MAP.put(WHITE,                "white");
        CSS_NAME_MAP.put(WHITESMOKE,           "whitesmoke");
        CSS_NAME_MAP.put(YELLOW,               "yellow");
        CSS_NAME_MAP.put(YELLOWGREEN,          "yellowgreen");
    }

    static String colorDisplayName(Color c) {
        if (c != null) {
            String displayName = COLOR_NAME_MAP.get(c);
            if (displayName == null) {
                displayName = formatHexString(c);
            }
            return displayName;
        } else {
            return null;
        }
    }

    public static String tooltipString(Paint p) {
        if(p instanceof LinearGradient ||
                p instanceof RadialGradient) {
            return "Gradient";
        }
        Color c = (Color) p;
        if (c != null) {
            String tooltipStr = "";
            String displayName = COLOR_NAME_MAP.get(c);
            if (displayName != null) {
                tooltipStr += displayName + " ";
            }

            tooltipStr += formatHexString(c);

            String cssName = CSS_NAME_MAP.get(c);
            if (cssName != null) {
                tooltipStr += " (css: " + cssName + ")";
            }
            return tooltipStr;
        } else {
            return null;
        }
    }
    
    static String formatHexString(Color c) {
        if (c != null) {
            final int red, green, blue, alpha;
            final String result;

            red = (int) Math.round(c.getRed() * 255.0);
            green = (int) Math.round(c.getGreen() * 255.0);
            blue = (int) Math.round(c.getBlue() * 255.0);
            alpha = (int) Math.round(c.getOpacity() * 255.0);

            if (alpha == 255) {
                result = String.format((Locale) null, "#%02x%02x%02x", red, green, blue);
            } else {
                result = String.format((Locale) null, "#%02x%02x%02x%02x", red, green, blue, alpha);
            }
            return result.toUpperCase();
        } else {
            return null;
        }
    }

    @Override protected Node getPopupContent() {
        if (popupContent == null) {
            popupContent = new PaintPalette((PaintPicker)getSkinnable());
            popupContent.setPopupControl(getPopup());
        }
       return popupContent;
    }

    @Override protected void focusLost() {
        // do nothing
    }

    @Override public void show() {
        super.show();
        final PaintPicker paintPicker = (PaintPicker)getSkinnable();
        popupContent.updateSelection(paintPicker.getValue());
    }

    @Override protected void handleControlPropertyChanged(String p) {
        super.handleControlPropertyChanged(p);

        if ("SHOWING".equals(p)) {
            if (getSkinnable().isShowing()) {
                show();
            } else {
                if (!popupContent.isCustomColorDialogShowing()) hide();
            }
        } else if ("VALUE".equals(p)) {
            updateColor();
           // Change the current selected color in the grid if ColorPicker value changes
            if (popupContent != null) {
//                popupContent.updateSelection(getSkinnable().getValue());
            }
        }
    }
    @Override public Node getDisplayNode() {
        return displayNode;
    }

    private void updateColor() {
        final PaintPicker colorPicker = (PaintPicker) getSkinnable();
        if (colorLabelVisible.getValue()) {
            if (colorPicker.getValue() instanceof LinearGradient) {
                displayNode.setText("Linear Gradient");
               // displayNode.setText(ColorEncoder.encodeLinearToCSS(paintPicker.getValue()));
            } else if (colorPicker.getValue() instanceof RadialGradient) {
                  displayNode.setText("Radial Gradient");
               // displayNode.setText(ColorEncoder.encodeRadialToCSS(paintPicker.getValue()));
            } else {
                displayNode.setText(colorDisplayName((Color) colorPicker.getValue()));
            }
        } else {
            displayNode.setText("");
        }
        colorRect.setFill(colorPicker.getValue());
    }
    public void syncWithAutoUpdate() {
        if (!getPopup().isShowing() && getSkinnable().isShowing()) {
            // Popup was dismissed. Maybe user clicked outside or typed ESCAPE.
            // Make sure ColorPicker button is in sync.
            getSkinnable().hide();
        }
    }

    @Override protected void layoutChildren(final double x, final double y,
            final double w, final double h) {
        updateComboBoxMode();
        super.layoutChildren(x,y,w,h);
    }

    public static String getString(String key) {
        return ControlResources.getString("ColorPicker."+key);
    }

    /***************************************************************************
    *                                                                         *
    *                         picker-color-cell                               *
    *                                                                         *
    **************************************************************************/

    private class PickerColorBox extends StackPane {
        @Override protected void layoutChildren() {
            final double top = snappedTopInset();
            final double left = snappedLeftInset();
            final double width = getWidth();
            final double height = getHeight();
            final double right = snappedRightInset();
            final double bottom = snappedBottomInset();
            colorRect.setX(snapPosition(colorRectX.get()));
            colorRect.setY(snapPosition(colorRectY.get()));
            colorRect.setWidth(snapSize(colorRectWidth.get()));
            colorRect.setHeight(snapSize(colorRectHeight.get()));
            if (getChildren().size() == 2) {
                final ImageView icon = (ImageView) getChildren().get(1);
                Pos childAlignment = StackPane.getAlignment(icon);
                layoutInArea(icon, left, top,
                             width - left - right, height - top - bottom,
                             0, getMargin(icon),
                             childAlignment != null? childAlignment.getHpos() : getAlignment().getHpos(),
                             childAlignment != null? childAlignment.getVpos() : getAlignment().getVpos());
                colorRect.setLayoutX(icon.getLayoutX());
                colorRect.setLayoutY(icon.getLayoutY());
            } else {
                Pos childAlignment = StackPane.getAlignment(colorRect);
                layoutInArea(colorRect, left, top,
                             width - left - right, height - top - bottom,
                             0, getMargin(colorRect),
                             childAlignment != null? childAlignment.getHpos() : getAlignment().getHpos(),
                             childAlignment != null? childAlignment.getVpos() : getAlignment().getVpos());
            }
        }
    }

    /***************************************************************************
    *                                                                         *
    *                         Stylesheet Handling                             *
    *                                                                         *
    **************************************************************************/

     private static class StyleableProperties {
        private static final ThreadLocal<CssMetaData<PaintPicker, Boolean>> COLOR_LABEL_VISIBLE =
                ThreadLocal.withInitial(() -> new CssMetaData<PaintPicker, Boolean>("-fx-color-label-visible",
                        BooleanConverter.getInstance(), Boolean.TRUE) {

                    @Override
                    public boolean isSettable(PaintPicker n) {
                        final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                        return skin.colorLabelVisible == null || !skin.colorLabelVisible.getValue();
                    }

                    @Override
                    public StyleableProperty<Boolean> getStyleableProperty(PaintPicker n) {
                        final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                        return  skin.colorLabelVisible;
                    }
                });
        private static final CssMetaData<PaintPicker,Number> COLOR_RECT_WIDTH =
                new CssMetaData<PaintPicker,Number>("-fx-color-rect-width", SizeConverter.getInstance(), 12d) {
                    @Override public boolean isSettable(PaintPicker n) {
                        final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                        return !skin.colorRectWidth.isBound();
                    }
                    @Override public StyleableProperty<Number> getStyleableProperty(PaintPicker n) {
                        final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                        return skin.colorRectWidth;
                    }
                };
        private static final CssMetaData<PaintPicker,Number> COLOR_RECT_HEIGHT =
                new CssMetaData<PaintPicker,Number>("-fx-color-rect-height", SizeConverter.getInstance(), 12d) {
                    @Override public boolean isSettable(PaintPicker n) {
                        final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                        return !skin.colorRectHeight.isBound();
                    }
                    @Override public StyleableProperty<Number> getStyleableProperty(PaintPicker n) {
                        final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                        return skin.colorRectHeight;
                    }
                };
        private static final CssMetaData<PaintPicker,Number> COLOR_RECT_X =
                new CssMetaData<PaintPicker,Number>("-fx-color-rect-x", SizeConverter.getInstance(), 0) {
                    @Override public boolean isSettable(PaintPicker n) {
                        final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                        return !skin.colorRectX.isBound();
                    }
                    @Override public StyleableProperty<Number> getStyleableProperty(PaintPicker n) {
                        final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                        return skin.colorRectX;
                    }
                };
        private static final CssMetaData<PaintPicker,Number> COLOR_RECT_Y =
                new CssMetaData<PaintPicker,Number>("-fx-color-rect-y", SizeConverter.getInstance(), 0) {
                    @Override public boolean isSettable(PaintPicker n) {
                        final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                        return !skin.colorRectY.isBound();
                    }
                    @Override public StyleableProperty<Number> getStyleableProperty(PaintPicker n) {
                        final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                        return skin.colorRectY;
                    }
                };
        private static final CssMetaData<PaintPicker,String> GRAPHIC =
            new CssMetaData<PaintPicker,String>("-fx-graphic", StringConverter.getInstance()) {
                @Override public boolean isSettable(PaintPicker n) {
                    final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                    return !skin.imageUrl.isBound();
                }
                @Override public StyleableProperty<String> getStyleableProperty(PaintPicker n) {
                    final PaintPickerSkin skin = (PaintPickerSkin) n.getSkin();
                    return skin.imageUrl;
                }
            };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<>(ComboBoxBaseSkin.getClassCssMetaData());
            styleables.add(COLOR_LABEL_VISIBLE.get());
            styleables.add(COLOR_RECT_WIDTH);
            styleables.add(COLOR_RECT_HEIGHT);
            styleables.add(COLOR_RECT_X);
            styleables.add(COLOR_RECT_Y);
            styleables.add(GRAPHIC);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     * CssMetaData of its super classes.
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    @Override protected javafx.util.StringConverter<Paint> getConverter() {
        return null;
    }
//
//    /**
//     * ColorPicker does not use a main text field.
//     * @return 
//     */
//    @Override protected TextField getEditor() {
//        return null;
//    }
}
