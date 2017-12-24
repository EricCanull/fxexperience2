package com.fxexperience.tools.util.css;

public class CSSBaseStyle {

    private static final String [] defaultCSS = {
            ".root {",
            "    -fx-font-family: \"null\";",  // 1
            "    -fx-font-size: 12px;",      // 2
            "    -fx-base: #213870;",        // 3
            "    -fx-background: #2C2F33;",  // 4
            "    -fx-focus-color: #0093FF;", // 5
            "    -fx-control-inner-background: #23272A;", // 6
            "    -fx-inner-border: linear-gradient(to bottom,", // 7-9
            "        derive(-fx-color,68.0%) 0%,",
            "        derive(-fx-color,52.0%) 100%);",
            "    -fx-body-color: linear-gradient( to bottom,", // 10-12
            "        derive(-fx-color, 20.0%) 0%,",
            "        derive(-fx-color, 20.0%) 100%);",
            "}",
            "",
            ".label,",
            ".button,",
            ".toggle-button,",
            ".choice-box,",
            ".text {",
            "    -fx-font-family: 'Roboto';", // 20
            "}",
            "",
            ".button,",
            ".toggle-button,",
            ".choice-box {",
            "    -fx-background-radius: 5, 5, 4, 3;", // 26
            "    -fx-padding: 3px 10px 3px 10px;",    // 27
            "}",
            "",
            ".menu-button {",
            "    -fx-background-radius: 5, 5, 4, 3;", // 31
            "}",
            "",
            ".menu-button .label {",
            "    -fx-padding: 3px 18px 3px 10px;", // 35
            "}",
            "",
            ".menu-button .arrow-button {",
            "    -fx-padding: 3px 6px 3px 0px;",   // 39
            "}",
            "",
            ".choice-box {",
            "    -fx-padding: 0 6px 0 0;",  // 43
            "}",
            " ",
            ".choice-box .label {",
            "    -fx-padding: 3px 4px 3px 6px;",  // 47
            "}",
            "",
            ".choice-box .open-button {",
            "    -fx-padding: 1 0 0 8px;",  // 51
            "}",
            "",
            ".combo-box-base:editable .text-field,",
            ".combo-box-base .arrow-button,",
            ".combo-box .list-cell {",
            "    -fx-padding: 3px 6px 3px 6px;",  // 57
            "}",
            "",
            ".hyperlink {",
            "    -fx-fill: -fx-text-background-color;", // 61
            "}",
            "",
            ".toggle-button:selected {",
            "    -fx-text-fill: -fx-text-base-color;", // 65
            "}",
            "",
            ".label, .check-box, .text, .radio-button {",
            "    -fx-font-family: -fx-font-type;",           // 69
            "    -fx-fill: -fx-text-background-color;",      // 70
            "    -fx-text-fill: -fx-text-background-color;", // 71
            "}",
            "",
            ".text {",
            "    -fx-font-family: 'null';", // 75
            "}",
            "",
            ".button,",
            ".toggle-button,",
            ".check-box .box,",
            ".radio-button .radio,",
            ".choice-box,",
            ".menu-button,",
            ".tab, .combo-box-base {",
            "    -fx-background-insets: 0 0 -1 0, 0, 1, 2;", // 85
            "}",
            "",
            ".button:focused,",
            ".toggle-button:focused,",
            ".check-box:focused .box,",
            ".radio-button:focused .radio,",
            ".choice-box:focused,",
            ".menu-button:focused,",
            ".combo-box-base:focused {",
            "    -fx-background-insets: -1.4, 0, 1, 2;", // 95
            "}",
            "",
            ".combo-box-base .arrow-button {",
            "    -fx-background-insets: 0, 1, 2;", // 99
            "}",
            "",
            ".choice-box .label { /* Workaround for RT-20015 */",
            "    -fx-text-fill: -fx-text-base-color;", // 103
            "}",
            "",
            ".menu-button .label { /* Workaround for RT-20015 */",
            "    -fx-text-fill: -fx-text-base-color;", // 107
            "}",
            "",
    };

    public static String[] getCodeArray() {
        return defaultCSS;
    }

    public static String getCodeString() {
        return String.join("\n", defaultCSS);
    }

    public static String getUpdatedStyle(String[] styleArray, String text, int index) {
        styleArray[index] = text;

        return String.join("\n", styleArray);
    }

    public CSSBaseStyle() {

    }

}