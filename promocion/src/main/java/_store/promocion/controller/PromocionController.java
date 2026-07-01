package _store.promocion.controller;

import _store.promocion.model.Promocion;
import _store.promocion.service.PromocionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Promociones", description = "Gestión de cupones y promociones de descuento")
@RestController
@RequestMapping("/api/store/promociones")
public class PromocionController {

    @Autowired
    PromocionService promocionService;

    @Operation(summary = "Listar todas las promociones registradas")
    @GetMapping("/listar")
    public ResponseEntity<List<Promocion>> listarPromociones() {
        List<Promocion> promociones = promocionService.listarTodos();
        if (promociones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(promociones);
    }

    @Operation(summary = "Buscar una promoción por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Promocion> buscarPorId(@PathVariable Integer id) {
        Promocion promocion = promocionService.buscarPorId(id);
        return ResponseEntity.ok(promocion);
    }

    @Operation(summary = "Crear una nueva promoción/cupón")
    @PostMapping
    public ResponseEntity<Promocion> crearPromocion(@RequestBody Promocion promocion) {
        Promocion promocionGuardada = promocionService.crear(promocion);
        return ResponseEntity.status(HttpStatus.CREATED).body(promocionGuardada);
    }

    @Operation(summary = "Validar un código de cupón antes de aplicar el descuento (usado por el microservicio pedido)")
    @GetMapping("/validar/{codigo}")
    public ResponseEntity<Promocion> validarCodigo(@PathVariable String codigo) {
        Promocion promocion = promocionService.validarCodigo(codigo);
        return ResponseEntity.ok(promocion);
    }

    @Operation(summary = "Eliminar una promoción por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        promocionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}