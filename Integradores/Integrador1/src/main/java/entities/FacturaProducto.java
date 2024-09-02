package main.java.entities;

public class FacturaProducto {

	private int idFactura;
	private int idProducto;
	private int cantidadProducto;

	public FacturaProducto(int idFactura, int idProducto, int cantidadProducto) {
		this.setIdFactura(idFactura);
		this.setIdProducto(idProducto);
		this.setCantidadProducto(cantidadProducto);
	}
	
	private void setIdFactura(int idFactura) {
		if(idFactura > 0)
			this.idFactura = idFactura;
	}

	private void setIdProducto(int idProducto) {
		if(idProducto > 0)
			this.idProducto = idProducto;
	}

	private void setCantidadProducto(int cantidadProducto) {
		if(cantidadProducto > 0)
			this.cantidadProducto = cantidadProducto;
	}

	public int getIdFactura() {
		return idFactura;
	}

	public int getIdProducto() {
		return idProducto;
	}

	public int getCantidadProducto() {
		return cantidadProducto;
	}
	
}
