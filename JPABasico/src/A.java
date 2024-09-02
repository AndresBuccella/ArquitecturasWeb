import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import main.java.dao.Direccion;
import main.java.dao.Persona;

public class A {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Example");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Direccion d = new Direccion();
		d.setCalle("Sarmiento");
		d.setCiudad("Tandil");
		em.persist(d);
		Persona j = new Persona();
		j.setNombre("Juan");
		j.setEdad(25);
		j.setDomicilio(d);
		Persona a = new Persona();
		a.setNombre("Ana");
		a.setEdad(20);
		a.setDomicilio(d);
		em.persist(j);
		em.persist(a);
		em.getTransaction().commit();
		em.close();
		emf.close();
	}
}
