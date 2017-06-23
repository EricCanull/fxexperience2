/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
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
package com.fxexperience.javafx.util.encoders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
 import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.*;

/**
 *
 */
public class ColorEncoder implements SyntaxConstants {
    
    private static Map<String, Color> standardColors;
    private static Map<Color, String> standardColorNames;

    
    public static String encodeColor(Color color) {
        final String colorName = getStandardColorNames().get(color);
        final String result;
        
        if (colorName != null) {
            result = colorName;
        } else {
            result = makeColorEncoding(color);
        }
        
        return result;
    }

    private static synchronized Map<String, Color> getStandardColors() {
        
        if (standardColors == null) {
            standardColors = new HashMap<>();
            
            standardColors.put("ALICEBLUE", Color.ALICEBLUE); //NOI18N
            standardColors.put("ANTIQUEWHITE", Color.ANTIQUEWHITE); //NOI18N
            standardColors.put("AQUA", Color.AQUA); //NOI18N
            standardColors.put("AQUAMARINE", Color.AQUAMARINE); //NOI18N
            standardColors.put("AZURE", Color.AZURE); //NOI18N
            standardColors.put("BEIGE", Color.BEIGE); //NOI18N
            standardColors.put("BISQUE", Color.BISQUE); //NOI18N
            standardColors.put("BLACK", Color.BLACK); //NOI18N
            standardColors.put("BLANCHEDALMOND", Color.BLANCHEDALMOND); //NOI18N
            standardColors.put("BLUE", Color.BLUE); //NOI18N
            standardColors.put("BLUEVIOLET", Color.BLUEVIOLET); //NOI18N
            standardColors.put("BROWN", Color.BROWN); //NOI18N
            standardColors.put("BURLYWOOD", Color.BURLYWOOD); //NOI18N
            standardColors.put("CADETBLUE", Color.CADETBLUE); //NOI18N
            standardColors.put("CHARTREUSE", Color.CHARTREUSE); //NOI18N
            standardColors.put("CHOCOLATE", Color.CHOCOLATE); //NOI18N
            standardColors.put("CORAL", Color.CORAL); //NOI18N
            standardColors.put("CORNFLOWERBLUE", Color.CORNFLOWERBLUE); //NOI18N
            standardColors.put("CORNSILK", Color.CORNSILK); //NOI18N
            standardColors.put("CRIMSON", Color.CRIMSON); //NOI18N
            standardColors.put("CYAN", Color.CYAN); //NOI18N
            standardColors.put("DARKBLUE", Color.DARKBLUE); //NOI18N
            standardColors.put("DARKCYAN", Color.DARKCYAN); //NOI18N
            standardColors.put("DARKGOLDENROD", Color.DARKGOLDENROD); //NOI18N
            standardColors.put("DARKGRAY", Color.DARKGRAY); //NOI18N
            standardColors.put("DARKGREEN", Color.DARKGREEN); //NOI18N
            standardColors.put("DARKGREY", Color.DARKGREY); //NOI18N
            standardColors.put("DARKKHAKI", Color.DARKKHAKI); //NOI18N
            standardColors.put("DARKMAGENTA", Color.DARKMAGENTA); //NOI18N
            standardColors.put("DARKOLIVEGREEN", Color.DARKOLIVEGREEN); //NOI18N
            standardColors.put("DARKORANGE", Color.DARKORANGE); //NOI18N
            standardColors.put("DARKORCHID", Color.DARKORCHID); //NOI18N
            standardColors.put("DARKRED", Color.DARKRED); //NOI18N
            standardColors.put("DARKSALMON", Color.DARKSALMON); //NOI18N
            standardColors.put("DARKSEAGREEN", Color.DARKSEAGREEN); //NOI18N
            standardColors.put("DARKSLATEBLUE", Color.DARKSLATEBLUE); //NOI18N
            standardColors.put("DARKSLATEGRAY", Color.DARKSLATEGRAY); //NOI18N
            standardColors.put("DARKSLATEGREY", Color.DARKSLATEGREY); //NOI18N
            standardColors.put("DARKTURQUOISE", Color.DARKTURQUOISE); //NOI18N
            standardColors.put("DARKVIOLET", Color.DARKVIOLET); //NOI18N
            standardColors.put("DEEPPINK", Color.DEEPPINK); //NOI18N
            standardColors.put("DEEPSKYBLUE", Color.DEEPSKYBLUE); //NOI18N
            standardColors.put("DIMGRAY", Color.DIMGRAY); //NOI18N
            standardColors.put("DIMGREY", Color.DIMGREY); //NOI18N
            standardColors.put("DODGERBLUE", Color.DODGERBLUE); //NOI18N
            standardColors.put("FIREBRICK", Color.FIREBRICK); //NOI18N
            standardColors.put("FLORALWHITE", Color.FLORALWHITE); //NOI18N
            standardColors.put("FORESTGREEN", Color.FORESTGREEN); //NOI18N
            standardColors.put("FUCHSIA", Color.FUCHSIA); //NOI18N
            standardColors.put("GAINSBORO", Color.GAINSBORO); //NOI18N
            standardColors.put("GHOSTWHITE", Color.GHOSTWHITE); //NOI18N
            standardColors.put("GOLD", Color.GOLD); //NOI18N
            standardColors.put("GOLDENROD", Color.GOLDENROD); //NOI18N
            standardColors.put("GRAY", Color.GRAY); //NOI18N
            standardColors.put("GREEN", Color.GREEN); //NOI18N
            standardColors.put("GREENYELLOW", Color.GREENYELLOW); //NOI18N
            standardColors.put("GREY", Color.GREY); //NOI18N
            standardColors.put("HONEYDEW", Color.HONEYDEW); //NOI18N
            standardColors.put("HOTPINK", Color.HOTPINK); //NOI18N
            standardColors.put("INDIANRED", Color.INDIANRED); //NOI18N
            standardColors.put("INDIGO", Color.INDIGO); //NOI18N
            standardColors.put("IVORY", Color.IVORY); //NOI18N
            standardColors.put("KHAKI", Color.KHAKI); //NOI18N
            standardColors.put("LAVENDER", Color.LAVENDER); //NOI18N
            standardColors.put("LAVENDERBLUSH", Color.LAVENDERBLUSH); //NOI18N
            standardColors.put("LAWNGREEN", Color.LAWNGREEN); //NOI18N
            standardColors.put("LEMONCHIFFON", Color.LEMONCHIFFON); //NOI18N
            standardColors.put("LIGHTBLUE", Color.LIGHTBLUE); //NOI18N
            standardColors.put("LIGHTCORAL", Color.LIGHTCORAL); //NOI18N
            standardColors.put("LIGHTCYAN", Color.LIGHTCYAN); //NOI18N
            standardColors.put("LIGHTGOLDENRODYELLOW", Color.LIGHTGOLDENRODYELLOW); //NOI18N
            standardColors.put("LIGHTGRAY", Color.LIGHTGRAY); //NOI18N
            standardColors.put("LIGHTGREEN", Color.LIGHTGREEN); //NOI18N
            standardColors.put("LIGHTGREY", Color.LIGHTGREY); //NOI18N
            standardColors.put("LIGHTPINK", Color.LIGHTPINK); //NOI18N
            standardColors.put("LIGHTSALMON", Color.LIGHTSALMON); //NOI18N
            standardColors.put("LIGHTSEAGREEN", Color.LIGHTSEAGREEN); //NOI18N
            standardColors.put("LIGHTSKYBLUE", Color.LIGHTSKYBLUE); //NOI18N
            standardColors.put("LIGHTSLATEGRAY", Color.LIGHTSLATEGRAY); //NOI18N
            standardColors.put("LIGHTSLATEGREY", Color.LIGHTSLATEGREY); //NOI18N
            standardColors.put("LIGHTSTEELBLUE", Color.LIGHTSTEELBLUE); //NOI18N
            standardColors.put("LIGHTYELLOW", Color.LIGHTYELLOW); //NOI18N
            standardColors.put("LIME", Color.LIME); //NOI18N
            standardColors.put("LIMEGREEN", Color.LIMEGREEN); //NOI18N
            standardColors.put("LINEN", Color.LINEN); //NOI18N
            standardColors.put("MAGENTA", Color.MAGENTA); //NOI18N
            standardColors.put("MAROON", Color.MAROON); //NOI18N
            standardColors.put("MEDIUMAQUAMARINE", Color.MEDIUMAQUAMARINE); //NOI18N
            standardColors.put("MEDIUMBLUE", Color.MEDIUMBLUE); //NOI18N
            standardColors.put("MEDIUMORCHID", Color.MEDIUMORCHID); //NOI18N
            standardColors.put("MEDIUMPURPLE", Color.MEDIUMPURPLE); //NOI18N
            standardColors.put("MEDIUMSEAGREEN", Color.MEDIUMSEAGREEN); //NOI18N
            standardColors.put("MEDIUMSLATEBLUE", Color.MEDIUMSLATEBLUE); //NOI18N
            standardColors.put("MEDIUMSPRINGGREEN", Color.MEDIUMSPRINGGREEN); //NOI18N
            standardColors.put("MEDIUMTURQUOISE", Color.MEDIUMTURQUOISE); //NOI18N
            standardColors.put("MEDIUMVIOLETRED", Color.MEDIUMVIOLETRED); //NOI18N
            standardColors.put("MIDNIGHTBLUE", Color.MIDNIGHTBLUE); //NOI18N
            standardColors.put("MINTCREAM", Color.MINTCREAM); //NOI18N
            standardColors.put("MISTYROSE", Color.MISTYROSE); //NOI18N
            standardColors.put("MOCCASIN", Color.MOCCASIN); //NOI18N
            standardColors.put("NAVAJOWHITE", Color.NAVAJOWHITE); //NOI18N
            standardColors.put("NAVY", Color.NAVY); //NOI18N
            standardColors.put("OLDLACE", Color.OLDLACE); //NOI18N
            standardColors.put("OLIVE", Color.OLIVE); //NOI18N
            standardColors.put("OLIVEDRAB", Color.OLIVEDRAB); //NOI18N
            standardColors.put("ORANGE", Color.ORANGE); //NOI18N
            standardColors.put("ORANGERED", Color.ORANGERED); //NOI18N
            standardColors.put("ORCHID", Color.ORCHID); //NOI18N
            standardColors.put("PALEGOLDENROD", Color.PALEGOLDENROD); //NOI18N
            standardColors.put("PALEGREEN", Color.PALEGREEN); //NOI18N
            standardColors.put("PALETURQUOISE", Color.PALETURQUOISE); //NOI18N
            standardColors.put("PALEVIOLETRED", Color.PALEVIOLETRED); //NOI18N
            standardColors.put("PAPAYAWHIP", Color.PAPAYAWHIP); //NOI18N
            standardColors.put("PEACHPUFF", Color.PEACHPUFF); //NOI18N
            standardColors.put("PERU", Color.PERU); //NOI18N
            standardColors.put("PINK", Color.PINK); //NOI18N
            standardColors.put("PLUM", Color.PLUM); //NOI18N
            standardColors.put("POWDERBLUE", Color.POWDERBLUE); //NOI18N
            standardColors.put("PURPLE", Color.PURPLE); //NOI18N
            standardColors.put("RED", Color.RED); //NOI18N
            standardColors.put("ROSYBROWN", Color.ROSYBROWN); //NOI18N
            standardColors.put("ROYALBLUE", Color.ROYALBLUE); //NOI18N
            standardColors.put("SADDLEBROWN", Color.SADDLEBROWN); //NOI18N
            standardColors.put("SALMON", Color.SALMON); //NOI18N
            standardColors.put("SANDYBROWN", Color.SANDYBROWN); //NOI18N
            standardColors.put("SEAGREEN", Color.SEAGREEN); //NOI18N
            standardColors.put("SEASHELL", Color.SEASHELL); //NOI18N
            standardColors.put("SIENNA", Color.SIENNA); //NOI18N
            standardColors.put("SILVER", Color.SILVER); //NOI18N
            standardColors.put("SKYBLUE", Color.SKYBLUE); //NOI18N
            standardColors.put("SLATEBLUE", Color.SLATEBLUE); //NOI18N
            standardColors.put("SLATEGRAY", Color.SLATEGRAY); //NOI18N
            standardColors.put("SLATEGREY", Color.SLATEGREY); //NOI18N
            standardColors.put("SNOW", Color.SNOW); //NOI18N
            standardColors.put("SPRINGGREEN", Color.SPRINGGREEN); //NOI18N
            standardColors.put("STEELBLUE", Color.STEELBLUE); //NOI18N
            standardColors.put("TAN", Color.TAN); //NOI18N
            standardColors.put("TEAL", Color.TEAL); //NOI18N
            standardColors.put("THISTLE", Color.THISTLE); //NOI18N
            standardColors.put("TOMATO", Color.TOMATO); //NOI18N
            standardColors.put("TRANSPARENT", Color.TRANSPARENT); //NOI18N
            standardColors.put("TURQUOISE", Color.TURQUOISE); //NOI18N
            standardColors.put("VIOLET", Color.VIOLET); //NOI18N
            standardColors.put("WHEAT", Color.WHEAT); //NOI18N
            standardColors.put("WHITE", Color.WHITE); //NOI18N
            standardColors.put("WHITESMOKE", Color.WHITESMOKE); //NOI18N
            standardColors.put("YELLOW", Color.YELLOW); //NOI18N
            standardColors.put("YELLOWGREEN", Color.YELLOWGREEN); //NOI18N

            standardColors = Collections.unmodifiableMap(standardColors);
        }
        
        return standardColors;
    }
    
    public static synchronized Map<Color, String> getStandardColorNames() {
        
        if (standardColorNames == null) {
            standardColorNames = new HashMap<>();
            for (Map.Entry<String, Color> e : getStandardColors().entrySet()) {
                standardColorNames.put(e.getValue(), e.getKey());
            }
            standardColorNames = Collections.unmodifiableMap(standardColorNames);
        }
        
        return standardColorNames;
    }

    public static String encodeRadialToCSS(Object paint) {
        assert paint instanceof RadialGradient;

        RadialGradient radialGradient = (RadialGradient) paint;

        List<Stop> stops = radialGradient.getStops();

        boolean radial_proportional = radialGradient.isProportional();

        final CycleMethod radial_cycleMethod = radialGradient.getCycleMethod();

        double focusAngle = radialGradient.getFocusAngle();
        double focusDistance = radialGradient.getFocusDistance();
        double centerX = round(radialGradient.getCenterX());
        double centerY = round(radialGradient.getCenterY());
        double radius =   round(radialGradient.getRadius());


        String radiusUnit = radial_proportional ? RADIUSPERCENTUNIT : RADIUSPIXELUNIT;

        StringBuilder sb = new StringBuilder(BGRADIAL);
        sb.append(FOCUSANGLESTART);
        sb.append(focusAngle).append(FOCUSANGLEUNIT).append(SEPARATOR);
        sb.append(FOCUSDISTSTART);
        sb.append(focusDistance).append(FOCUSDISTUNIT).append(SEPARATOR);
        sb.append(CENTERSTART);
        sb.append(centerX).append(CENTERUNIT).append(SPACER);
        sb.append(centerY).append(CENTERUNIT).append(SEPARATOR);
        sb.append(RADIUSSTART);
        sb.append(radius).append(radiusUnit).append(SEPARATOR);

        if (radial_cycleMethod.toString().equalsIgnoreCase("reflect")
                || radial_cycleMethod.toString().equalsIgnoreCase("repeat")) {
            sb.append(radial_cycleMethod);
            sb.append(SEPARATOR);
        }
        sb.append(getColorStops(stops));

        return  sb.append(BGGRADEND).toString();
    }

    public static String encodeLinearToCSS(Object paint) {
        assert paint instanceof LinearGradient;

        LinearGradient linearGradient = (LinearGradient) paint;

        List<Stop> stops = linearGradient.getStops();

        boolean linear_proportional = linearGradient.isProportional();

        final CycleMethod linear_cycleMethod = linearGradient.getCycleMethod();

        double startX = round(linearGradient.getStartX());
        double startY = round(linearGradient.getStartY());
        double endX =   round(linearGradient.getEndX());
        double endY =   round(linearGradient.getEndY());

        String fromUnit = linear_proportional ? FROMPERCENTUNIT : FROMPIXELUNIT;
        String toUnit = linear_proportional   ? TOPERCENTUNIT : TOPIXELUNIT;

        StringBuilder sb = new StringBuilder(BGLINEAR);
        sb.append(FROM);
        sb.append(startX).append(fromUnit);
        sb.append(startY).append(fromUnit);
        sb.append(TO);
        sb.append(endX).append(toUnit);
        sb.append(endY).append(toUnit);
        sb.append(SEPARATOR);

        if (linear_cycleMethod.toString().equalsIgnoreCase("reflect")
                || linear_cycleMethod.toString().equalsIgnoreCase("repeat")) {
            sb.append(linear_cycleMethod);
            sb.append(SEPARATOR);
        }

        sb.append(getColorStops(stops));

        return  sb.append(BGGRADEND).toString();
    }


    private static String getColorStops(List<Stop> stops) {
        // Color Stops
        Color dto;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stops.size(); i++) {
            dto = stops.get(i).getColor();
            double offset = Math.round(stops.get(i).getOffset() * 100);
            sb.append(encodeColor(dto));
            sb.append(SPACER);
            sb.append(offset);

            sb.append(COLORSTOPUNIT);
            if (i < (stops.size() - 1)) {
                sb.append(SEPARATOR);
            }
        }

        return sb.toString();
    }
    
    public static String encodeColorToRGBA(Color color) {
        final String result;
        if (color == null) {
            result = "null";//NOI18N
        } else {
            final int red = (int) (color.getRed() * 255);
            final int green = (int) (color.getGreen() * 255);
            final int blue = (int) (color.getBlue() * 255);
            result = "rgba("+red+","+green+","+blue +","+color.getOpacity()+")";//NOI18N
        }
        return result;    
    }

    /*
     * Private
     */
    private static String makeColorEncoding(Color c) {
        final int red, green, blue, alpha;
        final String result;
        
        red   = (int) Math.round(c.getRed() * 255.0);
        green = (int) Math.round(c.getGreen() * 255.0);
        blue  = (int) Math.round(c.getBlue() * 255.0);
        alpha = (int) Math.round(c.getOpacity() * 255.0);
        if (alpha == 255) {
            result = String.format((Locale)null, "#%02x%02x%02x", red, green, blue); //NOI18N
        } else {
            result = String.format((Locale)null, "#%02x%02x%02x%02x", red, green, blue, alpha); //NOI18N
        }
        
        return result;
    }
    
    private static double[] RGBtoHSB(double r, double g, double b) {
       double hue, saturation, brightness;
        double[] hsbvals = new double[3];
        double cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;
        double cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;

        brightness = cmax;
        if (cmax != 0)
            saturation = (cmax - cmin) / cmax;
        else
            saturation = 0;

        if (saturation == 0) {
            hue = 0;
        } else {
            double redc = (cmax - r) / (cmax - cmin);
            double greenc = (cmax - g) / (cmax - cmin);
            double bluec = (cmax - b) / (cmax - cmin);
            if (r == cmax)
                hue = bluec - greenc;
            else if (g == cmax)
                hue = 2.0 + redc - bluec;
            else
                hue = 4.0 + greenc - redc;
            hue = hue / 6.0;
            if (hue < 0)
                hue = hue + 1.0;
        }
        hsbvals[0] = hue * 360;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }
    
    private static double calculateBrightness(Color c) {
        double red = c.getRed();
        double green =  c.getGreen();
        double blue =  c.getBlue();
        
        return Math.sqrt(
                red * red * 0.241
                + green * green * 0.691
                + blue * blue * 0.068);
    }

    public static Color deriveColor(Color c, double brightness) {
        double baseBrightness = calculateBrightness(c);
        double calcBrightness = brightness;
        
        // Fine adjustments to colors in ranges of brightness to adjust the contrast for them
        if (brightness > 0) {
            if (baseBrightness > 0.85) {
                calcBrightness = calcBrightness * 1.6;
            } else if (baseBrightness > 0.6) {
                // no change
            } else if (baseBrightness > 0.5) {
                calcBrightness = calcBrightness * 0.9;
            } else if (baseBrightness > 0.4) {
                calcBrightness = calcBrightness * 0.8;
            } else if (baseBrightness > 0.3) {
                calcBrightness = calcBrightness * 0.7;
            } else {
                calcBrightness = calcBrightness * 0.6;
            }
            
        } else {
            if (baseBrightness < 0.2) {
                calcBrightness = calcBrightness * 0.6;
            }
        }

        // clamp brightness
        if (calcBrightness < -1) {
            calcBrightness = -1;
        } else if (calcBrightness > 1) {
            calcBrightness = 1;
        }

        // window two take the calculated brightness multiplier and derive color based on source color
        double[] hsb = null;
        hsb = RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue());
        // change brightness
        if (calcBrightness > 0) { // brighter
            hsb[1] *= 1 - calcBrightness;
            hsb[2] += (1 - hsb[2]) * calcBrightness;
        } else { // darker
            hsb[2] *= calcBrightness + 1;
        }
        
        // clip saturation and brightness
        if (hsb[1] < 0) {
            hsb[1] = 0;
        } else if (hsb[1] > 1) {
            hsb[1] = 1;
        }
        if (hsb[2] < 0) {
            hsb[2] = 0;
        } else if (hsb[2] > 1) {
            hsb[2] = 1;
        }

      
        // return hsb
        return Color.hsb((int) hsb[0], hsb[1], hsb[2], c.getOpacity());
    }

    private static double round(double value) {
        double doubleRounded = Math.round(value * 100);
        return doubleRounded;
    }
}
