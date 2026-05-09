package _store.pago.controller;

import _store.pago.dto.PagoRequest;
import _store.pago.model.Pago;
import _store.pago.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/store/pagos")
public class PagoController {
    @Autowired
    PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> listarPagos(){
        List<Pago> pagos = pagoService.listarTodos();
        if(pagos.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pagos);
    }

    @PostMapping
    public ResponseEntity<Pago> crearPago(@RequestBody Map<String, Object> datos){
        Pago pagoGuardado = pagoService.guardar(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoGuardado);
    }

    @PatchMapping("/{id}/{metodo}/confirmar")
    public ResponseEntity<Pago> confirmarPago(@PathVariable Integer id, @PathVariable String metodo){
        Pago pagoActualizado = pagoService.confirmarPago(id,metodo);
        return ResponseEntity.ok(pagoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}