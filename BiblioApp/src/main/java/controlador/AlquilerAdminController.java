package controlador;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;

import app.BiblioApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.ManagerPrincipal;
import modelo.Usuario;

public class AlquilerAdminController {
	
	@FXML
	private TableView<Usuario> tablaUsuarios;
	
	@FXML
	private TableColumn<Usuario, String> colUsuario;
	
	@FXML
	private TableColumn<Usuario, String> colRol;
	
	@FXML
	private TableColumn<Usuario, Boolean> colSeleccionar;
	
	@FXML
	private TextField buscador;
	
	private ObservableList<Usuario> listaUsuarios;
	
	public static Usuario elegidoPorAdmin;
	
	@FXML
	public void initialize() {
		colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
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
			
			for (Usuario usuario : listaUsuarios) {
				usuario.seleccionadoProperty().addListener((obs, wasSelected, isNowSelected) -> {
					if (isNowSelected) {
						for (Usuario otroUsuario : listaUsuarios) {
							if (otroUsuario != usuario) {
								otroUsuario.setSeleccionado(false);
							}
						}
						tablaUsuarios.refresh();
					}
				});
			}
		}
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
	private void elegirUsuario() throws IOException {
		Usuario usuario = listaUsuarios.stream().filter(Usuario::isSeleccionado).findFirst().orElse(null);
		if (usuario == null) {
			System.out.println("No hay usuario seleccionado");
			return;
		} else {
			elegidoPorAdmin = usuario;
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/alquiler.fxml"));
		Parent root = loader.load();
		
		Scene escenaActual = tablaUsuarios.getScene();
		escenaActual.setRoot(root);
		
	}
	
	@FXML
	private void regresar() throws IOException {
		BiblioApp.setRoot("menuAdmin");
	}
	
}
