package controlador;

import java.io.IOException;
import java.util.List;

import org.controlsfx.control.CheckComboBox;
import org.hibernate.Session;

import app.BiblioApp;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import modelo.Genero;
import modelo.Libro;
import modelo.ManagerPrincipal;

public class AgregarLibroController {

	@FXML
	private Label mensaje, mensaje2, mensaje3, mensaje4, mensaje5;

	@FXML
	private TextField nombreField, autorField, isbnField;

	@FXML
	private CheckComboBox<Genero> generosCombo;

	@FXML
	private DatePicker fechaPicker;

	@FXML
	private Pane filtro;

	@FXML
	private GridPane confirmarMensaje;

	private String campoVacio = "Este campo no puede estar vacío";

	@FXML
	public void initialize() {
		cargarGeneros();
	}

	private void cargarGeneros() {
		try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {
			List<Genero> generos = session.createQuery("FROM Genero", Genero.class).list();
			generosCombo.getItems().setAll(generos);
		}
	}

	@FXML
	private void registrarLibro() {
		mensaje.setText("");
		mensaje2.setText("");
		mensaje3.setText("");
		mensaje4.setText("");
		mensaje5.setText("");

		if (nombreField.getText().isBlank()) {
			mensaje.setText(campoVacio);
		} else if (autorField.getText().isBlank()) {
			mensaje2.setText(campoVacio);
		} else if (generosCombo.getCheckModel().isEmpty()) {
			mensaje3.setText(campoVacio);
		} else if (fechaPicker.getValue() == null) {
			mensaje4.setText(campoVacio);
		} else if (isbnField.getText().isBlank()) {
			mensaje5.setText(campoVacio);
		} else {
			Libro libro = new Libro(isbnField.getText(), nombreField.getText(), autorField.getText(),
					java.sql.Date.valueOf(fechaPicker.getValue()), true);
			
			List<Genero> generosSeleccionados = generosCombo.getCheckModel().getCheckedItems();
			libro.setGeneros(generosSeleccionados);
			
			try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {
				session.beginTransaction();
				session.persist(libro);
				session.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			confirmarRegistro();
			
			nombreField.clear();
			autorField.clear();
			isbnField.clear();
			fechaPicker.setValue(null);
			generosCombo.getCheckModel().clearChecks();
		}
	}

	@FXML
	private void registrarLibroEnter(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			registrarLibro();
		}
	}

	@FXML
	private void confirmarRegistro() {
		confirmarMensaje.setVisible(true);
		filtro.setVisible(true);
		filtro.toFront();
		confirmarMensaje.toFront();
	}

	@FXML
	private void confirmar() {
		confirmarMensaje.toBack();
		filtro.toBack();
		filtro.setVisible(false);
		confirmarMensaje.setVisible(false);
	}

	@FXML
	private void regresarAdministracion() throws IOException {
		BiblioApp.setRoot("administracion");
	}
}
