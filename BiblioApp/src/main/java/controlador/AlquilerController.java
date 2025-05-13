package controlador;

import java.io.IOException;

import app.BiblioApp;
import javafx.fxml.FXML;

public class AlquilerController {
	
	@FXML
	private void regresar() throws IOException {
		if (LoginController.esAdmin == true) {
			BiblioApp.setRoot("menuAdmin");
		} else {
			BiblioApp.setRoot("menu");
		}
	}
}
