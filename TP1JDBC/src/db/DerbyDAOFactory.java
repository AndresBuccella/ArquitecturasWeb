package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DerbyDAOFactory extends DBFactory {
	
	
	
	public DerbyDAOFactory() {}

	public static void main(String[] args) {
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		try {
			Class.forName(driver).getDeclaredConstructor().newInstance();			
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
		
		String uri = "jdbc:derby:MyDerbyDb;create=true";
		
		try {
			Connection conn = DriverManager.getConnection(uri);	
			//createTables(conn);
			conn.setAutoCommit(false);
			addPersona(conn, 1, "Esteban", 23);
			addPersona(conn, 2, "Juan", 20);
			addPersona(conn, 3, "Rodolfo", 73);
			addPersona(conn, 4, "Casto", 92);
			getAll(conn);
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}

	}
	
	private static void getAll(Connection conn) {
		String select = "SELECT * FROM persona";
		try {
			PreparedStatement ps = conn.prepareStatement(select);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println(rs.getInt(1) + ", " + rs.getString(2) + ", " + rs.getInt(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void addPersona(Connection conn, int id, String nombre, int edad) throws SQLException {
		String insert = "INSERT INTO persona (id, nombre, edad) VALUES(?,?,?)";
		PreparedStatement ps = conn.prepareStatement(insert);
		ps.setInt(1, id);
		ps.setString(2, nombre);
		ps.setInt(3, edad);
		ps.executeUpdate();
		ps.close();
		conn.commit();
	}

	private static void createTables(Connection conn) throws SQLException {
		String table = "CREATE TABLE persona(" +
						"id INT," +
						"nombre VARCHAR(200)," +
						"edad INT," +
						"PRIMARY KEY(id))";
		conn.prepareStatement(table).execute();
		conn.commit();
		
	}

	@Override
	public daos.PersonaDAO getPersonaDAO() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
