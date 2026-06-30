package _store.pago.config;

import _store.pago.model.Pago;
import _store.pago.repository.PagoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatosPago(PagoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<Pago> pagosNuevos = new ArrayList<>();

                // 1. Un pago fijo para pruebas predecibles (ideal para Postman y Testing)
                // Se alinea con el pedido fijo ID 1 de tu otro microservicio
                Pago pagoFijo = new Pago();
                pagoFijo.setMontoTotal(45000.0);
                pagoFijo.setMetodoPago("tarjeta");
                pagoFijo.setFechaPago(LocalDate.now());
                pagoFijo.setEstadoPago("aprobado");
                pagoFijo.setPedidoId(1);
                pagosNuevos.add(pagoFijo);

                // 2. Inicializamos DataFaker en español
                Faker faker = new Faker(new Locale("es"));

                // Arreglos para alternar datos aleatorios
                String[] metodos = {"tarjeta", "transferencia", "efectivo"};
                String[] estados = {"aprobado", "rechazado"};

                // 3. Generamos 4 pagos aleatorios con Faker
                for (int i = 0; i < 4; i++) {
                    Pago pagoAleatorio = new Pago();

                    // Monto aleatorio entre 5.000 y 150.000
                    pagoAleatorio.setMontoTotal(faker.number().randomDouble(2, 5000, 150000));

                    // Selección aleatoria del método y estado
                    pagoAleatorio.setMetodoPago(metodos[faker.number().numberBetween(0, metodos.length)]);
                    pagoAleatorio.setEstadoPago(estados[faker.number().numberBetween(0, estados.length)]);

                    // Fecha de pago (puedes dejar la actual o usar Faker para fechas recientes)
                    pagoAleatorio.setFechaPago(LocalDate.now());

                    // Asocia el pago a un ID de pedido simulado (del 2 al 5 para coincidir con los de Faker del otro servicio)
                    pagoAleatorio.setPedidoId(faker.number().numberBetween(2, 6));

                    pagosNuevos.add(pagoAleatorio);
                }

                // 4. Guardamos todos los pagos en lote en la BD
                repository.saveAll(pagosNuevos);

                System.out.println("====== ¡Datos iniciales de Pagos cargados con éxito! ======");
            }
        };
    }
}