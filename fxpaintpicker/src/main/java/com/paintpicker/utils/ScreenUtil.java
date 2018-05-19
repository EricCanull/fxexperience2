package com.paintpicker.utils;

import java.util.List;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.stage.Screen;
import javafx.stage.Window;

public class ScreenUtil {
	
    /**
     * To facilitate multiple types of parent object, we unfortunately must allow for 
     * Objects to be passed in. This method handles determining the bounds of the 
     * given Object. If the Object type is not supported, a default Bounds will be returned. 
     */ 
    private static Bounds getBounds(Object obj) { 
        if (obj instanceof Node) { 
            final Node n = (Node)obj; 
            Bounds b = n.localToScreen(n.getLayoutBounds()); 
            return b != null ? b : new BoundingBox(0, 0, 0, 0); 
        } else if (obj instanceof Window) { 
            final Window window = (Window)obj; 
            return new BoundingBox(window.getX(), window.getY(), window.getWidth(), window.getHeight()); 
        } else { 
            return new BoundingBox(0, 0, 0, 0); 
        } 
    } 
    
    /**
     * This function attempts to determine the best screen given the parent object
     * from which we are wanting to position another item relative to. This is particularly
     * important when we want to keep items from going off screen, and for handling
     * multiple monitor support.
     */
    public static Screen getScreen(Object obj) {
        final Bounds parentBounds = getBounds(obj);

        final Rectangle2D rect = new Rectangle2D(
                parentBounds.getMinX(),
                parentBounds.getMinY(),
                parentBounds.getWidth(),
                parentBounds.getHeight());

        return getScreenForRectangle(rect);
    }
    private static double getIntersectionLength( 
            final double a0, final double a1, 
            final double b0, final double b1) { 
        // (a0 <= a1) && (b0 <= b1) 
        return (a0 <= b0) ? getIntersectionLengthImpl(b0, b1, a1) 
                          : getIntersectionLengthImpl(a0, a1, b1); 
    } 
 
    private static double getIntersectionLengthImpl( 
            final double v0, final double v1, final double v) { 
        // (v0 <= v1) 
        if (v <= v0) { 
            return 0; 
        } 
 
        return (v <= v1) ? v - v0 : v1 - v0; 
    } 
    private static double getOuterDistance( 
            final double a0, final double a1, 
            final double b0, final double b1) { 
        // (a0 <= a1) && (b0 <= b1) 
        if (a1 <= b0) { 
            return b0 - a1; 
        } 
 
        if (b1 <= a0) { 
            return b1 - a0; 
        } 
 
        return 0; 
    } 

    public static Screen getScreenForRectangle(final Rectangle2D rect) {
        final List<Screen> screens = Screen.getScreens();

        final double rectX0 = rect.getMinX();
        final double rectX1 = rect.getMaxX();
        final double rectY0 = rect.getMinY();
        final double rectY1 = rect.getMaxY();

        Screen selectedScreen;

        selectedScreen = null;
        double maxIntersection = 0;
        for (final Screen screen: screens) {
            final Rectangle2D screenBounds = screen.getBounds();
            final double intersection =
                    getIntersectionLength(rectX0, rectX1,
                                          screenBounds.getMinX(),
                                          screenBounds.getMaxX())
                        * getIntersectionLength(rectY0, rectY1,
                                                screenBounds.getMinY(),
                                                screenBounds.getMaxY());

            if (maxIntersection < intersection) {
                maxIntersection = intersection;
                selectedScreen = screen;
            }
        }

        if (selectedScreen != null) {
            return selectedScreen;
        }

        selectedScreen = Screen.getPrimary();
        double minDistance = Double.MAX_VALUE;
        for (final Screen screen: screens) {
            final Rectangle2D screenBounds = screen.getBounds();
            final double dx = getOuterDistance(rectX0, rectX1,
                                               screenBounds.getMinX(),
                                               screenBounds.getMaxX());
            final double dy = getOuterDistance(rectY0, rectY1,
                                               screenBounds.getMinY(),
                                               screenBounds.getMaxY());
            final double distance = dx * dx + dy * dy;

            if (minDistance > distance) {
                minDistance = distance;
                selectedScreen = screen;
            }
        }

        return selectedScreen;
    }
}
