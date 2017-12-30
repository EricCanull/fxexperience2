package com.fxexperience.tools.ui;

import com.fxexperience.tools.controller.StyleController;
import com.fxexperience.tools.util.CSSFormatter;
import com.fxexperience.tools.util.css.CSSBaseStyle;
import com.fxexperience.tools.util.css.CSSTheme;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CssEditor extends CodeArea {

    public CssEditor() {
        getStylesheets().add(StyleController.class.getResource("/styles/code_area.css").toExternalForm());
        setParagraphGraphicFactory(LineNumberFactory.get(this));
        richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
                .subscribe(change -> this.setStyleSpans(0, CSSFormatter.computeHighlighting(this.getText())));
    }

//    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
//        return computeHighlightingRegex(text);
//        //return computeHighlightingByRegex(text);
//    }

    // private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String SELECTOR_PATTERN = "\\.([^ 0-9,{])+|#([^ 0-9,{])+";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String COLON_PATTERN = ":";
    private static final String SEMICOLON_PATTERN = ";";
    private static final String COMMA_PATTERN = ",";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    //private static final String COMMENT_REGEX = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String MUTI_COMMENT_REGE = "/\\*+([^*]|(\\*+[^*/]))*\\*+/";
    private static final String ENTRY_REGE = "-fx-([^: 0-9,;\\n])+";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<SELECTOR>" + SELECTOR_PATTERN + ")"
                    + "|(?<ENTRY>" + ENTRY_REGE + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<COLON>" + COLON_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<COMMA>" + COMMA_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + MUTI_COMMENT_REGE + ")"
    );

  //  private static StyleSpans<Collection<String>> computeHighlightingRegex(String text) {
//        Matcher matcher = PATTERN.matcher(text);
//        int lastKwEnd = 0;
//        StyleSpansBuilder<Collection<String>> spansBuilder
//                = new StyleSpansBuilder<>();
//        while(matcher.find()) {
//            String styleClass =
//                    matcher.group("SELECTOR")  != null ? "selector"  :
//                    matcher.group("ENTRY")     != null ? "entry"     :
//                    matcher.group("PAREN")     != null ? "paren"     :
//                    matcher.group("BRACE")     != null ? "brace"     :
//                    matcher.group("BRACKET")   != null ? "bracket"   :
//                    matcher.group("COLON")     != null ? "colon"     :
//                    matcher.group("SEMICOLON") != null ? "semicolon" :
//                    matcher.group("COMMA")     != null ? "comma"     :
//                    matcher.group("STRING")    != null ? "string"    :
//                    matcher.group("COMMENT")   != null ? "comment"   :
//                    null; /* never happens */ assert styleClass != null;
//            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
//            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
//            lastKwEnd = matcher.end();
//        }
//        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
//        return spansBuilder.create();
//    }
}
