package _store.cliente.dto;

// Este DTO es la respuesta del login.
// Devuelve el token y el tipo Bearer.
public class LoginResponse {

    private String token;
    private String tipo;

    public LoginResponse() {
    }

    public LoginResponse(String token, String tipo) {
        this.token = token;
        this.tipo = tipo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
