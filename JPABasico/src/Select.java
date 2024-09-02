import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import main.java.dao.Persona;

public class Select {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Example");
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		//GET
		//Persona p = em.find(Persona.class, 1);
		//System.out.println(p);
		
		//GETALL
		@SuppressWarnings("unchecked")
		List<Persona> personas = em.createQuery("SELECT p FROM Persona p").getResultList();
		personas.forEach(per -> System.out.println(per));
		
		em.getTransaction().commit();
		em.close();
		emf.close();
	}

}
