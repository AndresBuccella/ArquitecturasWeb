package main.java.utils;

import java.io.Reader;
import java.sql.Connection;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import main.java.dao.DAO;
import main.java.dao.FacturaDAO;
import main.java.dao.FacturaProductoDAO;
import main.java.dao.ClienteDAO;
import main.java.dao.ProductoDAO;
import main.java.entities.Cliente;
import main.java.entities.Factura;
import main.java.entities.FacturaProducto;
import main.java.entities.Producto;

public class HelperCSVtoDB {

	private static Iterable<CSVRecord> getData(String archivo, String[] header) throws IOException{
		String path = "Integrador1/src/main/resources/" + archivo; //si se ejecuta en ECLIPSE borrar Integrador1/
		Reader in = new FileReader(path);
		CSVParser csvParser = CSVFormat.Builder.create(CSVFormat.EXCEL)
								.setHeader(header)
								.setSkipHeaderRecord(true)
								.build()
								.parse(in);
		
		return csvParser.getRecords();
	}
	
	public static void insertCSVDataIntoDB(Connection conn) throws IOException {
		try {
			//CARGA CLIENTE
			String[] headerCSVCliente = {"idCliente", "nombre", "email"};
			for(CSVRecord row : getData("clientes.csv", headerCSVCliente)) {
				if(row.size() >= 3) {
					String idString = row.get(0);
					String nombre = row.get(1);
					String email = row.get(2);
					if(idString != null && nombre != null && email != null) {
						try {
							int idCliente = Integer.parseInt(idString);
							Cliente c = new Cliente(idCliente, nombre, email);
							DAO<Cliente> dao = new ClienteDAO();
							dao.save(conn, c);							
						}catch(NumberFormatException e) {
							System.err.println("Error de formato en datos de cliente: " + e.getMessage());
						}
					}
				}
			}
			System.out.println("Clientes cargados con exito");
			
			//CARGA PRODUCTO
			String[] headerCSVProducto = {"idProducto", "nombre", "valor"};
			for(CSVRecord row : getData("productos.csv", headerCSVProducto)) {
				if(row.size() >= 3) {
					String idString = row.get(0);
					String nombre = row.get(1);
					String valorString = row.get(2);
					if(idString != null && nombre != null && valorString != null) {
						try {
							int idCliente = Integer.parseInt(idString);
							float valor = Float.parseFloat(valorString);
							Producto c = new Producto(idCliente, nombre, valor);
							DAO<Producto> dao = new ProductoDAO();
							dao.save(conn, c);							
						}catch(NumberFormatException e) {
							System.err.println("Error de formato en datos de producto: " + e.getMessage());
						}
					}
				}
			}
			System.out.println("Productos cargados con exito");
			
			//CARGA FACTURA
			String[] headerCSVFacturas = {"idFactura", "idCliente"};
			for(CSVRecord row : getData("facturas.csv", headerCSVFacturas)) {
				if(row.size() >= 2) {
					String idFacturaString = row.get(0);
					String idClienteString = row.get(1);
					if(idFacturaString != null && idClienteString != null) {
						try {
							int idFactura = Integer.parseInt(idFacturaString);
							int idCliente = Integer.parseInt(idClienteString);
							Factura f = new Factura(idFactura, idCliente);
							DAO<Factura> dao = new FacturaDAO();
							dao.save(conn, f);							
						}catch(NumberFormatException e) {
							System.err.println("Error de formato en datos de producto: " + e.getMessage());
						}
					}
				}
			}
			System.out.println("Facturas cargadas con exito");
			
			//CARGA FACTURA_PRODUCTO
			String[] headerCSVFacturaProducto = {"idFactura", "idProducto", "cantidad"};
			for(CSVRecord row : getData("facturas-productos.csv", headerCSVFacturaProducto)) {
				if(row.size() >= 3) {
					String idFacturaString = row.get(0);
					String idClienteString = row.get(1);
					String cantidadString = row.get(2);
					if(idFacturaString != null && idClienteString != null && cantidadString != null) {
						try {
							int idFactura = Integer.parseInt(idFacturaString);
							int idCliente = Integer.parseInt(idClienteString);
							int cantidad = Integer.parseInt(cantidadString);
							FacturaProducto f = new FacturaProducto(idFactura, idCliente, cantidad);
							DAO<FacturaProducto> dao = new FacturaProductoDAO();
							dao.save(conn, f);
						}catch(NumberFormatException e) {
							System.err.println("Error de formato en datos de factura-producto: " + e.getMessage());
						}
					}
				}
			}
			System.out.println("Factura-producto cargadas con exito");
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Base de datos cargada con exito");
	}
	
}
