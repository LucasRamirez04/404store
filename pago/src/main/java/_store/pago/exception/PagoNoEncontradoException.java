package _store.pago.exception;

public class PagoNoEncontradoException extends RuntimeException {
    public PagoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}