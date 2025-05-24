package controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;

import app.BiblioApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Alquiler;
import modelo.Libro;
import modelo.ManagerPrincipal;
import modelo.Usuario;

public class PerfilController {
	
	@FXML
	private TableView<Alquiler> tablaAlquileres;

	@FXML
	private TableColumn<Alquiler, LocalDate> colFechaAlq;
	
	@FXML
	private TableColumn<Alquiler, LocalDate> colFechaPrev;

	@FXML
	private TableColumn<Alquiler, LocalDate> colFechaDev;

	@FXML
	private TableColumn<Alquiler, String> colLibros;

	private Usuario usuario;
	
	@FXML
	private void initialize() {

		colFechaAlq.setCellValueFactory(new PropertyValueFactory<>("fechaAlquiler"));
		colFechaPrev.setCellValueFactory(new PropertyValueFactory<>("fechaPrevista"));
		colFechaDev.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

		colLibros.setCellValueFactory(cd -> new SimpleStringProperty(
				cd.getValue().getLibros().stream().map(Libro::getNombre_libro).collect(Collectors.joining(", "))));
		
		cargarAlquileres();

	}
	
	private void cargarAlquileres() {
		usuario = LoginController.getUsuarioActual();
		
		try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {

			List<Alquiler> alquileres = session
					.createQuery("SELECT DISTINCT a " + "FROM Alquiler a " + "LEFT JOIN FETCH a.libros "
							+ "WHERE a.usuario.id = :uid", Alquiler.class)
					.setParameter("uid", usuario.getId_usuario()).list();

			tablaAlquileres.setItems(FXCollections.observableArrayList(alquileres));
		}
	}
	
	@FXML
	private void regresar() throws IOException {
		BiblioApp.setRoot("menu");
	}
}
