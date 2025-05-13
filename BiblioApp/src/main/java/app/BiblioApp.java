package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.ManagerPrincipal;

import java.io.IOException;

public class BiblioApp extends Application {
	
	private static Scene scene;
	
	@Override
	public void start(Stage stage) throws IOException {
		ManagerPrincipal.setup();
		scene = new Scene(loadFXML("login"), 1100, 850);
        stage.setTitle("BiblioApp");
        stage.setScene(scene);
        stage.show();
	}
	
	public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
	}
	
	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(BiblioApp.class.getResource("/vista/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
	
	@Override
	public void stop() throws Exception {
		ManagerPrincipal.exit();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}