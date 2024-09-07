package main.java.entities;

public class Cliente {
		
	private int idCliente;
	private String nombre;
	private String email;
	
	public Cliente() {}
	
	public Cliente(int idCliente, String nombre, String email) {
		this.idCliente = idCliente;
		this.setNombre(nombre);
		this.setEmail(email);
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		if(nombre == null || nombre.length() == 0 || nombre.length() > 400)
			throw new IllegalArgumentException("El nombre es invalido");
		this.nombre = nombre;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		if(nombre == null || nombre.length() == 0 || nombre.length() > 150)
			throw new IllegalArgumentException("El email es invalido");
		this.email = email;
	}
	
	public int getIdCliente() {
		return idCliente;
	}
	
	@Override
	public String toString() {
		return "ClienteDAO [idCliente=" + idCliente + ", nombre=" + nombre + ", email=" + email + "]";
	}
}
