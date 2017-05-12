/*
 * Permissions of this copy-left license are conditioned on making available 
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * String constants that are used to build the gradient syntax.
 *
 * @author Sai.Dandem
 *
 */
public interface SyntaxConstants {

    public String SPACER = " ";
    public String SEPARATOR = ", ";
    public String BGCOLOR = "-fx-background-color: ";
    public String BGRADIAL = "radial-gradient(";
    public String BGLINEAR = "linear-gradient(";
    public String BGGRADEND = ");";
    public String FOCUSANGLESTART = "focus-angle ";
    public String FOCUSANGLEUNIT = "deg ";
    public String FOCUSDISTSTART = "focus-distance ";
    public String FOCUSDISTUNIT = "% ";
    public String CENTERSTART = "center ";
    public String CENTERUNIT = "% ";
    public String RADIUS = "radius ";
    public String RADIUSPERCENTUNIT = "% ";
    public String RADIUSPIXELUNIT = "px ";
    public String REPEAT = "repeat ";
    public String REFLECT = "reflect ";
    public String COLORSTOPUNIT = "% ";
    public String POINTPERCENTUNIT = "% ";
    public String POINTPIXELUNIT = "px ";
    public String FROM = "from ";
    public String FROMPIXELUNIT = "px ";
    public String FROMPERCENTUNIT = "% ";
    public String TO = "to ";
    public String TOPIXELUNIT = "px ";
    public String TOPERCENTUNIT = "% ";

    public enum RepeatOrReflect {
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

    public enum LinearDirection {
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
