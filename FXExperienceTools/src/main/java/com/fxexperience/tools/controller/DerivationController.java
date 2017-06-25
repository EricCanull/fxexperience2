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

import com.fxexperience.javafx.fxanimations.FadeInDownBigTransition;
import com.fxexperience.javafx.scene.control.paintpicker.PaintPicker;
import com.fxexperience.javafx.scene.control.popup.PopupEditor;
import com.fxexperience.javafx.util.encoders.ColorEncoder;
import javafx.animation.PauseTransition;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerivationController extends BorderPane {

    public final static int INDEX_POS = 2;

    @FXML private BorderPane rootPane;
    @FXML private AnchorPane anchorPane;
    @FXML private GridPane gradientGridPane;
    @FXML private GridPane derivationGrid;
    @FXML private Label forwardDerivationLabel;
    @FXML private Slider derivationSlider;
    @FXML private Label derivedResultLabel;
    @FXML private Label reverseDerivationLabel;
    @FXML private Label reverseResultLabel;
    @FXML private Button fxButton;
    @FXML private Rectangle gradientSquare;
    @FXML private Circle gradientCircle;
    @FXML private TextArea derivationTextArea;
    @FXML private TextArea derivationTextOutput;
    @FXML private TextArea gradientCSSText;

    private PopupEditor baseColorPicker;
    private PopupEditor desiredColorPicker;
    private Region reverseResultColor;
    private PopupEditor gradientTextColorPicker;
    private AlertController alert;

    private final DecimalFormat df = new DecimalFormat("##.###");
    private final NumberFormat percentFormat = NumberFormat.getPercentInstance();

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
        percentFormat.setMaximumFractionDigits(3);
        baseColorPicker = new PopupEditor(PaintPicker.Mode.SINGLE, Color.web("#BBBBBB"));
        baseColorPicker.setPrefWidth(140);

        desiredColorPicker = new PopupEditor(PaintPicker.Mode.SINGLE, Color.web("#C3C3C3"));
        baseColorPicker.setPrefWidth(140);

        gradientTextColorPicker = new PopupEditor(PaintPicker.Mode.COLOR, Color.web("#C3C3C3"));
        gradientTextColorPicker.setPrefWidth(200);

        derivationTextArea.setText(getInfoText());
        gradientGridPane.add(gradientTextColorPicker, 1, 0);
        derivationGrid.add(baseColorPicker, 1, 1);
        derivationGrid.add(desiredColorPicker, 1, 2);

        final ChangeListener<Paint> onPaintChanged = ((ov, oldValue, newValue) ->  updateGradientCSS());

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
            { bind(derivationSlider.valueProperty(), baseColorPicker.getRectangle().fillProperty()); }
            @Override protected String computeValue() {
                return "-fx-border-color: #606060;" +
                       " -fx-background-color: derive("+baseColorPicker.getColorString() +", " +
                        df.format(derivationSlider.getValue())+"%);";
            }
        });

        derivedResultLabel.textProperty().bind(new StringBinding() {
            { bind(derivationSlider.valueProperty(),baseColorPicker.getRectangle().fillProperty()); }
            @Override protected String computeValue() {
                Color base = (Color) baseColorPicker.getPaintProperty();
                double derivation = derivationSlider.getValue();
                Color result = ColorEncoder.deriveColor(base, derivation/100);
                return getColorString(result);
            }
        });

        // BACKWARD
        reverseResultColor = new Region();
        reverseResultColor.setPrefSize(50, 20);
        reverseResultLabel.setGraphic(reverseResultColor);
        ChangeListener<Paint> updateReverse = (ObservableValue<? extends Paint> ov, Paint t, Paint desiredColor) -> updateReverse();
        baseColorPicker.getRectangle().fillProperty().addListener(updateReverse);
        desiredColorPicker.getRectangle().fillProperty().addListener(updateReverse);
    }

    public void updateGradientCSS() {
        setGradientStyles(gradientTextColorPicker.getGradientString());
        gradientCSSText.setText(gradientTextColorPicker.getGradientString());
    }

    private void updateReverse() {
        Color desiredColor = (Color) desiredColorPicker.getPaintProperty();
        final Color base = (Color) baseColorPicker.getPaintProperty();
        double desiredBrightness = desiredColor.getBrightness();
        double derivation = 0, max = 1, min = -1;
        Color derivedColor = Color.WHITE;
        for (int i = 0; i < 100; i++) {
            derivedColor = ColorEncoder.deriveColor(base, derivation);
            double derivedBrightness = derivedColor.getBrightness();
            double difference = Math.abs(derivedBrightness - desiredBrightness);

            if (difference < 0.0001) { //GOOD ENOUGH
                break;
            } else if (min == 1 || max == -1) { //TOO DIFFERENT
                break;
            } else if (derivedBrightness > desiredBrightness) { //TOO BRIGHT
                max = derivation;
                derivation = derivation + ((min - derivation) / 2);
            } else { // TO DARK
                min = derivation;
                derivation = derivation + ((max - derivation) / 2);
            }
        }

        reverseDerivationLabel.setText(percentFormat.format(derivation));
        reverseResultLabel.setText(getColorString(derivedColor));
        reverseResultColor.setStyle("-fx-border-color: #606060; -fx-background-color: " +
                getWebColor(derivedColor) + ";");

        if (!getWebColor(desiredColor).equals(getWebColor(derivedColor))) {
            displayStatusAlert("Warning: The selected colors are too different to derive the same color.");
            alert.setDisplayActive(true);
        }
        else if (alert != null  && getWebColor(desiredColor).equals(getWebColor(derivedColor))) {
            alert.setDisplayActive(false);
            removeAlert(alert);
            alert = null;
        }
    }
    
    private static String getColorString(Color color) {
        final int red = (int) (color.getRed()*255);
        final int green = (int)(color.getGreen()*255);
        final int blue = (int)(color.getBlue()*255);
        return String.format("#%02X%02X%02X R:%d G:%d B:%d", red, green, blue, red, green, blue);
    }
    
    private static String getWebColor(Color color) {
        final int red =   (int) (color.getRed() * 255);
        final int green = (int) (color.getGreen() * 255);
        final int blue =  (int) (color.getBlue() * 255);
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    public void setGradientStyles(String style){
        this.gradientSquare.setStyle("-fx-fill: " + style);
        this.gradientCircle.setStyle("-fx-fill: " + style);
        this.fxButton.setStyle("-fx-background-color: " + style);
    }

    private String getInfoText() {
        return  "Derived colors create a lighter or darker version of a source " +
                "base color. It is specified as a percentage with positive being lighter " +
                "and negative darker.\n\n" +
                "Forward conversion lets you preview what resulting " +
                "color you get for a given derivation percentage.\n\n" +
                "Reverse Conversion helps you calculate what the derivation" +
                "is for a given base color and required resulting color.";
    }

    private void displayStatusAlert(String textMessage) {
        double prefWidth = rootPane.getBoundsInLocal().getWidth();
        if(alert == null) {
            alert = new AlertController(textMessage);
            alert.setOpacity(0);
            alert.setPanelWidth(prefWidth, this.INDEX_POS);

            rootPane.layoutXProperty().addListener((observable, oldValue, newValue) ->
                    alert.setPanelWidth(newValue.doubleValue(), this.INDEX_POS));

            alert.setTranslateY(rootPane.getLayoutY()+alert.getPrefHeight());

            AnchorPane.setTopAnchor(alert, 0d);
            anchorPane.getChildren().add(alert);

            new FadeInDownBigTransition(alert).play();
        } else {
            alert.setStatusDialog(textMessage);
        }
    }

    private void removeAlert(Node alert) {
        PauseTransition pauseTransition = new PauseTransition();
        pauseTransition.setDuration(Duration.seconds(0));
        pauseTransition.play();
        pauseTransition.setOnFinished((ActionEvent t) -> anchorPane.getChildren().remove(alert));
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
