package fxwebview.app;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

public class EditorController extends AnchorPane {

    @FXML
    private WebView webView;
    private WebEngine webEngine;
    private String code = 
            "-fx-selection-bar-text: ladder(\n" +
            "    -fx-background,\n" +
            "    -fx-light-text-color 50%,\n" +
            "    -fx-mid-text-color -51%\n" +
            ");";
    
    public EditorController() {
        initialize();
    }

    private void initialize() {
        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(EditorController.class.getResource("/fxml/FXMLEditorPanel.fxml")); //NOI18N
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();

        } catch (IOException ex) {
            Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
        }

        webEngine = webView.getEngine();
        webEngine.setOnAlert(this::alertWorker);
        webEngine.load(this.getClass().getResource("/codemirror/index.html").toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener(this::stateChangeListener);
    }

    /**
     * Executes JavaScript code that sets the code in the editor after the WebView index html is loaded
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void stateChangeListener(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
        if (newValue == Worker.State.SUCCEEDED) {
              setEditorCode(code);
              enableFirebug(false);
        }
    }

    /**
     * Sets the code in the editor.
     * @param newCode
     */
    public void setEditorCode(String newCode) {
        newCode = newCode.replace("\n", "\\n");
        newCode = newCode.replace("\t", "\\t");
        webEngine.executeScript("editor.setValue('" + newCode + "' );");
    }

     /**
     * Gets the code in the editor.
     * @return 
     */
    public String getEditorCode() {
        this.code = (String) webEngine.executeScript("editor.getValue();");
        return code;
    }

    /**
     * Prints webView alerts to the console
     */
    private void alertWorker(WebEvent<String> alert) {
        System.out.println("Alert Event - Message: " + alert.getData());
    }
    
    /**
     * Quick one-liner that delimits using the end of file character and returns
     * the whole input stream as a String. Use for small files.
     *
     * @param inputStream byte input stream.
     * @return String a file or JSON text
     */
    private static String streamToString(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            return scanner.useDelimiter("\\Z").next();
        }
    }
    
   /**
     * Enables Firebug for debugging a webEngine.
     * @param engine the webEngine for which debugging is to be enabled.
     */
    private void enableFirebug(boolean enabled) {
        if (enabled) {
            InputStream file = this.getClass().getResourceAsStream("/firebug/firebug-script.js");
            final String script = streamToString(file);
            webEngine.executeScript(script);
        }
    }
}
    
