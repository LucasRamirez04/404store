package _store.producto.exception;

public class NoSuficienteStockException extends RuntimeException {
    public NoSuficienteStockException(String message) {
        super(message);
    }
}
