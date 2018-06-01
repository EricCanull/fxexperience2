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
    @FXML private BorderPane previewContainer;
    @FXML private StackPane editorContainer;
    @FXML private ComboBox<Gradient> gradientComboBox;
    
    @FXML private PaintPicker ppBase, ppAccent, ppBackground, ppTxtBase,
                  ppTxtBg, ppCntrlInnerBg, ppTxtInnerBg, ppFocus;
    
    @FXML private CheckBox cbTxtBase, cbTxtBg, cbTxtInnerBg,
                  cbBorder, cbShadow, cbInputBorder, cbTopMid, cbBottomMid;
    
    @FXML private Slider slPadding, slBorderWidth, slBorderRadius, slTopHlight,
                  slBottomHlight, slTopBody, slTopMidBody, slBottomMidBody,
                  slBottomBody, slBorder, slShadow, slInputBorder;
   
    @FXML private Label lblTxtBase, lblTxtBg, lblTxtInnerBg;
    
    @FXML private DoubleTextField tfPadding, tfBorderWidth, tfBorderRadius;

    private final FontPicker fontPane = new FontPicker();
    private final EditorController editorPane = new EditorController();
    private final PreviewController previewPane = new PreviewController();

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

        textTitlePane.setContent(fontPane);
        editorContainer.getChildren().add(editorPane);
        previewContainer.setCenter(previewPane);
        
        // create Integer Fields
        addTextFieldBinding(slPadding, tfPadding);
        addTextFieldBinding(slBorderWidth, tfBorderWidth);
        addTextFieldBinding(slBorderRadius, tfBorderRadius);
        
        // set default paint picker colors
        ppBase.setValue(Color.web("#213870"));
        ppAccent.setValue(Color.web("#0096C9"));
        ppBackground.setValue(Color.web("#2c2f33"));
        ppFocus.setValue(Color.web("#0093ff"));
        ppTxtBase.setValue(Color.web("#000000"));
        ppTxtBg.setValue(Color.web("#000000"));
        ppCntrlInnerBg.setValue(Color.web("#23272a"));
        ppTxtInnerBg.setValue(Color.web("#000000"));
       
        // add listeners to paint pickers
        ppBase.valueProperty().addListener(o -> createCSS());
        ppAccent.valueProperty().addListener(o -> createCSS());
        ppBackground.valueProperty().addListener(o -> createCSS());
        ppTxtBase.valueProperty().addListener(o -> createCSS());
        ppCntrlInnerBg.valueProperty().addListener(o -> createCSS());
        ppTxtBg.valueProperty().addListener(o -> createCSS());
        ppTxtInnerBg.valueProperty().addListener(o -> createCSS());
        ppFocus.valueProperty().addListener(o -> createCSS());
       
        // add listener to font choice box
        fontPane.fontProperty().addListener(o -> fontChanged());

        // bind disabled properties
        slTopMidBody.disableProperty().bind(cbTopMid.selectedProperty().not());
        slBottomMidBody.disableProperty().bind(cbBottomMid.selectedProperty().not());
        slBorder.disableProperty().bind(cbBorder.selectedProperty().not());
        slShadow.disableProperty().bind(cbShadow.selectedProperty().not());
        slInputBorder.disableProperty().bind(cbInputBorder.selectedProperty().not());
     
        lblTxtBase.disableProperty().bind(cbTxtBase.selectedProperty().not());
        lblTxtBg.disableProperty().bind(cbTxtBg.selectedProperty().not());
        lblTxtInnerBg.disableProperty().bind(cbTxtInnerBg.selectedProperty().not());
        ppTxtBase.disableProperty().bind(cbTxtBase.selectedProperty().not());
        ppTxtBg.disableProperty().bind(cbTxtBg.selectedProperty().not());
        ppTxtInnerBg.disableProperty().bind(cbTxtInnerBg.selectedProperty().not());

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
                        preview.setStyle("-fx-color: #4d8cf5; -fx-border-color: #676B6F; -fx-background-color: " + gradient.getCss());
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
        slTopBody.setValue(newValue.getTopDerivation());
        slBottomBody.setValue(newValue.getBottomDerivation());
        if (newValue.isShinny()) {
            cbTopMid.setSelected(true);
            cbBottomMid.setSelected(true);
            slTopMidBody.setValue(newValue.getTopMidDerivation());
            slBottomMidBody.setValue(newValue.getBottomMidDerivation());
        } else {
            cbTopMid.setSelected(false);
            cbBottomMid.setSelected(false);
        }
        onGradientChange();
    }
    
     private void onGradientChange() {
        innerTopDerivation.set(slTopBody.getValue()
                + (100 - slTopBody.getValue()) 
                * (slTopHlight.getValue() / 100));
        innerBottomDerivation.set(slBottomBody.getValue()
                + (100 - slBottomBody.getValue()) 
                * (slBottomHlight.getValue() / 100));
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
        previewPane.setFont(fontPane.fontProperty().get());
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
        cssBuffer.append("-fx-font-family: \"").append(fontPane.fontProperty().get().getFamily()).append("\";\n  ");
        cssBuffer.append("-fx-font-size: ").append(fontPane.fontProperty().get().getSize()).append("px;\n  ");
        cssBuffer.append("-fx-base: ").append(ColorEncoder.encodeColor((Color)ppBase.getValue())).append(";\n  ");
        cssBuffer.append("-fx-accent: ").append(ColorEncoder.encodeColor((Color)ppAccent.getValue())).append(";\n  ");
        cssBuffer.append("-fx-background: ").append(ColorEncoder.encodeColor((Color)ppBackground.getValue())).append(";\n  ");
        cssBuffer.append("-fx-focus-color: ").append(ColorEncoder.encodeColor((Color)ppFocus.getValue())).append(";\n  ");
        cssBuffer.append("-fx-control-inner-background: ").append(ColorEncoder.encodeColor((Color)ppCntrlInnerBg.getValue())).append(";\n  ");
        
        if (cbTxtBase.isSelected()) {
            cssBuffer.append("-fx-text-base-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)ppTxtBase.getValue()));
            cssBuffer.append(";\n  ");
        }
        if (cbTxtBg.isSelected()) {
            cssBuffer.append("-fx-text-background-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)ppTxtBg.getValue()));
            cssBuffer.append(";\n  ");
        }
        if (cbTxtInnerBg.isSelected()) {
            cssBuffer.append("-fx-text-inner-color: ");
            cssBuffer.append(ColorEncoder.encodeColor((Color)ppTxtInnerBg.getValue()));
            cssBuffer.append(";\n  ");
        }      
        cssBuffer.append("-fx-inner-border: linear-gradient(to bottom,\n    ");
        cssBuffer.append("derive(-fx-color,").append(df.format(innerTopDerivation.get())).append("%) 0%,\n    ");
        cssBuffer.append("derive(-fx-color,").append(df.format(innerBottomDerivation.get())).append("%) 100%);\n  ");

        cssBuffer.append("-fx-body-color: linear-gradient( to bottom,\n  ");
        cssBuffer.append("  derive(-fx-color, ").append(df.format(slTopBody.getValue())).append("%) 0%,\n  ");
       
        if (cbTopMid.isSelected()) {
            cssBuffer.append("  derive(-fx-color, ")
                     .append(df.format(slTopMidBody.getValue())).append("%) 50%,\n  ");
        }
        if (cbBottomMid.isSelected()) {
            cssBuffer.append("  derive(-fx-color, ")
                     .append(df.format(slBottomMidBody.getValue())).append("%) 50.5%,\n  ");
        }        
        cssBuffer.append("  derive(-fx-color, ").append(df.format(slBottomBody.getValue())).append("%) 100%);\n  ");

        if (cbBorder.isSelected()) {
            cssBuffer.append("-fx-outer-border: derive(-fx-color, ")
                     .append(df.format(slBorder.getValue())).append("%);\n  ");
        }
        if (cbShadow.isSelected()) {
            cssBuffer.append("-fx-shadow-highlight-color: derive(-fx-background,")
                     .append(df.format(slShadow.getValue())).append("%);\n  ");
        }
        if (cbInputBorder.isSelected()) {
            cssBuffer.append("-fx-text-box-border: derive(-fx-background,")
                     .append(df.format(slInputBorder.getValue())).append("%);\n");
        }

        cssBuffer.append("}\n");

//        if (cbTxtBase.isSelected()) {
//            cssBuffer.append(".hyperlink, .text {\n");
//            cssBuffer.append("  -fx-text-fill: -fx-text-background-color;\n");
//            cssBuffer.append("}\n");
//            cssBuffer.append(".toggle-button:selected {\n");
//            cssBuffer.append("  -fx-text-fill: -fx-text-base-color;\n");
//            cssBuffer.append("}\n");
//        }
//        cssBuffer.append(".label,\n.check-box,\n.radio-button {\n");
//        cssBuffer.append("  -fx-text-fill: -fx-text-background-color;\n");
//        cssBuffer.append("}\n");
//
//        cssBuffer.append(".choice-box .label { \n");
//        cssBuffer.append("  -fx-text-fill: -fx-text-base-color;\n");
//        cssBuffer.append("}\n");
//        cssBuffer.append(".menu-button .label { \n");
//        cssBuffer.append("  -fx-text-fill: -fx-text-base-color;\n");
//        cssBuffer.append("}\n");
        
        previewPane.setPreviewPanelStyle(cssBuffer.toString());
    }

    public String getCodeString() {
        return cssBuffer.toString();
    }

    private void setEditorCode() {
      editorPane.setEditorCode(cssBuffer.toString());
    }

    @FXML
    private void displayEditorPane() {
        editorContainer.setVisible(!editorContainer.isVisible());
        if (editorContainer.isVisible()) {
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

