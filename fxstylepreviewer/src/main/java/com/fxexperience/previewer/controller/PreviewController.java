/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.previewer.controller;

import com.sun.javafx.css.StyleManager;
import java.io.FileNotFoundException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.apache.commons.io.IOUtils;

public class PreviewController extends VBox {
    
    @FXML private ChoiceBox<?> choiceBox;
    @FXML private ComboBox<?> comboBox;
    @FXML private ListView<String> listView;
    
    private String style = "";

    public PreviewController() {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PreviewController.class.getResource("/fxml/FXMLPreviewPanel.fxml")); //NOI18N
            loader.setController(PreviewController.this);
            loader.setRoot(PreviewController.this);
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(PreviewController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        choiceBox.getSelectionModel().select(0);
        comboBox.getSelectionModel().select(0);

        listView.setItems(FXCollections.observableArrayList("Alpha", "Beta", "Gamma"));
//           System.out.println(this.getUserAgentStylesheet());
    }

    public void setPreviewPanelStyle(String code) {
        this.setStyle(code);
//        Platform.runLater(() -> {
//            style = code;
//            this.getStylesheets().clear();
//            this.getParent().add("internal:stylesheet.css");
//        });
    }

    public void setFont(Font font) {
       this.getChildren()
          .filtered(node -> node instanceof TextField)
          .forEach(node -> ((TextField)node).setFont(font));
    }
    
     
    {
        // URL Handler to create magic "internal:stylesheet.css" url for our css string
        URL.setURLStreamHandlerFactory(new StringURLStreamHandlerFactory());
    }

    /**
     * URLConnection implementation that returns the css string property, as a
     * stream, in the getInputStream method.
     */
    private class StringURLConnection extends URLConnection {

        public StringURLConnection(URL url) {
            super(url);
        }

        @Override
        public void connect() throws IOException {
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return IOUtils.toInputStream(style, "UTF-8");
        }
    }

    /**
     * URL Handler to create magic "internal:stylesheet.css" url for our css
     * string
     */
    private class StringURLStreamHandlerFactory implements URLStreamHandlerFactory {

        URLStreamHandler streamHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL url) throws IOException {
                if (url.toString().toLowerCase().endsWith(".css")) {
                    return new StringURLConnection(url);
                }
                throw new FileNotFoundException();
            }
        };

        @Override
        public URLStreamHandler createURLStreamHandler(String protocol) {
            if ("internal".equals(protocol)) {
                return streamHandler;
            }
            return null;
        }
    }
}
