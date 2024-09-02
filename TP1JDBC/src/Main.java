//import java.sql.Connection;
import java.util.Iterator;

import daos.PersonaDAO;
import db.DBFactory;
import entities.Persona;


public class Main {

	public static void main(String[] args) {
		try {
			DBFactory factory = DBFactory.getDAOFactory(1);
			PersonaDAO p = factory.getPersonaDAO();
			//System.out.println(p.get(2).get());
			Iterator<Persona> it = p.getAll();
			while(it.hasNext())
				System.out.println(it.next());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
