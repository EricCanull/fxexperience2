package com.fxexperience.tools.util;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSSFormatter {

    private static final String CSS = "css";
    private static final String SELECTOR = "selector";
    private static final String SELECTOR_ID = "selector-id";
    private static final String SELECTOR_CLASS = "selector-class";
    private static final String SELECTOR_STATE = "selector-state";
    private static final String SELECTOR_JAVACLASS = "selector-java-class";
    private static final String ENTRIES = "entries";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String MUTI_COMMENT = "muticomment";
    private static final String ENTRY = "entry";
    private static final String LEFT_BRACE = "leftbrace";
    private static final String RIGHT_BRACE = "rightbrace";
    private static final String COLON = "colon";
    private static final String SEMICOLON = "semicolon";


    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        return computeHighlightingByRegex(text);
    }
//    (?</\*+([^*]|(\*+[^*/]))*\*+/>/\*+([^*]|(\*+[^*/]))*\*+/)|(?<(([^\{\}/])*)(\{)(([^}])*)(\})>(([^\{\}/])*)(\{)(([^}])*)(\}))
    private static final String CSS_REGEX = "(([^\\{\\}/])*)(\\{)(([^}])*)(\\})";
    private static final String SELECTOR_REGEX = "([.#: ]|\\n)([A-Za-z0-9-_]+)";
    private static final String MUTI_COMMENT_REGEX = "/\\*+([^*]|(\\*+[^*/]))*\\*+/";
    private static final String ENTRY_REGEX = "([A-Za-z0-9-_]+)(\\s*:\\s*)(([^;]|\\R)*)(;)";

    private static final Pattern CSS_PATTERN = Pattern.compile(String.format(
            "(?<%s>%s)|(?<%s>%s)", MUTI_COMMENT, MUTI_COMMENT_REGEX, CSS, CSS_REGEX));
    private static final Pattern ENTRY_PATTERN = Pattern.compile(String.format(
            "(?<%s>%s)|(?<%s>%s)", MUTI_COMMENT, MUTI_COMMENT_REGEX, ENTRY, ENTRY_REGEX));
    private static final Pattern SELECTOR_PATTERN = Pattern.compile(SELECTOR_REGEX);

    private static final int GROUP_BEFORE = 5;

    private static final int GROUP_KEY = 5;
    private static final int GROUP_COLON = 6;
    private static final int GROUP_VALUE = 7;
    private static final int GROUP_SEMICOLON = 9;

    private static final int GROUP_SELECTOR = 5;
    private static final int GROUP_LEFT_BRACE = 7;
    private static final int GROUP_VALUES = 8;
    private static final int GROUP_RIGHT_BRACE = 10;

    private static final Pattern PATTERN = Pattern.compile(
             "(?<CSS>" + CSS_REGEX + ")"
           + "|(?<COMMENT>" + MUTI_COMMENT + ")"

          + "|(?<SELECT>" + SELECTOR + ")"
                    + "|(?<ENTRY>" + ENTRY_REGEX + ")"
                    + "|(?<SELECTOR>" + ENTRY_REGEX + ")"
//                    + "|(?<COLON>" + COLON_PATTERN + ")"
//                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
//                    + "|(?<COMMA>" + COMMA_PATTERN + ")"
//                    + "|(?<STRING>" + STRING_PATTERN + ")"
    );


    public static StyleSpans<Collection<String>> computeHighlightingByReg(String text) {
        Matcher matcher = PATTERN.matcher(text);
        System.out.println(CSS_PATTERN);
        System.out.println(MUTI_COMMENT_REGEX);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("CSS")  != null ? "css" :

            matcher.group("COMMENT")     != null ? "multicomment"     :

            matcher.group("ENTRY")     != null ? "entries"     :
            matcher.group("SELECTOR")   != null ? "selector"   :
//            matcher.group("COLON")     != null ? "colon"     :
//            matcher.group("SEMICOLON") != null ? "semicolon" :
//            matcher.group("COMMA")     != null ? "comma"     :
//            matcher.group("STRING")    != null ? "string"    :
            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }


    private static StyleSpans<Collection<String>> computeHighlightingByRegex(String text) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        Matcher cssMatcher = CSS_PATTERN.matcher(text);
        cssMatcher.toString();
        int lastKwEnd = 0;
        while (cssMatcher.find()) {
            spansBuilder.add(Collections.emptyList(), cssMatcher.start() - lastKwEnd);
            if (cssMatcher.group(MUTI_COMMENT) != null) {
                spansBuilder.add(Collections.singleton(MUTI_COMMENT), cssMatcher.end() - cssMatcher.start());
            } else {
                if (cssMatcher.group(CSS) != null) {
                    String selectorText = cssMatcher.group(GROUP_SELECTOR);
                    if (!selectorText.isEmpty()) {
                        lastKwEnd = 0;
                        Matcher selectorMatcher = SELECTOR_PATTERN.matcher(selectorText);
                        while (selectorMatcher.find()) {
                            spansBuilder.add(Collections.emptyList(), selectorMatcher.start() - lastKwEnd);
                            spansBuilder.add(Collections.emptyList(), selectorMatcher.end(1) - selectorMatcher.start(1));
                            String prefix = selectorMatcher.group(1);
                            List<String> list = new ArrayList<>();
                            switch (prefix) {
                                case "#":
                                    list.add(SELECTOR_ID);
                                    break;
                                case ".":
                                    list.add(SELECTOR_CLASS);
                                    break;
                                case ":":
                                    list.add(SELECTOR_STATE);
                                    break;
                                case " ":
                                case "\n":
                                    list.add(SELECTOR_JAVACLASS);
                                    break;
                            }
                            spansBuilder.add(list, selectorMatcher.end(2) - selectorMatcher.start(2));
                            lastKwEnd = selectorMatcher.end();
                        }
                        if (selectorText.length() > lastKwEnd) {
                            spansBuilder.add(Collections.emptyList(), selectorText.length() - lastKwEnd);
                        }
                    }
                    spansBuilder.add(Collections.singleton(LEFT_BRACE),
                            cssMatcher.end(GROUP_LEFT_BRACE) - cssMatcher.start(GROUP_LEFT_BRACE));
                    String entriesText = cssMatcher.group(GROUP_VALUES);
                    if (!entriesText.isEmpty()) {
                        lastKwEnd = 0;
                        Matcher entryMatcher = ENTRY_PATTERN.matcher(entriesText);
                        while (entryMatcher.find()) {
                            spansBuilder.add(Collections.emptyList(), entryMatcher.start() - lastKwEnd);
                            if (entryMatcher.group(MUTI_COMMENT) != null) {
                                spansBuilder
                                        .add(Collections.singleton(MUTI_COMMENT), entryMatcher.end() - entryMatcher.start());
                            } else {
                                if (entryMatcher.group(ENTRY) != null) {
                                    spansBuilder.add(Collections.singleton(KEY),
                                            entryMatcher.end(GROUP_KEY) - entryMatcher.start(GROUP_KEY));
                                    spansBuilder.add(Collections.singleton(COLON),
                                            entryMatcher.end(GROUP_COLON) - entryMatcher.start(GROUP_COLON));
                                    spansBuilder.add(Collections.singleton(VALUE),
                                            entryMatcher.end(GROUP_VALUE) - entryMatcher.start(GROUP_VALUE));
                                    spansBuilder.add(Collections.singleton(SEMICOLON),
                                            entryMatcher.end(GROUP_SEMICOLON) - entryMatcher.start(GROUP_SEMICOLON));
                                }
                            }
                            lastKwEnd = entryMatcher.end();
                        }
                        if (entriesText.length() > lastKwEnd) {
                            spansBuilder.add(Collections.emptyList(), entriesText.length() - lastKwEnd);
                        }
                    }

                    lastKwEnd = cssMatcher.end(GROUP_VALUES);

                    spansBuilder.add(Collections.singleton(RIGHT_BRACE), cssMatcher.end(GROUP_RIGHT_BRACE) - lastKwEnd);
                }
            }
            lastKwEnd = cssMatcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);

        return spansBuilder.create();
    }
}

