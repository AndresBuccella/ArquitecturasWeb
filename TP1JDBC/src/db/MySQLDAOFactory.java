package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;

import daos.PersonaDAO;


public class MySQLDAOFactory extends DBFactory {
	
	private static MySQLDAOFactory instance = null;
	
	private static final String URL = "jdbc:mysql://localhost:3306/primeraBDD";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
	private static Connection conn;
    
    private MySQLDAOFactory(){}
    
    public static synchronized DBFactory getInstance() {
    	if(instance == null) {
    		instance = new MySQLDAOFactory();
    	}
    	return instance;
    }
    
    public static Connection createConnection() {
    	if(conn!=null)
    		return conn;

        try {
        	Class.forName(DRIVER);
        } catch (Exception e) { //Por que no lo puedo hacer especifico??
        	/*InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | NoSuchMethodException | SecurityException | ClassNotFoundException */
            e.printStackTrace();
            System.exit(1);
        }
		try {
			if(conn == null || conn.isClosed()) {    		
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				conn.setAutoCommit(false);
			}			
		}catch(SQLException e) {
			e.printStackTrace();
		}
    		return conn;
    }
    
    public void closeConnection() throws SQLException{
    	if(conn != null) {
    		conn.close();
    		conn = null;
    	}
    }
	
	@Override
	public PersonaDAO getPersonaDAO() throws SQLException {
		return new PersonaDAO(createConnection());
	}

}
