package com.fxexperience.tools.controller;

import com.fxexperience.javafx.scene.control.paintpicker.PaintPicker;
import com.fxexperience.javafx.scene.control.popup.ColorPopupEditor;
import com.fxexperience.javafx.util.encoders.ColorEncoder;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColorController extends HBox {

    @FXML private HBox base_container, desired_container;
    @FXML public Label base_label, desired_label, result_label;
    @FXML public Region base_rect, desired_rect;
    @FXML public Region reverseResultColor;
    @FXML public TextField derivation_textfield;

    @FXML public Slider derivation_slider;

    private ColorPopupEditor basePicker, desiredPicker;
    private String percent = "0";
    private final DecimalFormat df = new DecimalFormat("##.###");
    private final NumberFormat percentFormat = NumberFormat.getPercentInstance();

    public ColorController(Color color) {
        initialize(color);

    }

    /* Initializes the controller class. */
    private void initialize(Color color) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SplineController.class.getResource("/fxml/FXMLColorPanel.fxml")); //NOI18N
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(PreviewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Set the socket values whenever the range changes
        derivation_slider.valueProperty().addListener((o) ->
                setDerivationPercent(derivation_slider.getValue()));

        basePicker = new ColorPopupEditor(PaintPicker.Mode.SINGLE, color);
        basePicker.setPrefWidth(200);

        desiredPicker = new ColorPopupEditor(PaintPicker.Mode.SINGLE, color);
        desiredPicker.setPrefWidth(200);

        base_container.getChildren().add(basePicker);
        desired_container.getChildren().add(desiredPicker);

        df.setRoundingMode(RoundingMode.CEILING);
        percentFormat.setMaximumFractionDigits(3);
        setDerivationPercent(0d);
//        // FORWARD
        base_rect.styleProperty().bind(new StringBinding() {
            {
                bind(derivation_slider.valueProperty(), basePicker.getRectangle().fillProperty());
            }

            @Override
            protected String computeValue() {
                return "-fx-background-color: derive(" + ColorEncoder.getWebColor((Color) basePicker.getPaint()) + ", " + derivation_slider.getValue() + "%);";
            }
        });

        desired_label.textProperty().bind(new StringBinding() {
            {
                bind(derivation_slider.valueProperty(), basePicker.getRectangle().fillProperty());
            }

            @Override
            protected String computeValue() {
                Color base = (Color) basePicker.getPaint();
                double derivation = derivation_slider.getValue();
                Color result = ColorEncoder.deriveColor(base, derivation / 100);
                return ColorEncoder.encodeColor(result);
            }
        });

        // BACKWARD
      //  ChangeListener<Paint> updateReverse = (ov, t, desiredColor) -> updateReverse();
     //   basePicker.getRectangle().fillProperty().addListener(updateReverse);
      //  desiredPicker.getRectangle().fillProperty().addListener(updateReverse);


        //final ChangeListener<Paint> onPaintChanged = ((ov, oldValue, newValue) -> setColorValues((Color) newValue));
    }


//    private void updateReverse() {
//        Color desiredColor = (Color) desiredPicker.getPaint();
//        final Color base = (Color) basePicker.getPaint();
////                System.out.println("base = " + base);
//        double desiredBrightness = desiredColor.getBrightness();
////                System.out.println("desiredBrightness = " + desiredBrightness);
//        double desiredSaturation = desiredColor.getSaturation();
////                System.out.println("desiredSaturation = " + desiredSaturation);
//        double derivation = 0, max = 1, min = -1;
//        Color derivedColor = Color.WHITE;
//        for(int i=0; i< 100;i++){
////                    System.out.println("---------- "+i+" ----------------");
////                    System.out.println("derivation = " + derivation);
////                    System.out.println("max = " + max);
////                    System.out.println("min = " + min);
//            derivedColor = ColorEncoder.deriveColor(base, derivation);
//            double derivedBrightness = derivedColor.getBrightness();
////                    System.out.println("derivedBrightness = " + derivedBrightness);
//            double derivedSaturation = derivedColor.getSaturation();
////                    System.out.println("derivedSaturation = " + derivedSaturation);
//            double saturationDifference = Math.abs(derivedSaturation-desiredSaturation);
////                    System.out.println("saturationDifference = " + saturationDifference);
//            double difference = Math.abs(derivedBrightness-desiredBrightness);
////                    System.out.println("brightness difference = " + difference);
//            if (difference < 0.0001) { // GOOD ENOUGH
//                break;
//            } else if(min == 1 || max == -1) { // TO DIFFERENT
//                break;
//            } else if(derivedBrightness > desiredBrightness) { // TO BRIGHT
////                        System.out.println("NEED DARKER");
//                max = derivation;
//                derivation = derivation + ((min-derivation)/2);
//            } else { // TO DARK
////                        System.out.println("NEED BRIGHTER");
//                min = derivation;
//                derivation = derivation + ((max-derivation)/2);
//            }
//        }
//
////                System.out.println("\nFINAL \nderivation = " + derivation+"\n\n");
//       // result_label.setText(String.format("%3.3f%%", derivation));
//        desired_label.setText(ColorEncoder.encodeColor(derivedColor));
//       desired_rect.setStyle("-fx-background-color: "+ColorEncoder.getWebColor(derivedColor) +";");
//
//      //  alert.setVisible(!getWebColor(desiredColor).equals(getWebColor(derivedColor)));
//    }


    public void setColorValues(Object value) {
        assert value != null;

        Color color = (Color) value;

    }



    public void setDerivationPercent(double percentage) {

        this.percent = df.format(percentage);
        this.derivation_textfield.setText("derive(" + basePicker.getColorString() + ", " + percent + "%)");
    }

    public Slider getDerivationSlider() {
        return derivation_slider;
    }

    public Paint getPaintProperty() {
        return basePicker.getPaint();
    }

    public void setDerived(String text, String style) {
       // derived_label.setText(text);
        base_rect.setStyle(style);
    }


    public Region getColor_rect() {
        return base_rect;
    }

    public void setDerivation(Color derivedColor) {
//        label_3.setText(ColorEncoder.getColorString(derivedColor));
//        label_3.setStyle("-fx-border-color: black; "
//                + "-fx-background-color: " + ColorEncoder.getWebColor(derivedColor) + ";");
//        this.derivation_rect.setStyle("-fx-fill: derive(" + colorPicker.getColorString() + ", " + percent + "%)");
//        //this.derivation_rect.fillProperty().set(value);
    }

    @FXML
    private void onColorRectSelected(MouseEvent event) {
        if (event.getSource() != null) {
            String source = event.getSource().toString();
            switch (source) {
                case "Region[id=base_rect]":
                    //setColorValues(base_rect.getStyle()
                    break;
                case "Region[id=desired_rect]":
                    setColorValues(desired_rect.getShape().fillProperty().getValue());
                    break;
            }
        }
    }
}
