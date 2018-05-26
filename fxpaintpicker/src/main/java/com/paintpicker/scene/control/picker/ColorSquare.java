package com.paintpicker.scene.control.picker;

import com.paintpicker.scene.control.picker.skins.PaintPickerSkin;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class ColorSquare extends StackPane {

    int SQUARE_SIZE = 14;
    Rectangle rectangle;
    int index;
    boolean isEmpty;
    boolean isCustom;

    public ColorSquare() {
        this(null, -1, false);
    }

    public ColorSquare(Paint color, int index) {
        this(color, index, false);
    }

    public ColorSquare(Paint color, int index, boolean isCustom) {
        this.getStyleClass().add("color-square-pane");
        getStylesheets().add(PaintPalette.class.getResource("/styles/custom_color_square.css").toExternalForm());

        rectangle = new Rectangle();
        rectangle = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        if (color == null) {
            rectangle.setFill(Color.WHITE);
            isEmpty = true;
        } else {
            rectangle.setFill(color);
        }

        rectangle.setStrokeType(StrokeType.INSIDE);

        String tooltipStr = PaintPickerSkin.tooltipString(color);
        Tooltip.install(this, new Tooltip((tooltipStr == null) ? "" : tooltipStr));

        rectangle.getStyleClass().add("color-rect");

        getChildren().add(rectangle);
    }
}
