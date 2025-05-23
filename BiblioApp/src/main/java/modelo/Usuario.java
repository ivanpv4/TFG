package modelo;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
	
	@Id
	@Column(name = "ID_Usuario")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_usuario;
	
	@Column(name = "Usuario", unique = true, nullable = false)
	private String usuario;
	
	@Column(name = "Contraseña", nullable = false)
	private String contraseña;
	
	@Column(name = "Admin")
	private boolean admin;
	
	@Transient
	private final BooleanProperty seleccionado = new SimpleBooleanProperty(false);
	
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Alquiler> alquileres;
	
	public Usuario(String usuario, String contraseña) {
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.admin = false;
	}
	
	public Usuario() {
		
	}
	
	public Usuario(String usuario, String contraseña, boolean admin) {
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.admin = admin;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
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
	
	public List<Alquiler> getAlquileres() {
	    return alquileres;
	}
	
	@Override
	public String toString() {
		return usuario;
	}
	
}
