package main.java.entities;

public class Factura {

	private int idFactura;
	private int idCliente;
	
	public Factura(int idFactura, int idCliente) {
		this.setIdFactura(idFactura);
		this.setIdCliente(idCliente);;
	}

	public int getIdFactura() {
		return idFactura;
	}

	public int getIdCliente() {
		return idCliente;
	}

	private void setIdFactura(int idFactura) {
		if (idFactura > 0)
			this.idFactura = idFactura;
	}

	private void setIdCliente(int idCliente) {
		if (idCliente > 0)
			this.idCliente = idCliente;
	}
}
