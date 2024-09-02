package main.java.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import main.java.dao.Direccion;
import main.java.dao.Persona;



public class MySQLDAOFactory extends DBFactory {
	
	private static MySQLDAOFactory instance = null;
	
	private static final String URL = "jdbc:mysql://localhost:3306/primeraBDD";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private MySQLDAOFactory(){
        try {
        	Class.forName(DRIVER);
        } catch (Exception e) { //Por que no lo puedo hacer especifico??
        	/*InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | NoSuchMethodException | SecurityException | ClassNotFoundException */
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static synchronized DBFactory getInstance() {
    	if(instance == null) {
    		instance = new MySQLDAOFactory();
    	}
    	return instance;
    }
    
    public static Connection createConnection() {
    	Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			conn.setAutoCommit(false);		
		}catch(SQLException e) {
			e.printStackTrace();
		}
    		return conn;
    }
    
    public void closeConnection(Connection conn) throws SQLException{
    	if(conn != null && !conn.isClosed())
    		conn.close();
    }
	
	private static void rollbackQuietly(Connection conn) {
		try {
			conn.rollback();
		}catch(SQLException e) {
			System.err.println("Error al hacer rollback: " + e.getMessage());
			//e.printStackTrace();
		}
	}

	public static void createTable(Connection conn){
		if(conn == null)
			throw new IllegalArgumentException("La conexión no puede ser null");
		
		String table = "CREATE TABLE IF NOT EXISTS persona(" +
				"id INT," +
				"nombre VARCHAR(200)," +
				"edad INT," +
				"PRIMARY KEY(id))";
		try(PreparedStatement ps = conn.prepareStatement(table)){
			ps.execute();
			conn.commit();
		}catch(SQLException e){
			rollbackQuietly(conn);
			System.err.println("Error al crear la tabla: " + e.getMessage());
			//e.printStackTrace();
		}
	}
	
	public static void dropTable(Connection conn){
		if(conn == null)
			throw new IllegalArgumentException("La conexión no puede ser null");
		
		String dropPersona = "DROP TABLE IF EXISTS persona";
		
		try(PreparedStatement ps = conn.prepareStatement(dropPersona)){
			ps.execute();
			conn.commit();
		}catch(SQLException e){
			rollbackQuietly(conn);
			System.err.println("Error al borrar la tabla: " + e.getMessage());
			//e.printStackTrace();
			//throw new RuntimeException("Error al eliminar la tabla", e); //Si necesito que el llamador maneje la excepcion
		}
	}
	
	
	@Override
	public Persona getPersonaDAO() throws SQLException {
		return null; //new Persona(createConnection());
	}

}
