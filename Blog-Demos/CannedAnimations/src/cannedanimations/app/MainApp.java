package cannedanimations.app;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Simple Demo application for the Canned animation transitions in 
 * FXExperience Controls
 * 
 * @author Jasper Potts, Eric Canull
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/FXMLCannedAnimations.fxml"));
       
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /* @param args the command line arguments */
    public static void main(String[] args) {
        launch(args);
    }
}
