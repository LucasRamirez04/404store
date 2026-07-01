package _store.envio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI envioOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("404Store - API Envío")
                        .description("Microservicio de logística y seguimiento de entregas. "
                                + "Se comunica con el microservicio Pedido para validar "
                                + "que el pedido asociado a cada envío exista.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Equipo 404Store")
                                .email("contacto@404store.cl")));
    }
}
