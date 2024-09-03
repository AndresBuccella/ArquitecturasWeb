package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import main.java.dto.ProductoFacturadoDTO;
import main.java.entities.Producto;

public class ProductoDAO implements DAO<Producto> {
	
	public ProductoDAO() {}
	
	private void validateConnection(Connection conn) {
	    if (conn == null) {
	        throw new IllegalArgumentException("La conexión no puede ser null");
	    }
	}
	@Override
	public Optional<Producto> get(Connection conn, int idProducto) {
		this.validateConnection(conn);
		Producto prod = null;
		String query = "SELECT * FROM producto WHERE (idProducto) like ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, idProducto);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				prod = new Producto(
						rs.getInt("idProducto"),
						rs.getString("nombre"),
						rs.getFloat("valor")
						);
			}
				
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al obtener el producto" + idProducto);
		}

		return Optional.ofNullable(prod);
	}

	@Override
	public Iterable<Producto> getAll(Connection conn) {
		this.validateConnection(conn);

		List<Producto> resultList = new LinkedList<Producto>();
		String query = "SELECT * FROM cliente";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Producto prod = new Producto(
						rs.getInt("idProducto"),
						rs.getString("nombre"),
						rs.getFloat("valor")
						);
				resultList.add(prod);
			}
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al obtener todos los productos");
		}
		
		return resultList;
	}

	@Override
	public void save(Connection conn, Producto productoNuevo) {
		this.validateConnection(conn);
		if(productoNuevo == null)
			throw new IllegalArgumentException("El producto actualizado no puede ser null");
		String query = "INSERT INTO producto VALUES(?,?,?)";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, productoNuevo.getIdProducto());
			ps.setString(2, productoNuevo.getNombre());
			ps.setFloat(3, productoNuevo.getValor());
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			e.printStackTrace();
			System.err.println("Error al insertar el nuevo producto");
		}

	}

	@Override
	public void update(Connection conn, Producto productoActualizado) {
		this.validateConnection(conn);
		if(productoActualizado == null)
			throw new IllegalArgumentException("El cliente actualizado no puede ser null");
		
		String query = "UPDATE cliente SET nombre = ?, valor = ? WHERE idProducto = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, productoActualizado.getNombre());
			ps.setFloat(2, productoActualizado.getValor());
			ps.setInt(3, productoActualizado.getIdProducto());
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			e.printStackTrace();
            System.err.println("Error al actualizar el producto: " + productoActualizado.getIdProducto());
		}

	}

	@Override
	public void delete(Connection conn, int id) {
		this.validateConnection(conn);
		if(id <= 0)
			throw new IllegalArgumentException("El id debe ser mayor a 0");
			
		String query = "DELETE FROM producto WHERE idProducto = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, id);
			ps.executeUpdate();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			e.printStackTrace();
            System.err.println("Error al borrar el producto: " + id);
		}

	}
	
	public Optional<ProductoFacturadoDTO> getProductoQueMasRecaudo(Connection conn) {
		ProductoFacturadoDTO p = null;
		String query = "SELECT p.nombre, fp.cantidad, (p.valor * fp.cantidad) as total "
				+ "FROM producto p JOIN factura_producto fp USING (idProducto) "
				+ "ORDER BY total desc "
				+ "LIMIT 1";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				p = new ProductoFacturadoDTO(
						rs.getString("nombre"),
						rs.getInt("cantidad"),
						rs.getFloat("total"));
			}
		}catch(SQLException e) {
            System.err.println("Error al obtener el producto que mas recaudo");
			e.printStackTrace();
		}

		return Optional.ofNullable(p);
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
		this.validateConnection(conn);
		String table = "CREATE TABLE IF NOT EXISTS producto("
				+ "idProducto INT,"
				+ "nombre VARCHAR(45),"
				+ "valor FLOAT,"
				+ "PRIMARY KEY(idProducto)"
				+ ")";
		try {
			PreparedStatement ps = conn.prepareStatement(table);
			ps.execute();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			System.err.println("Error al crear la tabla producto");
		}
	}
	public void dropTable(Connection conn) {
		this.validateConnection(conn);
		
		String delete = "DROP TABLE IF EXISTS producto";
		try {
			PreparedStatement ps = conn.prepareStatement(delete);
			ps.execute();
			this.closePsAndCommit(conn, ps);
		}catch(SQLException e) {
			rollbackQuietly(conn);
			System.err.println("Error al borrar la tabla producto");
		}
	}
}
