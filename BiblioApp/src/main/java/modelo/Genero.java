package modelo;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "género")
public class Genero implements Serializable {
	
	@Id
	@Column(name = "ID_Género")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_genero;
	@Column(name = "Nombre_Género", unique = true, nullable = false)
	private String nombre_genero;
	
	@ManyToMany(mappedBy = "generos")
	private List<Libro> libros;
	
	public Genero() {
		
	}
	
	public Genero(String nombre_genero) {
		this.nombre_genero = nombre_genero;
	}

	public int getId_genero() {
		return id_genero;
	}

	public void setId_genero(int id_genero) {
		this.id_genero = id_genero;
	}

	public String getNombre_genero() {
		return nombre_genero;
	}

	public void setNombre_genero(String nombre_genero) {
		this.nombre_genero = nombre_genero;
	}

	public List<Libro> getLibros() {
		return libros;
	}

	public void setLibros(List<Libro> libros) {
		this.libros = libros;
	}
	
}
