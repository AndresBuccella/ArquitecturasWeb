package db;

import java.sql.SQLException;

import daos.PersonaDAO;

public abstract class DBFactory {

	public static final int MYSQL_JDBC = 1;
	public static final int DERBY_JDBC = 2;
	public static final int JPA_HIBERNATE = 3;
	
	public abstract PersonaDAO getPersonaDAO() throws SQLException;
	
	public static DBFactory getDAOFactory(int whichFactory) throws SQLException {
		switch(whichFactory) {
			case MYSQL_JDBC: return MySQLDAOFactory.getInstance();
			case DERBY_JDBC: return new DerbyDAOFactory();
			//case JPA_HIBERNATE: return new JPAHibernateJDBCDAOFactory();
			default: return null;
		}
	}
	
}
