package controlador;

import app.BiblioApp;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MenuController {
	
	@FXML
	private GridPane logoutMessage;
	
	@FXML
	private Button logoutYes, logoutNo;
	
	@FXML
	private Pane filtro;
	
	@FXML
	private void catalogo() throws IOException {
		BiblioApp.setRoot("catalogo");
	}
	
	@FXML
	private void alquiler() throws IOException {
		BiblioApp.setRoot("alquiler");
	}
	
	@FXML
	private void alquilerAdmin() throws IOException {
		BiblioApp.setRoot("alquilerAdmin");
	}
	
	@FXML
	private void perfil() throws IOException {
		BiblioApp.setRoot("perfil");
	}
	
	@FXML
	private void historial() throws IOException {
		BiblioApp.setRoot("historial");
	}
	
	@FXML
	private void admin() throws IOException {
		BiblioApp.setRoot("administracion");
	}
	
	@FXML
	private void logout() {
		logoutMessage.setVisible(true);
		filtro.setVisible(true);
		filtro.toFront();
		logoutMessage.toFront();
		
	}
	
	@FXML
	private void logoutYes() throws IOException {
		BiblioApp.setRoot("login");
	}
	
	@FXML
	private void logoutNo() {
		logoutMessage.setVisible(false);
		filtro.setVisible(false);
	}
}
