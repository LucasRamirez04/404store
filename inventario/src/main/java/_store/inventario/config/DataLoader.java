package _store.inventario.dataloader;

import _store.inventario.model.Inventario;
import _store.inventario.repository.InventarioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final InventarioRepository inventarioRepository;

    public DataLoader(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public void run(String... args) {
        if (inventarioRepository.count() == 0) {
            Faker faker = new Faker();
            String[] zonas = {"Bodega A", "Bodega B", "Estante 1", "Estante 2",
                    "Zona Fría", "Zona Seca", "Depósito Central"};
            for (int i = 1; i <= 10; i++) {
                inventarioRepository.save(Inventario.builder()
                        .productoId(i)
                        .ubicacion(zonas[faker.number().numberBetween(0, zonas.length)])
                        .build());
            }
        }
    }
}