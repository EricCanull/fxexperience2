/*
 * Permissions of this Copyleft license are conditioned on making available 
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.controller;

import com.fxexperience.javafx.scene.control.DoubleField;
import com.fxexperience.javafx.scene.control.IntegerField;
import com.fxexperience.javafx.scene.control.paintpicker.DoubleTextField;
import com.fxexperience.javafx.scene.control.paintpicker.PaintPicker;
import com.fxexperience.javafx.scene.control.popup.ColorPopupEditor;
import com.fxexperience.tools.util.Gradient;
import com.fxexperience.tools.util.PropertyValue;
import com.fxexperience.tools.util.StringUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StylerController extends SplitPane {

    @FXML private SplitPane rootSplitPane;
    @FXML private BorderPane previewPane;

    @FXML private GridPane textGridPanel;
    @FXML private GridPane sizeGridPanel;
    @FXML private GridPane simpleGridPane;

    @FXML private ChoiceBox<String> fontChoiceBox;
    @FXML private ComboBox<Gradient> gradientComboBox;

    @FXML private Slider fontSizeSlider;
    @FXML private Slider paddingSlider;
    @FXML private Slider borderWidthSlider;
    @FXML private Slider borderRadiusSlider;
    @FXML private Slider topHighlightSlider;
    @FXML private Slider bottomHighlightSlider;
    @FXML private Slider bodyTopSlider;
    @FXML private Slider bodyTopMiddleSlider;
    @FXML private Slider bodyBottomMiddleSlider;
    @FXML private Slider bodyBottomSlider;
    @FXML private Slider borderSlider;
    @FXML private Slider shadowSlider;
    @FXML private Slider inputBorderSlider;

    @FXML private DoubleTextField fontTextField;
    @FXML private DoubleTextField paddingTextField;
    @FXML private DoubleTextField borderWidthTextField;
    @FXML private DoubleTextField borderRadiusTextField;

    @FXML private ToggleButton baseTextToggle;
    @FXML private ToggleButton backgroundTextToggle;
    @FXML private ToggleButton fieldTextToggle;
    @FXML private ToggleButton topMiddleToggle;
    @FXML private ToggleButton bottomMiddleToggle;
    @FXML private ToggleButton borderToggle;
    @FXML private ToggleButton shadowToggle;
    @FXML private ToggleButton inputBorderToggle;

    private PreviewController previewPanel;

    private final ColorPopupEditor basePicker = new ColorPopupEditor(PaintPicker.Mode.SINGLE, Color.web("#D0D0D0"));
    private final ColorPopupEditor backgroundColorPicker = new ColorPopupEditor(PaintPicker.Mode.SINGLE, Color.web("#f4f4f4"));
    private final ColorPopupEditor focusColorPicker = new ColorPopupEditor(PaintPicker.Mode.SINGLE, Color.web("#0093ff"));
    private final ColorPopupEditor textColorPicker = new ColorPopupEditor(PaintPicker.Mode.SINGLE, Color.web("#000000"));
    private final ColorPopupEditor bkgdTextColorPicker = new ColorPopupEditor(PaintPicker.Mode.SINGLE, Color.web("#000000"));
    private final ColorPopupEditor fieldBackgroundPicker = new ColorPopupEditor(PaintPicker.Mode.SINGLE, Color.web("#ffffff"));
    private final ColorPopupEditor fieldTextColorPicker = new ColorPopupEditor(PaintPicker.Mode.SINGLE, Color.web("#000000"));

    public StylerController() {
        initialize();
    }

    private void initialize() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StylerController.class.getResource("/fxml/FXMLStylerPanel.fxml")); //NOI18N
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(StylerController.class.getName()).log(Level.SEVERE, null, ex);
        }

        previewPanel = new PreviewController();
        previewPane.setCenter(previewPanel);

        addListeners();

        // populate fonts choicebox
        fontChoiceBox.getItems().setAll(Font.getFamilies());
        fontChoiceBox.getSelectionModel().select("System");

        // create Integer Fields
        addTextFieldBinding(fontSizeSlider, fontTextField);
        addTextFieldBinding(paddingSlider, paddingTextField);
        addTextFieldBinding(borderWidthSlider, borderWidthTextField);
        addTextFieldBinding(borderRadiusSlider, borderRadiusTextField);


        // Add color pickers Title Pane
        simpleGridPane.getChildren().addAll(basePicker, backgroundColorPicker, focusColorPicker,
                textColorPicker, fieldBackgroundPicker, fieldTextColorPicker, bkgdTextColorPicker);

        // Set color pickers grid constraints
        GridPane.setConstraints(basePicker, 1, 0, 2, 1);
        GridPane.setConstraints(textColorPicker, 1, 1);
        GridPane.setConstraints(backgroundColorPicker, 1, 2);
        GridPane.setConstraints(bkgdTextColorPicker, 1, 3);
        GridPane.setConstraints(fieldBackgroundPicker, 1, 4);
        GridPane.setConstraints(fieldTextColorPicker, 1, 5);
        GridPane.setConstraints(focusColorPicker, 1, 6);


        // Populate gradient combo
        gradientComboBox.getItems().addAll(Gradient.GRADIENTS);
        gradientComboBox.setCellFactory((ListView<Gradient> gradientList) -> {
            ListCell<Gradient> cell = new ListCell<Gradient>() {
                @Override protected void updateItem(Gradient gradient, boolean empty) {
                    super.updateItem(gradient, empty);
                    if (empty || gradient == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(gradient.getName());
                        Region preview = new Region();
                        preview.setPrefSize(30, 30);
                        preview.setStyle("-fx-border-color: #111; -fx-background-color: " + gradient.getCss());
                        setGraphic(preview);
                    }
                }
            };
            cell.setStyle("-fx-cell-size: 40;");
            return cell;
        });

        gradientComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Gradient> arg0, Gradient arg1, Gradient newGradient) -> {
            bodyTopSlider.setValue(newGradient.getTopDerivation());
            bodyBottomSlider.setValue(newGradient.getBottomDerivation());
            if (newGradient.isShinny()) {
                topMiddleToggle.setSelected(true);
                bottomMiddleToggle.setSelected(true);
                bodyTopMiddleSlider.setValue(newGradient.getTopMidDerivation());
                bodyBottomMiddleSlider.setValue(newGradient.getBottomMidDerivation());
            } else {
                topMiddleToggle.setSelected(false);
                bottomMiddleToggle.setSelected(false);
            }
            updateCSS();
        });

        gradientComboBox.getSelectionModel().select(0);

        bodyTopMiddleSlider.disableProperty().bind(topMiddleToggle.selectedProperty().not());
        bodyBottomMiddleSlider.disableProperty().bind(bottomMiddleToggle.selectedProperty().not());
        borderSlider.disableProperty().bind(borderToggle.selectedProperty().not());
        shadowSlider.disableProperty().bind(shadowToggle.selectedProperty().not());
        inputBorderSlider.disableProperty().bind(inputBorderToggle.selectedProperty().not());
    }

    private final ChangeListener<Paint> onPaintChanged = ((ov, oldValue, newValue) -> updateCSS());


    private void addListeners() {
        // create listener to call update css

        AtomicReference<ChangeListener<Object>> updateCssListener =
                new AtomicReference<>((ObservableValue<?> arg0, Object arg1, Object arg2) ->
                        updateCSS());

        // add listeners to call update css
        fontChoiceBox.valueProperty().addListener(updateCssListener.get());
        fontSizeSlider.valueProperty().addListener(updateCssListener.get());
        paddingSlider.valueProperty().addListener(updateCssListener.get());
        borderRadiusSlider.valueProperty().addListener(updateCssListener.get());
        borderWidthSlider.valueProperty().addListener(updateCssListener.get());

        basePicker.getRectangle().fillProperty().addListener(onPaintChanged);
        backgroundColorPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        focusColorPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        textColorPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        baseTextToggle.selectedProperty().addListener(updateCssListener.get());
        textColorPicker.disableProperty().bind(baseTextToggle.selectedProperty().not());
        fieldBackgroundPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        fieldTextColorPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        fieldTextToggle.selectedProperty().addListener(updateCssListener.get());
        fieldTextColorPicker.disableProperty().bind(fieldTextToggle.selectedProperty().not());
        bkgdTextColorPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        backgroundTextToggle.selectedProperty().addListener(updateCssListener.get());
        bkgdTextColorPicker.disableProperty().bind(backgroundTextToggle.selectedProperty().not());


        topHighlightSlider.valueProperty().addListener(updateCssListener.get());
        bottomHighlightSlider.valueProperty().addListener(updateCssListener.get());
        bodyTopSlider.valueProperty().addListener(updateCssListener.get());
        bodyTopMiddleSlider.valueProperty().addListener(updateCssListener.get());
        bodyBottomMiddleSlider.valueProperty().addListener(updateCssListener.get());
        bodyBottomSlider.valueProperty().addListener(updateCssListener.get());
        borderSlider.valueProperty().addListener(updateCssListener.get());
        shadowSlider.valueProperty().addListener(updateCssListener.get());
        inputBorderSlider.valueProperty().addListener(updateCssListener.get());

        // Advanced toggle buttons
        topMiddleToggle.selectedProperty().addListener(updateCssListener.get());
        bottomMiddleToggle.selectedProperty().addListener(updateCssListener.get());
        borderToggle.selectedProperty().addListener(updateCssListener.get());
        shadowToggle.selectedProperty().addListener(updateCssListener.get());
        inputBorderToggle.selectedProperty().addListener(updateCssListener.get());
    }

    /**
     * Bind Slider values to the text fields
     */
    private void addTextFieldBinding(Slider slider, DoubleTextField field) {
        field.textProperty().bind(Bindings.format("%.2f", slider.valueProperty()));
    }

    private void updateCSS() {
       String css = createCSS();
       previewPanel.setPreviewPanelStyle(css);
    }

    private String createCSS() {

        int fontSize = (int) fontSizeSlider.getValue();
        int borderWidth = (int) borderWidthSlider.getValue();
        int borderWidthForPadding = (borderWidth <= 1) ? 0 : borderWidth - 1;
        int padding = (int) paddingSlider.getValue() + borderWidthForPadding;
        int borderRadius = (int) borderRadiusSlider.getValue();
        double checkPadding = (((0.25 * fontSize) + borderWidthForPadding) / fontSize);
        double radioPadding = (((0.333333 * fontSize) + borderWidthForPadding) / fontSize);

        StringBuilder cssBuffer = new StringBuilder();

        cssBuffer.append(".root {\n");
        //cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: " + fontSizeSlider.getValue() + "px " + "\"" + fontChoiceBox.getValue() + "\";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: " + "\"" + fontChoiceBox.getValue() + "\";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-font-size: " + fontSizeSlider.getValue() + "px;", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-base: " + basePicker.getColorString() + ";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-background: " + backgroundColorPicker.getColorString() + ";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-focus-color: " + focusColorPicker.getColorString() + ";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-control-inner-background: " + fieldBackgroundPicker.getColorString() + ";", true, 4));

        if (baseTextToggle.isSelected()) {
            baseTextToggle.setText("ON");
            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-base-color: "
                    + textColorPicker.getColorString()+ ";", true, 4));
        } else {
             baseTextToggle.setText("AUTO");
        }
        if (backgroundTextToggle.isSelected()) {
            backgroundTextToggle.setText("ON");
            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-background-color: "
                    + bkgdTextColorPicker.getColorString() + ";", true, 4));
        } else {
             backgroundTextToggle.setText("AUTO");
        }
        if (fieldTextToggle.isSelected()) {
             fieldTextToggle.setText("ON");
            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-inner-color: "
                    + fieldTextColorPicker.getColorString() + ";", true, 4));
        } else {
             fieldTextToggle.setText("AUTO");
        }

        double innerTopDerivation = bodyTopSlider.getValue()
                + ((100 - bodyTopSlider.getValue()) * (topHighlightSlider.getValue() / 100));
        double innerBottomDerivation = bodyBottomSlider.getValue()
                + ((100 - bodyBottomSlider.getValue()) * (bottomHighlightSlider.getValue() / 100));

        cssBuffer.append(StringUtil.padWithSpaces("-fx-inner-border: linear-gradient(to bottom, " + "derive(-fx-color," + innerTopDerivation + "%) 0%, "
                + "derive(-fx-color," + innerBottomDerivation + "%) 100%);", true, 4));

        cssBuffer.append(StringUtil.padWithSpaces("-fx-body-color: linear-gradient( to bottom, ", false, 4));
        cssBuffer.append(StringUtil.padWithSpaces("derive(-fx-color, "
                + bodyTopSlider.getValue() + "%) 0%, ", false, 0));

        if (topMiddleToggle.isSelected()) {
            topMiddleToggle.setText("ON");
            cssBuffer.append(StringUtil.padWithSpaces("derive(-fx-color, "
                    + bodyTopMiddleSlider.getValue() + "%) 50%, ", false, 0));
        } else {
            topMiddleToggle.setText("AUTO");
        }

        if (bottomMiddleToggle.isSelected()) {
            bottomMiddleToggle.setText("ON");
            cssBuffer.append(StringUtil.padWithSpaces("derive(-fx-color, "
                    + bodyBottomMiddleSlider.getValue() + "%) 50.5%, ", false, 0));
        } else {
            bottomMiddleToggle.setText("AUTO");
        }

        cssBuffer.append(StringUtil.padWithSpaces("derive(-fx-color, "
                + bodyBottomSlider.getValue() + "%) 100%);",true, 0));

        if (borderToggle.isSelected()) {
            borderToggle.setText("ON");
            cssBuffer.append(StringUtil.padWithSpaces("-fx-outer-border: derive(-fx-color,"
                    + borderSlider.getValue() + "%);", true, 4));
        } else {
            borderToggle.setText("AUTO");
        }

        if (shadowToggle.isSelected()) {
            shadowToggle.setText("ON");
            cssBuffer.append(StringUtil.padWithSpaces("-fx-shadow-highlight-color: derive(-fx-background,"
                    + shadowSlider.getValue() + "%);", true, 4));
        } else {
            shadowToggle.setText("AUTO");
        }

        if (inputBorderToggle.isSelected()) {
            inputBorderToggle.setText("ON");
            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-box-border: derive(-fx-background,"
                    + inputBorderSlider.getValue() + "%);", true, 4));
        } else {
            inputBorderToggle.setText("AUTO");
        }

        cssBuffer.append("}\n");
        cssBuffer.append(".button, .toggle-button, .choice-box {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-background-radius: "
                + borderRadius + ", "
                + borderRadius + ", "
                + (borderRadius - 1) + ", "
                + (borderRadius - 2) + ";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: "
                + padding + "px "
                + (padding + 7) + "px "
                + padding + "px "
                + (padding + 7) + "px;", true, 4));
        cssBuffer.append("}\n");

        cssBuffer.append(".menu-button {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-background-radius: " + borderRadius + ", "
                + borderRadius + ", "
                + (borderRadius - 1) + ", "
                + (borderRadius - 2) + ";", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button .label {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px " + (padding + 15) + "px " + padding + "px " + (padding + 7) + "px;", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button .arrow-button {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px " + (padding + 3) + "px " + padding + "px 0px;", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".choice-box {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: 0 " + (padding + 3) + "px 0 0;", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".choice-box .label {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px " + (padding + 1) + "px " + padding + "px " + (padding + 3) + "px;", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".choice-box .open-button {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: 1 0 0 " + (padding + 5) + "px;", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".combo-box-base:editable .text-field, .combo-box-base .arrow-button, " + ".combo-box .list-cell {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px "  + (padding + 3) + "px " + padding + "px " + (padding + 3) + "px;", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".check-box .box {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + checkPadding + "em;", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".radio-button .radio {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + radioPadding + "em;", true, 4));
        cssBuffer.append("}\n");
        if (!baseTextToggle.isSelected()) {
            cssBuffer.append(".hyperlink {\n");
            cssBuffer.append(StringUtil.padWithSpaces("-fx-fill: -fx-text-background-color;", true, 4));
            cssBuffer.append("}\n");
            cssBuffer.append(".toggle-button:selected {\n");
            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;", true, 4));
            cssBuffer.append("}\n");
        }
        cssBuffer.append(".label, .check-box, Text, .radio-button {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: -fx-font-type; -fx-fill: -fx-text-background-color; -fx-fx-text-fill: -fx-text-background-color;", true, 4));
        cssBuffer.append("}\n");
         cssBuffer.append(".text {\n");

        cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: \"" + fontChoiceBox.getValue() + "\";", true, 4));
        cssBuffer.append("}\n");

        cssBuffer.append(".button, .toggle-button, .check-box .box, .radio-button .radio, "
                + ".choice-box, .menu-button, .tab, .combo-box-base {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-background-insets: 0 0 -1 0, 0, "
                + borderWidth + ", " + (borderWidth + 1) + ";", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".button:focused, .toggle-button:focused, .check-box:focused .box, "
                + ".radio-button:focused .radio, .choice-box:focused, .menu-button:focused, "
                + ".combo-box-base:focused {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-background-insets: -1.4, 0, "
                + borderWidth + ", " + (borderWidth + 1) + ";", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".combo-box-base .arrow-button {\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-background-insets: 0, "
                + borderWidth + ", " + (borderWidth + 1) + ";", true, 4));
        cssBuffer.append("}\n");

        cssBuffer.append(".choice-box .label { /* Workaround for RT-20015 */\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;", true, 4));
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button .label { /* Workaround for RT-20015 */\n");
        cssBuffer.append(StringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;", true, 4));
        cssBuffer.append("}");


        return cssBuffer.toString();
    }

    public String getCodeOutput() {
     return createCSS();
    }


    public void startAnimations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public void stopAnimations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

