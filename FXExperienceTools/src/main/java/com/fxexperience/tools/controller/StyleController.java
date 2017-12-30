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
import com.fxexperience.tools.ui.CssEditor;
import com.fxexperience.tools.util.FileUtil;
import com.fxexperience.tools.util.Gradient;
import com.fxexperience.tools.util.StringUtil;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.parser.CSSParser;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.w3c.dom.css.CSSStyleSheet;

import javax.swing.text.html.StyleSheet;
import java.io.IOException;
import java.net.URISyntaxException;
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

    @FXML private Button btnEditor, btnSave, btnCopy;

    @FXML private ToggleButton baseTextToggle;
    @FXML private ToggleButton backgroundTextToggle;
    @FXML private ToggleButton fieldTextToggle;
    @FXML private ToggleButton topMiddleToggle;
    @FXML private ToggleButton bottomMiddleToggle;
    @FXML private ToggleButton borderToggle;
    @FXML private ToggleButton shadowToggle;
    @FXML private ToggleButton inputBorderToggle;

    @FXML private Label baseTextLabel, backgroundTextLabel, fieldTextLabel;

    private final CssEditor codeArea = new CssEditor();
    private final PreviewController previewController = new PreviewController();
    private final FontPickerController fontPickerController = new FontPickerController();

    private final ColorPopupEditor basePicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#213870"));
    private final ColorPopupEditor accentPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#0096C9"));
    private final ColorPopupEditor backgroundPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#2c2f33"));
    private final ColorPopupEditor focusPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#0093ff"));
    private final ColorPopupEditor baseTextPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#000000"));
    private final ColorPopupEditor backgroundTextPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#000000"));
    private final ColorPopupEditor fieldBackgroundPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#23272a"));
    private final ColorPopupEditor fieldTextPicker = new ColorPopupEditor(Mode.SINGLE, Color.web("#000000"));

    public StyleController() {
        initialize();
    }

    private void initialize() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(StyleController.class.getResource("/fxml/FXMLStylePanel.fxml")); //NOI18N
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
        GridPane.setConstraints(baseTextPicker, 1, 2);
        GridPane.setConstraints(backgroundPicker, 1, 3);
        GridPane.setConstraints(backgroundTextPicker, 1, 4);
        GridPane.setConstraints(fieldBackgroundPicker, 1, 5);
        GridPane.setConstraints(fieldTextPicker, 1, 6);
        GridPane.setConstraints(focusPicker, 1, 7);

        // Add color pickers Title Pane
        simpleGridPane.getChildren().addAll(basePicker, accentPicker, backgroundPicker, focusPicker,
                baseTextPicker, fieldBackgroundPicker, fieldTextPicker, backgroundTextPicker);

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

        // Font choice box
        fontPickerController.fontProperty().addListener(o -> buildRootStyle());

        // Disabled properties
        baseTextLabel.disableProperty().bind(baseTextToggle.selectedProperty().not());
        baseTextPicker.disableProperty().bind(baseTextToggle.selectedProperty().not());
        backgroundTextLabel.disableProperty().bind(baseTextToggle.selectedProperty().not());
        backgroundTextPicker.disableProperty().bind(backgroundTextToggle.selectedProperty().not());
        fieldTextLabel.disableProperty().bind(baseTextToggle.selectedProperty().not());
        fieldTextPicker.disableProperty().bind(fieldTextToggle.selectedProperty().not());

        // Toggles
        baseTextToggle.selectedProperty().addListener(o -> buildRootStyle());
        backgroundTextToggle.selectedProperty().addListener(o -> buildRootStyle());
        fieldTextToggle.selectedProperty().addListener(o -> buildRootStyle());

        // Paint pickers
        basePicker.getRectangle().fillProperty().addListener(o -> buildRootStyle());
        accentPicker.getRectangle().fillProperty().addListener(o -> buildRootStyle());
        backgroundPicker.getRectangle().fillProperty().addListener(o -> buildRootStyle());
        baseTextPicker.getRectangle().fillProperty().addListener(o -> buildRootStyle());
        fieldBackgroundPicker.getRectangle().fillProperty().addListener(o -> buildRootStyle());
        backgroundTextPicker.getRectangle().fillProperty().addListener(o -> buildRootStyle());
        fieldTextPicker.getRectangle().fillProperty().addListener(o -> buildRootStyle());
        focusPicker.getRectangle().fillProperty().addListener(o -> buildRootStyle());

        // Slider controls
        borderRadiusSlider.valueProperty().addListener(o -> buildBodyStyle());
        borderWidthSlider.valueProperty().addListener(o -> buildBodyStyle());
        paddingSlider.valueProperty().addListener(o -> buildBodyStyle());
        topHighlightSlider.valueProperty().addListener(o -> buildRootStyle());
        bottomHighlightSlider.valueProperty().addListener(o -> buildRootStyle());
        bodyTopSlider.valueProperty().addListener(o -> buildRootStyle());
        bodyTopMiddleSlider.valueProperty().addListener(o -> buildRootStyle());
        bodyBottomMiddleSlider.valueProperty().addListener(o -> buildRootStyle());
        bodyBottomSlider.valueProperty().addListener(o -> buildRootStyle());
        borderSlider.valueProperty().addListener(o -> buildRootStyle());
        shadowSlider.valueProperty().addListener(o -> buildRootStyle());
        inputBorderSlider.valueProperty().addListener(o -> buildRootStyle());

        // Toggle buttons
        topMiddleToggle.selectedProperty().addListener(o -> buildRootStyle());
        bottomMiddleToggle.selectedProperty().addListener(o -> buildRootStyle());
        borderToggle.selectedProperty().addListener(o -> buildRootStyle());
        shadowToggle.selectedProperty().addListener(o -> buildRootStyle());
        inputBorderToggle.selectedProperty().addListener(o -> buildRootStyle());
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

        sbRoot.append(".root { ");
        sbRoot.append("-fx-font-family: \"").append(fontPickerController.fontProperty().get().getFamily()).append("\";");
        sbRoot.append("-fx-font-size: ").append(fontPickerController.fontProperty().get().getSize()).append("px;");
        sbRoot.append("-fx-base: ").append(basePicker.getColorString()).append(";");
        sbRoot.append("-fx-accent: ").append(accentPicker.getColorString()).append(";");
        sbRoot.append("-fx-background: ").append(backgroundPicker.getColorString()).append(";");
        sbRoot.append("-fx-focus-color: ").append(focusPicker.getColorString()).append(";");
        sbRoot.append("-fx-control-inner-background: ").append(fieldBackgroundPicker.getColorString()).append(";");

        if (baseTextToggle.isSelected()) {
            baseTextToggle.setText("ON");
            sbRoot.append("-fx-text-base-color: ");
            sbRoot.append(baseTextPicker.getColorString());
            sbRoot.append(";");
        } else {
            baseTextToggle.setText("AUTO");
        }
        if (backgroundTextToggle.isSelected()) {
            backgroundTextToggle.setText("ON");
            sbRoot.append("-fx-text-background-color: ");
            sbRoot.append(backgroundTextPicker.getColorString());
            sbRoot.append(";");
        } else {
            backgroundTextToggle.setText("AUTO");
        }
        if (fieldTextToggle.isSelected()) {
            fieldTextToggle.setText("ON");
            sbRoot.append("-fx-text-inner-color: ");
            sbRoot.append(fieldTextPicker.getColorString());
            sbRoot.append(";");
        } else {
            fieldTextToggle.setText("AUTO");
        }
        
        String innerTopDerivation = String.format("%.1f", bodyTopSlider.getValue()
                + (100 - bodyTopSlider.getValue()) * (topHighlightSlider.getValue() / 100));

        String innerBottomDerivation = String.format("%.1f", bodyBottomSlider.getValue()
                + (100 - bodyBottomSlider.getValue()) * (bottomHighlightSlider.getValue() / 100));

        sbRoot.append("-fx-inner-border: linear-gradient(to bottom, ");
        sbRoot.append("derive(-fx-color,");
        sbRoot.append(innerTopDerivation);
        sbRoot.append("%) 0%, ");
        sbRoot.append("derive(-fx-color,");
        sbRoot.append(innerBottomDerivation);
        sbRoot.append("%) 100%);");
        sbRoot.append("-fx-body-color: linear-gradient( to bottom, ");
        sbRoot.append("derive(-fx-color, ");
        sbRoot.append(String.format("%.1f", bodyTopSlider.getValue()));
        sbRoot.append("%) 0%, ");

        if (topMiddleToggle.isSelected()) {
            topMiddleToggle.setText("ON");
            sbRoot.append("derive(-fx-color, ");
            sbRoot.append(String.format("%.1f", bodyTopMiddleSlider.getValue()));
            sbRoot.append("%) 50%, ");
        } else {
            topMiddleToggle.setText("AUTO");
        }

        if (bottomMiddleToggle.isSelected()) {
            bottomMiddleToggle.setText("ON");
            sbRoot.append("derive(-fx-color, ");
            sbRoot.append(String.format("%.1f",bodyBottomMiddleSlider.getValue()));
            sbRoot.append("%) 50.5%, ");
        } else {
            bottomMiddleToggle.setText("AUTO");
        }

        sbRoot.append("derive(-fx-color, ");
        sbRoot.append(String.format("%.1f", bodyBottomSlider.getValue()));
        sbRoot.append("%) 100%); ");

        if (borderToggle.isSelected()) {
            borderToggle.setText("ON");
            sbRoot.append("-fx-outer-border: derive(-fx-color,");
            sbRoot.append(String.format("%.1f", borderSlider.getValue()));
            sbRoot.append("%);");
        } else {
            borderToggle.setText("AUTO");
        }

        if (shadowToggle.isSelected()) {
            shadowToggle.setText("ON");
            sbRoot.append("-fx-shadow-highlight-color: derive(-fx-background,");
            sbRoot.append(String.format("%.1f", shadowSlider.getValue()));
            sbRoot.append("%);");
        } else {
            shadowToggle.setText("AUTO");
        }

        if (inputBorderToggle.isSelected()) {
            inputBorderToggle.setText("ON");
            sbRoot.append("-fx-text-box-border: derive(-fx-background,");
            sbRoot.append(String.format("%.1f", inputBorderSlider.getValue()));
            sbRoot.append("%);");
        } else {
            inputBorderToggle.setText("AUTO");
            sbRoot.append("} ");
        }

        previewController.setPreviewPanelStyle(sbRoot.toString());
    }

    private void buildBodyStyle() {
        sbBody = new StringBuilder();
        double fontSize = fontPickerController.fontProperty().get().getSize();

        int borderWidth = (int) borderWidthSlider.getValue();
        int borderWidthForPadding = (borderWidth <= 1) ? 0 : borderWidth - 1;
        int padding = (int) paddingSlider.getValue() + borderWidthForPadding;
        int borderRadius = (int) borderRadiusSlider.getValue();
        double checkPadding = (0.25 * fontSize + borderWidthForPadding / fontSize);
        double radioPadding = (0.333333 * (int) fontSize + borderWidthForPadding / fontSize);

        sbBody.append(".button, .toggle-button, .choice-box {");
        sbBody.append("-fx-background-radius: ");
        sbBody.append(borderRadius);
        sbBody.append(", ");
        sbBody.append(borderRadius);
        sbBody.append(", ");
        sbBody.append(borderRadius - 1);
        sbBody.append(", ");
        sbBody.append(borderRadius - 2);
        sbBody.append(";");
        sbBody.append("-fx-padding: ");
        sbBody.append(padding);
        sbBody.append("px ");
        sbBody.append(padding + 7);
        sbBody.append("px ");
        sbBody.append(padding);
        sbBody.append("px ");
        sbBody.append(padding + 7);
        sbBody.append("px; } ");
        sbBody.append(".menu-button {");
        sbBody.append("-fx-background-radius: ");

        sbBody.append(borderRadius);
        sbBody.append(",");
        sbBody.append(borderRadius);
        sbBody.append(",");
        sbBody.append(borderRadius - 1);
        sbBody.append(",");
        sbBody.append(borderRadius - 2);
        sbBody.append("; } ");

        sbBody.append(".menu-button .label { ");
        sbBody.append("-fx-padding: ");
        sbBody.append(padding);
        sbBody.append("px ");
        sbBody.append(padding + 15);
        sbBody.append("px ");
        sbBody.append(padding);
        sbBody.append("px ");
        sbBody.append(padding + 7);
        sbBody.append("px; } ");

        sbBody.append(".menu-button .arrow-button { ");
        sbBody.append("-fx-padding: ");
        sbBody.append(padding);
        sbBody.append("px ");
        sbBody.append(padding + 3);
        sbBody.append("px ");
        sbBody.append(padding);
        sbBody.append("px 0px; }");

        sbBody.append(".choice-box {");
        sbBody.append("-fx-padding: 0 ");
        sbBody.append(padding + 3);
        sbBody.append("px 0 0; }");

        sbBody.append(".choice-box .label { ");
        sbBody.append("-fx-padding: ");
        sbBody.append(padding);
        sbBody.append("px ");
        sbBody.append(padding + 1);
        sbBody.append("px ");
        sbBody.append(padding);
        sbBody.append("px ");
        sbBody.append(padding + 3);
        sbBody.append("px; } ");

        sbBody.append(".choice-box .open-button { ");
        sbBody.append("-fx-padding: 1 0 0 ");
        sbBody.append(padding + 5);
        sbBody.append("px; }");

        sbBody.append(".combo-box-base:editable .text-field, .combo-box-base .arrow-button, .combo-box .list-cell { ");
        sbBody.append("-fx-padding: ");
        sbBody.append(padding);
        sbBody.append("px ");
        sbBody.append(padding + 3);
        sbBody.append("px ");
        sbBody.append(padding);
        sbBody.append("px ");
        sbBody.append(padding + 3);
        sbBody.append("px; } ");

        sbBody.append(".check-box .box { ");
        sbBody.append("-fx-padding: " + checkPadding + "em; } ");

        sbBody.append(".radio-button .radio { ");
        sbBody.append("-fx-padding: ").append(radioPadding).append("em; } ");

        if (!baseTextToggle.isSelected()) {
            sbBody.append(".hyperlink { ");
            sbBody.append("-fx-fill: -fx-text-background-color; } ");

            sbBody.append(".toggle-button:selected {");
            sbBody.append("-fx-text-fill: -fx-text-base-color; } ");
        }

        sbBody.append(".label, .check-box, .text, .radio-button {");
        sbBody.append("-fx-font-family: -fx-font-type; -fx-fill: -fx-text-background-color; -fx-text-fill: -fx-text-background-color; } ");

        sbBody.append(".text {");
        sbBody.append("-fx-font-family: -fx-font-type; -fx-fill: -fx-text-background-color; }");
        sbBody.append(".button, .toggle-button, .check-box .box, .radio-button .radio, "
                + ".choice-box, .menu-button, .tab, .combo-box-base {");
        sbBody.append("-fx-background-insets: 0 0 -1 0, 0, ");
        sbBody.append(borderWidth);
        sbBody.append(", ");
        sbBody.append(borderWidth + 1);
        sbBody.append("; } ");
        sbBody.append(".button:focused, .toggle-button:focused, .check-box:focused .box, ");
        sbBody.append(".radio-button:focused .radio, .choice-box:focused, .menu-button:focused, ");
        sbBody.append(".combo-box-base:focused {");

        sbBody.append("-fx-background-insets: -1.4, 0, ");
        sbBody.append(borderWidth);
        sbBody.append(", ");
        sbBody.append(borderWidth + 1);
        sbBody.append("; } ");

        sbBody.append(".combo-box-base .arrow-button {");
        sbBody.append("-fx-background-insets: 0, ");
        sbBody.append(borderWidth);
        sbBody.append(", ");
        sbBody.append(borderWidth + 1);
        sbBody.append("; } ");
        sbBody.append(".choice-box .label { /* Workaround for RT-20015 */\n");

        sbBody.append("-fx-text-fill: -fx-text-base-color; } ");
        sbBody.append(".menu-button .label { /* Workaround for RT-20015 */\n");
        sbBody.append("-fx-text-fill: -fx-text-base-color; } ");

        //  previewController.setPreviewPanelStyle(sbRoot.append(sbBody.toString()).toString());
    }

    public String getCodeString() {
     return sbRoot.append(sbBody.toString()).toString();
    }

    private void setCodeAreaText() {
//        CSSParser parser = new CSSParser();
//        Stylesheet css = parser.parse(getCodeString());
//        System.out.println(css.toString());

//        previewController.getStylesheets().clear();
//        previewController.getStylesheets().setAll(css.getUrl());
        codeArea.replaceText(0, codeArea.getLength(), StringUtil.formatCSStoString(getCodeString()));
        codeArea.showParagraphAtTop(0);


        //codeArea.replaceText(0, codeArea.getLength(), getCodeString());

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

