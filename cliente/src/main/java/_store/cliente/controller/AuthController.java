package _store.cliente.controller;

import _store.cliente.dto.LoginRequest;
import _store.cliente.dto.LoginResponse;
import _store.cliente.security.JwtService;
import _store.cliente.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    // AuthService valida usuario y contraseña.
    private final AuthService authService;

    // JwtService crea el token cuando el login es correcto.
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Si el usuario o la clave están mal, devolvemos 401.
        if (!authService.credencialesValidas(request.getUsername(), request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Credenciales incorrectas"));
        }

        // Si todo está bien, generamos el JWT y lo devolvemos al cliente.
        String token = jwtService.generarToken(request.getUsername());
        return ResponseEntity.ok(new LoginResponse(token, "Bearer"));
    }
}

