package com.fxexperience.tools.handler;

import com.fxexperience.tools.view.WindowFactory;
import com.fxexperience.tools.view.window.AbstractWindow;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class AppViewHandler implements ViewHandler {

	private final Stage primaryStage;
	private final ResourceBundle bundle;
//	private Gatherer gatherer;

	public AppViewHandler(Stage primaryStage, ResourceBundle bundle) {
		this.primaryStage = primaryStage;
		this.bundle = bundle;
//		gatherer = new Gatherer();
	}

	@Override
	public void launchMainWindow() throws IOException {
		buildAndShowScene(primaryStage, WindowFactory.MAIN.createWindow(this, bundle));
	}

	@Override
	public void launchStylerWindow() throws IOException {
		Stage aboutStage = new Stage();
		aboutStage.initModality(Modality.WINDOW_MODAL);
		buildAndShowScene(aboutStage, WindowFactory.STYLER.createWindow(this, bundle));
	}

//	@Override
//	public void updateGatherer(Gatherer gatherer) {
//		this.gatherer = gatherer;
//	}
//
//	@Override
//	public Gatherer gatherer() {
//		return gatherer;
//	}

	private void buildAndShowScene(Stage stage, AbstractWindow window) throws IOException {
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(window.iconFilePath())) {
			stage.getIcons().add(new Image(is));
		}
		stage.setTitle(bundle.getString(window.titleBundleKey()));
		stage.setResizable(window.resizable());
		stage.setScene(new Scene(window.root()));
		stage.show();
	}

    @Override
    public void launchDerivedCalculatorWindow() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
