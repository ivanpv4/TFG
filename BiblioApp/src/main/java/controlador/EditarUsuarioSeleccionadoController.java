package controlador;

import java.io.IOException;

import org.hibernate.Session;

import app.BiblioApp;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import modelo.ManagerPrincipal;
import modelo.Usuario;

public class EditarUsuarioSeleccionadoController {
	
	private Usuario usuario;
	
	@FXML
	private TextField userField;
	
	@FXML
	private TextField passField;
	
	@FXML
	private RadioButton siRB, noRB;
	
	@FXML
	private Pane filtro;
	
	@FXML
	private GridPane confirmarEdicion, confirmarMensaje;
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		
		userField.setText(usuario.getUsuario());
		passField.setText(usuario.getContraseña());
		siRB.setSelected(usuario.isAdmin());
		noRB.setSelected(!usuario.isAdmin());
	}
	
	@FXML
	private void guardarCambios() {
		usuario.setUsuario(userField.getText());
		usuario.setContraseña(passField.getText());
		usuario.setAdmin(siRB.isSelected());
		
		try (Session session = ManagerPrincipal.getSessionFactory().openSession()){
			session.beginTransaction();
			session.merge(usuario);
			session.getTransaction().commit();
		}
		
		apareceConfirmarMensaje();
	}
	
	@FXML
	private void confirmarEdicion() {
    	confirmarEdicion.setVisible(true);
    	filtro.setVisible(true);
    	filtro.toFront();
    	confirmarEdicion.toFront();
    }
	
	@FXML
    private void confirmarEdicionEnter(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		confirmarEdicion();
    	}
    }
	
	@FXML
    private void rechazar() {
    	confirmarEdicion.toBack();
    	filtro.toBack();
    	filtro.setVisible(false);
    	confirmarEdicion.setVisible(false);
    }
	
	@FXML
    private void apareceConfirmarMensaje() {
    	confirmarMensaje.toFront();
    	confirmarMensaje.setVisible(true);
    }
	
	@FXML
    private void confirmarMensaje() {
    	confirmarMensaje.toBack();
    	filtro.toBack();
    	filtro.setVisible(false);
    	confirmarMensaje.setVisible(false);
    	confirmarEdicion.setVisible(false);
    	confirmarEdicion.toBack();
    }
	
	@FXML
	private void regresarEditar() throws IOException {
		BiblioApp.setRoot("editarUsuario");
	}
	
}
