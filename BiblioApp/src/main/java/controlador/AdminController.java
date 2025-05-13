package controlador;

import java.io.IOException;

import app.BiblioApp;
import javafx.fxml.FXML;

public class AdminController {
	
	@FXML
	private void regresar() throws IOException {
		BiblioApp.setRoot("menuAdmin");
	}
	
	@FXML
	private void goToAddUser() throws IOException {
		BiblioApp.setRoot("agregarUsuario");
	}
	
	@FXML
	private void goToEditUser() throws IOException {
		BiblioApp.setRoot("editarUsuario");
	}
	
	@FXML
	private void goToDeleteUser() throws IOException {
		BiblioApp.setRoot("eliminarUsuario");
	}
	
	@FXML
	private void goToAddBook() throws IOException {
		BiblioApp.setRoot("agregarLibro");
	}
	
	@FXML
	private void goToEditBook() throws IOException {
		BiblioApp.setRoot("editarLibro");
	}
	
	@FXML
	private void goToDeleteBook() throws IOException {
		BiblioApp.setRoot("eliminarLibro");
	}
	
}
