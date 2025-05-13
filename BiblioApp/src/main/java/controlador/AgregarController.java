package controlador;

import java.io.IOException;

import app.BiblioApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import modelo.ManagerUsuario;

public class AgregarController {
	
	@FXML
	private Label mensaje;
	
	@FXML
	private Label mensajePass;
	
	@FXML
	private Label mensajePass2;
	
	@FXML
	private TextField userField;
	
	@FXML
	private PasswordField passField;
	
	@FXML
	private PasswordField passField2;
	
	@FXML
	private ToggleGroup admin;
	
	@FXML
	private RadioButton selectedRB;
	
	@FXML
	private Pane filtro;
	
	@FXML
	private GridPane confirmarMensaje;
	
	@FXML
	private Button botonRegistrar, botonConfirmar;
	
	String campoVacio = "Este campo no puede estar vacío";
	
	@FXML
	private void regresarAdministracion() throws IOException {
		BiblioApp.setRoot("administracion");
	}
	
	@FXML
	private void confirmarRegistro() {
		confirmarMensaje.setVisible(true);
		filtro.setVisible(true);
		filtro.toFront();
		confirmarMensaje.toFront();
	}

	@FXML
	private void confirmar() {
		confirmarMensaje.toBack();
		filtro.toBack();
		filtro.setVisible(false);
		confirmarMensaje.setVisible(false);
	}
	
	@FXML
	private void hacerRegistroAdmin() throws IOException {
		mensaje.setText("");
		mensajePass.setText("");
		mensajePass2.setText("");
		selectedRB = (RadioButton) admin.getSelectedToggle();
		String rbValue = selectedRB.getText();
		
		if (userField.getText().isBlank()) {
			mensaje.setText(campoVacio);
		} else if (userField.getText().contains(" ")) {
			mensaje.setText("El nombre no puede contener espacios");
		} else if (passField.getText().isBlank()) {
			mensajePass.setText(campoVacio);
		} else if (passField2.getText().isBlank()) {
			mensajePass2.setText(campoVacio);
		} else if (!passField.getText().equals(passField2.getText())) {
			mensajePass2.setText("Las contraseñas no coinciden");
		} else {
			if (rbValue.equalsIgnoreCase("No")) {
				if (!ManagerUsuario.registrarUsuarioAdmin(userField.getText(), passField.getText(), false)) {
					mensaje.setText("El usuario ya está registrado");
				} else {
					confirmarRegistro();
				}
			} else {
				if (!ManagerUsuario.registrarUsuarioAdmin(userField.getText(), passField.getText(), true)) {
					mensaje.setText("El usuario ya está registrado");
				} else {
					confirmarRegistro();
				}
			}
		}
	}
	
	@FXML
    private void hacerRegistroAdminEnter(KeyEvent event) throws IOException {
    	if (event.getCode() == KeyCode.ENTER) {
    		hacerRegistroAdmin();
    	}
    }

}
