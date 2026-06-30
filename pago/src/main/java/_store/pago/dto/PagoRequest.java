package _store.pago.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class PagoRequest {

    @NotNull(message = "El monto total es obligatorio")
    @Positive(message = "El monto total debe ser un valor mayor a cero")
    private Double montoTotal;

    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 50, message = "El método de pago es demasiado largo")
    private String metodoPago;

    @NotNull(message = "La fecha de pago es obligatoria")
    @PastOrPresent(message = "La fecha de pago no puede ser una fecha futura")
    private LocalDate fechaPago;

    @NotBlank(message = "El estado del pago es obligatorio")
    @Pattern(regexp = "^(?i)(aprobado|rechazado)$",
            message = "El estado del pago solo puede ser 'aprobado' o 'rechazado'")
    private String estadoPago;

    @NotNull(message = "El ID del pedido es obligatorio")
    @Positive(message = "El ID del pedido debe ser un número válido")
    private Integer pedidoId;


}
