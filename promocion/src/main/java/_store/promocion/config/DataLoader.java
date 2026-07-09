package _store.promocion.config;

import _store.promocion.model.Promocion;
import _store.promocion.repository.PromocionRepository;
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
    CommandLineRunner cargarDatosPromocion(PromocionRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<Promocion> promocionesNuevas = new ArrayList<>();

                // 1. Cupón fijo para pruebas predecibles
                Promocion fija = Promocion.builder()
                        .codigo("VERANO2026")
                        .porcentajeDescuento(15.0)
                        .fechaInicio(LocalDate.now())
                        .fechaExpiracion(LocalDate.now().plusMonths(6))
                        .activo(true)
                        .build();
                promocionesNuevas.add(fija);

                // 2. Cupón ya expirado, para probar el caso de error
                Promocion expirado = Promocion.builder()
                        .codigo("INVIERNO2024")
                        .porcentajeDescuento(10.0)
                        .fechaInicio(LocalDate.now().minusYears(1))
                        .fechaExpiracion(LocalDate.now().minusMonths(2))
                        .activo(true)
                        .build();
                promocionesNuevas.add(expirado);

                // 3. Inicializamos Datafaker en español
                Faker faker = new Faker(new Locale("es"));

                // 4. Generamos 4 cupones aleatorios
                for (int i = 0; i < 4; i++) {
                    Promocion p = Promocion.builder()
                            .codigo(faker.commerce().promotionCode().toUpperCase())
                            .porcentajeDescuento((double) faker.number().numberBetween(5, 40))
                            .fechaInicio(LocalDate.now())
                            .fechaExpiracion(LocalDate.now().plusMonths(faker.number().numberBetween(1, 12)))
                            .activo(true)
                            .build();
                    promocionesNuevas.add(p);
                }

                repository.saveAll(promocionesNuevas);
            }
        };
    }
}