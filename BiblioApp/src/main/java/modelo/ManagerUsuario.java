package modelo;

import org.hibernate.Session;
import org.hibernate.query.Query;

public class ManagerUsuario {
	
	public static boolean existeUsuario(String usuario) {
		Session session = ManagerPrincipal.sessionFactory.openSession();
		try {
			// Como la base de datos MySQL usa colaciones case-insensitive como utf8_general_ci, no es necesario especificar la query a minúsculas con un 
			// "LOWER(usuario) = :nombre" Si cambiase la colación a otra como utf8mb4_bin o la base de datos a PostgreSQL, sí habría que especificar
			Query<Usuario> query = session.createQuery("FROM Usuario WHERE usuario = :nombre", Usuario.class);
			query.setParameter("nombre", usuario);
			return !query.list().isEmpty();
		} finally {
			session.close();
		}
	}
	
	public static boolean hacerLogin(String usuario, String contraseña) {
		Session session = ManagerPrincipal.sessionFactory.openSession();
		try {
			Query<Usuario> query = session.createQuery("FROM Usuario WHERE usuario = :nombre AND contraseña = :pass", Usuario.class);
			query.setParameter("nombre", usuario);
			query.setParameter("pass", contraseña);
			return !query.list().isEmpty();
		} finally {
			session.close();
		}
	}
	
	public static boolean esAdmin(String usuario) {
		Session session = ManagerPrincipal.sessionFactory.openSession();
		try {
			Query<Usuario> query = session.createQuery("FROM Usuario WHERE usuario = :nombre AND admin = TRUE", Usuario.class);
			query.setParameter("nombre", usuario);
			return !query.list().isEmpty();
		} finally {
			session.close();
		}
	}
	
	public static boolean registrarUsuario(String usuario, String contraseña) {
		if (existeUsuario(usuario)) {
			return false;
		}
		
		Usuario nuevoUsuario = new Usuario(usuario, contraseña);
		
		Session session = ManagerPrincipal.sessionFactory.openSession();
		session.beginTransaction();
		session.persist(nuevoUsuario);
		session.getTransaction().commit();
		session.close();
		return true;
	}
	
	public static boolean registrarUsuarioAdmin(String usuario, String contraseña, boolean admin) {
		if (existeUsuario(usuario)) {
			return false;
		}
		
		Usuario nuevoUsuario = new Usuario(usuario, contraseña, admin);
		
		Session session = ManagerPrincipal.sessionFactory.openSession();
		session.beginTransaction();
		session.persist(nuevoUsuario);
		session.getTransaction().commit();
		session.close();
		return true;
	}
	
	public static Usuario usuarioActual(String usuario) {
		try (Session session = ManagerPrincipal.sessionFactory.openSession()) {
			Query<Usuario> query = session.createQuery("FROM Usuario WHERE usuario = :nombre", Usuario.class);
			query.setParameter("nombre", usuario);
			return query.getSingleResult();
		}
	}
}

