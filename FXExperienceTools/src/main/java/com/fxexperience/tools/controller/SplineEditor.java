package com.fxexperience.tools.controller;

/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public final class SplineEditor extends XYChart<Number, Number> {

    private final DoubleProperty controlPoint1x = new SimpleDoubleProperty(0.8);

    public double getControlPoint1x() {
        return controlPoint1x.get();
    }

    public DoubleProperty controlPoint1xProperty() {
        return controlPoint1x;
    }
    public final DoubleProperty controlPoint1y = new SimpleDoubleProperty(0.2);

    public double getControlPoint1y() {
        return controlPoint1y.get();
    }

    public DoubleProperty controlPoint1yProperty() {
        return controlPoint1y;
    }
    public final DoubleProperty controlPoint2x = new SimpleDoubleProperty(0.2);

    public double getControlPoint2x() {
        return controlPoint2x.get();
    }

    public DoubleProperty controlPoint2xProperty() {
        return controlPoint2x;
    }
    public final DoubleProperty controlPoint2y = new SimpleDoubleProperty(0.8);

    public double getControlPoint2y() {
        return controlPoint2y.get();
    }

    public DoubleProperty controlPoint2yProperty() {
        return controlPoint2y;
    }

    private final Circle controlPoint1Circle;
    private final Circle controlPoint2Circle;

    private final Line cp1Line;
    private final Line cp2Line;

    private final Path dottedLinesPath;
    private final Path splinePath;

    public SplineEditor() {
        super(new NumberAxis(0, 1, 0.1), new NumberAxis(0, 1, 0.1));
        controlPoint1Circle = createCircle(Color.WHITE, Color.RED, 3d, 6d, Cursor.HAND);
        controlPoint2Circle = createCircle(Color.WHITE, Color.RED, 3d, 6d, Cursor.HAND);

        cp1Line = createLine(Color.RED, 2);
        cp2Line = createLine(Color.RED, 2);

        dottedLinesPath = createPath(Color.WHITE, 1d, 1d, 8d);
        splinePath = createPath(Color.WHITE, 2d, 1d, 1d);
        initialize();
    }

    public void initialize() {
        setAnimated(false);
        getXAxis().setAutoRanging(false);
        getXAxis().setAutoRanging(false);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        setAlternativeRowFillVisible(false);
        getPlotChildren().addAll(splinePath, dottedLinesPath, cp1Line, cp2Line, controlPoint1Circle, controlPoint2Circle);

        ObservableList<XYChart.Series<Number, Number>> answer = FXCollections.observableArrayList();

        setData(FXCollections.observableArrayList(answer));

        controlPoint1Circle.setOnMouseDragged((event) ->
            setPointCircles(event, controlPoint1Circle, controlPoint1x, controlPoint1y));

        controlPoint2Circle.setOnMouseDragged((event) ->
            setPointCircles(event, controlPoint2Circle, controlPoint2x, controlPoint2y));
    }


    public void setPointCircles(MouseEvent event, Circle controlPointCircle, DoubleProperty controlPointX,
                                DoubleProperty controlPointY) {

        double x = event.getX() + controlPointCircle.getLayoutX();
        double y = event.getY() + controlPointCircle.getLayoutY();
        double dataX = getXAxis().getValueForDisplay(x).doubleValue();
        double dataY = getYAxis().getValueForDisplay(y).doubleValue();
        controlPointX.set(clamp(dataX));
        controlPointY.set(clamp(dataY));
        requestChartLayout();
    }
    
    @Override
    protected void layoutPlotChildren() {
        double cp1x = getXAxis().getDisplayPosition(controlPoint1x.get());
        double cp1y = getYAxis().getDisplayPosition(controlPoint1y.get());
        double cp2x = getXAxis().getDisplayPosition(controlPoint2x.get());
        double cp2y = getYAxis().getDisplayPosition(controlPoint2y.get());
        double minx = getXAxis().getZeroPosition();
        double miny = getYAxis().getZeroPosition();
        double maxx = getXAxis().getDisplayPosition(1);
        double maxy = getYAxis().getDisplayPosition(1);
        controlPoint1Circle.setLayoutX(cp1x);
        controlPoint1Circle.setLayoutY(cp1y);
        controlPoint2Circle.setLayoutX(cp2x);
        controlPoint2Circle.setLayoutY(cp2y);
        cp1Line.setStartX(minx);
        cp1Line.setStartY(miny);
        cp1Line.setEndX(cp1x);
        cp1Line.setEndY(cp1y);
        cp2Line.setStartX(maxx);
        cp2Line.setStartY(maxy);
        cp2Line.setEndX(cp2x);
        cp2Line.setEndY(cp2y);
        dottedLinesPath.getElements().setAll(
                new MoveTo(minx - 0.5, cp1y - 0.5),
                new LineTo(cp1x - 0.5, cp1y - 0.5),
                new LineTo(cp1x - 0.5, miny - 0.5),
                new MoveTo(minx - 0.5, cp2y - 0.5),
                new LineTo(cp2x - 0.5, cp2y - 0.5),
                new LineTo(cp2x - 0.5, miny - 0.5)
        );
        splinePath.getElements().setAll(
                new MoveTo(minx, miny),
                new CubicCurveTo(cp1x, cp1y, cp2x, cp2y, maxx, maxy));
    }

    private Circle createCircle(Color fill, Color stroke, double width, double radius, Cursor cursor) {
        Circle circle = new Circle();
        circle.fillProperty().set(fill);
        circle.setStroke(stroke);
        circle.strokeWidthProperty().set(width);
        circle.setRadius(radius);
        circle.setCursor(cursor);

        return circle;
    }

    private Line createLine(Color stroke, double width) {
        Line line = new Line();
        line.setStroke(stroke);
        line.strokeWidthProperty().set(width);
        return line;
    }

    private Path createPath(Color stroke, double width, double a, double b) {
        Path path = new Path();
        path.setStroke(stroke);
        path.strokeWidthProperty().set(width);
        path.getStrokeDashArray().addAll(a, b);
        return path;
    }

    private static double clamp(double value) {
        return value < 0 ? 0 : value > 1 ? 1 : value;
    }
    

    @Override
    protected void dataItemAdded(Series<Number, Number> series, int i, Data<Number, Number> data) {
    }

    @Override
    protected void dataItemRemoved(Data<Number, Number> data, Series<Number, Number> series) {
    }

    @Override
    protected void dataItemChanged(Data<Number, Number> data) {
    }

    @Override
    protected void seriesAdded(Series<Number, Number> series, int i) {
    }

    @Override
    protected void seriesRemoved(Series<Number, Number> series) {
    }
}
