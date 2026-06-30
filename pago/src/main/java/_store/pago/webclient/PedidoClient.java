package _store.pago.webclient;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class PedidoClient {
    private static final Logger log = LoggerFactory.getLogger(EnvioClient.class);

    private final WebClient webClient;

    public PedidoClient(@Value("${pedido-service.url}") String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public String obtenerDireccionPedido(Integer pedidoId) {
        Map<String, Object> response = this.webClient.get()
                .uri("/{id}/detalle-envio", pedidoId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        return (String) response.get("direccion");
    }
}
