package _store.envio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos necesarios para registrar un nuevo envío")
public class EnvioRequest {

    @Schema(description = "Dirección de destino del envío", example = "Av. Siempre Viva 742, Santiago")
    @NotBlank(message = "La direccion es obligatoria")
    @Size(max = 200, message = "La direccion es demasiado larga")
    private String direccion;

    @Schema(description = "Costo del envío (calculado por el microservicio Pago como 5% del total del pedido)", example = "2990.0")
    @NotNull(message = "El costo de envio debe estar especiificado")
    @PositiveOrZero(message = "El costo de envio no puede ser negativo")
    private Double costoEnvio;

    @Schema(description = "ID del pedido al que corresponde este envío", example = "15")
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
