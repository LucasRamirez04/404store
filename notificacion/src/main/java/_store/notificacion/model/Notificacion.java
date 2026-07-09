package _store.notificacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer clienteId;

    @Column(nullable = true)
    private Integer pedidoId; // usado para validar contra el microservicio Pedido

    @Column(nullable = false)
    private String tipo; // PEDIDO, PAGO, ENVIO

    @Column(nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private String estadoNotificacion;

    @Column(nullable = false)
    private LocalDate fechaEnvio;

    @PrePersist
    protected void onCreate() {
        if (this.estadoNotificacion == null) this.estadoNotificacion = "PENDIENTE";
        this.fechaEnvio = LocalDate.now();
    }
}