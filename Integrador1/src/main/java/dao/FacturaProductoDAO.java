package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import main.java.entities.FacturaProducto;

public class FacturaProductoDAO implements DAO<FacturaProducto>{

	public FacturaProductoDAO() {}
	
	private void validateConnection(Connection conn) {
	    if (conn == null) {
	        throw new IllegalArgumentException("La conexión no puede ser null");
	    }
	}
	@Override
	public Optional<FacturaProducto> get(Connection conn, int idFactura) {return Optional.ofNullable(null);}
/*	public Optional<FacturaProducto> get(Connection conn, int idFactura) {
		this.validateConnection(conn);
		FacturaProducto prod = null;
		String query = "SELECT * FROM factura_producto WHERE (idFactura, idProducto) like (?, ?)";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idFactura);
			ps.setInt(2, idProducto);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				prod = new FacturaProducto(
						rs.getInt("idFactura"),
						rs.getInt("idProducto"),
						rs.getInt("cantidad")
						);
			}
				
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al obtener la factura_producto" + idFactura);
		}

		return Optional.ofNullable(prod);
	}
*/
	
	@Override
	public Iterable<FacturaProducto> getAll(Connection conn) {
		this.validateConnection(conn);

		List<FacturaProducto> resultList = new LinkedList<FacturaProducto>();
		String query = "SELECT * FROM factura_producto";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				FacturaProducto prod = new FacturaProducto(
						rs.getInt("idFactura"),
						rs.getInt("idProducto"),
						rs.getInt("cantidad")
						);
				resultList.add(prod);
			}
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al obtener todas las factura_producto");
		}
		
		return resultList;
	}

	@Override
	public void save(Connection conn, FacturaProducto facturaProductoNueva) {
		this.validateConnection(conn);
		if(facturaProductoNueva == null)
			throw new IllegalArgumentException("La factura actualizada no puede ser null");
		String query = "INSERT INTO factura_producto VALUES(?,?,?)";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, facturaProductoNueva.getIdFactura());
			ps.setInt(2, facturaProductoNueva.getIdProducto());
			ps.setInt(3, facturaProductoNueva.getCantidadProducto());
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println("Error al insertar la nueva factura_producto");
		}

	}
	
	@Override
	public void update(Connection conn, FacturaProducto facturaProductoActualizada) {}
	/* Es correcto implementar un update en factura_producto? No se cambiaría el historial?
	@Override
	public void update(Connection conn, Factura facturaActualizada) {
		this.validateConnection(conn);
		if(facturaActualizada == null)
			throw new IllegalArgumentException("La factura actualizada no puede ser null");
		
		String query = "UPDATE factura_producto SET idCliente = ? WHERE id = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, facturaActualizada.getIdCliente());
			ps.setInt(2, facturaActualizada.getIdFactura());
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			e.printStackTrace();
            System.err.println("Error al actualizar factura_producto: " + facturaActualizada.getIdFactura());
		}

	}
*/
	
	@Override
	public void delete(Connection conn, int id) {}
	/* Es correcto implementar un delete en factura_producto? No se cambiaría el historial?
	@Override
	public void delete(Connection conn, int id) {
		this.validateConnection(conn);
		if(id <= 0)
			throw new IllegalArgumentException("El id debe ser mayor a 0");
			
		String query = "DELETE FROM factura_producto WHERE id = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al borrar la factura_producto: " + id);
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
		String table = "CREATE TABLE IF NOT EXISTS factura_producto("
				+ "idFactura INT,"
				+ "idProducto INT,"
				+ "cantidad INT,"
				+ "PRIMARY KEY(idFactura, idProducto),"
				+ "FOREIGN KEY(idFactura) REFERENCES factura(idFactura),"
				+ "FOREIGN KEY(idProducto) REFERENCES producto(idProducto)"
				+ ")";
		try {
			PreparedStatement ps = conn.prepareStatement(table);
			ps.execute();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			System.err.println("Error al crear la tabla factura_producto");
			e.printStackTrace();
		}
	}
	public void dropTable(Connection conn) {
		this.validateConnection(conn);
		
		String delete = "DROP TABLE IF EXISTS factura_producto";
		try {
			PreparedStatement ps = conn.prepareStatement(delete);
			ps.execute();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			System.err.println("Error al borrar la tabla factura_producto");
		}
	}
}
