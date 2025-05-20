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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import modelo.Genero;
import modelo.Libro;
import modelo.ManagerPrincipal;

public class EditarLibroController {
	
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
			
			for (Libro libro : listaLibros) {
			    libro.seleccionadoProperty().addListener((obs, wasSelected, isNowSelected) -> {
			        if (isNowSelected) {
			            for (Libro otroLibro : listaLibros) {
			                if (otroLibro != libro) {
			                    otroLibro.setSeleccionado(false);
			                }
			            }
			            tablaLibros.refresh();
			        }
			    });
			}
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
	private void editarLibroSeleccionado() throws IOException {

	    Libro libro = listaLibros.stream()
	            .filter(Libro::isSeleccionado)
	            .findFirst()
	            .orElse(null);

	    if (libro == null) {
	        System.out.println("No hay libro seleccionado");
	        return;
	    }

	    // 1. Cargar FXML con FXMLLoader
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/editarLibroSeleccionado.fxml"));
	    Parent root = loader.load();

	    // 2. Obtener el controller recién creado
	    EditarLibroSeleccionadoController controller = loader.getController();

	    // 3. Pasar el libro a editar
	    controller.setLibro(libro);

	    Scene escenaActual = tablaLibros.getScene();
	    escenaActual.setRoot(root);
	}

	
	@FXML
	private void regresarAdministracion() throws IOException {
		BiblioApp.setRoot("administracion");
	}
	
}
