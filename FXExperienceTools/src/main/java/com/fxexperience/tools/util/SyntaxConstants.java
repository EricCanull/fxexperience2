package com.fxexperience.tools.util;
/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public interface SyntaxConstants {

    String SPACER = " ";
    String SEPARATOR = ", ";
    String BGCOLOR = "-fx-background-color: ";
    String BGRADIAL = "radial-gradient(";
    String BGLINEAR = "linear-gradient(";
    String BGGRADEND = ");";
    String FOCUSANGLESTART = "focus-angle ";
    String FOCUSANGLEUNIT = "deg ";
    String FOCUSDISTSTART = "focus-distance ";
    String FOCUSDISTUNIT = "% ";
    String CENTERSTART = "center ";
    String CENTERUNIT = "% ";
    String RADIUS = "radius ";
    String RADIUSPERCENTUNIT = "% ";
    String RADIUSPIXELUNIT = "px ";
    String REPEAT = "repeat ";
    String REFLECT = "reflect ";
    String COLORSTOPUNIT = "% ";
    String POINTPERCENTUNIT = "% ";
    String POINTPIXELUNIT = "px ";
    String FROM = "from ";
    String FROMPIXELUNIT = "px ";
    String FROMPERCENTUNIT = "% ";
    String TO = "to ";
    String TOPIXELUNIT = "px ";
    String TOPERCENTUNIT = "% ";

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
            list.addAll(RepeatOrReflect.values());
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
            list.addAll(LinearDirection.values());
            return list;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}
