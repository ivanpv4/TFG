package controlador;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;

import app.BiblioApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import modelo.ManagerPrincipal;
import modelo.Usuario;

public class EliminarUsuarioController {
	
	@FXML
	private TableView<Usuario> tablaUsuarios;
	
	@FXML
	private TableColumn<Usuario, String> colUsuario;
	
	@FXML
	private TableColumn<Usuario, String> colPass;
	
	@FXML
	private TableColumn<Usuario, String> colRol;
	
	@FXML
	private TableColumn<Usuario, Boolean> colSeleccionar;
	
	@FXML
	private TextField buscador;
	
	@FXML
	private Pane filtro;
	
	@FXML
	private GridPane confirmarEliminacion, confirmarMensaje;
	
	private ObservableList<Usuario> listaUsuarios;
	
	@FXML
	public void initialize() {
		colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
		colPass.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
		colRol.setCellValueFactory(cellData -> {
			String estado = cellData.getValue().isAdmin() ? "Administrador" : "Usuario";
			return new SimpleStringProperty(estado);
		});
		colSeleccionar.setCellValueFactory(cellData -> cellData.getValue().seleccionadoProperty());
		colSeleccionar.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccionar));
		
		cargarUsuarios();
	}
	
	private void cargarUsuarios() {
		try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {
			List<Usuario> usuarios = session.createQuery("FROM Usuario", Usuario.class).getResultList();
			listaUsuarios = FXCollections.observableArrayList(usuarios);
			tablaUsuarios.setItems(listaUsuarios);
		}
	}
	
	@FXML
	private void eliminarUsuarios() {
		List<Usuario> usuarios = listaUsuarios.stream().filter(Usuario::isSeleccionado).collect(Collectors.toList());
		if (usuarios.isEmpty()) {
			System.out.println("No hay usuarios seleccionados");
			return;
		}
		
		try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {
			session.beginTransaction();
			for (Usuario usuario : usuarios) {
				session.remove(session.contains(usuario) ? usuario : session.merge(usuario));
			}
			
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		apareceConfirmarMensaje();
		
		listaUsuarios.removeAll(usuarios);
		tablaUsuarios.refresh();
	}
	
	@FXML
	private void filtrarUsuarios() {
		String filtro = normalizar(buscador.getText().trim());
		
		ObservableList<Usuario> filtrados = listaUsuarios.filtered(usuario -> {
			String user = normalizar(usuario.getUsuario());
			String pass = normalizar(usuario.getContraseña());
			String administrador = usuario.isAdmin() ? "Administrador" : "Usuario";
			
			return user.contains(filtro) || pass.contains(filtro) || administrador.contains(filtro) || normalizar(administrador).contains(filtro);
		});
		
		tablaUsuarios.setItems(filtrados);
	}
	
	private String normalizar(String texto) {
		return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
	}
	
	@FXML
	private void confirmarEliminacion() {
    	confirmarEliminacion.setVisible(true);
    	filtro.setVisible(true);
    	filtro.toFront();
    	confirmarEliminacion.toFront();
    }
	
	@FXML
    private void rechazar() {
    	confirmarEliminacion.toBack();
    	filtro.toBack();
    	filtro.setVisible(false);
    	confirmarEliminacion.setVisible(false);
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
    	confirmarEliminacion.setVisible(false);
    	confirmarEliminacion.toBack();
    }
	
	@FXML
	private void regresarAdministracion() throws IOException {
		BiblioApp.setRoot("administracion");
	}
	
}
