package _store.notificacion.config;

import _store.notificacion.model.Notificacion;
import _store.notificacion.repository.NotificacionRepository;
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
    CommandLineRunner cargarDatosNotificacion(NotificacionRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<Notificacion> notificacionesNuevas = new ArrayList<>();

                // 1. Una notificación fija para pruebas predecibles (ideal para Postman)
                Notificacion fija = Notificacion.builder()
                        .clienteId(1)
                        .tipo("PEDIDO")
                        .mensaje("Tu pedido #1 fue creado exitosamente. Total: $45000")
                        .estadoNotificacion("ENVIADA")
                        .fechaEnvio(LocalDate.now())
                        .build();
                notificacionesNuevas.add(fija);

                // 2. Inicializamos Datafaker en español
                Faker faker = new Faker(new Locale("es"));
                String[] tipos = {"PEDIDO", "PAGO", "ENVIO"};

                // 3. Generamos 6 notificaciones aleatorias
                for (int i = 0; i < 6; i++) {
                    Notificacion n = Notificacion.builder()
                            .clienteId(faker.number().numberBetween(1, 10))
                            .tipo(tipos[faker.number().numberBetween(0, 3)])
                            .mensaje(faker.lorem().sentence(8))
                            .estadoNotificacion("ENVIADA")
                            .fechaEnvio(LocalDate.now())
                            .build();
                    notificacionesNuevas.add(n);
                }

                repository.saveAll(notificacionesNuevas);
            }
        };
    }
}