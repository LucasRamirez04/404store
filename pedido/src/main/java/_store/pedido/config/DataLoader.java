package _store.pedido.config;

import _store.pedido.model.Pedido;
import _store.pedido.repository.PedidoRepository;
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
    CommandLineRunner cargarDatos(PedidoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<Pedido> pedidosNuevos = new ArrayList<>();

                // 1. Un pedido fijo para pruebas predecibles (ideal para Postman y Testing)
                pedidosNuevos.add(Pedido.builder()
                        .productoId(1)
                        .clienteId(1)
                        .cantidad(2)
                        .fecha(LocalDate.now())
                        .estado("PENDIENTE")
                        .total(45000.0)
                        .build());

                // 2. Inicializamos DataFaker
                Faker faker = new Faker(new Locale("es"));

                // Arreglo para alternar estados aleatorios
                String[] estados = {"PENDIENTE", "PAGADO", "ENVIADO"};

                // 3. Generamos 4 pedidos aleatorios con Faker
                for (int i = 0; i < 4; i++) {
                    int cantidadAleatoria = faker.number().numberBetween(1, 5);
                    double precioSimulado = faker.number().randomDouble(2, 5000, 50000);

                    pedidosNuevos.add(Pedido.builder()
                            .productoId(faker.number().numberBetween(1, 20)) // IDs de producto del 1 al 20
                            .clienteId(faker.number().numberBetween(1, 10))  // IDs de cliente del 1 al 10
                            .cantidad(cantidadAleatoria)
                            .fecha(LocalDate.now())
                            .estado(estados[faker.number().numberBetween(0, 3)])
                            .total(precioSimulado * cantidadAleatoria)
                            .build());
                }

                // 4. Guardamos todos los pedidos en lote en la BD H2
                repository.saveAll(pedidosNuevos);

                System.out.println("====== ¡Datos iniciales de Pedidos cargados con éxito! ======");
            }
        };
    }
}
