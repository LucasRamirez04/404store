package _store.envio.exception;

public class EnvioNoEncontradoException extends RuntimeException {
    public EnvioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}