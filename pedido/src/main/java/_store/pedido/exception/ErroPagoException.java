package _store.pedido.exception;

public class ErroPagoException extends RuntimeException {
    public ErroPagoException(String message) {
        super(message);
    }
}
