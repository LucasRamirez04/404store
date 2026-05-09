package _store.cliente.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClienteRequest {

    @NotBlank(message ="El run es obligatorio")
    @Size(min = 9, max = 12, message = "El run debe tener entre 9 y 12 caracteres")
    private String run;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String nombre;

    @NotBlank(message ="El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no debe superar los 100 caracteres")
    private String apellido;

    @NotBlank(message ="El correo es obligatorio")
    @Email(message = "El correo debe tener un formato valido")
    private String correo;

    @NotBlank(message = "El telefono es obligatorio")
    @Size(min = 9, max = 12, message = "El telefono debe tener minimo 9 digitos")
    private String telefono;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;

    public ClienteRequest() {
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
