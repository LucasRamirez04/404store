package _store.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InventarioRequest {

    @NotNull(message = "El ID del producto es obligatorio")
    private Integer productoId;

    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion;

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}