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
import com.paintpicker.scene.control.fields.DoubleTextField;

import com.paintpicker.utils.ColorEncoder;

import fxwebview.app.EditorController;
import java.io.IOException;
import java.text.DecimalFormat;
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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;

import javafx.scene.input.MouseEvent;

public class StyleController extends VBox {

    @FXML private TitledPane textTitlePane;
    @FXML private BorderPane previewPane;
    @FXML private StackPane editorPane;
  
    @FXML private ComboBox<Gradient> gradientComboBox;

    @FXML private Slider paddingSlider, borderWidthSlider, borderRadiusSlider, topHighlightSlider;
    @FXML private Slider bottomHighlightSlider, bodyTopSlider, bodyTopMiddleSlider, bodyBottomMiddleSlider;
    @FXML private Slider bodyBottomSlider, borderSlider, shadowSlider, inputBorderSlider;

    @FXML private DoubleTextField paddingTextField, borderWidthTextField, borderRadiusTextField;

    @FXML private ToggleButton baseTextToggle, backgroundTextToggle, fieldTextToggle, topMiddleToggle;
    @FXML private ToggleButton bottomMiddleToggle, borderToggle, shadowToggle, inputBorderToggle;

    @FXML private Label baseTextLabel, backgroundTextLabel, fieldTextLabel;
    
    @FXML private PaintPicker base, accent, background;
    @FXML private PaintPicker focus_color, txt_base_color, txt_bg_color; 
    @FXML private PaintPicker cntrl_inner_bg_color, txt_inner_bg;
    
    private final FontPickerController font = new FontPickerController();
    private final EditorController editor = new EditorController();
    private final PreviewController previewer = new PreviewController();
    
    DecimalFormat df = new DecimalFormat("##.##");
    SimpleDoubleProperty innerTopDerivation = new SimpleDoubleProperty();
    SimpleDoubleProperty innerBottomDerivation= new SimpleDoubleProperty(); 
        
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

        textTitlePane.setContent(font);
        editorPane.getChildren().add(editor);
        previewPane.setCenter(previewer);
        
        // create Integer Fields
        addTextFieldBinding(paddingSlider, paddingTextField);
        addTextFieldBinding(borderWidthSlider, borderWidthTextField);
        addTextFieldBinding(borderRadiusSlider, borderRadiusTextField);
        
        base.setValue(Color.web("#213870"));
        accent.setValue(Color.web("#0096C9"));
        background.setValue(Color.web("#2c2f33"));
        focus_color.setValue(Color.web("#0093ff"));
        txt_base_color.setValue(Color.web("#000000"));
        txt_bg_color.setValue(Color.web("#000000"));
        cntrl_inner_bg_color.setValue(Color.web("#23272a"));
        txt_inner_bg.setValue(Color.web("#000000"));
       
        addControlsListeners();

        // Populate gradient combo
        gradientComboBox.getItems().addAll(FXCollections.observableArrayList(Gradient.GRADIENTS));
        gradientComboBox.setValue(Gradient.GRADIENTS[0]);
        gradientComboBox.setOnAction(Event::consume);
        gradientComboBox.getSelectionModel().selectedItemProperty().addListener(this::onGradientSelection);
       
        gradientComboBox.setCellFactory((ListView<Gradient> gradientList) -> {
            ListCell<Gradient> cell = new ListCell<Gradient>() {
                @Override
                protected void updateItem(Gradient gradient, boolean empty) {
                    super.updateItem(gradient, empty);
                    if (empty || gradient == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(gradient.getName());
                        Region preview = new Region();
                        preview.setPrefSize(30, 30);
                        preview.setStyle("-fx-border-color: #676B6F; -fx-background-color: " + gradient.getCss());
                        setGraphic(preview);
                    }
                }
            };
            cell.setStyle("-fx-cell-size: 32;");
            return cell;
        });
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
        font.fontProperty().addListener(o -> createCSS());

        // Disabled properties
        baseTextLabel.disableProperty().bind(baseTextToggle.selectedProperty().not());
        txt_base_color.disableProperty().bind(baseTextToggle.selectedProperty().not());
        backgroundTextLabel.disableProperty().bind(baseTextToggle.selectedProperty().not());
        txt_bg_color.disableProperty().bind(backgroundTextToggle.selectedProperty().not());
        fieldTextLabel.disableProperty().bind(baseTextToggle.selectedProperty().not());
        txt_inner_bg.disableProperty().bind(fieldTextToggle.selectedProperty().not());

        // Paint pickers
        base.valueProperty().addListener(o -> createCSS());
        accent.valueProperty().addListener(o -> createCSS());
        background.valueProperty().addListener(o -> createCSS());
        txt_base_color.valueProperty().addListener(o -> createCSS());
        cntrl_inner_bg_color.valueProperty().addListener(o -> createCSS());
        txt_bg_color.valueProperty().addListener(o -> createCSS());
        txt_inner_bg.valueProperty().addListener(o -> createCSS());
        focus_color.valueProperty().addListener(o -> createCSS());
    }
    
    private void onGradientSelection(ObservableValue<? extends Object> observable, Gradient oldValue, Gradient newValue) {
        bodyTopSlider.setValue(newValue.getTopDerivation());
        bodyBottomSlider.setValue(newValue.getBottomDerivation());
        if (newValue.isShinny()) {
            topMiddleToggle.setSelected(true);
            bottomMiddleToggle.setSelected(true);
            bodyTopMiddleSlider.setValue(newValue.getTopMidDerivation());
            bodyBottomMiddleSlider.setValue(newValue.getBottomMidDerivation());
        } else {
            topMiddleToggle.setSelected(false);
            bottomMiddleToggle.setSelected(false);
        }
        onGradientChange();
    }
    
     private void onGradientChange() {
        innerTopDerivation.set(bodyTopSlider.getValue()
                + (100 - bodyTopSlider.getValue()) 
                * (topHighlightSlider.getValue() / 100));
        innerBottomDerivation.set(bodyBottomSlider.getValue()
                + (100 - bodyBottomSlider.getValue()) 
                * (bottomHighlightSlider.getValue() / 100));
        createCSS();
    }
    
    @FXML
    private void onSliderDragged(MouseEvent event) {
        onGradientChange();
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
    
    private void createCSS() {
        cssBuffer = new StringBuilder();
   
        cssBuffer.append(".root {\n  ");
        cssBuffer.append("-fx-font-family: \"").append(font.fontProperty().get().getFamily()).append("\";\n  ");
        cssBuffer.append("-fx-font-size: ").append(font.fontProperty().get().getSize()).append("px;\n  ");
        cssBuffer.append("-fx-base: ").append(ColorEncoder.encodeColor((Color)base.getValue())).append(";\n  ");
        cssBuffer.append("-fx-accent: ").append(ColorEncoder.encodeColor((Color)accent.getValue())).append(";\n  ");
        cssBuffer.append("-fx-background: ").append(ColorEncoder.encodeColor((Color)background.getValue())).append(";\n  ");
        cssBuffer.append("-fx-focus-color: ").append(ColorEncoder.encodeColor((Color)focus_color.getValue())).append(";\n  ");
        cssBuffer.append("-fx-control-inner-background: ").append(ColorEncoder.encodeColor((Color)cntrl_inner_bg_color.getValue())).append(";\n  ");
        
        if (baseTextToggle.isSelected()) {
            baseTextToggle.setText("ON");
            cssBuffer.append("-fx-text-base-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)txt_base_color.getValue()));
            cssBuffer.append(";\n  ");
        } else {
            baseTextToggle.setText("AUTO");
        }
        if (backgroundTextToggle.isSelected()) {
            backgroundTextToggle.setText("ON");
            cssBuffer.append("-fx-text-background-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)txt_bg_color.getValue()));
            cssBuffer.append(";\n  ");
        } else {
            backgroundTextToggle.setText("AUTO");
        }
        if (fieldTextToggle.isSelected()) {
            fieldTextToggle.setText("ON");
            cssBuffer.append("-fx-text-inner-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)txt_inner_bg.getValue()));
            cssBuffer.append(";\n  ");
        } else {
            fieldTextToggle.setText("AUTO");
        }
      
        cssBuffer.append("-fx-inner-border: linear-gradient(to bottom,\n    ");
        cssBuffer.append("derive(-fx-color,").append(df.format(innerTopDerivation.get())).append("%) 0%,\n    ");
        cssBuffer.append("derive(-fx-color,").append(df.format(innerBottomDerivation.get())).append("%) 100%);\n  ");

        cssBuffer.append("-fx-body-color: linear-gradient( to bottom,\n  ");
        cssBuffer.append("  derive(-fx-color, ").append(df.format(bodyTopSlider.getValue())).append("%) 0%,\n  ");
       
        if (topMiddleToggle.isSelected()) {
            cssBuffer.append("  derive(-fx-color, ")
                     .append(df.format(bodyTopMiddleSlider.getValue())).append("%) 50%,\n  ");
        }
        if (bottomMiddleToggle.isSelected()) {
            cssBuffer.append("  derive(-fx-color, ")
                     .append(df.format(bodyBottomMiddleSlider.getValue())).append("%) 50.5%,\n  ");
        }
        
        cssBuffer.append("  derive(-fx-color, ").append(df.format(bodyBottomSlider.getValue())).append("%) 100%);\n  ");

        if (borderToggle.isSelected()) {
            cssBuffer.append("-fx-outer-border: derive(-fx-color, ")
                     .append(df.format(borderSlider.getValue())).append("%);\n  ");
        }

        if (shadowToggle.isSelected()) {
            cssBuffer.append("-fx-shadow-highlight-color: derive(-fx-background,")
                     .append(df.format(shadowSlider.getValue())).append("%);\n  ");
        }

        if (inputBorderToggle.isSelected()) {
            cssBuffer.append("-fx-text-box-border: derive(-fx-background,")
                     .append(df.format(inputBorderSlider.getValue())).append("%);\n");
        }

        cssBuffer.append("}\n");

        if (!baseTextToggle.isSelected()) {
            cssBuffer.append(".hyperlink, {\n");
            cssBuffer.append("  -fx-text-fill: -fx-text-background-color;\n");
            cssBuffer.append("}\n");
            cssBuffer.append(".toggle-button:selected {\n");
            cssBuffer.append("  -fx-text-fill: -fx-text-base-color;\n");
            cssBuffer.append("}\n");
        }
        cssBuffer.append(".label,\n.check-box,\n.radio-button {\n");
        cssBuffer.append("  -fx-text-fill: -fx-text-background-color;\n");
        cssBuffer.append("}\n");

        cssBuffer.append(".choice-box .label { \n");
        cssBuffer.append("  -fx-text-fill: -fx-text-base-color;\n");
        cssBuffer.append("}\n");
        cssBuffer.append(".menu-button .label { \n");
        cssBuffer.append("  -fx-text-fill: -fx-text-base-color;\n");
        cssBuffer.append("}\n");
        
        previewer.setPreviewPanelStyle(cssBuffer.toString());
    }

    public String getCodeString() {
        return cssBuffer.toString();
    }

    private void setCodeAreaText() {
      editor.setEditorCode(cssBuffer.toString());
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

