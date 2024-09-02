package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import entities.Persona;

public class PersonaDAO implements DAO<Persona> {
	
    private Connection conn;
	
	public PersonaDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public Optional<Persona> get(long id){
		Persona p = null;
		String query = "SELECT * FROM persona WHERE (id) like ?";
		PreparedStatement ps = null;
		
		try {
			ps = this.conn.prepareStatement(query);
			ps.setInt(1, (int) id);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				
				int personaId = rs.getInt("id");
				String nombre = rs.getString("nombre");
				int edad = rs.getInt("edad");
				
				p = new Persona(personaId, nombre, edad);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			this.closePsAndCommit(conn, ps);
		}
		return Optional.ofNullable(p);
		
	}

	@Override
	public Iterator<Persona> getAll(){
		List<Persona> list = new LinkedList<Persona>();
		String query = "SELECT * FROM persona";
		PreparedStatement ps = null;
		try {
			ps = this.conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Persona p = new Persona(
						rs.getInt("id"), 
						rs.getString("nombre"), 
						rs.getInt("anios")
						);
				list.add(0, p);
			}			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			this.closePsAndCommit(conn, ps);
		}
		
		return list.iterator();
	}
	
	@Override
	public void add(Persona p){

		if (p!=null) {
			String query = "INSERT INTO persona VALUES (?,?,?)";
			PreparedStatement ps = null;
			
			try {
				ps = conn.prepareStatement(query);
				ps.setInt(1, p.getId());
				ps.setString(2, p.getNombre());
				ps.setInt(3, p.getEdad());
				ps.execute();
				System.out.println("Persona agregada exitosamente");
			}catch(SQLException e) {
				rollbackQuietly(conn);
				e.printStackTrace();
			}finally {
				this.closePsAndCommit(conn, ps);
			}
		}
		
	}

	@Override
	public void update(long id, Persona p){

		String query = "UPDATE persona VALUES(?,?) WHERE id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, p.getNombre());
			ps.setInt(2, p.getEdad());
			ps.setInt(3, p.getId());
			ps.executeUpdate();
			System.out.println("Persona actualizada con éxito");
		}catch(SQLException e) {
			rollbackQuietly(conn);
			e.printStackTrace();
		}finally {
			this.closePsAndCommit(conn, ps);
		}
		
	}

	@Override
	public void delete(long id){
		// TODO Auto-generated method stub
		
	}

    private void closePsAndCommit(Connection conn, PreparedStatement ps) {
        if (conn != null){
            try {
            	if(ps!=null)
            		ps.close();
                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
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
	
	public static void dropTables(Connection conn){
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
}
