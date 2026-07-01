package _store.producto.config;

import _store.producto.model.Producto;
import _store.producto.repository.ProductoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatos(ProductoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<Producto> productosNuevos = new ArrayList<>();

                // 1. Un producto fijo para pruebas predecibles (ideal para Postman y Testing)
                Producto productoFijo = new Producto();
                productoFijo.setNombre("Notebook Lenovo IdeaPad");
                productoFijo.setDescripcion("Notebook 15.6 pulgadas, 8GB RAM, 256GB SSD");
                productoFijo.setPrecio(450000.0);
                productoFijo.setStock(20);
                productoFijo.setCategoria("Tecnologia");
                productoFijo.setMarca("Lenovo");
                productosNuevos.add(productoFijo);

                // 2. Inicializamos DataFaker
                Faker faker = new Faker(new Locale("es"));

                String[] categorias = {"Tecnologia", "Hogar", "Ropa", "Deportes", "Juguetes"};

                // 3. Generamos 4 productos aleatorios con Faker
                for (int i = 0; i < 4; i++) {
                    Producto producto = new Producto();
                    producto.setNombre(faker.commerce().productName());
                    producto.setDescripcion(faker.lorem().sentence());
                    producto.setPrecio(faker.number().randomDouble(2, 1000, 100000));
                    producto.setStock(faker.number().numberBetween(1, 100));
                    producto.setCategoria(categorias[faker.number().numberBetween(0, categorias.length)]);
                    producto.setMarca(faker.company().name());
                    productosNuevos.add(producto);
                }

                // 4. Guardamos todos los productos en lote en la BD H2
                repository.saveAll(productosNuevos);

                System.out.println("====== ¡Datos iniciales de Productos cargados con éxito! ======");
            }
        };
    }
}
