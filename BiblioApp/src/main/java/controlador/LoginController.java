package controlador;

import java.io.IOException;

import app.BiblioApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import modelo.ManagerUsuario;
import modelo.Usuario;

public class LoginController {

	@FXML
	private TextField usuarioField;
	
	@FXML
	private PasswordField contraseñaField;
	
	@FXML
	private Label mensaje;
	
	public static boolean esAdmin;
	
	private static Usuario usuarioActual;
	
	public static Usuario getUsuarioActual() {
		return usuarioActual;
	}
	
	private void setUsuarioActual(Usuario user) {
		usuarioActual = user;
	}
	
	@FXML
    private void goToMenu() throws IOException {
        BiblioApp.setRoot("menu");
    }
    
    @FXML
    private void goToMenuAdmin() throws IOException {
    	BiblioApp.setRoot("menuAdmin");
    }
    
    @FXML
    private void registro() throws IOException {
    	BiblioApp.setRoot("registro");
    }
    
    
    @FXML
    private void login() throws IOException {
    	mensaje.setText("");
    	if (!ManagerUsuario.hacerLogin(usuarioField.getText(), contraseñaField.getText())) {
    		mensaje.setText("El usuario y/o la contraseña son incorrectos");
    	} else if (ManagerUsuario.esAdmin(usuarioField.getText())) {
    		esAdmin = true;
    		goToMenuAdmin();
    		setUsuarioActual(ManagerUsuario.usuarioActual(usuarioField.getText()));
    	} else {
    		esAdmin = false;
    		goToMenu();
    		setUsuarioActual(ManagerUsuario.usuarioActual(usuarioField.getText()));
    	}
    }
    
    @FXML
    private void loginEnter(KeyEvent event) throws IOException {
    	if (event.getCode() == KeyCode.ENTER) {
    		login();
    	}
    }
}
