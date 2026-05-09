package _store.pedido.webclient;

import java.util.Map;

import _store.pedido.exception.NoStock;
import _store.pedido.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProductoClient {

    private static final Logger log = LoggerFactory.getLogger(ProductoClient.class);

    private final WebClient webClient;

    public ProductoClient(@Value("${producto-service.url}") String productoServidor) {
        this.webClient = WebClient.builder().baseUrl(productoServidor).build();
    }

    // metodo para verificar si el producto existe y tiene stock disponible
    public Map<String, Object> obtenerProductoId(Integer id) {
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Producto no encontrado en inventario")))
                .bodyToMono(Map.class)
                .block();
    }

    //metodo para verificar y descontar el stock al producto cuando se haga un pedido
    public void descontarStock(Integer productoId, Integer cantidad){
        log.info("Enviando petición de descuento al Microservicio Producto para ID: {}", productoId);
        this.webClient.put()
                .uri("/{id}/descontar?cantidad={cantidad}", productoId, cantidad)
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                        Mono.error(new NoStock("Error remoto: Stock no disponible")))
                .bodyToMono(Void.class)
                .block();
    }

}
