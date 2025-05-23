package controlador;

import java.io.IOException;

import app.BiblioApp;
import javafx.fxml.FXML;

public class HistorialController {
	
	@FXML
	private void regresar() throws IOException {
		BiblioApp.setRoot("menuAdmin");
	}
	
}
