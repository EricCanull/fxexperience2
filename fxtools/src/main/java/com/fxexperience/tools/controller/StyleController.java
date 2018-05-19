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


import com.paintpicker.scene.control.picker.PaintPicker;
import com.fxexperience.javafx.control.fontpicker.FontPickerController;

import com.fxexperience.previewer.controller.PreviewController;
import com.fxexperience.tools.util.Gradient;

import com.fxexperience.tools.util.FileUtil;
import com.fxexperience.tools.util.StringUtil;
import com.paintpicker.scene.control.fields.DoubleTextField;

import com.paintpicker.utils.ColorEncoder;

import fxwebview.app.EditorController;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.input.MouseEvent;


public class StyleController extends VBox {

    @FXML private TitledPane textTitlePane;
    @FXML private BorderPane previewPane;
    @FXML private StackPane editorPane;
    @FXML private GridPane simpleGridPane;
 
    @FXML private ComboBox<Gradient> gradientComboBox;

    @FXML private Slider paddingSlider, borderWidthSlider, borderRadiusSlider, topHighlightSlider;
    @FXML private Slider bottomHighlightSlider, bodyTopSlider, bodyTopMiddleSlider, bodyBottomMiddleSlider;
    @FXML private Slider bodyBottomSlider, borderSlider, shadowSlider, inputBorderSlider;

    @FXML private DoubleTextField paddingTextField, borderWidthTextField, borderRadiusTextField;

    @FXML private ToggleButton baseTextToggle, backgroundTextToggle, fieldTextToggle, topMiddleToggle;
    @FXML private ToggleButton bottomMiddleToggle, borderToggle, shadowToggle, inputBorderToggle;

    @FXML private Label baseTextLabel, backgroundTextLabel, fieldTextLabel;
    
    private final EditorController editor = new EditorController();

    @FXML
    private PaintPicker basePicker, accentPicker, backgroundPicker,
                        focusPicker, baseTextPicker, backgroundTextPicker, 
                        fieldBackgroundPicker, fieldTextPicker;
    
   
    private final PreviewController previewController = new PreviewController();
    private final FontPickerController fontPickerController = new FontPickerController();

    public StyleController() {
        initialize();
    }

    private void initialize() {

        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StyleController.class.getResource("/fxml/FXMLStylePanel.fxml")); //NOI18N
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(StyleController.class.getName()).log(Level.SEVERE, null, ex);
        }

        textTitlePane.setContent(fontPickerController);
        editorPane.getChildren().add(editor);
        previewPane.setCenter(previewController);
        
        // create Integer Fields
        addTextFieldBinding(paddingSlider, paddingTextField);
        addTextFieldBinding(borderWidthSlider, borderWidthTextField);
        addTextFieldBinding(borderRadiusSlider, borderRadiusTextField);
        
        basePicker.setValue(Color.web("#213870"));
        accentPicker.setValue(Color.web("#0096C9"));
        backgroundPicker.setValue(Color.web("#2c2f33"));
        focusPicker.setValue(Color.web("#0093ff"));
        baseTextPicker.setValue(Color.web("#000000"));
        backgroundTextPicker.setValue(Color.web("#000000"));
        fieldBackgroundPicker.setValue(Color.web("#23272a"));
        fieldTextPicker.setValue(Color.web("#000000"));
        addControlsListeners();

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
                        preview.setStyle("-fx-border-color: #676B6F; -fx-background-color: "+ gradient.getCss());
                        setGraphic(preview);
                    }
                }
            };
            cell.setStyle("-fx-cell-size: 32;");
            return cell;
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
            createCSS();
        });
        gradientComboBox.getSelectionModel().select(0);
    }

    // Add listeners to update the css
    private void addControlsListeners() {
        editorPane.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == true) {
                 setCodeAreaText();
            }
        });
        bodyTopMiddleSlider.disableProperty().bind(topMiddleToggle.selectedProperty().not());
        bodyBottomMiddleSlider.disableProperty().bind(bottomMiddleToggle.selectedProperty().not());
        borderSlider.disableProperty().bind(borderToggle.selectedProperty().not());
        shadowSlider.disableProperty().bind(shadowToggle.selectedProperty().not());
        inputBorderSlider.disableProperty().bind(inputBorderToggle.selectedProperty().not());

        // Font choice box
        fontPickerController.fontProperty().addListener(o -> createCSS());

        // Disabled properties
        baseTextLabel.disableProperty().bind(baseTextToggle.selectedProperty().not());
        baseTextPicker.disableProperty().bind(baseTextToggle.selectedProperty().not());
        backgroundTextLabel.disableProperty().bind(baseTextToggle.selectedProperty().not());
        backgroundTextPicker.disableProperty().bind(backgroundTextToggle.selectedProperty().not());
        fieldTextLabel.disableProperty().bind(baseTextToggle.selectedProperty().not());
        fieldTextPicker.disableProperty().bind(fieldTextToggle.selectedProperty().not());

        // Paint pickers
        basePicker.valueProperty().addListener(o -> createCSS());
        accentPicker.valueProperty().addListener(o -> createCSS());
        backgroundPicker.valueProperty().addListener(o -> createCSS());
        baseTextPicker.valueProperty().addListener(o -> createCSS());
        fieldBackgroundPicker.valueProperty().addListener(o -> createCSS());
        backgroundTextPicker.valueProperty().addListener(o -> createCSS());
        fieldTextPicker.valueProperty().addListener(o -> createCSS());
        focusPicker.valueProperty().addListener(o -> createCSS());
    }
    
    @FXML
    private void onSliderDragged(MouseEvent event) {
        createCSS();
    }
    
    @FXML
    private void onToggleSelected(MouseEvent event) {
        createCSS();
    }
    
     private void fontChanged() {
         
        
    }

    /**
     * Bind Slider values to the text fields
     */
    private void addTextFieldBinding(Slider slider, DoubleTextField field) {
        field.textProperty().bind(Bindings.format("%2.0f", slider.valueProperty()));
    }
    
    private StringBuilder cssBuffer = new StringBuilder();
    
    private String createCSS() {
        cssBuffer = new StringBuilder();
      
        int fontSize = (int) fontPickerController.fontProperty().get().getSize();
        int borderWidth = (int) borderWidthSlider.getValue();
        int borderWidthForPadding = (borderWidth <= 1) ? 0 : borderWidth - 1;
        int padding = (int) paddingSlider.getValue() + borderWidthForPadding;
        int borderRadius = (int) borderRadiusSlider.getValue();
        double checkPadding = (((0.25 * fontSize) + borderWidthForPadding) / fontSize);
        double radioPadding = (((0.333333 * fontSize) + borderWidthForPadding) / fontSize);
        
        cssBuffer.append(".root { ");
        cssBuffer.append("-fx-font-family: \"").append(fontPickerController.fontProperty().get().getFamily()).append("\";");
        cssBuffer.append("-fx-font-size: ").append(fontPickerController.fontProperty().get().getSize()).append("px;");
        cssBuffer.append("-fx-base: ").append(ColorEncoder.encodeColor((Color)basePicker.getValue())).append(";");
        cssBuffer.append("-fx-accent: ").append(ColorEncoder.encodeColor((Color)accentPicker.getValue())).append(";");
        cssBuffer.append("-fx-background: ").append(ColorEncoder.encodeColor((Color)backgroundPicker.getValue())).append(";");
        cssBuffer.append("-fx-focus-color: ").append(ColorEncoder.encodeColor((Color)focusPicker.getValue())).append(";");
        cssBuffer.append("-fx-control-inner-background: ").append(ColorEncoder.encodeColor((Color)fieldBackgroundPicker.getValue())).append(";");
        
        if (baseTextToggle.isSelected()) {
            baseTextToggle.setText("ON");
            cssBuffer.append("-fx-text-base-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)baseTextPicker.getValue()));
            cssBuffer.append(";");
        } else {
            baseTextToggle.setText("AUTO");
        }
        if (backgroundTextToggle.isSelected()) {
            backgroundTextToggle.setText("ON");
            cssBuffer.append("-fx-text-background-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)backgroundTextPicker.getValue()));
            cssBuffer.append(";");
        } else {
            backgroundTextToggle.setText("AUTO");
        }
        if (fieldTextToggle.isSelected()) {
            fieldTextToggle.setText("ON");
            cssBuffer.append("-fx-text-inner-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)fieldTextPicker.getValue()));
            cssBuffer.append(";");
        } else {
            fieldTextToggle.setText("AUTO");
        }
            

        double innerTopDerivation = bodyTopSlider.getValue() + ((100 - bodyTopSlider.getValue()) * (topHighlightSlider.getValue() / 100));
        double innerBottomDerivation = bodyBottomSlider.getValue() + ((100 - bodyBottomSlider.getValue()) * (bottomHighlightSlider.getValue() / 100));
        
        cssBuffer.append("    -fx-inner-border: linear-gradient(to bottom, derive(-fx-color,").append(innerTopDerivation).append("%) 0%, derive(-fx-color,").append(innerBottomDerivation).append("%) 100%);\n");

        cssBuffer.append("    -fx-body-color: linear-gradient( to bottom, ");
        cssBuffer.append("derive(-fx-color, ").append(bodyTopSlider.getValue()).append("%) 0%, ");
        if (topMiddleToggle.isSelected()) {
            cssBuffer.append("derive(-fx-color, ").append(bodyTopMiddleSlider.getValue()).append("%) 50%, ");
        }
        if (bottomMiddleToggle.isSelected()) {
            cssBuffer.append("derive(-fx-color, ").append(bodyBottomMiddleSlider.getValue()).append("%) 50.5%, ");
        }
        cssBuffer.append("derive(-fx-color, ").append(bodyBottomSlider.getValue()).append("%) 100%);\n");

        if (borderToggle.isSelected()) {
            cssBuffer.append("    -fx-outer-border: derive(-fx-color,").append(borderSlider.getValue()).append("%);\n");
        }

        if (shadowToggle.isSelected()) {
            cssBuffer.append("    -fx-shadow-highlight-color: derive(-fx-background,").append(shadowSlider.getValue()).append("%);\n");
        }

        if (inputBorderToggle.isSelected()) {
            cssBuffer.append("    -fx-text-box-border: derive(-fx-background,").append(inputBorderSlider.getValue()).append("%);\n");
        }

        cssBuffer.append("}\n");
        cssBuffer.append(".button, .toggle-button, .choice-box {\n");
        cssBuffer.append("    -fx-background-radius: ").append(borderRadius).append(", ").append(borderRadius).append(", ").append(borderRadius - 1).append(", ").append(borderRadius - 2).append(";\n");
        cssBuffer.append("    -fx-padding: ").append(padding).append("px ").append(padding).append(7).append("px ").append(padding).append("px ").append(padding).append(7).append("px;\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button {\n");
        cssBuffer.append("    -fx-background-radius: ").append(borderRadius).append(", ").append(borderRadius).append(", ").append(borderRadius - 1).append(", ").append(borderRadius - 2).append(";\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button .label {\n");
        cssBuffer.append("    -fx-padding: ").append(padding).append("px ").append(padding).append(15).append("px ").append(padding).append("px ").append(padding).append(7).append("px;\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button .arrow-button {\n");
        cssBuffer.append("    -fx-padding: ").append(padding).append("px ").append(padding).append(3).append("px ").append(padding).append("px 0px;\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".choice-box {\n");
        cssBuffer.append("    -fx-padding: 0 ").append(padding).append(3).append("px 0 0;\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".choice-box .label {\n");
        cssBuffer.append("    -fx-padding: ").append(padding).append("px ").append(padding).append(1).append("px ").append(padding).append("px ").append(padding).append(3).append("px;\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".choice-box .open-button {\n");
        cssBuffer.append("    -fx-padding: 1 0 0 ").append(padding).append(5).append("px;\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".combo-box-base:editable .text-field, .combo-box-base .arrow-button, .combo-box .list-cell {\n");
        cssBuffer.append("    -fx-padding: ").append(padding).append("px ").append(padding).append(3).append("px ").append(padding).append("px ").append(padding).append(3).append("px;\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".check-box .box {\n");
        cssBuffer.append("    -fx-padding: ").append(checkPadding).append("em;\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".radio-button .radio {\n");
        cssBuffer.append("    -fx-padding: ").append(radioPadding).append("em;\n");
        cssBuffer.append("}\n");
        if (!baseTextToggle.isSelected()) {
            cssBuffer.append(".hyperlink, {\n");
            cssBuffer.append("    -fx-text-fill: -fx-text-background-color;\n");
            cssBuffer.append("}\n");
            cssBuffer.append(".toggle-button:selected {\n");
            cssBuffer.append("    -fx-text-fill: -fx-text-base-color;\n");
            cssBuffer.append("}\n");
        }
        cssBuffer.append(".label, .check-box, .radio-button {\n");
        cssBuffer.append("    -fx-text-fill: -fx-text-background-color;\n");
        cssBuffer.append("}\n");

        cssBuffer.append(".button, .toggle-button, .check-box .box, .radio-button .radio, .choice-box, .menu-button, .tab, .combo-box-base {\n");
        cssBuffer.append("    -fx-background-insets: 0 0 -1 0, 0, ").append(borderWidth).append(", ").append(borderWidth).append(1).append(";\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".button:focused, .toggle-button:focused, .check-box:focused .box, .radio-button:focused .radio, .choice-box:focused, .menu-button:focused, .combo-box-base:focused {\n");
        cssBuffer.append("    -fx-background-insets: -1.4, 0, ").append(borderWidth).append(", ").append(borderWidth).append(1).append(";\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".combo-box-base .arrow-button {\n");
        cssBuffer.append("    -fx-background-insets: 0, ").append(borderWidth).append(", ").append(borderWidth).append(1).append(";\n");
        cssBuffer.append("}\n");

        cssBuffer.append(".choice-box .label { /* Workaround for RT-20015 */\n");
        cssBuffer.append("    -fx-text-fill: -fx-text-base-color;\n");
//        cssBuffer.append("    -fx-text-fill: blue;\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button .label { /* Workaround for RT-20015 */\n");
        cssBuffer.append("    -fx-text-fill: -fx-text-base-color;\n");
//        cssBuffer.append("    -fx-text-fill: green;\n");
        cssBuffer.append("}\n");
        
         previewController.setPreviewPanelStyle(cssBuffer.toString());

        return cssBuffer.toString();
    }

    public String getCodeString() {
        return cssBuffer.toString();
    // return cssBuffer.append(cssBuffer.toString()).toString();
    }

    private void setCodeAreaText() {
      editor.setEditorCode(StringUtil.formatCSStoString(getCodeString()));
    }


    @FXML
    private void displayEditorPane() {
            editorPane.setVisible(!editorPane.isVisible());
           
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

