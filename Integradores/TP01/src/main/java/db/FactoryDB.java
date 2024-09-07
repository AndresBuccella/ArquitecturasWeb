package main.java.db;

import java.sql.Connection;
import java.sql.SQLException;

import main.java.dao.ClienteDAO;
import main.java.dao.FacturaDAO;
import main.java.dao.FacturaProductoDAO;
import main.java.dao.ProductoDAO;

public abstract class FactoryDB {

	public static final int MYSQL_JDBC = 1;
	public static final int DERBY_JDBC = 2;
	public static final int JPA_HIBERNATE = 3;

	public abstract ClienteDAO getClienteDAO() throws SQLException;
	public abstract ProductoDAO getProductoDAO() throws SQLException;
	public abstract FacturaDAO getFacturaDAO() throws SQLException;
	public abstract FacturaProductoDAO getFacturaProductoDAO() throws SQLException;
	public abstract Connection getConnection() throws SQLException;
	public abstract void closeConnection(Connection conn) throws SQLException;

	public static FactoryDB getDAOFactory(int whichFactory) throws SQLException {
		switch(whichFactory) {
			case MYSQL_JDBC: return MySQLDAOFactory.getInstance();
			//case DERBY_JDBC: return new DerbyDAOFactory();
			//case JPA_HIBERNATE: return new JPAHibernateJDBCDAOFactory();
			default: return null;
		}
	}
}
