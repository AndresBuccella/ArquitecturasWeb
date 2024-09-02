package main.java.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Persona {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private String nombre;
	
	@Column(name="anios")
	private int edad;
	
	@ManyToOne
	private Direccion domicilio;
	
	public Persona() {}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public Direccion getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(Direccion domicilio) {
		this.domicilio = domicilio;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", edad=" + edad + ", domicilio=" + domicilio + "]";
	}
	
}
