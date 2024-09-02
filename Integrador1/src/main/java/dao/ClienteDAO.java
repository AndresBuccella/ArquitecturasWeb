package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import main.java.entities.Cliente;

public class ClienteDAO implements DAO<Cliente>{

	public ClienteDAO() {}
	
	private void validateConnection(Connection conn) {
	    if (conn == null) {
	        throw new IllegalArgumentException("La conexión no puede ser null");
	    }
	}
	@Override
	public Optional<Cliente> get(Connection conn, int idCliente) {
		this.validateConnection(conn);
		Cliente c = null;
		String query = "SELECT * FROM cliente WHERE (idCliente) like ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				c = new Cliente(
						rs.getInt("idCliente"),
						rs.getString("nombre"),
						rs.getString("email")
						);
			}
				
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al obtener el cliente" + idCliente);
		}

		return Optional.ofNullable(c);
	}

	@Override
	public Iterable<Cliente> getAll(Connection conn) {
		this.validateConnection(conn);

		List<Cliente> resultList = new LinkedList<Cliente>();
		String query = "SELECT * FROM cliente";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Cliente c = new Cliente(
						rs.getInt("idCliente"),
						rs.getString("nombre"),
						rs.getString("email")
						);
				resultList.addLast(c);
			}
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al obtener todos los clientes");
		}
		
		return resultList;
	}

	@Override
	public void save(Connection conn, Cliente clienteNuevo) {
		this.validateConnection(conn);
		if(clienteNuevo == null)
			throw new IllegalArgumentException("El cliente actualizado no puede ser null");
		String query = "INSERT INTO cliente VALUES(?,?,?)";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, clienteNuevo.getIdCliente());
			ps.setString(2, clienteNuevo.getNombre());
			ps.setString(3, clienteNuevo.getEmail());
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println("Error al insertar el nuevo cliente");
		}

	}

	@Override
	public void update(Connection conn, Cliente clienteActualizado) {
		this.validateConnection(conn);
		if(clienteActualizado == null)
			throw new IllegalArgumentException("El cliente actualizado no puede ser null");
		
		String query = "UPDATE cliente SET nombre = ?, email = ? WHERE idCliente = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, clienteActualizado.getNombre());
			ps.setString(2, clienteActualizado.getEmail());
			ps.setInt(3, clienteActualizado.getIdCliente());
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			e.printStackTrace();
            System.err.println("Error al actualizar cliente: " + clienteActualizado.getIdCliente());
		}

	}

	@Override
	public void delete(Connection conn, int id) {
		this.validateConnection(conn);
		if(id <= 0)
			throw new IllegalArgumentException("El id debe ser mayor a 0");
			
		String query = "DELETE FROM cliente WHERE idCliente = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al borrar el cliente: " + id);
		}

	}
	
	public Iterable<Cliente> getClientesPorFacturacion(Connection conn){
		List<Cliente> list = new LinkedList<Cliente>();
		String query = "SELECT c.*, SUM(p.valor * fp.cantidad) as total "
				+ "FROM cliente c "
				+ "JOIN factura f USING (idCliente) "
				+ "JOIN factura_producto fp USING (idFactura) "
				+ "JOIN producto p USING (idProducto) "
				+ "GROUP BY c.idCliente "
				+ "ORDER BY total desc";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Cliente cliente = new Cliente(
						rs.getInt("idCliente"),
						rs.getString("nombre"),
						rs.getString("email"));
				list.addLast(cliente);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println("Error al obtener los clientes que mas facturaron en orden");
		}
		return list;
	}

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
		if(conn == null)
			throw new IllegalArgumentException("La conexión no puede ser null");
		String table = "CREATE TABLE IF NOT EXISTS cliente("
				+ "idCliente INT,"
				+ "nombre VARCHAR(500),"
				+ "email VARCHAR(350),"
				+ "PRIMARY KEY(idCliente)"
				+ ")";
		try {
			PreparedStatement ps = conn.prepareStatement(table);
			ps.execute();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			System.err.println("Error al crear la tabla cliente");
		}
	}
	public void dropTable(Connection conn) {
		if(conn == null)
			throw new IllegalArgumentException("La conexión no puede ser null");
		
		String delete = "DROP TABLE IF EXISTS cliente";
		try {
			PreparedStatement ps = conn.prepareStatement(delete);
			ps.execute();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			System.err.println("Error al borrar la tabla cliente");
		}
	}
}
