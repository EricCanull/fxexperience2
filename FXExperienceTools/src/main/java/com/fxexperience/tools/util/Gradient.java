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
public class Gradient {
    private final String css;
    private final String name;
    private final double topDerivation;
    private final double topMidDerivation;
    private final double bottomMidDerivation;
    private final double bottomDerivation;
    private final boolean shinny;

    public Gradient(String name, double topDerivation, double bottomDerivation) {
        this.name = name;
        this.topDerivation = topDerivation;
        this.bottomDerivation = bottomDerivation;
        this.topMidDerivation = Double.NaN;
        this.bottomMidDerivation = Double.NaN;
        this.shinny = false;
        this.css = "linear-gradient( to bottom, derive(-fx-color, "+topDerivation+"%) 0%, derive(-fx-color, "+bottomDerivation+"%) 100%);";
    }

    public Gradient(String name, double topDerivation, double topMidDerivation, double bottomMidDerivation, double bottomDerivation) {
        this.name = name;
        this.topDerivation = topDerivation;
        this.topMidDerivation = topMidDerivation;
        this.bottomMidDerivation = bottomMidDerivation;
        this.bottomDerivation = bottomDerivation;
        this.shinny = true;
        this.css = "linear-gradient( to bottom, "
                + "derive(-fx-color, "+topDerivation+"%) 0%, "
                + "derive(-fx-color, "+topMidDerivation+"%) 50%, "
                + "derive(-fx-color, "+bottomMidDerivation+"%) 50.5%, "
                + "derive(-fx-color, "+bottomDerivation+"%) 100%);";
    }

    public String getCss() {
        return css;
    }

    public String getName() {
        return name;
    }

    public double getBottomDerivation() {
        return bottomDerivation;
    }

    public double getBottomMidDerivation() {
        return bottomMidDerivation;
    }

    public boolean isShinny() {
        return shinny;
    }

    public double getTopDerivation() {
        return topDerivation;
    }

    public double getTopMidDerivation() {
        return topMidDerivation;
    }

    @Override public String toString() {
        return name;
    }
    
    public static final Gradient[] GRADIENTS = new Gradient[]{
        new Gradient("Default",34,-18),
        new Gradient("Subtle",35,-6),
        new Gradient("Shinny",45,34,5,-10)
    };
}
