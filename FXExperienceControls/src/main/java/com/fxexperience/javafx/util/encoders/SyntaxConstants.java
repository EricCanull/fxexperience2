package com.fxexperience.javafx.util.encoders;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by andje22 on 6/4/17.
 */
public interface SyntaxConstants {

    String SPACER            = " ";
    String TAB               = "    ";
    String SEPARATOR         = ", ";
    String COLON             = ";/n";
    String FONT_TYPE         = "-fx-font-family";
    String FONT_SIZE         = "-fx-font-size";
    String BASE              = "-fx-base";
    String BACKGROUND        = "-fx-background";
    String BACKGROUND_COLOR  = "-fx-background-color";
    String FOCUS             = "-fx-focus-color";
    String CONTROL_INNER_BG  = "-fx-control-inner-background";
    String TEXT_BASE         = "-fx-text-base-color";
    String TEXT_BACKGROUND   = "-fx-text-background-color";
    String TEXT_INNER        = "-fx-text-inner-color";
    String INNER_BORDER      = "-fx-inner-border";
    String BODY_COLOR        = "-fx-body-color";
    String OUTER_BORDER      = "-fx-outer-border";
    String SHADOW_HIGHLIGHT  = "-fx-shadow-highlight-color";
    String TEXT_BOX_BORDER   = "-fx-text-box-border";
    String TEXTFILL          = "-fx-text-fill";
    String DERIVE            = "derive(";
    String BGRADIAL          = "radial-gradient(";
    String BGLINEAR          = "linear-gradient(";
    String BGGRADEND         = ");";
    String FOCUSANGLESTART   = "focus-angle ";
    String FOCUSANGLEUNIT    = "deg";
    String FOCUSDISTSTART    = "focus-distance ";
    String FOCUSDISTUNIT     = "%";
    String CENTERSTART       = "center ";
    String CENTERUNIT        = "%";
    String RADIUSSTART = "radius ";
    String RADIUS            = "radius ";
    String RADIUSPERCENTUNIT = "%";
    String RADIUSPIXELUNIT   = "px ";
    String REPEAT            = "repeat ";
    String REFLECT           = "reflect ";
    String COLORSTOPUNIT     = "%";
    String POINTPERCENTUNIT  = "%";
    String POINTPIXELUNIT    = "px ";
    String FROM              = "from ";
    String FROMPIXELUNIT     = "px ";
    String FROMPERCENTUNIT   = "%";
    String TO                = "to ";
    String TOPIXELUNIT       = "px ";
    String TOPERCENTUNIT     = "%";

    enum BaseConstants {
        COLOR("-fx-color: "),
        BACKGROUND("-fx-background: ");

        String value;

        BaseConstants(String value) {
            this.value = value;
        }

        public static ObservableList<BaseConstants> getList() {
            ObservableList<BaseConstants> list = FXCollections.observableArrayList();
            list.addAll(SyntaxConstants.BaseConstants.values());
            return list;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum CSSProperty {

    }

    enum RepeatOrReflect {
        NONE("None"),
        REPEAT("repeat"),
        REFLECT("reflect");

        String value;

        RepeatOrReflect(String value) {
            this.value = value;
        }

        public static ObservableList<RepeatOrReflect> getList() {
            ObservableList<RepeatOrReflect> list = FXCollections.observableArrayList();
            list.addAll(SyntaxConstants.RepeatOrReflect.values());
            return list;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    enum LinearDirection {
        TOP("top"),
        LEFT("left"),
        BOTTOM("bottom"),
        RIGHT("right"),
        TOP_LEFT("top left"),
        TOP_RIGHT("top right"),
        BOTTOM_LEFT("bottom left"),
        BOTTOM_RIGHT("bottom right");

        String value;

        LinearDirection(String value) {
            this.value = value;
        }

        public static ObservableList<LinearDirection> getList() {
            ObservableList<LinearDirection> list = FXCollections.observableArrayList();
            list.addAll(SyntaxConstants.LinearDirection.values());
            return list;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}

