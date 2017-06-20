package com.fxexperience.tools.util;

/**
 * Created by andje22 on 6/4/17.
 */
public class StyleBuilder implements SyntaxConstants {

//    int fontSize = (int) fontSizeSlider.getValue();
//    int borderWidth = (int) borderWidthSlider.getValue();
//    int borderWidthForPadding = (borderWidth <= 1) ? 0 : borderWidth - 1;
//    int padding = (int) paddingSlider.getValue() + borderWidthForPadding;
//    int borderRadius = (int) borderRadiusSlider.getValue();
//    double checkPadding = (((0.25 * fontSize) + borderWidthForPadding) / fontSize);
//    double radioPadding = (((0.333333 * fontSize) + borderWidthForPadding) / fontSize);
//
//
//
//    private String createCSS() {
//
//
//
//        StringBuilder cssBuffer = new StringBuilder();
//
//        cssBuffer.append(".root {\n");
//        //cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: " + fontSizeSlider.getValue() + "px " + "\"" + fontChoiceBox.getValue() + "\";", true, 4));
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: " + "\"" + fontChoiceBox.getValue() + "\";", true, 4));
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-font-size: " + fontSizeSlider.getValue() + "px;", true, 4));
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-base: " + basePicker.getColorString() + ";", true, 4));
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-background: " + backgroundColorPicker.getColorString() + ";", true, 4));
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-focus-color: " + focusColorPicker.getColorString() + ";", true, 4));
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-control-inner-background: " + fieldBackgroundPicker.getColorString() + ";", true, 4));
//
//        if (baseTextToggle.isSelected()) {
//            baseTextToggle.setText("ON");
//            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-base-color: "
//                    + textColorPicker.getColorString()+ ";", true, 4));
//        } else {
//            baseTextToggle.setText("AUTO");
//        }
//        if (backgroundTextToggle.isSelected()) {
//            backgroundTextToggle.setText("ON");
//            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-background-color: "
//                    + bkgdTextColorPicker.getColorString() + ";", true, 4));
//        } else {
//            backgroundTextToggle.setText("AUTO");
//        }
//        if (fieldTextToggle.isSelected()) {
//            fieldTextToggle.setText("ON");
//            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-inner-color: "
//                    + fieldTextColorPicker.getColorString() + ";", true, 4));
//        } else {
//            fieldTextToggle.setText("AUTO");
//        }
//
//        double innerTopDerivation = bodyTopSlider.getValue()
//                + ((100 - bodyTopSlider.getValue()) * (topHighlightSlider.getValue() / 100));
//        double innerBottomDerivation = bodyBottomSlider.getValue()
//                + ((100 - bodyBottomSlider.getValue()) * (bottomHighlightSlider.getValue() / 100));
//
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-inner-border: linear-gradient(to bottom, " + "derive(-fx-color," + innerTopDerivation + "%) 0%, "
//                + "derive(-fx-color," + innerBottomDerivation + "%) 100%);", true, 4));
//
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-body-color: linear-gradient( to bottom, ", false, 4));
//        cssBuffer.append(StringUtil.padWithSpaces("derive(-fx-color, "
//                + bodyTopSlider.getValue() + "%) 0%, ", false, 0));
//
//        if (topMiddleToggle.isSelected()) {
//            topMiddleToggle.setText("ON");
//            cssBuffer.append(StringUtil.padWithSpaces("derive(-fx-color, "
//                    + bodyTopMiddleSlider.getValue() + "%) 50%, ", false, 0));
//        } else {
//            topMiddleToggle.setText("AUTO");
//        }
//
//        if (bottomMiddleToggle.isSelected()) {
//            bottomMiddleToggle.setText("ON");
//            cssBuffer.append(StringUtil.padWithSpaces("derive(-fx-color, "
//                    + bodyBottomMiddleSlider.getValue() + "%) 50.5%, ", false, 0));
//        } else {
//            bottomMiddleToggle.setText("AUTO");
//        }
//
//        cssBuffer.append(StringUtil.padWithSpaces("derive(-fx-color, "
//                + bodyBottomSlider.getValue() + "%) 100%);",true, 0));
//
//        if (borderToggle.isSelected()) {
//            borderToggle.setText("ON");
//            cssBuffer.append(StringUtil.padWithSpaces("-fx-outer-border: derive(-fx-color,"
//                    + borderSlider.getValue() + "%);", true, 4));
//        } else {
//            borderToggle.setText("AUTO");
//        }
//
//        if (shadowToggle.isSelected()) {
//            shadowToggle.setText("ON");
//            cssBuffer.append(StringUtil.padWithSpaces("-fx-shadow-highlight-color: derive(-fx-background,"
//                    + shadowSlider.getValue() + "%);", true, 4));
//        } else {
//            shadowToggle.setText("AUTO");
//        }
//
//        if (inputBorderToggle.isSelected()) {
//            inputBorderToggle.setText("ON");
//            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-box-border: derive(-fx-background,"
//                    + inputBorderSlider.getValue() + "%);", true, 4));
//        } else {
//            inputBorderToggle.setText("AUTO");
//        }
//
//        cssBuffer.append("}\n");
//        cssBuffer.append(".button, .toggle-button, .choice-box {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-background-radius: "
//                + borderRadius + ", "
//                + borderRadius + ", "
//                + (borderRadius - 1) + ", "
//                + (borderRadius - 2) + ";", true, 4));
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: "
//                + padding + "px "
//                + (padding + 7) + "px "
//                + padding + "px "
//                + (padding + 7) + "px;", true, 4));
//        cssBuffer.append("}\n");
//
//        cssBuffer.append(".menu-button {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-background-radius: " + borderRadius + ", "
//                + borderRadius + ", "
//                + (borderRadius - 1) + ", "
//                + (borderRadius - 2) + ";", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".menu-button .label {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px " + (padding + 15) + "px " + padding + "px " + (padding + 7) + "px;", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".menu-button .arrow-button {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px " + (padding + 3) + "px " + padding + "px 0px;", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".choice-box {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: 0 " + (padding + 3) + "px 0 0;", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".choice-box .label {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px " + (padding + 1) + "px " + padding + "px " + (padding + 3) + "px;", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".choice-box .open-button {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: 1 0 0 " + (padding + 5) + "px;", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".combo-box-base:editable .text-field, .combo-box-base .arrow-button, " + ".combo-box .list-cell {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + padding + "px "  + (padding + 3) + "px " + padding + "px " + (padding + 3) + "px;", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".check-box .box {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + checkPadding + "em;", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".radio-button .radio {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-padding: " + radioPadding + "em;", true, 4));
//        cssBuffer.append("}\n");
//        if (!baseTextToggle.isSelected()) {
//            cssBuffer.append(".hyperlink {\n");
//            cssBuffer.append(StringUtil.padWithSpaces("-fx-fill: -fx-text-background-color;", true, 4));
//            cssBuffer.append("}\n");
//            cssBuffer.append(".toggle-button:selected {\n");
//            cssBuffer.append(StringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;", true, 4));
//            cssBuffer.append("}\n");
//        }
//        cssBuffer.append(".label, .check-box, Text, .radio-button {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: -fx-font-type; -fx-fill: -fx-text-background-color; -fx-fx-text-fill: -fx-text-background-color;", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".text {\n");
//
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-font-family: \"" + fontChoiceBox.getValue() + "\";", true, 4));
//        cssBuffer.append("}\n");
//
//        cssBuffer.append(".button, .toggle-button, .check-box .box, .radio-button .radio, "
//                + ".choice-box, .menu-button, .tab, .combo-box-base {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-background-insets: 0 0 -1 0, 0, "
//                + borderWidth + ", " + (borderWidth + 1) + ";", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".button:focused, .toggle-button:focused, .check-box:focused .box, "
//                + ".radio-button:focused .radio, .choice-box:focused, .menu-button:focused, "
//                + ".combo-box-base:focused {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-background-insets: -1.4, 0, "
//                + borderWidth + ", " + (borderWidth + 1) + ";", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".combo-box-base .arrow-button {\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-background-insets: 0, "
//                + borderWidth + ", " + (borderWidth + 1) + ";", true, 4));
//        cssBuffer.append("}\n");
//
//        cssBuffer.append(".choice-box .label { /* Workaround for RT-20015 */\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;", true, 4));
//        cssBuffer.append("}\n");
//        cssBuffer.append(".menu-button .label { /* Workaround for RT-20015 */\n");
//        cssBuffer.append(StringUtil.padWithSpaces("-fx-text-fill: -fx-text-base-color;", true, 4));
//        cssBuffer.append("}\n");
//
//        return cssBuffer.toString();
//    }
//
//    public String getCodeOutput() {
//        return createCSS();
//    }
//

}
