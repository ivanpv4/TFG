package controlador;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;

import app.BiblioApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import modelo.Libro;
import modelo.ManagerPrincipal;
import modelo.Genero;

public class CatalogoController {

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
	private TextField buscador;
	
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
	private void filtrarLibros() {
		String filtro = normalizar(buscador.getText().trim());
		
		ObservableList<Libro> filtrados = listaLibros.filtered(libro -> {
			String nombre = normalizar(libro.getNombre_libro());
			String autor = normalizar(libro.getAutor());
			String genero = normalizar(libro.getGeneros().stream().map(g -> g.getNombre_genero()).collect(Collectors.joining(", ")));
			String año = String.valueOf(libro.getAño_publicacion());
			String isbn = normalizar(libro.getIsbn());
			String disponibilidad = libro.isDisponibilidad() ? "Libre" : "Alquilado";
			
			return nombre.contains(filtro) || autor.contains(filtro) || genero.contains(filtro) || año.contains(filtro) || isbn.contains(filtro)
					|| disponibilidad.contains(filtro) || normalizar(disponibilidad).contains(filtro);
		});
		
		tablaLibros.setItems(filtrados);
	}
	
	private String normalizar(String texto) {
		return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
	}
	
	@FXML
	private void regresar() throws IOException {
		if (LoginController.esAdmin) {
			BiblioApp.setRoot("menuAdmin");
		} else {
			BiblioApp.setRoot("menu");
		}
		
	}

}
