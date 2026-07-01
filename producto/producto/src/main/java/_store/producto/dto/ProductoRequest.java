package _store.producto.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductoRequest {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripcion del producto es obligatoria")
    private String descripcion;

    @NotNull(message = "El precio del producto es obligatorio")
    @Positive(message = "El precio debe ser un número mayor a cero")
    private Double precio;

    @NotNull(message = "El stock del producto es obligatorio")
    @Positive(message = "El stock tiene que ser positivo")
    private Integer stock;

    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    @NotBlank(message = "La marca es obligatoria")
    private String marca;

    public ProductoRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
