/*
 * Permissions of this Copyleft license are conditioned on making available
 * complete source code of licensed works and modifications under the same
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger
 * work using the licensed work through interfaces provided by the licensed
 * work may be distributed under different terms and without source code
 * for the larger work.
 */
package com.fxexperience.tools.controllers;

import com.paintpicker.scene.control.picker.PaintPicker;
import com.paintpicker.scene.control.picker.mode.Mode;
import com.paintpicker.utils.ColorEncoder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerivationController extends AnchorPane {

    @FXML private AnchorPane anchorPane;
    @FXML private GridPane gridPane;

    @FXML private Slider derivationSlider;

    @FXML private Label forwardSliderLabel;
    @FXML private Label derivedResultLabel;
    @FXML private Label reverseDerivationLabel;
    @FXML private Label reverseResultLabel;

    @FXML Region forwardRegion;
    @FXML Region reverseRegion;

    @FXML private TextField rgbDerivedTextfield;
    @FXML private TextField hexDerivedTextfield;
    @FXML private TextField rgbReverseTextfield;
    @FXML private TextField hexReverseTextfield;
    @FXML private ImageView alert;

    private PaintPicker basePicker, desiredPicker;

    public DerivationController() {
        initialize();
    }

    private void initialize() {
        
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DerivationController.class.getResource("/fxml/FXMLDerivationPanel.fxml")); //NOI18N
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(GradientController.class.getName()).log(Level.SEVERE, null, ex);
        }

        basePicker = new PaintPicker(Color.GAINSBORO, Mode.SINGLE);
        basePicker.setPrefWidth(200);

        desiredPicker = new PaintPicker(Color.DEEPSKYBLUE, Mode.SINGLE);
        desiredPicker.setPrefWidth(200);

        basePicker.valueProperty().addListener(o -> updateForwardDerivation());
        desiredPicker.valueProperty().addListener(o -> updateReverseDerivation());
        derivationSlider.valueProperty().addListener(o -> updateForwardDerivation());

        GridPane.setConstraints(basePicker, 4, 1);
        GridPane.setConstraints(desiredPicker, 4, 5);
        gridPane.getChildren().addAll(basePicker, desiredPicker);
        
        updateForwardDerivation();
        updateReverseDerivation();
    }

     private void updateForwardDerivation() {
        double brightness = derivationSlider.getValue();

        Color forwardColorResult = ColorEncoder.deriveColor((Color) basePicker.getValue(), brightness);

        forwardRegion.setStyle("-fx-border-color: black; "
                 + "-fx-background-color: "+ ColorEncoder.getWebColor(forwardColorResult) +";");

        forwardSliderLabel.setText(String.format("%3.1f%%", brightness));
        hexDerivedTextfield.setText(ColorEncoder.encodeColor(forwardColorResult).toUpperCase());
        rgbDerivedTextfield.setText(rgbColor(forwardColorResult));
    }

    private void updateReverseDerivation() {
        Color desiredColor = (Color) desiredPicker.getValue();
        final Color base = (Color) basePicker.getValue();
//                System.out.println("base = " + base);
        double desiredBrightness = desiredColor.getBrightness();
//                System.out.println("desiredBrightness = " + desiredBrightness);
        //    double desiredSaturation = desiredColor.getSaturation();
//                System.out.println("desiredSaturation = " + desiredSaturation);
        double derivation = 0, max = 1, min = -1;
        Color derivedColor = Color.WHITE;
        for (int i = 0; i < 100; i++) {
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
            double difference = Math.abs(derivedBrightness - desiredBrightness);
//                    System.out.println("brightness difference = " + difference);
            if (difference < 0.0001) { // GOOD ENOUGH
                break;
            } else if (min == 1 || max == -1) { // TO DIFFERENT
                break;
            } else if (derivedBrightness > desiredBrightness) { // TO BRIGHT
//                        System.out.println("NEED DARKER");
                max = derivation;
                derivation = derivation + ((min - derivation) / 2);
            } else { // TO DARK
//                        System.out.println("NEED BRIGHTER");
                min = derivation;
                derivation = derivation + ((max - derivation) / 2);
            }
        }

//       System.out.println("\nFINAL \nderivation = " + derivation+"\n\n");
        reverseDerivationLabel.setText(String.format("%3.1f%%", derivation));
        hexReverseTextfield.setText(ColorEncoder.encodeColor(derivedColor).toUpperCase());
        rgbReverseTextfield.setText(rgbColor(derivedColor));
        reverseRegion.setStyle("-fx-border-color: black; "
                + "-fx-background-color: "+ ColorEncoder.getWebColor(derivedColor) +";");

//        alert.setVisible(!ColorEncoder.getWebColor(desiredColor).equals(ColorEncoder.getWebColor(derivedColor)));
    }

    private static String rgbColor(Color color) {
        final int r = (int) (color.getRed()*255);
        final int g = (int) (color.getGreen()*255);
        final int b = (int) (color.getBlue()*255);
        final double a = color.getOpacity();
        return String.format("rgba(\"%d, %d, %d, %1.1f\")", r ,g, b, a);
    }

    private static String derivationColor(Color color, double percent) {
        return String.format("derive(\"%s, %1.1f\")", ColorEncoder.encodeColor(color), percent);
    }
}
