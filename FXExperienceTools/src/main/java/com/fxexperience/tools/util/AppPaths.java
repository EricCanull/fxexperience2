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

public final class AppPaths {

    public static final String RESOURCE_BUNDLE = "bundle.appResources";
    public static final String CONTROLLER_PATH = "com/fxexperience/tools/controller/";
    public static final String STYLE_PATH = "/styles/";
    public static final String IMG_PATH = "images/";
    public static final String FXML_PATH ="/fxml/";
    
    public final static Integer STYLER_ID = 0;
    public final static String STYLER_FXML_PATH = FXML_PATH + "FXMLStylerPanel.fxml";
    public final static Integer SPLINE_ID = 1;
    public final static String SPLINE_FXML_PATH = FXML_PATH + "FXMLSplinePanel.fxml";
    public final static Integer DERIVED_ID = 2;
    public final static String DERIVED_FXML_PATH = FXML_PATH + "FXMLDerivationPanel.fxml";

    private AppPaths() {
    }
}
