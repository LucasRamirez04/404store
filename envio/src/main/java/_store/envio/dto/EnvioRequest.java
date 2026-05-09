package _store.envio.dto;

import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
public class EnvioRequest {

    @NotBlank(message = "La direccion es obligatoria")
    @Size(max = 200, message = "La direccion es demasiado larga")
    private String direccion;

    @NotNull(message = "El costo de envio debe estar especiificado")
    @PositiveOrZero(message = "El costo de envio no puede ser negativo")
    private Double costoEnvio;

    @NotNull(message = "El ID del pedido es obligatorio")
    private Integer pedidoId;

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(Double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }
}
