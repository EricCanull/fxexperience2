/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger
 * work using the licensed work through interfaces provided by the licensed
 * work may be distributed under different terms and without source code
 * for the larger work.
 */
package com.fxexperience.tools.controller;


import com.fxexperience.javafx.scene.control.colorpicker.ColorPickerTool;
import com.fxexperience.javafx.scene.control.paintpicker.PaintPicker;
import com.fxexperience.javafx.scene.control.popup.PopupEditor;
import com.fxexperience.javafx.util.encoders.ColorEncoder;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerivationController extends BorderPane {

    public final static int INDEX_POS = 2;

    @FXML private AnchorPane anchorPane;
    @FXML private GridPane gradientGridPane;
    @FXML private Label forwardDerivationLabel;
    @FXML private Slider derivationSlider;
    @FXML private Label derivedResultLabel;
    @FXML private Label reverseDerivationLabel;
    @FXML private Label reverseResultLabel;
    @FXML private Button fxButton;
    @FXML private Rectangle gradientSquare;
    @FXML private Circle gradientCircle;
   // @FXML private ImageView alert;
    @FXML private ColorPickerTool baseColorPicker;
    @FXML private ColorPickerTool desiredColorPicker;
    private Region reverseResultColor;
    private final PopupEditor gradientTextColorPicker = new PopupEditor(PaintPicker.Mode.COLOR, Color.web("#000000"));
    @FXML private TextArea derivationTextArea;
    @FXML private TextArea gradientCSSText;

    private DecimalFormat df = new DecimalFormat("#.###");

    public DerivationController() {
        initialize();
    }

    private void initialize() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(DerivationController.class.getResource("/fxml/FXMLDerivationPanel.fxml")); //NOI18N
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(DerivationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        derivationTextArea.setText(getInfoText());

        gradientGridPane.add(gradientTextColorPicker, 1, 0);


        final ChangeListener<Paint> onPaintChanged = (ov, oldValue, newValue) -> {
        if (newValue instanceof Color || newValue instanceof LinearGradient) {
            updateGradientCSS();
        }};


        gradientTextColorPicker.getRectangle().fillProperty().addListener(onPaintChanged);

        df.setRoundingMode(RoundingMode.CEILING);

        // Set the socket values whenever the range changes
        derivationSlider.valueProperty().addListener((o) ->
                       forwardDerivationLabel.setText(df.format(derivationSlider.getValue())));

//        // FORWARD
//        forwardDerivationLabel.textProperty().bind(new StringBinding() {
//            { bind(derivationSlider.valueProperty()); }
//            @Override protected String computeValue() {
//                return df.format(derivationSlider.getValue());
//            }
//        });

        Region derivedResultColor = new Region();
        derivedResultColor.setPrefSize(50, 20);
        derivedResultLabel.setGraphic(derivedResultColor);
        derivedResultColor.styleProperty().bind(new StringBinding() {
            { bind(derivationSlider.valueProperty(),baseColorPicker.colorProperty()); }
            @Override protected String computeValue() {
                return "-fx-border-color: #606060;" +
                       " -fx-background-color: derive("+baseColorPicker.getWebColor() +", " +
                        df.format(derivationSlider.getValue())+"%);";
            }
        });

        derivedResultLabel.textProperty().bind(new StringBinding() {
            { bind(derivationSlider.valueProperty(),baseColorPicker.colorProperty()); }
            @Override protected String computeValue() {
                Color base = baseColorPicker.getColor();
                double derivation = derivationSlider.getValue();
                Color result = ColorEncoder.deriveColor(base, derivation/100);
                return getColorString(result);
            }
        });

        // BACKWARD
        reverseResultColor = new Region();
        reverseResultColor.setPrefSize(50, 20);
        reverseResultLabel.setGraphic(reverseResultColor);
        ChangeListener<Color> updateReverse = (ObservableValue<? extends Color> ov, Color t, Color desiredColor) -> updateReverse();
        baseColorPicker.colorProperty().addListener(updateReverse);
        desiredColorPicker.colorProperty().addListener(updateReverse);
        baseColorPicker.colorProperty().setValue(Color.web("#BBBBBB"));
        desiredColorPicker.colorProperty().setValue(Color.web("#C3C3C3"));
    }

    public void updateGradientCSS() {
        setGradientStyles(gradientTextColorPicker.getGradientString());
        gradientCSSText.setText(gradientTextColorPicker.getGradientString());
    }
    
    private void updateReverse() {
        Color desiredColor = desiredColorPicker.getColor();
        final Color base = baseColorPicker.getColor();
//                System.out.println("base = " + base);
        double desiredBrightness = desiredColor.getBrightness();
//                System.out.println("desiredBrightness = " + desiredBrightness);
    //    double desiredSaturation = desiredColor.getSaturation();
//                System.out.println("desiredSaturation = " + desiredSaturation);
        double derivation = 0, max = 1, min = -1;
        Color derivedColor = Color.WHITE;
        for(int i=0; i< 100;i++){
//                    System.out.println("---------- "+i+" ----------------");
//                    System.out.println("derivation = " + derivation);
//                    System.out.println("max = " + max);
//                    System.out.println("min = " + min);
            derivedColor = ColorEncoder.deriveColor(base, derivation);
            double derivedBrightness = derivedColor.getBrightness();
//                    System.out.println("derivedBrightness = " + derivedBrightness);
        //    double derivedSaturation = derivedColor.getSaturation();
//                    System.out.println("derivedSaturation = " + derivedSaturation);
      //     double saturationDifference = Math.abs(derivedSaturation-desiredSaturation);
//                    System.out.println("saturationDifference = " + saturationDifference);
            double difference = Math.abs(derivedBrightness-desiredBrightness);
//                    System.out.println("brightness difference = " + difference);
            if (difference < 0.0001) { // GOOD ENOUGH
                break;
            } else if(min == 1 || max == -1) { // TOO DIFFERENT
                break;
            } else if(derivedBrightness > desiredBrightness) { // TOO BRIGHT
//                        System.out.println("NEED DARKER");
                max = derivation;
                derivation = derivation + ((min-derivation)/2);
            } else { // TO DARK
//                        System.out.println("NEED BRIGHTER");
                min = derivation;
                derivation = derivation + ((max-derivation)/2);
            }
        }

//       System.out.println("\nFINAL \nderivation = " + derivation+"\n\n");
        reverseDerivationLabel.setText(df.format(derivation));
        reverseResultLabel.setText(getColorString(derivedColor));
        reverseResultColor.setStyle("-fx-border-color: #606060; "
                + "-fx-background-color: "+getWebColor(derivedColor) +";");

       // alert.setVisible(!getWebColor(desiredColor).equals(getWebColor(derivedColor)));
    }
    
    private static String getColorString(Color color) {
        final int red = (int) (color.getRed()*255);
        final int green = (int)(color.getGreen()*255);
        final int blue = (int)(color.getBlue()*255);
        return String.format("#%02X%02X%02X R:%d G:%d B:%d", red, green, blue, red, green, blue);
    }
    
    private static String getWebColor(Color color) {
        final int red = (int)(color.getRed()*255);
        final int green = (int)(color.getGreen()*255);
        final int blue = (int)(color.getBlue()*255);
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public void setGradientStyles(String style){
        this.gradientSquare.setStyle("-fx-fill: " + style);
        this.gradientCircle.setStyle("-fx-fill: " + style);
        this.fxButton.setStyle("-fx-background-color: " + style);
    }

    private String getInfoText() {
        String info = "Derived colors create a lighter or darker version of a source" +
                "base color. It is specified as a percentage with positive being lighter" +
                "and negative darker.\n\n" +
                "Forward conversion lets you preview what resulting " +
                "color you get for a given derivation percentage.\n\n" +
                "Reverse Conversion helps you calculate what the derivation" +
                "is for a given base color and required resulting color.";
        return info;
    }

    public String getCodeOutput() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void startAnimations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void stopAnimations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
