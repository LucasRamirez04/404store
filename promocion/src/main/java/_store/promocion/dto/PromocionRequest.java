package _store.promocion.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class PromocionRequest {

    @NotBlank(message = "El código de la promoción es obligatorio")
    @Size(max = 30, message = "El código no debe superar los 30 caracteres")
    private String codigo;

    @NotNull(message = "El porcentaje de descuento es obligatorio")
    @Positive(message = "El porcentaje de descuento debe ser mayor a cero")
    @Max(value = 100, message = "El porcentaje de descuento no puede ser mayor a 100")
    private Double porcentajeDescuento;

    @NotNull(message = "La fecha de expiración es obligatoria")
    @Future(message = "La fecha de expiración debe ser una fecha futura")
    private LocalDate fechaExpiracion;

    public PromocionRequest() {
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public Double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(Double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }
    public LocalDate getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDate fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
}