package _store.envio.controller;

import _store.envio.dto.EnvioRequest;
import _store.envio.webclient.PedidoClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba de integración: levanta el contexto completo de Spring Boot con la
 * base de datos H2 en memoria (perfil "test") y hace peticiones HTTP reales
 * contra el controller con MockMvc. La única pieza que se simula es
 * PedidoClient, porque depende de otro microservicio que aquí no existe.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EnvioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PedidoClient pedidoClient;

    private static final Faker faker = new Faker();

    @Test
    void crearYConsultarEnvio_flujoCompleto() throws Exception {
        // Given: el pedido referenciado existe según el microservicio Pedido (mockeado)
        when(pedidoClient.obtenerPedidoId(anyInt()))
                .thenReturn(Map.of("id", 1, "estado", "PAGADO"));

        EnvioRequest request = new EnvioRequest();
        request.setDireccion(faker.address().fullAddress());
        request.setCostoEnvio(faker.number().randomDouble(2, 1000, 5000));
        request.setPedidoId(1);

        // When: se crea el envío vía POST
        mockMvc.perform(post("/api/store/envios")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                // Then: responde 201, con los datos y los enlaces HATEOAS esperados
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.direccion").value(request.getDireccion()))
                .andExpect(jsonPath("$.estadoEnvio").value("PENDIENTE"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.confirmar.href").exists());
    }

    @Test
    void crearEnvio_conPedidoInexistente_debeResponder404() throws Exception {
        // Given: el microservicio Pedido indica que el pedido no existe
        when(pedidoClient.obtenerPedidoId(anyInt()))
                .thenThrow(new RuntimeException("Pedido no encontrado"));

        EnvioRequest request = new EnvioRequest();
        request.setDireccion(faker.address().fullAddress());
        request.setCostoEnvio(faker.number().randomDouble(2, 1000, 5000));
        request.setPedidoId(999);

        // When / Then
        mockMvc.perform(post("/api/store/envios")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearEnvio_conDireccionVacia_debeResponder400() throws Exception {
        // Given: un request inválido según las validaciones de EnvioRequest
        EnvioRequest request = new EnvioRequest();
        request.setDireccion(""); // viola @NotBlank
        request.setCostoEnvio(1000.0);
        request.setPedidoId(1);

        // When / Then
        mockMvc.perform(post("/api/store/envios")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarEnvioInexistente_debeResponder404() throws Exception {
        mockMvc.perform(get("/api/store/envios/{id}", 999_999))
                .andExpect(status().isNotFound());
    }
}
