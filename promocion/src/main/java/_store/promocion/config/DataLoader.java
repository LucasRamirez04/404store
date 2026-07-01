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
                Promocion fija = new Promocion();
                fija.setCodigo("VERANO2026");
                fija.setPorcentajeDescuento(15.0);
                fija.setFechaInicio(LocalDate.now());
                fija.setFechaExpiracion(LocalDate.now().plusMonths(6));
                fija.setActivo(true);
                promocionesNuevas.add(fija);

                // 2. Cupón ya expirado, para probar el caso de error
                Promocion expirado = new Promocion();
                expirado.setCodigo("INVIERNO2024");
                expirado.setPorcentajeDescuento(10.0);
                expirado.setFechaInicio(LocalDate.now().minusYears(1));
                expirado.setFechaExpiracion(LocalDate.now().minusMonths(2));
                expirado.setActivo(true);
                promocionesNuevas.add(expirado);

                // 3. Inicializamos Datafaker en español
                Faker faker = new Faker(new Locale("es"));

                // 4. Generamos 4 cupones aleatorios
                for (int i = 0; i < 4; i++) {
                    Promocion p = new Promocion();
                    p.setCodigo(faker.commerce().promotionCode().toUpperCase());
                    p.setPorcentajeDescuento((double) faker.number().numberBetween(5, 40));
                    p.setFechaInicio(LocalDate.now());
                    p.setFechaExpiracion(LocalDate.now().plusMonths(faker.number().numberBetween(1, 12)));
                    p.setActivo(true);
                    promocionesNuevas.add(p);
                }

                repository.saveAll(promocionesNuevas);
            }
        };
    }
}