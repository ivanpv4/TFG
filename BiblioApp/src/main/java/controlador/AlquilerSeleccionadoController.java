package controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import app.BiblioApp;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import modelo.Alquiler;
import modelo.Libro;
import modelo.ManagerPrincipal;
import modelo.Usuario;
import javafx.beans.property.SimpleStringProperty;


public class AlquilerSeleccionadoController {

    @FXML
    private TableView<Libro> tablaSeleccionados;
    
    @FXML
    private TableColumn<Libro, String> colNombre;
    
    @FXML
    private TableColumn<Libro, String> colAutor;
    
    @FXML
    private TableColumn<Libro, Integer> colAño;
    
    @FXML
    private TableColumn<Libro, String> colISBN;
    
    @FXML
    private TableColumn<Libro, String> colGenero;
    
    @FXML
    private Pane filtro;
    
    @FXML
    private GridPane mostrarError, mostrarInfo;
    
    @FXML
    private Label mFechaAlquiler, mFechaDevolucion, mError, mInfo;

    private List<Libro> librosSeleccionados;

    public void añadirLibros(List<Libro> libros) {
        this.librosSeleccionados = libros;
        if (tablaSeleccionados != null) { // En caso se llama después de initialize()
            tablaSeleccionados.setItems(FXCollections.observableArrayList(librosSeleccionados));
        }
    }

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre_libro"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAño.setCellValueFactory(new PropertyValueFactory<>("año_publicacion"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colGenero.setCellValueFactory(cellData -> 
        new SimpleStringProperty(
            cellData.getValue().getGeneros().stream()
                .map(g -> g.getNombre_genero())
                .collect(Collectors.joining(", "))
        )
    );
        mFechaAlquiler.setText("La fecha prevista de alquiler es: " + LocalDate.now());
        mFechaDevolucion.setText("La fecha máxima prevista de devolución es: " + LocalDate.now().plusDays(14));
    }

    @FXML
    private void confirmarAlquiler() {
        Usuario usuario = LoginController.getUsuarioActual();

        try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {
            session.beginTransaction();
            usuario = session.get(Usuario.class, usuario.getId_usuario());

            long librosEnAlquiler = usuario.getAlquileres().stream()
                    .filter(this::esAlquilerActivo)
                    .flatMap(a -> a.getLibros().stream())
                    .count();

            if (librosEnAlquiler + librosSeleccionados.size() > 5) {
                mostrarError("Actualmente tienes " + librosEnAlquiler + " libro/s alquilado/s");
                session.getTransaction().rollback();
                return;
            }

            Alquiler nuevoAlquiler = new Alquiler();
            nuevoAlquiler.setUsuario(usuario);
            nuevoAlquiler.setFechaAlquiler(LocalDate.now());
            nuevoAlquiler.setFechaDevolucion(LocalDate.now().plusDays(14));
            nuevoAlquiler.setLibros(librosSeleccionados);

            for (Libro libro : librosSeleccionados) {
                libro.setDisponibilidad(false);
                session.merge(libro);
            }

            session.persist(nuevoAlquiler);
            session.getTransaction().commit();

            mostrarInfo("La fecha máxima de devolución es: " + nuevoAlquiler.getFechaDevolucion());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("HA PETADO");
        }
    }

    private boolean esAlquilerActivo(Alquiler a) {
        return a.getFechaDevolucion().isAfter(LocalDate.now());
    }
    
    private void mostrarInfo(String mensaje) {
    	mostrarInfo.setVisible(true);
    	filtro.setVisible(true);
    	mInfo.setText(mensaje);
    	filtro.toFront();
    	mostrarInfo.toFront();
    }
    
    private void mostrarError(String mensaje) {
    	mostrarError.setVisible(true);
    	filtro.setVisible(true);
    	mError.setText(mensaje);
    	filtro.toFront();
    	mostrarError.toFront();
    }
    
    @FXML
    private void ocultarError() {
    	mostrarError.setVisible(false);
    	filtro.setVisible(false);
    	filtro.toBack();
    	mostrarError.toBack();
    }
    
    @FXML
    private void regresar() throws IOException {
    	BiblioApp.setRoot("alquiler");
    }
}
