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

import com.fxexperience.javafx.scene.control.popup.ColorPopupEditor;
import com.fxexperience.javafx.scene.control.textfields.DoubleTextField;
import com.fxexperience.tools.ui.CSSCodeArea;
import com.fxexperience.tools.util.FileUtil;
import com.fxexperience.tools.util.Gradient;
import com.fxexperience.tools.util.StringUtil;
import com.fxexperience.tools.util.css.CSSBaseStyle;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.fxmisc.flowless.VirtualizedScrollPane;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.fxexperience.javafx.scene.control.paintpicker.PaintPicker.Mode;

public class StyleController extends VBox {

    @FXML private BorderPane previewPane;
    @FXML private StackPane editorPane;
    @FXML private GridPane simpleGridPane;

    @FXML private ChoiceBox<String> fontChoice;
    @FXML private ComboBox<String> fontSizeBox;
    @FXML private ComboBox<Gradient> gradientComboBox;
    @FXML private ToggleGroup fontRadioGroup;
    @FXML private RadioButton fixedFontRadio;
    @FXML private RadioButton scalableFontRadio;

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

    private final CSSCodeArea codeArea = new CSSCodeArea();
    private final PreviewController previewController = new PreviewController();

    private final ColorPopupEditor basePicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#213870"));
    private final ColorPopupEditor backgroundPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#2c2f33"));
    private final ColorPopupEditor focusPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#0093ff"));
    private final ColorPopupEditor textPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#000000"));
    private final ColorPopupEditor foregroundTextPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#000000"));
    private final ColorPopupEditor fieldBackgroundPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#23272a"));
    private final ColorPopupEditor fieldTextPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#000000"));

    private String[] styleCode = CSSBaseStyle.getCodeArray();

    public StyleController() {
        initialize();
    }

    private void initialize() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StyleController.class.getResource("/fxml/FXMLStylerPanel.fxml")); //NOI18N
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(StyleController.class.getName()).log(Level.SEVERE, null, ex);
        }


        editorPane.getChildren().add(new VirtualizedScrollPane<>(codeArea));
        previewPane.setCenter(previewController);

        addListeners();

        // populate fonts choicebox
        fontChoice.getItems().setAll(Font.getFamilies());
        fontSizeBox.getItems().setAll(getFixedFontSizes());

        fontSizeBox.setCellFactory(l -> new ListCell<String>() {
            @Override
            protected void updateItem(String text, boolean empty) {
                super.updateItem(text, empty);
                if (empty || text == null
                    || text.toLowerCase(Locale.ROOT).contains("d")
                    || text.toLowerCase(Locale.ROOT).contains("f")) {
                    setText("13");
                    return;
                }
                try {
                    setText(text);
                } catch (NumberFormatException e) {
                    Logger.getLogger(StyleController.class.getName()).log(Level.SEVERE, null, e);
                }
                buildRootStyle();
            }
        });
        fontSizeBox.getSelectionModel().select(4);

        fixedFontRadio.setUserData("px;");
        scalableFontRadio.setUserData("em;");
        fontRadioGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (old_toggle.equals(new_toggle)) {
                return;
            }
            if (fontRadioGroup.getSelectedToggle().equals(fixedFontRadio)) {
                fontSizeBox.getItems().setAll(getFixedFontSizes());
                fontSizeBox.getSelectionModel().select("13");
            } else {
                fontSizeBox.getItems().setAll(getScalableFontSizes());
                fontSizeBox.getSelectionModel().select("1");
            }
            buildRootStyle();
        });
        fontChoice.getSelectionModel().select("Roboto");

        // create Integer Fields
        addTextFieldBinding(paddingSlider, paddingTextField);
        addTextFieldBinding(borderWidthSlider, borderWidthTextField);
        addTextFieldBinding(borderRadiusSlider, borderRadiusTextField);

        // Add color pickers Title Pane
        simpleGridPane.getChildren().addAll(basePicker, backgroundPicker, focusPicker,
                textPicker, fieldBackgroundPicker, fieldTextPicker, foregroundTextPicker);

        // Set color pickers grid constraints
        GridPane.setConstraints(basePicker, 1, 0, 2, 1);
        GridPane.setConstraints(textPicker, 1, 1);
        GridPane.setConstraints(backgroundPicker, 1, 2);
        GridPane.setConstraints(foregroundTextPicker, 1, 3);
        GridPane.setConstraints(fieldBackgroundPicker, 1, 4);
        GridPane.setConstraints(fieldTextPicker, 1, 5);
        GridPane.setConstraints(focusPicker, 1, 6);


        // Populate gradient combo
        gradientComboBox.getItems().addAll(Gradient.GRADIENTS);
        gradientComboBox.setCellFactory(new Callback<ListView<Gradient>, ListCell<Gradient>>() {
            @Override public ListCell<Gradient> call(ListView<Gradient> gradientList) {
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
                            preview.setStyle("-fx-border-color: #676B6F; -fx-background-color: "+ gradient.getCss());
                            setGraphic(preview);
                        }
                    }
                };
                cell.setStyle("-fx-cell-size: 32;");
                return cell;
            }
        });
        gradientComboBox.getSelectionModel().selectedItemProperty().addListener((arg0, arg1, newGradient) -> {
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
            buildBodyStyle();
        });
        gradientComboBox.getSelectionModel().select(0);

        bodyTopMiddleSlider.disableProperty().bind(topMiddleToggle.selectedProperty().not());
        bodyBottomMiddleSlider.disableProperty().bind(bottomMiddleToggle.selectedProperty().not());
        borderSlider.disableProperty().bind(borderToggle.selectedProperty().not());
        shadowSlider.disableProperty().bind(shadowToggle.selectedProperty().not());
        inputBorderSlider.disableProperty().bind(inputBorderToggle.selectedProperty().not());
    }

    // Add listeners to update the css
    private void addListeners() {
        // Listeners
        ChangeListener<Object> onRootCSSChange = ((ov, oldValue, newValue) -> buildRootStyle());
        ChangeListener<Paint> onPaintChanged = ((ov, oldValue, newValue) -> buildRootStyle());
        ChangeListener<Object> onBodyCSSChange = ((ov, oldValue, newValue) -> buildBodyStyle());

        // Font choice box
        fontChoice.valueProperty().addListener(onRootCSSChange);
        fontSizeBox.valueProperty().addListener(onRootCSSChange);

        // Paint pickers and toggles
        basePicker.getRectangle().fillProperty().addListener(onPaintChanged);
        backgroundPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        focusPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        textPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        baseTextToggle.selectedProperty().addListener(onRootCSSChange);
        textPicker.disableProperty().bind(baseTextToggle.selectedProperty().not());
        fieldBackgroundPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        fieldTextPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        fieldTextToggle.selectedProperty().addListener(onRootCSSChange);
        fieldTextPicker.disableProperty().bind(fieldTextToggle.selectedProperty().not());
        foregroundTextPicker.getRectangle().fillProperty().addListener(onPaintChanged);
        backgroundTextToggle.selectedProperty().addListener(onRootCSSChange);
        foregroundTextPicker.disableProperty().bind(backgroundTextToggle.selectedProperty().not());

        // Slider controls
        borderRadiusSlider.valueProperty().addListener(onBodyCSSChange);
        borderWidthSlider.valueProperty().addListener(onBodyCSSChange);
        paddingSlider.valueProperty().addListener(onBodyCSSChange);
        topHighlightSlider.valueProperty().addListener(onRootCSSChange);
        bottomHighlightSlider.valueProperty().addListener(onRootCSSChange);
        bodyTopSlider.valueProperty().addListener(onRootCSSChange);
        bodyTopMiddleSlider.valueProperty().addListener(onRootCSSChange);
        bodyBottomMiddleSlider.valueProperty().addListener(onRootCSSChange);
        bodyBottomSlider.valueProperty().addListener(onRootCSSChange);
        borderSlider.valueProperty().addListener(onRootCSSChange);
        shadowSlider.valueProperty().addListener(onRootCSSChange);
        inputBorderSlider.valueProperty().addListener(onRootCSSChange);

        // Toggle buttons
        topMiddleToggle.selectedProperty().addListener(onRootCSSChange);
        bottomMiddleToggle.selectedProperty().addListener(onRootCSSChange);
        borderToggle.selectedProperty().addListener(onRootCSSChange);
        shadowToggle.selectedProperty().addListener(onRootCSSChange);
        inputBorderToggle.selectedProperty().addListener(onRootCSSChange);
    }

    /**
     * Bind Slider values to the text fields
     */
    private void addTextFieldBinding(Slider slider, DoubleTextField field) {
        field.textProperty().bind(Bindings.format("%2.0f", slider.valueProperty()));
    }
    
    private StringBuilder sbRoot = new StringBuilder();
    private StringBuilder sbBody = new StringBuilder();

    private void buildRootStyle() {
            sbRoot = new StringBuilder();
            //sbRoot.setLength(0);

            String fontSize = fontSizeBox.getValue() + fontRadioGroup.getSelectedToggle().getUserData();

            sbRoot.append("*.root {\n");
            //cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: " + fontSizeSlider.getValue() + "px " + "\"" + fontChoiceBox.getValue() + "\";", true, 4));
            sbRoot.append(StringUtil.padWithSpaces("-fx-font-family: \"" + fontChoice.getValue() + "\";", true, 4));
            sbRoot.append(StringUtil.padWithSpaces("-fx-font-size: " + fontSize+ ";", true, 4));
            sbRoot.append(StringUtil.padWithSpaces("-fx-base: " + basePicker.getColorString() + ";", true, 4));
            sbRoot.append(StringUtil.padWithSpaces("-fx-background: " + backgroundPicker.getColorString() + ";", true, 4));
            sbRoot.append(StringUtil.padWithSpaces("-fx-focus-color: " + focusPicker.getColorString() + ";", true, 4));
            sbRoot.append(StringUtil.padWithSpaces("-fx-control-inner-background: " + fieldBackgroundPicker.getColorString() + ";", true, 4));

            if (baseTextToggle.isSelected()) {
                baseTextToggle.setText("ON");
                sbRoot.append(StringUtil.padWithSpaces("-fx-text-base-color: "
                        + fieldBackgroundPicker.getColorString() + ";", true, 4));
            } else {
                baseTextToggle.setText("AUTO");
            }
            if (backgroundTextToggle.isSelected()) {
                backgroundTextToggle.setText("ON");
                sbRoot.append(StringUtil.padWithSpaces("-fx-text-background-color: "
                        + fieldTextPicker.getColorString() + ";", true, 4));
            } else {
                backgroundTextToggle.setText("AUTO");
            }
            if (fieldTextToggle.isSelected()) {
                fieldTextToggle.setText("ON");
                sbRoot.append(StringUtil.padWithSpaces("-fx-text-inner-color: "
                        + fieldTextPicker.getColorString() + ";", true, 4));
            } else {
                fieldTextToggle.setText("AUTO");
            }

            if (baseTextToggle.isSelected()) {
                baseTextToggle.setText("ON");
                sbRoot.append(String.format("%4s%s%s;%n", " ",
                        "-fx-text-base-color: ", textPicker.getColorString()));
            } else {
                baseTextToggle.setText("AUTO");
            }
            if (backgroundTextToggle.isSelected()) {
                backgroundTextToggle.setText("ON");
                sbRoot.append(StringUtil.padWithSpaces("-fx-text-background-color: "
                        + foregroundTextPicker.getColorString() + ";", true, 4));
            } else {
                backgroundTextToggle.setText("AUTO");
            }
            if (fieldTextToggle.isSelected()) {
                fieldTextToggle.setText("ON");
                sbRoot.append(StringUtil.padWithSpaces("-fx-text-inner-color: "
                        + fieldTextPicker.getColorString() + ";", true, 4));
            } else {
                fieldTextToggle.setText("AUTO");
            }

            String innerTopDerivation = String.format("%.1f", bodyTopSlider.getValue()
                    + ((100 - bodyTopSlider.getValue()) * (topHighlightSlider.getValue() / 100)));

            String innerBottomDerivation = String.format("%.1f", bodyBottomSlider.getValue()
                    + ((100 - bodyBottomSlider.getValue()) * (bottomHighlightSlider.getValue() / 100)));

            sbRoot.append(StringUtil.padWithSpaces(
                    "-fx-inner-border: linear-gradient(to bottom, "
                            + "derive(-fx-color," + innerTopDerivation + "%) 0%, "
                            + "derive(-fx-color," + innerBottomDerivation + "%) 100%);", true, 4));

            sbRoot.append(StringUtil.padWithSpaces("" +
                    "-fx-body-color: linear-gradient( to bottom, "
                    + "derive(-fx-color, " + String.format("%.1f", bodyTopSlider.getValue()) + "%) 0%, ", false, 0));

            if (topMiddleToggle.isSelected()) {
                topMiddleToggle.setText("ON");
                sbRoot.append(StringUtil.padWithSpaces("derive(-fx-color, "
                        + String.format("%.1f", bodyTopMiddleSlider.getValue()) + "%) 50%, ", false, 0));
            } else {
                topMiddleToggle.setText("AUTO");
            }

            if (bottomMiddleToggle.isSelected()) {
                bottomMiddleToggle.setText("ON");
                sbRoot.append(StringUtil.padWithSpaces("derive(-fx-color, "
                        + String.format("%.1f", bodyBottomMiddleSlider.getValue()) + "%) 50.5%, ", false, 0));
            } else {
                bottomMiddleToggle.setText("AUTO");
            }

            sbRoot.append(StringUtil.padWithSpaces("derive(-fx-color, "
                    + String.format("%.1f", bodyBottomSlider.getValue()) + "%) 100%);", true, 0));

            if (borderToggle.isSelected()) {
                borderToggle.setText("ON");
                sbRoot.append(StringUtil.padWithSpaces("-fx-outer-border: derive(-fx-color,"
                        + String.format("%.1f", borderSlider.getValue()) + "%);", true, 4));
            } else {
                borderToggle.setText("AUTO");
            }

            if (shadowToggle.isSelected()) {
                shadowToggle.setText("ON");
                sbRoot.append(StringUtil.padWithSpaces("-fx-shadow-highlight-color: derive(-fx-background,"
                        + String.format("%.1f", shadowSlider.getValue()) + "%);", true, 4));
            } else {
                shadowToggle.setText("AUTO");
            }

            if (inputBorderToggle.isSelected()) {
                inputBorderToggle.setText("ON");
                sbRoot.append(StringUtil.padWithSpaces("-fx-text-box-border: derive(-fx-background,"
                        + String.format("%.1f", inputBorderSlider.getValue()) + "%);", true, 4));
            } else {
                inputBorderToggle.setText("AUTO");
                sbRoot.append("}\n");
                sbRoot.append("@font-face {\n" +
                        "src: url(“Roboto-Light.ttf”);\n" +
                        "}\n" +
                        "\n" +
                        ".label, .button, .toggle-button, .choice-box, .text {\n" +
                        "-fx-font-family: 'Roboto';\n" +
                        "}\n");
            }

        previewController.setPreviewPanelStyle(sbRoot.toString());
    }

    public void buildBodyStyle() {

        sbBody = new StringBuilder();
        int borderWidth = (int) borderWidthSlider.getValue();
//        int borderWidthForPadding = (borderWidth <= 1) ? 0 : borderWidth - 1;
//        int padding = (int) paddingSlider.getValue() + borderWidthForPadding;
        int padding = (int) paddingSlider.getValue();
        int borderRadius = (int) borderRadiusSlider.getValue();
//        double checkPadding = (((0.25 * fontSize) + borderWidthForPadding) / fontSize);
//        double radioPadding = (((0.333333 * (int) fontSize.getValue() + borderWidthForPadding) / fontSize);


        sbBody.append(".button, .toggle-button, .choice-box {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-background-radius: "
                + borderRadius + ", "
                + borderRadius + ", "
                + (borderRadius - 1) + ", "
                + (borderRadius - 2) + ";", true, 4));
        sbBody.append(StringUtil.padWithSpaces("-fx-padding: "
                + padding + "px "
                + (padding + 7) + "px "
                + padding + "px "
                + (padding + 7) + "px;", true, 4));
        //  sbBody.append(StringUtil.padWithSpaces("-fx-font-family: " + fontChoice.getValue() + ";", true, 4));
        // sbBody.append(StringUtil.padWithSpaces("-fx-font-family: 'Roboto';", true, 4));
        sbBody.append("}\n");

        sbBody.append(".menu-button {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-background-radius: " + borderRadius + ", "
                + borderRadius + ", "
                + (borderRadius - 1) + ", "
                + (borderRadius - 2) + ";", true, 4));
        sbBody.append("}\n");
        sbBody.append(".menu-button .label {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px " + (padding + 15) + "px " + padding + "px " + (padding + 7) + "px;", true, 4));
        sbBody.append("}\n");
        sbBody.append(".menu-button .arrow-button {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px " + (padding + 3) + "px " + padding + "px 0px;", true, 4));
        sbBody.append("}\n");
        sbBody.append(".choice-box {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-padding: 0 " + (padding + 3) + "px 0 0;", true, 4));
        sbBody.append("}\n");
        sbBody.append(".choice-box .label {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px " + (padding + 1) + "px " + padding + "px " + (padding + 3) + "px;", true, 4));
        sbBody.append("}\n");
        sbBody.append(".choice-box .open-button {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-padding: 1 0 0 " + (padding + 5) + "px;", true, 4));
        sbBody.append("}\n");
        sbBody.append(".combo-box-base:editable .text-field, .combo-box-base .arrow-button, " + ".combo-box .list-cell {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px " + (padding + 3) + "px " + padding + "px " + (padding + 3) + "px;", true, 4));
        sbBody.append("}\n");
        //  sbBody.append(".check-box .box {\n");
        //sbBody.append(StringUtil.padWithSpaces("-fx-padding: " + checkPadding + "em;", true, 4));
        //  sbBody.append("}\n");
        //     sbBody.append(".radio-button .radio {\n");
        //    sbBody.append(StringUtil.padWithSpaces("-fx-padding: " + radioPadding + "em;", true, 4));
        //    sbBody.append("}\n");
        if (!baseTextToggle.isSelected()) {
            sbBody.append(".hyperlink {\n");
            sbBody.append(StringUtil.padWithSpaces("-fx-fill: -fx-text-background-color;", true, 4));
            sbBody.append("}\n");
            sbBody.append(".toggle-button:selected {\n");
            sbBody.append(StringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;", true, 4));
            sbBody.append("}\n");
        }
        sbBody.append(".label, .check-box, .text, .radio-button {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-font-family: -fx-font-type; -fx-fill: -fx-text-background-color; -fx-text-fill: -fx-text-background-color;", true, 4));
        sbBody.append("}\n");
        sbBody.append(".text {\n");

        sbBody.append(StringUtil.padWithSpaces("-fx-font-family: \"" + fontChoice.getValue() + "\";", true, 4));
        sbBody.append("}\n");

        sbBody.append(".button, .toggle-button, .check-box .box, .radio-button .radio, "
                + ".choice-box, .menu-button, .tab, .combo-box-base {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-background-insets: 0 0 -1 0, 0, "
                + borderWidth + ", " + (borderWidth + 1) + ";", true, 4));
        sbBody.append("}\n");
        sbBody.append(".button:focused, .toggle-button:focused, .check-box:focused .box, "
                + ".radio-button:focused .radio, .choice-box:focused, .menu-button:focused, "
                + ".combo-box-base:focused {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-background-insets: -1.4, 0, "
                + borderWidth + ", " + (borderWidth + 1) + ";", true, 4));
        sbBody.append("}\n");
        sbBody.append(".combo-box-base .arrow-button {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-background-insets: 0, "
                + borderWidth + ", " + (borderWidth + 1) + ";", true, 4));
        sbBody.append("}\n");

        sbBody.append(".choice-box .label { /* Workaround for RT-20015 */\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;", true, 4));
        sbBody.append("}\n");
        sbBody.append(".menu-button .label { /* Workaround for RT-20015 */\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;", true, 4));
        sbBody.append("}");

        previewController.setPreviewPanelStyle(sbRoot.toString() + sbBody.toString());
    }

    public String getCodeString() {
     return sbRoot.toString();
    }

    private void setCodeAreaText() {
        codeArea.replaceText(0, 0, getCodeString());
    }

    @FXML
    private void onFontRadioSelected(ActionEvent event) {

    }

    @FXML
    private void displayEditorPane() {
        if (!editorPane.isVisible()) {
            editorPane.setVisible(true);
            setCodeAreaText();
        }
        else {
            editorPane.setVisible(false);
        }
    }

    @FXML
    private void copyButtonAction(ActionEvent event) {
        Clipboard.getSystemClipboard().setContent(
                Collections.singletonMap(DataFormat.PLAIN_TEXT, getCodeString()));
        //    displayStatusAlert("Code has been copied to the clipboard.");
    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        FileUtil.saveCSSFile(this.getScene().getWindow(), getCodeString());
    }

    private static List<String> getFixedFontSizes() {
        String[] predefinedFontSizes
                = {"9", "10", "11", "12", "13", "14", "18", "24"};//NOI18N
        return Arrays.asList(predefinedFontSizes);
    }

    private static List<String> getScalableFontSizes() {
        String[] predefinedFontSizes
                = {"0.083333", "0.166667", "0.25", "0.333333", "0.416667", "0.5",
                   "0.583333", " 0.666667", "0.75", "0.833333", "0.916667", "1" };//NOI18N
        return Arrays.asList(predefinedFontSizes);
    }

}

