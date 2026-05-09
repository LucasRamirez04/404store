package _store.cliente.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private LocalDateTime fecha;
    private int status;
    private String error;
    private String mensaje;
    private String ruta;

    public ErrorResponse() {
    }

    public ErrorResponse(LocalDateTime fecha, int status, String error, String mensaje, String ruta) {
        this.fecha = fecha;
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
        this.ruta = ruta;
    }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }
}

