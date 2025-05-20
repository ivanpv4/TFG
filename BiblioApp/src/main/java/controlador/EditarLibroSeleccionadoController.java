package controlador;

import java.io.IOException;
import java.util.List;

import org.controlsfx.control.CheckComboBox;
import org.hibernate.Session;

import app.BiblioApp;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import modelo.Libro;
import modelo.Genero;
import modelo.ManagerPrincipal;

public class EditarLibroSeleccionadoController {
	
	private Libro libro;

    @FXML 
    private TextField nombreField;
    
    @FXML 
    private TextField autorField;
    
    @FXML
    private CheckComboBox<Genero> generosCombo;
    
    @FXML
    private DatePicker fechaPicker;
    
    @FXML 
    private TextField isbnField;
    
    @FXML
    private RadioButton disponibleRB;
    
    @FXML
    private RadioButton alquiladoRB;
    
    @FXML
    private Pane filtro;
    
    @FXML
    private GridPane confirmarEdicion, confirmarMensaje;
    
    @FXML
    public void initialize() {
        cargarGeneros();
    }

    public void setLibro(Libro libro) {
    	this.libro = libro;
    	
    	nombreField.setText(libro.getNombre_libro());
    	autorField.setText(libro.getAutor());
    	if (libro.getAño_publicacion() != null) {
    	    fechaPicker.setValue(libro.getAño_publicacion().toLocalDate());
    	} else {
    	    fechaPicker.setValue(null);
    	}
    	isbnField.setText(libro.getIsbn());
    	
    	disponibleRB.setSelected(libro.isDisponibilidad());
    	alquiladoRB.setSelected(!libro.isDisponibilidad());
    	
    	marcarGenerosSeleccionados();
    }
    
    private void cargarGeneros() {
    	try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {
    		List<Genero> generos = session.createQuery("FROM Genero", Genero.class).list();
    		generosCombo.getItems().setAll(generos);
    	}
    }
    
    private void marcarGenerosSeleccionados() {
    	generosCombo.getCheckModel().clearChecks();
    	for (Genero g : libro.getGeneros()) {
    		generosCombo.getCheckModel().check(g);
    	}
    }
    
    @FXML
    private void guardarCambios() {
    	libro.setNombre_libro(nombreField.getText());
    	libro.setAutor(autorField.getText());
    	libro.setIsbn(isbnField.getText());
    	
    	if (fechaPicker.getValue() != null) {
    	    libro.setAño_publicacion(java.sql.Date.valueOf(fechaPicker.getValue()));
    	} else {
    	    libro.setAño_publicacion(null);
    	}
    	
    	libro.setDisponibilidad(disponibleRB.isSelected());
    	
    	List<Genero> generosSeleccionados = generosCombo.getCheckModel().getCheckedItems();
    	libro.setGeneros(generosSeleccionados);
    	
    	try (Session session = ManagerPrincipal.getSessionFactory().openSession()) {
    		session.beginTransaction();
    		session.merge(libro);
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
    private void confirmar() {
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
    	BiblioApp.setRoot("editarLibro");
    }
    
}
