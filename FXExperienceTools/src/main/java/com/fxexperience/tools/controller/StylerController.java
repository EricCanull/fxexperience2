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

import com.fxexperience.javafx.scene.control.IntegerField;
import com.fxexperience.javafx.scene.control.popup.PopupEditor;
import com.fxexperience.tools.handler.ToolsHandler;
import com.fxexperience.tools.util.Gradient;
import com.fxexperience.tools.util.PropertyValue;
import com.fxexperience.tools.util.StringUtil;
import com.fxexperience.tools.util.SyntaxConstants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.Collections;


import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StylerController implements Initializable, ToolsHandler {

    private final HashMap<String, Object> styleMap = new HashMap<>();

    @FXML private SplitPane rootSplitPane;

    // Common Properties
    @FXML private GridPane textGridPanel;
    @FXML private GridPane sizeGridPanel;
    @FXML private ChoiceBox<String> fontChoiceBox;
    @FXML private Slider fontSizeSlider;
    @FXML private Slider paddingSlider;
    @FXML private Slider borderWidthSlider;
    @FXML private Slider borderRadiusSlider;
   
    // Tabs
    @FXML private GridPane simpleGridPane;
      
    private final PopupEditor basePicker = new PopupEditor(Color.web("#D0D0D0"));
    private final PopupEditor backgroundColorPicker = new PopupEditor(Color.web("#f4f4f4"));
    private final PopupEditor focusColorPicker = new PopupEditor(Color.web("#0093ff"));
    private final PopupEditor textColorPicker = new PopupEditor(Color.web("#000000"));
    private final PopupEditor bkgdTextColorPicker = new PopupEditor(Color.web("#000000"));
    private final PopupEditor fieldBackgroundPicker = new PopupEditor(Color.web("#ffffff"));
    private final PopupEditor fieldTextColorPicker = new PopupEditor(Color.web("#000000"));
   
    @FXML private Slider topHighlightSlider;
    @FXML private Slider bottomHighlightSlider;
    @FXML private ComboBox<Gradient> gradientComboBox;
   
    // Advanced Tab
    @FXML private Slider bodyTopSlider;
    @FXML private Slider bodyTopMiddleSlider;
    @FXML private Slider bodyBottomMiddleSlider;
    @FXML private Slider bodyBottomSlider;
    @FXML private Slider borderSlider;
    @FXML private Slider shadowSlider;
    @FXML private Slider inputBorderSlider;

    @FXML private ToggleButton baseTextToggle;
    @FXML private ToggleButton backgroundTextToggle;
    @FXML private ToggleButton fieldTextToggle;
   
    @FXML private ToggleButton topMiddleToggle;
    @FXML private ToggleButton bottomMiddleToggle;
    @FXML private ToggleButton borderToggle;
    @FXML private ToggleButton shadowToggle;
    @FXML private ToggleButton inputBorderToggle;

    @FXML private BorderPane previewPane;
   
    private PreviewPanelController previewPanel;
   
    @Override public void initialize(URL url, ResourceBundle rb) {

        previewPanel = new PreviewPanelController();
        previewPane.setCenter(previewPanel);

        // populate fonts choicebox
        fontChoiceBox.getItems().setAll(Font.getFamilies());
        fontChoiceBox.getSelectionModel().select("System");

        // create listener to call update css
        ChangeListener<Object> updateCssListener = (ObservableValue<?> arg0, Object arg1, Object arg2) -> {
            updateCss();
        };

        // create listener to call update css
        ChangeListener<Object> basePickerListener = (ObservableValue<?> arg0, Object arg1, Object arg2) -> {
            basePickerAction();
        };

        // add listeners to call update css
        fontChoiceBox.valueProperty().addListener(updateCssListener);
        fontSizeSlider.valueProperty().addListener(updateCssListener);
        paddingSlider.valueProperty().addListener(updateCssListener);
        borderRadiusSlider.valueProperty().addListener(updateCssListener);
        borderWidthSlider.valueProperty().addListener(updateCssListener);

        // create Integer Fields
        createNumberFieldForSlider(fontSizeSlider, textGridPanel, 1);
        createNumberFieldForSlider(paddingSlider, sizeGridPanel, 0);
        createNumberFieldForSlider(borderWidthSlider, sizeGridPanel, 1);
        createNumberFieldForSlider(borderRadiusSlider, sizeGridPanel, 2);

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

        // basePicker.colorProperty().addListener(basePickerListener);

        basePicker.colorProperty().addListener(updateCssListener);
        backgroundColorPicker.colorProperty().addListener(updateCssListener);
        focusColorPicker.colorProperty().addListener(updateCssListener);
        textColorPicker.colorProperty().addListener(updateCssListener);
        baseTextToggle.selectedProperty().addListener(updateCssListener);
        textColorPicker.disableProperty().bind(baseTextToggle.selectedProperty().not());
        fieldBackgroundPicker.colorProperty().addListener(updateCssListener);
        fieldTextColorPicker.colorProperty().addListener(updateCssListener);
        fieldTextToggle.selectedProperty().addListener(updateCssListener);
        fieldTextColorPicker.disableProperty().bind(fieldTextToggle.selectedProperty().not());
        bkgdTextColorPicker.colorProperty().addListener(updateCssListener);
        backgroundTextToggle.selectedProperty().addListener(updateCssListener);
        bkgdTextColorPicker.disableProperty().bind(backgroundTextToggle.selectedProperty().not());

        // add listeners to sliders
        topHighlightSlider.valueProperty().addListener(updateCssListener);
        bottomHighlightSlider.valueProperty().addListener(updateCssListener);
       
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
            updateCss();
        });
        
        gradientComboBox.getSelectionModel().select(0);

        // Advanced toggle buttons
        topMiddleToggle.selectedProperty().addListener(updateCssListener);
        bottomMiddleToggle.selectedProperty().addListener(updateCssListener);
        borderToggle.selectedProperty().addListener(updateCssListener);
        shadowToggle.selectedProperty().addListener(updateCssListener);
        inputBorderToggle.selectedProperty().addListener(updateCssListener);

        bodyTopSlider.valueProperty().addListener(updateCssListener);
        bodyTopMiddleSlider.valueProperty().addListener(updateCssListener);
        bodyBottomMiddleSlider.valueProperty().addListener(updateCssListener);
        bodyBottomSlider.valueProperty().addListener(updateCssListener);
        borderSlider.valueProperty().addListener(updateCssListener);
        shadowSlider.valueProperty().addListener(updateCssListener);
        inputBorderSlider.valueProperty().addListener(updateCssListener);

        bodyTopMiddleSlider.disableProperty().bind(topMiddleToggle.selectedProperty().not());
        bodyBottomMiddleSlider.disableProperty().bind(bottomMiddleToggle.selectedProperty().not());
        borderSlider.disableProperty().bind(borderToggle.selectedProperty().not());
        shadowSlider.disableProperty().bind(shadowToggle.selectedProperty().not());
        inputBorderSlider.disableProperty().bind(inputBorderToggle.selectedProperty().not());

    }
   
    /**
     * The SceneBuilder does not work with custom controls yet so create a 
     * IntegerField and add to GridPane
     * 
     * @param slider The slider to bind to and sit next to
     * @param parent The GridPane to add to
     */
    private void createNumberFieldForSlider(Slider slider, GridPane parent, int row){
       final int column = 2;

        IntegerField field = new IntegerField();
        field.setMaxHeight(IntegerField.USE_PREF_SIZE);
        field.setMaxWidth(IntegerField.USE_PREF_SIZE);
        field.setPrefColumnCount(3);
        parent.getChildren().add(field);
        GridPane.setColumnIndex(field, column);
        GridPane.setRowIndex(field, row);
        field.valueProperty().bindBidirectional(slider.valueProperty());
    }

    public SplitPane getRootSplitPane() {
        return rootSplitPane;
    }

    private void updateCss() {
       String css = createCSS(true);
       previewPanel.setPreviewPanelStyle(css);
    }

    private void basePickerAction() {
        String property = SyntaxConstants.TAB + SyntaxConstants.BASE;
        String value = basePicker.getColorText();
        PropertyValue p = new PropertyValue(property, value);

        addStyleElement(".root { \n", p);
    }

    private void addStyleElement(String element, PropertyValue propertyValue) {
        if(styleMap.isEmpty()) {
         styleMap.put(element, propertyValue);
        } else {
            styleMap.forEach((String k, Object v) ->{
//                System.out.printf("%s%s%n", k, v.toString());
                if (element.equals(k) &&
                    propertyValue.toString().equals(v)) {
                    System.out.printf("%s%s%s%n", "Prop same:", k, v.toString());
                    return;
                } else  if (element.equals(k) &&
                        !propertyValue.toString().equals(v)) {
                    System.out.printf("%s%s%s%n", "Prop not same: ", k, v.toString());
                }
            });
        }
    }
     
    private String createCSS(Boolean isRoot) {

        int fontSize = (int) fontSizeSlider.getValue();
        int borderWidth = (int) borderWidthSlider.getValue();
        int borderWidthForPadding = (borderWidth <= 1) ? 0 : borderWidth - 1;
        int padding = (int) paddingSlider.getValue() + borderWidthForPadding;
        int borderRadius = (int) borderRadiusSlider.getValue();
        double checkPadding = (((0.25 * fontSize) + borderWidthForPadding) / fontSize);
        double radioPadding = (((0.333333 * fontSize) + borderWidthForPadding) / fontSize);

       
        StringBuilder cssBuffer = new StringBuilder();
        if (isRoot) {
            cssBuffer.append(".root {\n");
        } else {
            cssBuffer.append("#Preview-area {\n");
        }

        //cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: " + fontSizeSlider.getValue() + "px " + "\"" + fontChoiceBox.getValue() + "\";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: " + "\"" + fontChoiceBox.getValue() + "\";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-font-size: " + fontSizeSlider.getValue() + "px;", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-base: " + basePicker.getColorText() + ";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-background: " + backgroundColorPicker.getColorText() + ";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-focus-color: " + focusColorPicker.getColorText() + ";", true, 4));
        cssBuffer.append(StringUtil.padWithSpaces("-fx-control-inner-background: " + fieldBackgroundPicker.getWebColor() + ";", true, 4));
        
        if (baseTextToggle.isSelected()) {
            baseTextToggle.setText("ON"); 
            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-base-color: "
                    + textColorPicker.getWebColor() + ";", true, 4));
        } else {
             baseTextToggle.setText("AUTO");
        }
        if (backgroundTextToggle.isSelected()) {
            backgroundTextToggle.setText("ON"); 
            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-background-color: "
                    + bkgdTextColorPicker.getWebColor() + ";", true, 4));
        } else {
             backgroundTextToggle.setText("AUTO");
        }
        if (fieldTextToggle.isSelected()) {
             fieldTextToggle.setText("ON"); 
            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-inner-color: "
                    + fieldTextColorPicker.getWebColor() + ";", true, 4));
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
        cssBuffer.append("}\n");

        return cssBuffer.toString();
    }

    @Override
    public void setParentTool(Node parentTool) {
        Node stylerController = parentTool;
    }

    @Override
    public String getCodeOutput() {
     return createCSS(true);
    }

    @Override
    public void startAnimations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stopAnimations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

