package modelo;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import jakarta.persistence.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

@Entity
@Table(name = "libro")
public class Libro implements Serializable {

	@Id
	@Column(name = "ID_Libro")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_libro;
	@Column(name = "ISBN", nullable = false)
	private String isbn;
	@Column(name = "Nombre_Libro", nullable = false)
	private String nombre_libro;
	@Column(name = "Autor", nullable = false)
	private String autor;
	@Column(name = "Año_Publicación", nullable = false)
	private Date año_publicacion;
	@Column(name = "Disponibilidad")
	private boolean disponibilidad;

	@ManyToMany
	@JoinTable(name = "género_libros", joinColumns = @JoinColumn(name = "ID_Libro"), inverseJoinColumns = @JoinColumn(name = "ID_Género"))
	private List<Genero> generos;
	
	@Transient
	private final BooleanProperty seleccionado = new SimpleBooleanProperty(false);
	
	public Libro() {
		
	}

	public Libro(String isbn, String nombre_libro, String autor, Date año_publicacion, boolean disponibilidad) {
		this.isbn = isbn;
		this.nombre_libro = nombre_libro;
		this.autor = autor;
		this.año_publicacion = año_publicacion;
		this.disponibilidad = disponibilidad;
	}

	public int getId_libro() {
		return id_libro;
	}

	public void setId_libro(int id_libro) {
		this.id_libro = id_libro;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getNombre_libro() {
		return nombre_libro;
	}

	public void setNombre_libro(String nombre_libro) {
		this.nombre_libro = nombre_libro;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Date getAño_publicacion() {
		return año_publicacion;
	}

	public void setAño_publicacion(Date año_publicacion) {
		this.año_publicacion = año_publicacion;
	}

	public boolean isDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(boolean disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(List<Genero> generos) {
		this.generos = generos;
	}

	public BooleanProperty seleccionadoProperty() {
	    return seleccionado;
	}
	
	public boolean isSeleccionado() {
	    return seleccionado.get();
	}
	
	public void setSeleccionado(boolean seleccionado) {
	    this.seleccionado.set(seleccionado);
	}

}
