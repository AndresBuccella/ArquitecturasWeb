package main.java.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import main.java.dao.ClienteDAO;
import main.java.dao.FacturaDAO;
import main.java.dao.FacturaProductoDAO;
import main.java.dao.ProductoDAO;

public class MySQLDAOFactory extends FactoryDB{
	
	private static final String URL = "jdbc:mysql://localhost:3306/bdd_integrador1";
	private static final String USER = "root";
	private static final String PASSWORD = "password";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	
	private static MySQLDAOFactory instance = null;
	
	private MySQLDAOFactory() {
        try {
        	Class.forName(DRIVER);
        } catch (Exception e) { //Por que no se puede hacer especifico??
        	/*InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                 | NoSuchMethodException | SecurityException | ClassNotFoundException */
            e.printStackTrace();
            System.exit(1);
        }
	}

	public static MySQLDAOFactory getInstance() throws SQLException{
    	if(instance == null) {
    		instance = new MySQLDAOFactory();
    	}
    	return instance;
	}

    public Connection getConnection() {
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
    
	@Override
	public ClienteDAO getClienteDAO() throws SQLException {
		return ClienteDAO.getInstance();
	}

	@Override
	public ProductoDAO getProductoDAO() throws SQLException {
		return ProductoDAO.getInstance();
	}

	@Override
	public FacturaDAO getFacturaDAO() throws SQLException {
		return FacturaDAO.getInstance();
	}

	@Override
	public FacturaProductoDAO getFacturaProductoDAO() throws SQLException {
		return FacturaProductoDAO.getInstance();
	}
}
