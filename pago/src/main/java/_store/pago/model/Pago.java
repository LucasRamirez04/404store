package _store.pago.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "pagos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Double montoTotal; // monto total cobrado al cliente

    @Column(nullable = false)
    private String metodoPago; // tarjeta, transferencia, efectivo, etc.

    @Column(nullable = false)
    private LocalDate fechaPago; // fecha en que se registra el pago

    @Column(nullable = false)
    private String estadoPago; // aprobado, rechazado

    @Column(nullable = false)
    private Integer pedidoId; // id del pedido asociado al pago

    @PrePersist
    protected void onCreate() {
        if (this.metodoPago == null) this.metodoPago = "PENDIENTE";
        if (this.fechaPago == null) this.fechaPago = LocalDate.now();
    }
}
