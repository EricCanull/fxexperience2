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

import com.fxexperience.previewer.controller.PreviewController;
import com.fxexperience.tools.util.Gradient;
import com.fxexperience.tools.util.FileUtil;
import com.paintpicker.scene.control.fields.DoubleTextField;
import com.paintpicker.utils.ColorEncoder;
import fxfontpicker.app.FontPicker;

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
    
    @FXML private PaintPicker pp_base, pp_accent, pp_background, pp_txt_base,
                  pp_txt_bg, pp_cntrl_inner_bg, pp_txt_inner_bg, pp_focus;
    
    @FXML private CheckBox cb_txt_base, cb_txt_bg, cb_txt_inner_bg,
                  cb_border, cb_shadow, cb_input_border, cb_top_mid, cb_bottom_mid;
    
    @FXML private Slider sl_padding, sl_border_width, sl_border_radius, sl_top_hlight,
                  sl_bottom_hlight, sl_top_body, sl_top_mid_body, sl_bottom_mid_body,
                  sl_bottom_body, sl_border, sl_shadow, sl_input_border;
   
    @FXML private Label lbl_txt_base, lbl_txt_bg, lbl_txt_inner_bg;
    
    @FXML private DoubleTextField tf_padding, tf_border_width, tf_border_radius;

    private final FontPicker font = new FontPicker();
    private final EditorController editor = new EditorController();
    private final PreviewController previewer = new PreviewController();

    private final DecimalFormat df = new DecimalFormat("##.##");
    private final SimpleDoubleProperty innerTopDerivation = new SimpleDoubleProperty();
    private final SimpleDoubleProperty innerBottomDerivation = new SimpleDoubleProperty();

    public StyleController() {
       
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(StyleController.class.getResource("/fxml/FXMLStylePanel.fxml")); //NOI18N
        loader.setController(StyleController.this);
        loader.setRoot(StyleController.this);
        try {
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(StyleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        initialize();
    }

    private void initialize() {

        textTitlePane.setContent(font);
        editorPane.getChildren().add(editor);
        previewPane.setCenter(previewer);
        
        // create Integer Fields
        addTextFieldBinding(sl_padding, tf_padding);
        addTextFieldBinding(sl_border_width, tf_border_width);
        addTextFieldBinding(sl_border_radius, tf_border_radius);
        
        // set default paint picker colors
        pp_base.setValue(Color.web("#213870"));
        pp_accent.setValue(Color.web("#0096C9"));
        pp_background.setValue(Color.web("#2c2f33"));
        pp_focus.setValue(Color.web("#0093ff"));
        pp_txt_base.setValue(Color.web("#000000"));
        pp_txt_bg.setValue(Color.web("#000000"));
        pp_cntrl_inner_bg.setValue(Color.web("#23272a"));
        pp_txt_inner_bg.setValue(Color.web("#000000"));
       
        // add listeners to paint pickers
        pp_base.valueProperty().addListener(o -> createCSS());
        pp_accent.valueProperty().addListener(o -> createCSS());
        pp_background.valueProperty().addListener(o -> createCSS());
        pp_txt_base.valueProperty().addListener(o -> createCSS());
        pp_cntrl_inner_bg.valueProperty().addListener(o -> createCSS());
        pp_txt_bg.valueProperty().addListener(o -> createCSS());
        pp_txt_inner_bg.valueProperty().addListener(o -> createCSS());
        pp_focus.valueProperty().addListener(o -> createCSS());
       
        // add listener to font choice box
        font.fontProperty().addListener(o -> fontChanged());

        // bind disabled properties
        sl_top_mid_body.disableProperty().bind(cb_top_mid.selectedProperty().not());
        sl_bottom_mid_body.disableProperty().bind(cb_bottom_mid.selectedProperty().not());
        sl_border.disableProperty().bind(cb_border.selectedProperty().not());
        sl_shadow.disableProperty().bind(cb_shadow.selectedProperty().not());
        sl_input_border.disableProperty().bind(cb_input_border.selectedProperty().not());
     
        lbl_txt_base.disableProperty().bind(cb_txt_base.selectedProperty().not());
        lbl_txt_bg.disableProperty().bind(cb_txt_bg.selectedProperty().not());
        lbl_txt_inner_bg.disableProperty().bind(cb_txt_inner_bg.selectedProperty().not());
        pp_txt_base.disableProperty().bind(cb_txt_base.selectedProperty().not());
        pp_txt_bg.disableProperty().bind(cb_txt_bg.selectedProperty().not());
        pp_txt_inner_bg.disableProperty().bind(cb_txt_inner_bg.selectedProperty().not());

        // populate gradient combo
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
                        System.out.println(gradient.getCss());
                        preview.setStyle("-fx-border-color: #676B6F; -fx-background-color: " + gradient.getCss());
                        setGraphic(preview);
                    }
                }
            };
            cell.setStyle("-fx-cell-size: 32;");
            return cell;
        });
        
        createCSS();
    }
    
    private void onGradientSelection(ObservableValue<? extends Object> observable, Gradient oldValue, Gradient newValue) {
        sl_top_body.setValue(newValue.getTopDerivation());
        sl_bottom_body.setValue(newValue.getBottomDerivation());
        if (newValue.isShinny()) {
            cb_top_mid.setSelected(true);
            cb_bottom_mid.setSelected(true);
            sl_top_mid_body.setValue(newValue.getTopMidDerivation());
            sl_bottom_mid_body.setValue(newValue.getBottomMidDerivation());
        } else {
            cb_top_mid.setSelected(false);
            cb_bottom_mid.setSelected(false);
        }
        onGradientChange();
    }
    
     private void onGradientChange() {
        innerTopDerivation.set(sl_top_body.getValue()
                + (100 - sl_top_body.getValue()) 
                * (sl_top_hlight.getValue() / 100));
        innerBottomDerivation.set(sl_bottom_body.getValue()
                + (100 - sl_bottom_body.getValue()) 
                * (sl_bottom_hlight.getValue() / 100));
        createCSS();
    }
    
    @FXML
    private void onSliderDragged(MouseEvent event) {
        onGradientChange();
    }
    
    @FXML
    private void onCheckBoxSelected(ActionEvent event) {
         createCSS();
    }
    
    private void fontChanged() {
        previewer.setFont(font.fontProperty().get());
        createCSS();
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
        cssBuffer.append("-fx-base: ").append(ColorEncoder.encodeColor((Color)pp_base.getValue())).append(";\n  ");
        cssBuffer.append("-fx-accent: ").append(ColorEncoder.encodeColor((Color)pp_accent.getValue())).append(";\n  ");
        cssBuffer.append("-fx-background: ").append(ColorEncoder.encodeColor((Color)pp_background.getValue())).append(";\n  ");
        cssBuffer.append("-fx-focus-color: ").append(ColorEncoder.encodeColor((Color)pp_focus.getValue())).append(";\n  ");
        cssBuffer.append("-fx-control-inner-background: ").append(ColorEncoder.encodeColor((Color)pp_cntrl_inner_bg.getValue())).append(";\n  ");
        
        if (cb_txt_base.isSelected()) {
            cssBuffer.append("-fx-text-base-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)pp_txt_base.getValue()));
            cssBuffer.append(";\n  ");
        }
        if (cb_txt_bg.isSelected()) {
            cssBuffer.append("-fx-text-background-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)pp_txt_bg.getValue()));
            cssBuffer.append(";\n  ");
        }
        if (cb_txt_inner_bg.isSelected()) {
            cssBuffer.append("-fx-text-inner-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)pp_txt_inner_bg.getValue()));
            cssBuffer.append(";\n  ");
        }      
        cssBuffer.append("-fx-inner-border: linear-gradient(to bottom,\n    ");
        cssBuffer.append("derive(-fx-color,").append(df.format(innerTopDerivation.get())).append("%) 0%,\n    ");
        cssBuffer.append("derive(-fx-color,").append(df.format(innerBottomDerivation.get())).append("%) 100%);\n  ");

        cssBuffer.append("-fx-body-color: linear-gradient( to bottom,\n  ");
        cssBuffer.append("  derive(-fx-color, ").append(df.format(sl_top_body.getValue())).append("%) 0%,\n  ");
       
        if (cb_top_mid.isSelected()) {
            cssBuffer.append("  derive(-fx-color, ")
                     .append(df.format(sl_top_mid_body.getValue())).append("%) 50%,\n  ");
        }
        if (cb_bottom_mid.isSelected()) {
            cssBuffer.append("  derive(-fx-color, ")
                     .append(df.format(sl_bottom_mid_body.getValue())).append("%) 50.5%,\n  ");
        }        
        cssBuffer.append("  derive(-fx-color, ").append(df.format(sl_bottom_body.getValue())).append("%) 100%);\n  ");

        if (cb_border.isSelected()) {
            cssBuffer.append("-fx-outer-border: derive(-fx-color, ")
                     .append(df.format(sl_border.getValue())).append("%);\n  ");
        }
        if (cb_shadow.isSelected()) {
            cssBuffer.append("-fx-shadow-highlight-color: derive(-fx-background,")
                     .append(df.format(sl_shadow.getValue())).append("%);\n  ");
        }
        if (cb_input_border.isSelected()) {
            cssBuffer.append("-fx-text-box-border: derive(-fx-background,")
                     .append(df.format(sl_input_border.getValue())).append("%);\n");
        }

        cssBuffer.append("}\n");

        if (cb_txt_base.isSelected()) {
            cssBuffer.append(".hyperlink, .text {\n");
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

    private void setEditorCode() {
      editor.setEditorCode(cssBuffer.toString());
    }

    @FXML
    private void displayEditorPane() {
        editorPane.setVisible(!editorPane.isVisible());
        if (editorPane.isVisible()) {
            setEditorCode();
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

