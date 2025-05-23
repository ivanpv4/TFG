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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Alquiler;
import modelo.Libro;
import modelo.ManagerPrincipal;
import modelo.Usuario;

public class HistorialSeleccionadoController {

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

	@FXML
	private TableColumn<Alquiler, Void> colAccion;

	private Usuario usuario;

	public void setUsuario(Usuario user) {
		this.usuario = user;
		cargarAlquileres();
	}

	@FXML
	private void initialize() {

		colFechaAlq.setCellValueFactory(new PropertyValueFactory<>("fechaAlquiler"));
		colFechaPrev.setCellValueFactory(new PropertyValueFactory<>("fechaPrevista"));
		colFechaDev.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));

		colLibros.setCellValueFactory(cd -> new SimpleStringProperty(
				cd.getValue().getLibros().stream().map(Libro::getNombre_libro).collect(Collectors.joining(", "))));

		colAccion.setCellFactory(col -> new TableCell<Alquiler, Void>() {
			private final Button btn = new Button("Finalizar");
			{
				btn.setOnAction(e -> {
					Alquiler alq = getTableView().getItems().get(getIndex());
					finalizarAlquiler(alq);
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					Alquiler alq = getTableView().getItems().get(getIndex());
					btn.setDisable(!esActivo(alq));
					setGraphic(btn);
				}
			}
		});
	}

	private void cargarAlquileres() {
		try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {

			List<Alquiler> alquileres = session
					.createQuery("SELECT DISTINCT a " + "FROM Alquiler a " + "LEFT JOIN FETCH a.libros "
							+ "WHERE a.usuario.id = :uid", Alquiler.class)
					.setParameter("uid", usuario.getId_usuario()).list();

			tablaAlquileres.setItems(FXCollections.observableArrayList(alquileres));
		}
	}

	private void finalizarAlquiler(Alquiler alq) {
		if (!esActivo(alq))
			return;

		try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {
			session.beginTransaction();

			Alquiler gest = session.get(Alquiler.class, alq.getId());

			gest.setFechaDevolucion(LocalDate.now());

			for (Libro l : gest.getLibros()) {
				l.setDisponibilidad(true);
				session.merge(l);
			}

			session.getTransaction().commit();
		}
		cargarAlquileres();
	}

	private boolean esActivo(Alquiler a) {
		return a.getFechaDevolucion() == null;
	}

	@FXML
	private void regresar() throws IOException {
		BiblioApp.setRoot("historial");
	}
}
