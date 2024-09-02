package main.java.entities;

public class Producto {

	private int idProducto;
	private String nombre;
	private float valor;
	
	public Producto(int idProducto, String nombre, float valor) {
		this.idProducto = idProducto;
		this.setNombre(nombre);
		this.setValor(valor);
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		if(nombre == null || nombre.length() == 0 || nombre.length() > 45)
			throw new IllegalArgumentException("El nombre es invalido");
		this.nombre = nombre;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		if(valor < 0)
			throw new IllegalArgumentException("El valor es invalido");
		this.valor = valor;
	}


	public int getIdProducto() {
		return idProducto;
	}

	@Override
	public String toString() {
		return "Producto [idProducto=" + idProducto + ", nombre=" + nombre + ", valor=" + valor + "]";
	}
}
