package _store.promocion.webclient;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PedidoClient {

    private static final Logger log = LoggerFactory.getLogger(PedidoClient.class);

    private final WebClient webClient;

    public PedidoClient(@Value("${pedido-service.url}") String pedidoServidor) {
        this.webClient = WebClient.builder().baseUrl(pedidoServidor).build();
    }

    // Valida que el pedido exista antes de aplicar/registrar una promoción asociada
    public Map<String, Object> obtenerPedidoId(Integer id) {
        log.info("Validando existencia del pedido con ID: {}", id);
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Pedido no encontrado")))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}