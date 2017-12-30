package com.fxexperience.tools.util.css;

import com.fxexperience.javafx.util.encoders.ColorEncoder;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.function.Supplier;

public class CSSTheme {
    public ObjectProperty<Paint> base = new SimpleObjectProperty<>(Color.web("#ececec"));
    public ObjectProperty<Paint> accent = new SimpleObjectProperty<>(Color.web("#0096c9"));
    public ObjectProperty<Paint> background = new SimpleObjectProperty<>(Color.web("#0096c9"));
    public ObjectProperty<Paint> defaultButton = new SimpleObjectProperty<>(Color.web("#abd8ed"));
    public ObjectProperty<Paint> focusColor = new SimpleObjectProperty<>(Color.web("#039ed3"));
    public ObjectProperty<Paint> faintFocusColor = new SimpleObjectProperty<>(Color.web("039ed322"));

    public final StringProperty css = new SimpleStringProperty();

    public CSSTheme() {

        css.bind(Bindings.createStringBinding(() -> String.format(
                ".root { %n  "
               + "-fx-base: %s;%n  "
               + "-fx-background: %s;%n  "
               + "-fx-accent: %s;%n  "
               + "-fx-default-button: %s;%n  "
               + "-fx-focus-color: %s;%n  "
               + "-fx-faint-focus-color: %s;%n}",
                ColorEncoder.encodeColor((Color) base.get()),
                ColorEncoder.encodeColor((Color) background.get()),
                ColorEncoder.encodeColor((Color) accent.get()),
                ColorEncoder.encodeColor((Color) defaultButton.get()),
                ColorEncoder.encodeColor((Color) focusColor.get()),
                ColorEncoder.encodeColor((Color) faintFocusColor.get())),
                base, background, accent, defaultButton, focusColor, faintFocusColor));
    }

    public void printCSS() {
        System.out.println(cssProperty());
    }

    public ReadOnlyStringProperty cssProperty() {
        return css;
    }

    public <T extends Parent> T createThemedNode(Supplier<T> factory) {
        T node = factory.get();
        node.styleProperty().bind(cssProperty());
        return node ;
    }


}