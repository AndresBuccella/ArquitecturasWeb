package main.java.dto;

public class ProductoFacturadoDTO {
    private String nombre;
    private int cantidad;
    private float total;

    public ProductoFacturadoDTO(String nombre, int cantidad, float total) {
        this.setNombre(nombre);
        this.setCantidad(cantidad);
        this.setTotal(total);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad < 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor o igual a 0");
        this.cantidad = cantidad;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        if (total < 0)
            throw new IllegalArgumentException("El total debe ser mayor o igual a 0");
        this.total = total;
    }

    @Override
    public String toString() {
        return "ProductoFacturadoDTO{" +
                "nombre: '" + nombre + '\'' +
                ", cantidad: " + cantidad +
                ", total: " + total +
                '}';
    }
}
