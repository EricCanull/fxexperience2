package com.fxexperience.tools.util.css;

import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.parser.CSSParser;
import javafx.css.ParsedValue;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSSContext {
    private static final CSSContext MODENA = new CSSContext();
    private static final String url = CSSContext.class.getResource(
            "/com/sun/javafx/scene/control/skin/modena/modena.css").toExternalForm();
    private static final String ROOT = "*.root";
    private static final Font FONT = Font.font(1);

    private static final String CLASS_PATTERN = "\\.[a-zA-Z][A-Za-z0-9-_]*";
    private static final String ID_PATTERN = "#[a-zA-Z][A-Za-z0-9-_]*";
    private static final String STATE_PATTERN = ":[a-zA-Z][A-Za-z0-9-_]*";
    private static final String JAVA_PATTERN = "([^.#:A-Za-z0-9-_]|(?<START>^))[a-zA-Z][A-Za-z0-9-_]*";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<CLASS>" + CLASS_PATTERN + ")"
                    + "|(?<ID>" + ID_PATTERN + ")"
                    + "|(?<STATE>" + STATE_PATTERN + ")"
                    + "|(?<JAVA>" + JAVA_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    static {
        try {
            MODENA.load(new URL(url));
            System.out.println("load modena");
        } catch (IOException e) {
            System.out.println("Load modena.css fail!" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static CSSContext createByDefault() {
        return new CSSContext(MODENA);
    }

    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("CLASS")     != null ? "class" :
                    matcher.group("ID")        != null ? "id" :
                    matcher.group("STATE")     != null ? "state" :
                    matcher.group("JAVA")      != null ? "java" :
                    matcher.group("BRACE")     != null ? "brace" :
                    matcher.group("BRACKET")   != null ? "bracket" :
                    matcher.group("SEMICOLON") != null ? "semicolon" :
                    matcher.group("STRING")    != null ? "string" :
                    matcher.group("COMMENT")   != null ? "comment" :
                    null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public void load(URL url) throws IOException {
        load(new CSSParser().parse(url));
    }

    public void load(String styleText) {
        load(new CSSParser().parse(styleText));
    }

    private void load(Stylesheet css) {
        loadSelector(css);
//      loadDeclaration(css);
//      resolveRoot(css);
    }

    final Multiset<String> selectors;
    final Multiset<String> classes;
    final Multiset<String> javaClasses;
    final Multiset<String> ids;
    final Multiset<String> states;
    final LinkedListMultimap<String, ParsedValue<?, ?>> entries;
    final LinkedListMultimap<String, ParsedValue<?, Paint>> paints;

    public CSSContext() {
        selectors = LinkedHashMultiset.create();
        classes = LinkedHashMultiset.create();
        javaClasses = LinkedHashMultiset.create();
        ids = LinkedHashMultiset.create();
        states = LinkedHashMultiset.create();
        entries = LinkedListMultimap.create();
        paints = LinkedListMultimap.create();
    }

    public CSSContext(String text) {
        this();
        load(text);
    }

    public CSSContext(CSSContext modena) {
        this();
        add(modena);
    }

    public void add(CSSContext cr) {
        this.selectors.addAll(cr.selectors);
        this.classes.addAll(cr.classes);
        this.javaClasses.addAll(cr.javaClasses);
        this.ids.addAll(cr.ids);
        this.states.addAll(cr.states);
        this.entries.putAll(cr.entries);
        this.paints.putAll(cr.paints);
    }

    private void loadSelector(Stylesheet css) {
//        Observable(css)
//                .flatMap(s -> Observable.from(s.getRules()))
//                .flatMap(r -> Observable.from(r.getSelectors()))
//                .map(com.sun.javafx.css.Selector::toString)
//                .map(s -> s.replace("*", ""))
//                .subscribe(s -> {
//                    selectors.add(s);
//                    classes.addAll(loadClass(s));
//                    ids.addAll(loadId(s));
//                    states.addAll(loadState(s));
//                    javaClasses.addAll(loadJavaClass(s));
            //    });
    }

    private void loadDeclaration(Stylesheet css) {
        css.getRules().stream()
                .flatMap(r -> r.getDeclarations().stream())
                .forEach(d -> entries.put(d.getProperty(), d.getParsedValue()));
    }

    private static void resolveRoot(Stylesheet css) {
//        css.getRules().stream()
//                .filter(r -> r.getSelectors().stream()
//                        .map(Selector::toString)
//                        .map(String::trim)
//                        .filter(s -> s.equals(ROOT))
//                        .findAny()
//                        .isPresent())
//                .flatMap(r -> r.getDeclarations().stream())
//                .forEach(d -> {
//                    ParsedValue<?, ?> pv = d.getParsedValue();
//                    ParsedValue<?, ?> resolve = resolve(pv);
//                    if (classifyValue(resolve.getValue(), d.getProperty(), d.getParsedValue()) == false) {
//                        classifyValue(resolve.convert(FONT), d.getProperty(), Util.createParsedValue(() -> resolve(pv).convert(FONT)));
//                    }
//                });
    }

    public void remove(CSSContext cr) {
        this.selectors.removeAll(cr.selectors);
        this.classes.removeAll(cr.classes);
        this.javaClasses.removeAll(cr.javaClasses);
        this.ids.removeAll(cr.ids);
        this.states.removeAll(cr.states);
        cr.entries.entries().forEach(e -> this.entries.remove(e.getKey(), e.getValue()));
        cr.paints.entries().forEach(e -> this.paints.remove(e.getKey(), e.getValue()));
    }

    @SuppressWarnings("unchecked")
    private boolean classifyValue(Object classObject, String key, ParsedValue<?, ?> value) {
        if (classObject instanceof Paint) {
            paints.put(key, (ParsedValue<?, Paint>) value);
            return true;
        }
        return false;
    }

//    private List<String> loadClass(String selector) {
//        List<String> ids = new ArrayList<>();
//        Matcher matcher = CLASS_PATTERN.matcher(selector);
//        while (matcher.find()) {
//            ids.add(selector.substring(matcher.start() + 1, matcher.end()));
//        }
//        return ids;
//    }
//
//    private List<String> loadJavaClass(String selector) {
//        List<String> javaClasses = new ArrayList<>();
//        Matcher matcher = javaPattern.matcher(selector);
//        while (matcher.find()) {
//            javaClasses.add(selector.substring(matcher.start() + (matcher.group("START") == null ? 1 : 0), matcher.end()));
//        }
//        return javaClasses;
//    }

//    private List<String> loadId(String selector) {
//        List<String> ids = new ArrayList<>();
//        Matcher matcher = idPattern.matcher(selector);
//        while (matcher.find()) {
//            ids.add(selector.substring(matcher.start() + 1, matcher.end()));
//        }
//        return ids;
//    }

//    private List<String> loadState(String selector) {
//        List<String> state = new ArrayList<>();
//        Matcher matcher = statePattern.matcher(selector);
//        while (matcher.find()) {
//            state.add(selector.substring(matcher.start() + 1, matcher.end()));
//        }
//        return state;
//    }

//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    public <V, T> ParsedValue<V, T> resolve(ParsedValue<V, T> pv) {
//        try {
//            if (pv instanceof ParsedValueImpl) {
//                ParsedValueImpl<V, T> pvi = (ParsedValueImpl<V, T>) pv;
//                if (pvi.isContainsLookups() == false) {
//                    return pv;
//                } else if (pvi.isLookup()) {
//                    T lookup = lookup(pv.getValue());
//                    // System.out.printf("lookup %s find %s\n", pv.getValue(), lookup);
//                    return Util.createParsedValue(lookup);
//                } else {
//                    V value = pv.getValue();
//                    if (value instanceof ParsedValue) {
//                        return new ParsedValueImpl<V, T>((V) resolve((ParsedValue<?, ?>) value), pv.getConverter());
//                    } else if (value instanceof ParsedValue[]) {
//                        ParsedValue[] originPvs = (ParsedValue[]) value;
//                        ParsedValue[] pvs = new ParsedValue[originPvs.length];
//                        for (int i = 0; i < pvs.length; i++) {
//                            pvs[i] = resolve(originPvs[i]);
//                        }
//                        return new ParsedValueImpl<V, T>((V) pvs, pv.getConverter());
//                    }
//                }
//            }
//        } catch (ClassCastException e) {
//            return pv;
//        }
//        return pv;
//    }

//    public <T> T lookup(Object o) {
//        return TaskUtil.firstSuccess(
//                () -> _lookup(o),
//                () -> this == MODENA ? null : MODENA._lookup(o)
//        );
//    }
//
//    @SuppressWarnings("unchecked")
//    private <T> T _lookup(Object o) {
//        return TaskUtil.firstSuccess(
//                () -> (T) ListUtil.lastGet(paints.get(o.toString().trim().toLowerCase()), 0).convert(FONT)
//        );
//    }

    public Multiset<String> getSelectors() {
        return selectors;
    }

    public Multiset<String> getClasses() {
        return classes;
    }

    public Multiset<String> getIds() {
        return ids;
    }

    public Multiset<String> getStates() {
        return states;
    }

    public Multiset<String> getKeys() {
        return entries.keys();
    }

    public Multimap<String, ParsedValue<?, ?>> getEntries() {
        return entries;
    }

    public Multiset<String> getJavaClasses() {
        return javaClasses;
    }

    public LinkedListMultimap<String, ParsedValue<?, Paint>> getPaints() {
        return paints;
    }
}
