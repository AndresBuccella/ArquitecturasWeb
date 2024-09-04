package main.java;

import java.sql.Connection;

import main.java.dao.ClienteDAO;
import main.java.dao.DAO;
import main.java.dao.ProductoDAO;
import main.java.db.FactoryDB;
import main.java.dto.ClienteConFacturacionDTO;
import main.java.entities.Cliente;
import main.java.entities.Factura;
import main.java.entities.FacturaProducto;
import main.java.entities.Producto;
import main.java.utils.HelperCSVtoDB;

public class Main {

	public static void main(String[] args) {
		try {
			FactoryDB fDB = FactoryDB.getDAOFactory(1);			
			Connection conn = fDB.getConnection();

			DAO<Cliente> clienteDAO = fDB.getClienteDAO();
			DAO<Producto> productoDAO = fDB.getProductoDAO();
			//DAO<Factura> facturaDAO = fDB.getFacturaDAO();
			//DAO<FacturaProducto> facturaProductoDAO = fDB.getFacturaProductoDAO();

			ProductoDAO pd = (ProductoDAO) productoDAO;
			ClienteDAO cd = (ClienteDAO) clienteDAO;

/*
			facturaProductoDAO.dropTable(conn);
			facturaDAO.dropTable(conn);
			clienteDAO.dropTable(conn);
			productoDAO.dropTable(conn);
			
			clienteDAO.createTable(conn);
			productoDAO.createTable(conn);
			facturaDAO.createTable(conn);
			facturaProductoDAO.createTable(conn);

			HelperCSVtoDB.insertCSVDataIntoDB(conn);
*/
/*
			for (ClienteConFacturacionDTO ccfDTO : cd.getClientesPorFacturacion(conn)) {
				System.out.println(ccfDTO);
			}*/
			//System.out.println(clienteDAO.get(conn, 1).get()); //El ultimo get obtiene el elemento dentro del optional
			//System.out.println(pd.getProductoQueMasRecaudo(conn).get());
			for(Cliente c : clienteDAO.getAll(conn))
				System.out.println(c);
			fDB.closeConnection(conn);
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
