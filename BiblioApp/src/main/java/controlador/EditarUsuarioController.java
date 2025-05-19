package controlador;

import java.io.IOException;

import app.BiblioApp;
import javafx.fxml.FXML;

public class EditarUsuarioController {
	
	@FXML
	private void regresarAdministracion() throws IOException {
		BiblioApp.setRoot("administracion");
	}
	
}
