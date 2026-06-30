package _store.pedido.controller;

import _store.pedido.dto.PedidoRequest;
import _store.pedido.model.Pedido;
import _store.pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/store/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con los pedidos")
public class PedidoController {

    @Autowired
    PedidoService pedidoService;

    @GetMapping("/listar")
    @Operation(summary = "Obtener todos los pedidos", description = "Obtienes una lista de las carreras")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> listarPedidos(){
        List<Pedido> pedidos = pedidoService.listarTodos();
        if(pedidos.isEmpty()){
            CollectionModel<EntityModel<Pedido>> vacio = CollectionModel.empty();
            vacio.add(linkTo(methodOn(PedidoController.class).listarPedidos()).withSelfRel());
            return ResponseEntity.ok(vacio);
        }

        List<EntityModel<Pedido>> pedidosConLinks = pedidos.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidosConLinks,
                linkTo(methodOn(PedidoController.class).listarPedidos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> buscarPorId(@PathVariable Integer id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(toEntityModel(pedido));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Pedido>> guardar(@Valid @RequestBody PedidoRequest request){
        Pedido pedidoGuardado = pedidoService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toEntityModel(pedidoGuardado));
    }

    @GetMapping("/{id}/detalle-envio")
    public ResponseEntity<Map<String, Object>> obtenerDetalleEnvio(@PathVariable Integer id) {
        // El controller solo delega la tarea
        return ResponseEntity.ok(pedidoService.obtenerDetalleEnvio(id));
    }

    // Método auxiliar para construir el EntityModel con sus enlaces HATEOAS
    private EntityModel<Pedido> toEntityModel(Pedido pedido) {
        return EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).buscarPorId(pedido.getId())).withSelfRel(),
                linkTo(methodOn(PedidoController.class).listarPedidos()).withRel("pedidos"),
                linkTo(methodOn(PedidoController.class).obtenerDetalleEnvio(pedido.getId())).withRel("detalle-envio")
        );
    }
}