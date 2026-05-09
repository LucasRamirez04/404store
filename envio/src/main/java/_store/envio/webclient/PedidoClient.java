package _store.envio.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PedidoClient {

    private final WebClient webClient;

    public PedidoClient(@Value("${pedido-service.url}") String pedidoServidor){
        this.webClient = WebClient.builder().baseUrl(pedidoServidor).build();
    }

    public Map<String, Object> obtenerPedidoId(Integer id){
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Pedido no encontrado")))
                .bodyToMono(Map.class)
                .block();
    }
}