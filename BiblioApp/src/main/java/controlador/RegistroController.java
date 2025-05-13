package controlador;

import app.BiblioApp;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import modelo.ManagerUsuario;

public class RegistroController {

	@FXML
	private Label mensaje;
	
	@FXML
	private Label mensajePass;
	
	@FXML
	private Label mensajePass2;
	
	@FXML
	private TextField usuarioField;
	
	@FXML
	private PasswordField contraseñaField;
	
	@FXML
	private PasswordField contraseña2Field;
	
	String campoVacio = "Este campo no puede estar vacío";
	
	@FXML
	private void regresar() throws IOException {
		BiblioApp.setRoot("login");
	}
	
	@FXML
	private void hacerRegistro() throws IOException {
		mensaje.setText("");
		mensajePass.setText("");
		mensajePass2.setText("");
		
		if (usuarioField.getText().isBlank()) {
			mensaje.setText(campoVacio);
		} else if (usuarioField.getText().contains(" ")) {
			mensaje.setText("El nombre no puede contener espacios en blanco");
		} else if (contraseñaField.getText().isBlank()) {
			mensajePass.setText(campoVacio);
		} else if (contraseña2Field.getText().isBlank()) {
			mensajePass2.setText(campoVacio);
		} else if (!contraseñaField.getText().equals(contraseña2Field.getText())) {
			mensajePass2.setText("Las contraseñas no coinciden");
		} else {
			if (!ManagerUsuario.registrarUsuario(usuarioField.getText(), contraseñaField.getText())) {
				mensaje.setText("El usuario ya está registrado");
			} else {
				regresar();
			}
		}
		
	}
	
	@FXML
    private void hacerRegistroEnter(KeyEvent event) throws IOException {
    	if (event.getCode() == KeyCode.ENTER) {
    		hacerRegistro();
    	}
    }
}
