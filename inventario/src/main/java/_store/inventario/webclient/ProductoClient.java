package _store.inventario.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(@Value("${producto-service.url}") String productoUrl) {
        this.webClient = WebClient.builder().baseUrl(productoUrl).build();
    }

    public Map<String, Object> obtenerProductoPorId(Integer id) {
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Producto no encontrado")))
                .bodyToMono(Map.class)
                .block();
    }
}