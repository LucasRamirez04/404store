package _store.cliente.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/publico")
@CrossOrigin(origins = "*")
public class PublicoController {

    // Este endpoint sirve para demostrar que algunas rutas pueden quedar libres.
    @GetMapping("/saludo")
    public ResponseEntity<Map<String, String>> saludoPublico() {
        return ResponseEntity.ok(Map.of("mensaje", "Hola, este endpoint es público"));
    }
}
