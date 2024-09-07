package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

import main.java.dto.ClienteConFacturacionDTO;
import main.java.entities.Cliente;

public class ClienteDAO implements DAO<Cliente>{

	private static ClienteDAO instance = null;

	private ClienteDAO() {}

	public static ClienteDAO getInstance(){
		if(instance == null){
			instance = new ClienteDAO();
		}
		return instance;
	}

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
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
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
		}finally {
			this.closePs(ps);
		}
		return Optional.ofNullable(c);
	}

	@Override
	public Iterable<Cliente> getAll(Connection conn) {
		this.validateConnection(conn);

		Deque<Cliente> resultList = new LinkedList<Cliente>();
		String query = "SELECT * FROM cliente";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
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
		}finally {
			this.closePs(ps);
		}
		//conn.close();
		return resultList;
	}

	@Override
	public void save(Connection conn, Cliente clienteNuevo) {
		this.validateConnection(conn);
		if(clienteNuevo == null)
			throw new IllegalArgumentException("El cliente actualizado no puede ser null");
		String query = "INSERT INTO cliente VALUES(?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, clienteNuevo.getIdCliente());
			ps.setString(2, clienteNuevo.getNombre());
			ps.setString(3, clienteNuevo.getEmail());
			ps.executeUpdate();
			this.commit(conn);
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println("Error al insertar el nuevo cliente");
		}finally {
			this.closePs(ps);
		}
	}

	@Override
	public void update(Connection conn, Cliente clienteActualizado) {
		this.validateConnection(conn);
		if(clienteActualizado == null)
			throw new IllegalArgumentException("El cliente actualizado no puede ser null");
		
		String query = "UPDATE cliente SET nombre = ?, email = ? WHERE idCliente = ?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, clienteActualizado.getNombre());
			ps.setString(2, clienteActualizado.getEmail());
			ps.setInt(3, clienteActualizado.getIdCliente());
			ps.executeUpdate();
			this.commit(conn);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			e.printStackTrace();
            System.err.println("Error al actualizar cliente: " + clienteActualizado.getIdCliente());
		}finally {
			this.closePs(ps);
		}

	}

	@Override
	public void delete(Connection conn, int id) {
		this.validateConnection(conn);
		if(id <= 0)
			throw new IllegalArgumentException("El id debe ser mayor a 0");
			
		String query = "DELETE FROM cliente WHERE idCliente = ?";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ps.executeUpdate();
			this.commit(conn);
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al borrar el cliente: " + id);
		}finally {
			this.closePs(ps);
		}

	}
	
	public Iterable<ClienteConFacturacionDTO> getClientesPorFacturacion(Connection conn){
		this.validateConnection(conn);
		Deque<ClienteConFacturacionDTO> list = new LinkedList<ClienteConFacturacionDTO>();
		String query = "SELECT c.idCliente, c.nombre, SUM(fp.cantidad) as cantidad, SUM(p.valor * fp.cantidad) as total "
				+ "FROM cliente c "
				+ "JOIN factura f USING (idCliente) "
				+ "JOIN factura_producto fp USING (idFactura) "
				+ "JOIN producto p USING (idProducto) "
				+ "GROUP BY c.idCliente "
				+ "ORDER BY total desc";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ClienteConFacturacionDTO cliente = new ClienteConFacturacionDTO(
						rs.getString("nombre"),
						rs.getInt("cantidad"),
						rs.getFloat("total"));
				list.addLast(cliente);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println("Error al obtener los clientes que mas facturaron en orden");
		}
		this.closePs(ps);
		return list;
	}

    private void closePs(PreparedStatement ps) {
		try {
			if(ps!=null)
				ps.close();
		}catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al cerrar el prepared statement");
		}

    }

	private void commit(Connection conn){
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al hacer commit");
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
		String newTable = "CREATE TABLE IF NOT EXISTS cliente("
				+ "idCliente INT,"
				+ "nombre VARCHAR(500),"
				+ "email VARCHAR(350),"
				+ "PRIMARY KEY(idCliente)"
				+ ")";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(newTable);
			ps.execute();
			this.commit(conn);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			System.err.println("Error al crear la tabla cliente");
		}finally {
			this.closePs(ps);
		}
	}
	public void dropTable(Connection conn) {
		this.validateConnection(conn);
		
		String delete = "DROP TABLE IF EXISTS cliente";
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(delete);
			ps.execute();
			this.commit(conn);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			System.err.println("Error al borrar la tabla cliente");
		}finally {
			this.closePs(ps);
		}
	}
}
