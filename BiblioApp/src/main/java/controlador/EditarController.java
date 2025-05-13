package controlador;

import java.io.IOException;

import app.BiblioApp;
import javafx.fxml.FXML;

public class EditarController {
	
	@FXML
	private void regresarAdministracion() throws IOException {
		BiblioApp.setRoot("administracion");
	}
	
}
