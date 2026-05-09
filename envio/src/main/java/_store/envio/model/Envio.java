package _store.envio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "envios")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Double costoEnvio;

    @Column(nullable = false)
    private LocalDate fechaEnvio;

    @Column(nullable = true)
    private LocalDate fechaRecibido;

    @Column(nullable = false)
    private String estadoEnvio;

    @Column(nullable = false)
    private Integer pedidoId;

    @PrePersist
    protected void onCreate(){
        this.fechaEnvio = LocalDate.now();
        this.estadoEnvio = "PENDIENTE";
    }

}
