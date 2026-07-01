package _store.envio.exception;

/**
 * Se lanza cuando el pedidoId enviado en la solicitud no corresponde a un
 * pedido válido (el microservicio Pedido respondió 404, o no respondió).
 */
public class PedidoNoValidoException extends RuntimeException {
    public PedidoNoValidoException(String mensaje) {
        super(mensaje);
    }
}
