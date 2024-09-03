package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import main.java.entities.Factura;

public class FacturaDAO implements DAO<Factura>{
	
	public FacturaDAO() {}

	private void validateConnection(Connection conn) {
	    if (conn == null) {
	        throw new IllegalArgumentException("La conexión no puede ser null");
	    }
	}
	@Override
	public Optional<Factura> get(Connection conn, int idFactura) {
		this.validateConnection(conn);
		Factura prod = null;
		String query = "SELECT * FROM factura WHERE (idFactura) like ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idFactura);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				prod = new Factura(
						rs.getInt("idFactura"),
						rs.getInt("idCliente")
						);
			}
				
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al obtener la factura" + idFactura);
		}

		return Optional.ofNullable(prod);
	}

	@Override
	public Iterable<Factura> getAll(Connection conn) {
		this.validateConnection(conn);

		List<Factura> resultList = new LinkedList<Factura>();
		String query = "SELECT * FROM factura";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Factura prod = new Factura(
						rs.getInt("idFactura"),
						rs.getInt("idCliente")
						);
				resultList.add(prod);
			}
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al obtener todas las factura");
		}
		
		return resultList;
	}

	@Override
	public void save(Connection conn, Factura facturaNueva) {
		this.validateConnection(conn);
		if(facturaNueva == null)
			throw new IllegalArgumentException("La factura actualizada no puede ser null");
		String query = "INSERT INTO factura VALUES(?,?)";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, facturaNueva.getIdFactura());
			ps.setInt(2, facturaNueva.getIdCliente());
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println("Error al insertar la nueva factura");
		}

	}
	
	@Override
	public void update(Connection conn, Factura facturaActualizada) {}
	/* Es correcto implementar un update en factura? No se cambiaría el historial?
	@Override
	public void update(Connection conn, Factura facturaActualizada) {
		this.validateConnection(conn);
		if(facturaActualizada == null)
			throw new IllegalArgumentException("La factura actualizada no puede ser null");
		
		String query = "UPDATE factura SET idCliente = ? WHERE id = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, facturaActualizada.getIdCliente());
			ps.setInt(2, facturaActualizada.getIdFactura());
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			e.printStackTrace();
            System.err.println("Error al actualizar factura: " + facturaActualizada.getIdFactura());
		}

	}
*/
	
	@Override
	public void delete(Connection conn, int id) {}
	/* Es correcto implementar un delete en factura? No se cambiaría el historial?
	@Override
	public void delete(Connection conn, int id) {
		this.validateConnection(conn);
		if(id <= 0)
			throw new IllegalArgumentException("El id debe ser mayor a 0");
			
		String query = "DELETE FROM factura WHERE id = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al borrar la factura: " + id);
		}

	}
	*/
    
		private void closePsAndCommit(Connection conn, PreparedStatement ps) {
		this.validateConnection(conn);
            try {
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error al hacer commit");
            }finally {
            	try {
            		if(ps!=null)
            			ps.close();            		
            	}catch (SQLException e) {
                    e.printStackTrace();
                    System.err.println("Error al cerrar el prepared statement");
                }
            }
    }
	private static void rollbackQuietly(Connection conn) {
		try {
			conn.rollback();
		}catch(SQLException e) {
			System.err.println("Error al hacer rollback: " + e.getMessage());
			//e.printStackTrace();
		}
	}
	public void createTable(Connection conn) {
		this.validateConnection(conn);
		String table = "CREATE TABLE IF NOT EXISTS factura("
				+ "idFactura INT,"
				+ "idCliente INT,"
				+ "PRIMARY KEY(idFactura),"
                + "FOREIGN KEY (idCliente) REFERENCES cliente(idCliente)"
				+ ")";
		try {
			PreparedStatement ps = conn.prepareStatement(table);
			ps.execute();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			System.err.println("Error al crear la tabla factura");
		}
	}
	public void dropTable(Connection conn) {
		this.validateConnection(conn);
		
		String delete = "DROP TABLE IF EXISTS factura";
		try {
			PreparedStatement ps = conn.prepareStatement(delete);
			ps.execute();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			System.err.println("Error al borrar la tabla factura");
		}
	}
}
