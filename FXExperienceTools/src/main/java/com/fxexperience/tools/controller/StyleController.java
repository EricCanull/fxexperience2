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

import com.fxexperience.javafx.control.fontpicker.FontPickerController;
import com.fxexperience.javafx.scene.control.popup.ColorPopupEditor;
import com.fxexperience.javafx.scene.control.textfields.DoubleTextField;
import com.fxexperience.tools.ui.CSSCodeArea;
import com.fxexperience.tools.util.FileUtil;
import com.fxexperience.tools.util.Gradient;
import com.fxexperience.tools.util.StringUtil;
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
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.fxexperience.javafx.scene.control.paintpicker.PaintPicker.Mode;

public class StyleController extends VBox {
    @FXML private TitledPane textTitlePane;
    @FXML private BorderPane previewPane;
    @FXML private StackPane editorPane;
    @FXML private GridPane simpleGridPane;

    @FXML private ComboBox<Gradient> gradientCB;

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

    private Font font = Font.getDefault();

    private final CSSCodeArea codeArea = new CSSCodeArea();
    private final FontPickerController fontPickerController = new FontPickerController();
    private final PreviewController previewController = new PreviewController();

    private final ColorPopupEditor basePicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#213870"));
    private final ColorPopupEditor accentPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#0096C9"));
    private final ColorPopupEditor backgroundPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#2c2f33"));
    private final ColorPopupEditor focusPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#0093ff"));
    private final ColorPopupEditor textPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#000000"));
    private final ColorPopupEditor foregroundTextPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#000000"));
    private final ColorPopupEditor fieldBackgroundPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#23272a"));
    private final ColorPopupEditor fieldTextPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#000000"));

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

        textTitlePane.setContent(fontPickerController);


        editorPane.getChildren().add(new VirtualizedScrollPane<>(codeArea));
        previewPane.setCenter(previewController);

        addListeners();


        // create Integer Fields
        addTextFieldBinding(paddingSlider, paddingTextField);
        addTextFieldBinding(borderWidthSlider, borderWidthTextField);
        addTextFieldBinding(borderRadiusSlider, borderRadiusTextField);

        // Set color pickers grid constraints
        GridPane.setConstraints(basePicker, 1, 0, 2, 1);
        GridPane.setConstraints(accentPicker, 1, 1, 2, 1);
        GridPane.setConstraints(textPicker, 1, 2);
        GridPane.setConstraints(backgroundPicker, 1, 3);
        GridPane.setConstraints(foregroundTextPicker, 1, 4);
        GridPane.setConstraints(fieldBackgroundPicker, 1, 5);
        GridPane.setConstraints(fieldTextPicker, 1, 6);
        GridPane.setConstraints(focusPicker, 1, 7);

        // Add color pickers Title Pane
        simpleGridPane.getChildren().addAll(basePicker, accentPicker, backgroundPicker, focusPicker,
                textPicker, fieldBackgroundPicker, fieldTextPicker, foregroundTextPicker);

        // Populate gradient combo
        gradientCB.getItems().addAll(Gradient.GRADIENTS);
        gradientCB.setCellFactory(new Callback<ListView<Gradient>, ListCell<Gradient>>() {
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
        gradientCB.getSelectionModel().selectedItemProperty().addListener((arg0, arg1, newGradient) -> {
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
        gradientCB.getSelectionModel().select(0);

        bodyTopMiddleSlider.disableProperty().bind(topMiddleToggle.selectedProperty().not());
        bodyBottomMiddleSlider.disableProperty().bind(bottomMiddleToggle.selectedProperty().not());
        borderSlider.disableProperty().bind(borderToggle.selectedProperty().not());
        shadowSlider.disableProperty().bind(shadowToggle.selectedProperty().not());
        inputBorderSlider.disableProperty().bind(inputBorderToggle.selectedProperty().not());
    }

    // Add listeners to update the css
    private void addListeners() {
        // Listeners

        // Font choice box
     //   fontFamilyCB.valueProperty().addListener(onRootCSSChange);
//        fontSizeSpinner.valueProperty().addListener(onRootCSSChange);
        fontPickerController.fontProperty().addListener(observable -> buildRootStyle());

        // Paint pickers and toggles
        basePicker.getRectangle().fillProperty().addListener(observable -> buildRootStyle());
        accentPicker.getRectangle().fillProperty().addListener(observable -> buildRootStyle());
        backgroundPicker.getRectangle().fillProperty().addListener(observable -> buildRootStyle());
        focusPicker.getRectangle().fillProperty().addListener(observable -> buildRootStyle());
        textPicker.getRectangle().fillProperty().addListener(observable -> buildRootStyle());
        baseTextToggle.selectedProperty().addListener(observable -> buildRootStyle());
        textPicker.disableProperty().bind(baseTextToggle.selectedProperty().not());
        fieldBackgroundPicker.getRectangle().fillProperty().addListener(observable -> buildRootStyle());
        fieldTextPicker.getRectangle().fillProperty().addListener(observable -> buildRootStyle());
        fieldTextToggle.selectedProperty().addListener(observable -> buildRootStyle());
        fieldTextPicker.disableProperty().bind(fieldTextToggle.selectedProperty().not());
        foregroundTextPicker.getRectangle().fillProperty().addListener(observable -> buildRootStyle());
        backgroundTextToggle.selectedProperty().addListener(observable -> buildRootStyle());
        foregroundTextPicker.disableProperty().bind(backgroundTextToggle.selectedProperty().not());

        // Slider controls
        borderRadiusSlider.valueProperty().addListener(observable -> buildBodyStyle());
        borderWidthSlider.valueProperty().addListener(observable -> buildBodyStyle());
        paddingSlider.valueProperty().addListener(observable -> buildBodyStyle());
        topHighlightSlider.valueProperty().addListener(observable -> buildRootStyle());
        bottomHighlightSlider.valueProperty().addListener(observable -> buildRootStyle());
        bodyTopSlider.valueProperty().addListener(observable -> buildRootStyle());
        bodyTopMiddleSlider.valueProperty().addListener(observable -> buildRootStyle());
        bodyBottomMiddleSlider.valueProperty().addListener(observable -> buildRootStyle());
        bodyBottomSlider.valueProperty().addListener(observable -> buildRootStyle());
        borderSlider.valueProperty().addListener(observable -> buildRootStyle());
        shadowSlider.valueProperty().addListener(observable -> buildRootStyle());
        inputBorderSlider.valueProperty().addListener(observable -> buildRootStyle());

        // Toggle buttons
        topMiddleToggle.selectedProperty().addListener(observable -> buildRootStyle());
        bottomMiddleToggle.selectedProperty().addListener(observable -> buildRootStyle());
        borderToggle.selectedProperty().addListener(observable -> buildRootStyle());
        shadowToggle.selectedProperty().addListener(observable -> buildRootStyle());
        inputBorderToggle.selectedProperty().addListener(observable -> buildRootStyle());
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

             sbRoot.append(".root {\n");
            //cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: " + fontSizeSlider.getValue() + "px " + "\"" + fontChoiceBox.getValue() + "\";", true, 4));
            sbRoot.append(StringUtil.padWithSpaces("-fx-font-family: \"" + fontPickerController.fontProperty().get().getFamily() + "\";", true, 4));
            sbRoot.append(StringUtil.padWithSpaces("-fx-font-size: " + fontPickerController.fontProperty().get().getSize()  + "px;", true, 4));
            sbRoot.append(StringUtil.padWithSpaces("-fx-base: " + basePicker.getColorString() + ";", true, 4));
            sbRoot.append(StringUtil.padWithSpaces("-fx-accent: " + accentPicker.getColorString() + ";", true, 4));
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
        double fontSize = fontPickerController.fontProperty().get().getSize();
        sbBody = new StringBuilder();
        int borderWidth = (int) borderWidthSlider.getValue();
      int borderWidthForPadding = (borderWidth <= 1) ? 0 : borderWidth - 1;
      int padding = (int) paddingSlider.getValue() + borderWidthForPadding;
     //   int padding = (int) paddingSlider.getValue();
        int borderRadius = (int) borderRadiusSlider.getValue();
       double checkPadding = ((0.25 * fontSize + borderWidthForPadding) / fontSize);
       double radioPadding = ((0.333333 * (int) fontSize + borderWidthForPadding) / fontSize);


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
        //  sbBody.append(StringUtil.padWithSpaces("-fx-font-family: " + fontFamilyCB.getValue() + ";", true, 4));
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
          sbBody.append(".check-box .box {\n");
        sbBody.append(StringUtil.padWithSpaces("-fx-padding: " + checkPadding + "em;", true, 4));
          sbBody.append("}\n");
             sbBody.append(".radio-button .radio {\n");
            sbBody.append(StringUtil.padWithSpaces("-fx-padding: " + radioPadding + "em;", true, 4));
            sbBody.append("}\n");
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

        sbBody.append(StringUtil.padWithSpaces("-fx-font-family: -fx-font-type;", true, 4));
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
     return sbRoot.toString() + sbBody.toString() ;
    }

    private void setCodeAreaText() {
        codeArea.replaceText(0, 0, getCodeString());
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
}

