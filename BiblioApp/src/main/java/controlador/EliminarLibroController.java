package controlador;

import java.io.IOException;

import app.BiblioApp;
import javafx.fxml.FXML;

public class EliminarLibroController {
	
	@FXML
	private void regresarAdministracion() throws IOException {
		BiblioApp.setRoot("administracion");
	}
	
}
