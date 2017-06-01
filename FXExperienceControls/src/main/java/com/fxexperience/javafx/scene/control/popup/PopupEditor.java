/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fxexperience.javafx.scene.control.popup;

import com.fxexperience.javafx.scene.control.paintpicker.PaintPicker;
import com.fxexperience.javafx.scene.control.paintpicker.PaintPicker.Mode;
import java.io.IOException;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Rectangle;
import com.fxexperience.javafx.util.encoders.ColorEncoder;
import javafx.scene.control.ColorPicker;

/**
 * FXML Controller class
 *
 * @author ericc
 */
public class PopupEditor extends HBox implements PopupEditorValidation {

    @FXML
    private CustomMenuItem customMenuItem;
    @FXML
    private StackPane editorHost;
    @FXML
    private Rectangle rectangle;

    @FXML
    private MenuButton menuButton;

    ColorPicker colorPicker = new ColorPicker();

    private boolean initialized = false;

    private PaintPicker paintPicker;

    private ObjectProperty<Color> color = new SimpleObjectProperty<>(Color.RED);

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public Color getColor() {
        return color.get();
    }

    public void setColor(Color newColor) {
        color.set(newColor);
    }

    public PopupEditor(Object startColor) {
        initialize(startColor);

    }

    private void initialize(Object startColor) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/FXMLPopupEditor.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        initializePopup(startColor);
    }

    private void initializePopup(Object startColor) {
        menuButton.setText(getPreviewString(startColor));
        color.set((Color) startColor);

        menuButton.textProperty().bind(new StringBinding() {
            {
                bind(color);
            }

            @Override
            protected String computeValue() {
                return getWebColor();
            }
        });
        menuButton.showingProperty().addListener((ChangeListener<Boolean>) (ov, previousVal, newVal) -> {
            if (newVal) {
                if (!initialized) {

                    paintPicker = new PaintPicker(Mode.COLOR);
                    editorHost.getChildren().add(getPopupContentNode());
                    paintPicker.paintProperty().addListener(paintChangeListener);
                }
               
                paintPicker.setPaintProperty(color.get());
            }

        });

        rectangle.fillProperty().bind(new ObjectBinding<Paint>() {
            {
                bind(color);
            }

            @Override
            protected Paint computeValue() {
                return getColor();
            }
        });
    }

    private final ChangeListener<Paint> paintChangeListener = (ov, oldValue, newValue) -> {
        if (newValue == null) {
            return;
        }
        assert newValue instanceof Paint;
        if (newValue instanceof LinearGradient
                || newValue instanceof RadialGradient
                || newValue instanceof ImagePattern) {
            return;
        }
        assert newValue instanceof Color;

        setColor((Color) newValue);

    };

    public final String getColorText() {
        return getPreviewString(menuButton);
    }

    public String getPreviewString(Object value) {
        if (value == null) {
            return null;
        }
        assert value instanceof Paint;
        if (value instanceof LinearGradient
                || value instanceof RadialGradient
                || value instanceof ImagePattern) {
            return value.getClass().getSimpleName();
        }
        assert value instanceof Color;

        return ColorEncoder.encodeColor((Color) value);
    }

    public String getWebColor() {
        String webColor = color.get().toString();
        return "#" + webColor.substring(2, webColor.length());
    }

    private Node getPopupContentNode() {
        return paintPicker;
    }

//    /**
//     * Initializes the controller class.
//     *
//     * @param value
//     */
//    public PopupEditor(Object value) {
//       paint = value;
//       initialize();
//    }
//
//    private void initialize() {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLPopupEditor.fxml"));
//            fxmlLoader.setRoot(this);
//            fxmlLoader.setController(this);
//            fxmlLoader.load();
//
//        } catch (IOException exception) {
//            throw new RuntimeException(exception);
//        }
//     
//        initializeEditor();
//
//    }
//
//    public final String getColorText() {
//        return getPreviewString(menuButton);
//    }
//
//    private void initializeEditor() {
//          menuButton.setText(getPreviewString(paint));
//         rectangle.setFill((Paint) paint);
//        // Lazy initialization of the editor,
//        // the first time the popup is opened.
//        menuButton.showingProperty().addListener((ChangeListener<Boolean>) (ov, previousVal, newVal) -> {
//            if (newVal) {
//                
//                if (!initialized) {
//                    initializePopup();
//                    initialized = true;
//                    setPopupContentValue((Paint) paint);
//                }
//            }
//        });
//    }
//     public void setPopupContentValue(Object value) {
//        assert value == null || value instanceof Paint;
//       
//        paintPicker.paintProperty().addListener(paintChangeListener);
//      
//    }
//    private void initializePopup() {
//        initializePopupContent();
//        editorHost.getChildren().add(getPopupContentNode());
//    }
//
//    private Node getPopupContentNode() {
//        return paintPicker;
//    }
//
//    private void initializePopupContent() {
//        paintPicker = new PaintPicker(Mode.COLOR);
//        paintPicker.setPaintProperty((Paint) paint);
//        rectangle.fillProperty().bind(paintPicker.paintProperty());
//    }
//
//    public String getPreviewString(Object value) {
//        if (value == null) {
//            return null;
//        }
//        assert value instanceof Paint;
//        if (value instanceof LinearGradient
//                || value instanceof RadialGradient
//                || value instanceof ImagePattern) {
//            return value.getClass().getSimpleName();
//        }
//        assert value instanceof Color;
//
//        return ColorEncoder.encodeColor((Color) value);
//    }
//
//    private final ChangeListener<Paint> paintChangeListener = (ov, oldValue, newValue) -> {
//       this.paint = newValue;
//       menuButton.setText(getPreviewString(newValue));
//    };
//   
//    public Rectangle getRectangle() {
//       return rectangle;
//   }
//
//    public Object getValue() {
//        return paint;
//    }
//
//    public void setValue(Object value) {
//        commitValue(value);
//    }
//
//    public void reset() {
////      System.out.println(getPropertyNameText() + " : resetPopupContent()");
//        menuButton.setText(null);
//    }

    /*
     * PopupEditorValidation interface.
     * Methods to be used by concrete popup editors
     */
    @Override
    public void commitValue(Object value) {
        //    userUpdateValueProperty(value);

    }

    @Override
    public void transientValue(Object value) {
        //   userUpdateTransientValueProperty(value);
    }

    @Override
    public void invalidValue(Object value) {
        // TBD
    }
}
