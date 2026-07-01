package _store.producto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productoOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("404Store - Microservicio Producto")
                .description("Gestion de inventario y catalogo de productos")
                .version("1.0.0"));
    }
}
