package _store.pedido.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "pedidos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer productoId;

    @Column(nullable = false)
    private Integer clienteId;

    @Column(nullable = false)
    private Integer cantidad;

    // Cambiamos Date por LocalDateTime para que sea compatible con tu método onCreate
    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Double total;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDate.now();

        if (this.estado == null) {
            this.estado = "PENDIENTE";
        }
    }
}


