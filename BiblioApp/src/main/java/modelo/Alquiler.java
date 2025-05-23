package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "alquiler")
public class Alquiler implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_Alquiler")
	private int id;

	@Column(name = "Fecha_Alquiler", nullable = false)
	private LocalDate fechaAlquiler;
	
	@Column(name = "Fecha_Prevista", nullable = false)
	private LocalDate fechaPrevista;

	@Column(name = "Fecha_Devolución")
	private LocalDate fechaDevolucion;

	@ManyToOne
	@JoinColumn(name = "ID_Usuario")
	private Usuario usuario;

	@ManyToMany
	@JoinTable(name = "alquiler_libros", joinColumns = @JoinColumn(name = "ID_Alquiler"), inverseJoinColumns = @JoinColumn(name = "ID_Libro"))
	private List<Libro> libros;

	public Alquiler() {
	
	}

	public int getId() {
		return id;
	}

	public LocalDate getFechaAlquiler() {
		return fechaAlquiler;
	}

	public void setFechaAlquiler(LocalDate fechaAlquiler) {
		this.fechaAlquiler = fechaAlquiler;
	}
	
	public LocalDate getFechaPrevista() {
		return fechaPrevista;
	}
	
	public void setFechaPrevista(LocalDate fechaPrevista) {
		this.fechaPrevista = fechaPrevista;
	}

	public LocalDate getFechaDevolucion() {
		return fechaDevolucion;
	}

	public void setFechaDevolucion(LocalDate fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Libro> getLibros() {
		return libros;
	}

	public void setLibros(List<Libro> libros) {
		this.libros = libros;
	}

	@Transient
	public boolean estaActivo() {
		return fechaDevolucion == null;
	}
}
