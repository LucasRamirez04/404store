package _store.pedido.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class PagoClient {

    private final WebClient webClient;

    public PagoClient (@Value("${pago-service.url}") String pagoServidor){
        this.webClient = WebClient.builder().baseUrl(pagoServidor).build();
    }


    //metodo para poder comunicarme con el microservicio pago y verificar si el pago existe
    //devolver una estructura de mapa que representa el json
    public Map<String, Object> obtenerPagoId(Integer id){
        //realizar una consulta HTTP de tipo get al microservicio cliente
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Cliente no encontrado"))).bodyToMono(Map.class).block();

    }

    //Metodo para crear el pago automaticamente luego de realizar un pedido exitosamente
    public void registrarPagoAutomatico(Integer pedidoID,Double montoTotal){
        Map<String,Object> body = new HashMap<>();
        body.put("pedidoId",pedidoID);
        body.put("montoTotal",montoTotal);
        body.put("estadoPago","Pendiente");

        this.webClient.post().uri("").bodyValue(body).retrieve().bodyToMono(Void.class).block();

    }


}
