package _store.pedido.exception;

public class NoStock extends RuntimeException {
    public NoStock(String message) {
        super(message);
    }
}
