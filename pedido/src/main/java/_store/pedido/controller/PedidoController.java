package _store.pedido.controller;

import _store.pedido.dto.PedidoRequest;
import _store.pedido.model.Pedido;
import _store.pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/store/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con los pedidos")
public class PedidoController {
    @Autowired
    PedidoService pedidoService;

    @GetMapping("/listar")
    @Operation(summary = "Obtener todos los pedidos", description = "Obtienes una lista de las carreras")
    public ResponseEntity<List<Pedido>> listarPedidos(){
        List<Pedido> pedidos = pedidoService.listarTodos();
        if(pedidos.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Pedido> guardar(@Valid @RequestBody PedidoRequest request){
        Pedido pedidoGuardado = pedidoService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoGuardado);
    }

    @GetMapping("/{id}/detalle-envio")
    public ResponseEntity<Map<String, Object>> obtenerDetalleEnvio(@PathVariable Integer id) {
        // El controller solo delega la tarea
        return ResponseEntity.ok(pedidoService.obtenerDetalleEnvio(id));
    }

}
