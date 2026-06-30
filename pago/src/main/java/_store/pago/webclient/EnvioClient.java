package _store.pago.webclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class EnvioClient {
    private static final Logger log = LoggerFactory.getLogger(EnvioClient.class);

    private final WebClient webClient;

    public EnvioClient(@Value("${envio-service.url}") String envioServidor){
    this.webClient = WebClient.builder().baseUrl(envioServidor).build();
    }

    public void crearNuevoEnvio(Integer pedidoId, String direccion, Double costo) {
        Map<String, Object> body = new HashMap<>();
        body.put("pedidoId", pedidoId);
        body.put("direccion", direccion);
        body.put("costoEnvio", costo);

        this.webClient.post()
                .uri("")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class)
                .block(); // Esperamos a que se cree para asegurar la integración
    }
}
