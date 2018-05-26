package fxfontpicker.app;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.MouseEvent;

public class FontPicker extends AnchorPane {
    
    @FXML private ComboBox<String> familyComboBox;
    @FXML private ChoiceBox<String> styleChoiceBox;
    @FXML private ComboBox<String> sizeComboBox;
    @FXML private Slider sl_font_size;
    @FXML private Text sampleFontText;

    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(Font.getDefault());

    public Font getFont() {
        return font.get();
    }

    public ObjectProperty<Font> fontProperty() {
        return font;
    }

    public void setFont(Font font) {
        assert font instanceof Font;
        
        this.font.set(font);
    }
    
    public FontPicker() {
       
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FontPicker.class.getResource("/fxml/FXMLFontPicker.fxml")); //NOI18N
            loader.setController(FontPicker.this);
            loader.setRoot(FontPicker.this);

            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(FontPicker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final NumberFormat nf = DecimalFormat.getInstance(Locale.getDefault());
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(5);

        // Family font combo-box
        familyComboBox.getItems().setAll(Font.getFamilies());
        familyComboBox.getSelectionModel().select(0);
        familyComboBox.valueProperty().addListener(observable -> changeFont(nf));
        familyComboBox.setCellFactory((ListView<String> listView) -> {
            final ListCell<String> cell = new ListCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);
                        setFont(new Font(item, 12));
                    }
                }
            };
            cell.setPrefWidth(200);
            return cell;
        });

        // Style font combo-box
        styleChoiceBox.setItems(FXCollections.observableArrayList(
                "Bold", "Bold Italic", "Italic", "Regular"));
        styleChoiceBox.getSelectionModel().select(3);
        styleChoiceBox.valueProperty().addListener(observable -> changeFont(nf));

        // Size font combo-box
        sizeComboBox.getItems().setAll(FXCollections.observableArrayList(
                "9",  "10", "11", "12", "13", "14", "18", "24", "36", "48", "72"));
        sizeComboBox.getSelectionModel().select(4);
        sizeComboBox.valueProperty().addListener(observable -> changeFont(nf));
    }

    private void changeFont(NumberFormat nf) {
        try {
            double size = nf.parse(sizeComboBox.getValue()).doubleValue();

            FontWeight weight = styleChoiceBox.getSelectionModel().isSelected(0) ||
                    styleChoiceBox.getSelectionModel().isSelected(1)
                    ? FontWeight.BOLD : FontWeight.NORMAL;
            FontPosture posture = styleChoiceBox.getSelectionModel().isSelected(1) ||
                    styleChoiceBox.getSelectionModel().isSelected(2)
                    ? FontPosture.ITALIC : FontPosture.REGULAR;
            String family = familyComboBox.getValue();
            font.setValue(Font.font(family, weight, posture, size));
            sampleFontText.setFont(font.get());
        } catch (java.text.ParseException ex) {
            Logger.getLogger(FontPicker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML 
    private void onSliderUpdate(MouseEvent event) {
        sizeComboBox.setValue(Integer.toString((int) sl_font_size.getValue()));
    }
}