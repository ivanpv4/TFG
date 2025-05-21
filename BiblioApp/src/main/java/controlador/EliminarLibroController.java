package controlador;

import java.io.IOException;
import java.sql.Date;
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
import modelo.Genero;
import modelo.Libro;
import modelo.ManagerPrincipal;

public class EliminarLibroController {
	
	@FXML
	private TableView<Libro> tablaLibros;

	@FXML
	private TableColumn<Libro, String> colNombre;

	@FXML
	private TableColumn<Libro, String> colAutor;

	@FXML
	private TableColumn<Libro, String> colGenero;

	@FXML
	private TableColumn<Libro, Date> colAño;

	@FXML
	private TableColumn<Libro, String> colISBN;

	@FXML
	private TableColumn<Libro, String> colDisponibilidad;
	
	@FXML
	private TableColumn<Libro, Boolean> colSeleccionar;
	
	@FXML
	private TextField buscador;
	
	@FXML
	private Pane filtro;
	
	@FXML
	private GridPane confirmarEliminacion, confirmarMensaje;
	
	private ObservableList<Libro> listaLibros;
	
	@FXML
	public void initialize() {
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_libro"));
		colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
		colAño.setCellValueFactory(new PropertyValueFactory<>("año_publicacion"));
		colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		colDisponibilidad.setCellValueFactory(cellData -> {
			String estado = cellData.getValue().isDisponibilidad() ? "Disponible" : "Alquilado";
			return new SimpleStringProperty(estado);
		});

		colGenero.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGeneros().stream()
				.map(Genero::getNombre_genero).collect(Collectors.joining(", "))));

		colSeleccionar.setCellValueFactory(cellData -> cellData.getValue().seleccionadoProperty());
		colSeleccionar.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccionar));
		
		cargarLibros();

	}
	
	private void cargarLibros() {
		try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {
			List<Libro> libros = session.createQuery("SELECT DISTINCT l FROM Libro l  LEFT JOIN FETCH l.generos", Libro.class).getResultList();
			listaLibros = FXCollections.observableArrayList(libros);
			tablaLibros.setItems(listaLibros);
		}
	}
	
	@FXML
	private void eliminarLibros() {
		List<Libro> libros = listaLibros.stream().filter(Libro::isSeleccionado).collect(Collectors.toList());
		if (libros.isEmpty()) {
			System.out.println("No hay libros seleccionados");
			return;
		}
		
		try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {
			session.beginTransaction();
			for (Libro libro : libros) {
				session.remove(session.contains(libro) ? libro : session.merge(libro));
			}
			
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		apareceConfirmarMensaje();
		
		listaLibros.removeAll(libros);
		tablaLibros.refresh();
	}
	
	@FXML
	private void filtrarLibros() {
		String filtro = normalizar(buscador.getText().trim());
		
		ObservableList<Libro> filtrados = listaLibros.filtered(libro -> {
			String nombre = normalizar(libro.getNombre_libro());
			String autor = normalizar(libro.getAutor());
			String genero = normalizar(libro.getGeneros().stream().map(g -> g.getNombre_genero()).collect(Collectors.joining(", ")));
			String año = String.valueOf(libro.getAño_publicacion());
			String isbn = normalizar(libro.getIsbn());
			String disponibilidad = libro.isDisponibilidad() ? "Disponible" : "Alquilado";
			
			return nombre.contains(filtro) || autor.contains(filtro) || genero.contains(filtro) || año.contains(filtro) || isbn.contains(filtro)
					|| disponibilidad.contains(filtro) || normalizar(disponibilidad).contains(filtro);
		});
		
		tablaLibros.setItems(filtrados);
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
