package _store.envio.config;

import _store.envio.model.Envio;
import _store.envio.repository.EnvioRepository;
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
    CommandLineRunner cargarDatos(EnvioRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<Envio> enviosNuevos = new ArrayList<>();

                // 1. Registro fijo: predecible para Postman y para la defensa técnica
                enviosNuevos.add(Envio.builder()
                        .direccion("Av. Providencia 1234, Santiago")
                        .costoEnvio(2990.0)
                        .fechaEnvio(LocalDate.now())
                        .estadoEnvio("PENDIENTE")
                        .pedidoId(1)
                        .build());

                // 2. Registro entregado fijo: útil para probar el flujo completo
                enviosNuevos.add(Envio.builder()
                        .direccion("Calle O'Higgins 456, Valparaíso")
                        .costoEnvio(3500.0)
                        .fechaEnvio(LocalDate.now().minusDays(3))
                        .fechaRecibido(LocalDate.now())
                        .estadoEnvio("ENTREGADO")
                        .pedidoId(2)
                        .build());

                // 3. Datos aleatorios con DataFaker
                Faker faker = new Faker(Locale.of("es"));
                String[] estados = {"PENDIENTE", "ENTREGADO"};

                for (int i = 0; i < 3; i++) {
                    boolean entregado = estados[faker.number().numberBetween(0, 2)].equals("ENTREGADO");
                    LocalDate fechaEnvio = LocalDate.now().minusDays(faker.number().numberBetween(1, 10));

                    enviosNuevos.add(Envio.builder()
                            .direccion(faker.address().streetAddress() + ", " + faker.address().city())
                            .costoEnvio(faker.number().randomDouble(2, 1000, 9000))
                            .fechaEnvio(fechaEnvio)
                            .fechaRecibido(entregado ? fechaEnvio.plusDays(faker.number().numberBetween(1, 5)) : null)
                            .estadoEnvio(entregado ? "ENTREGADO" : "PENDIENTE")
                            .pedidoId(faker.number().numberBetween(1, 10))
                            .build());
                }

                repository.saveAll(enviosNuevos);
                System.out.println("====== ¡Datos iniciales de Envíos cargados con éxito! ======");
            }
        };
    }
}
