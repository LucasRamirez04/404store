package _store.inventario.controller;

import _store.inventario.dto.InventarioRequest;
import _store.inventario.model.Inventario;
import _store.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/store/inventario")
@Tag(name = "Inventario", description = "Operaciones relacionadas con la ubicación de productos en inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/listar")
    @Operation(summary = "Listar todo el inventario")
    public ResponseEntity<CollectionModel<EntityModel<Inventario>>> listarInventario() {
        List<Inventario> inventarios = inventarioService.listarTodos();
        if (inventarios.isEmpty()) {
            CollectionModel<EntityModel<Inventario>> vacio = CollectionModel.empty();
            vacio.add(linkTo(methodOn(InventarioController.class).listarInventario()).withSelfRel());
            return ResponseEntity.ok(vacio);
        }
        List<EntityModel<Inventario>> lista = inventarios.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(lista,
                linkTo(methodOn(InventarioController.class).listarInventario()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar inventario por ID")
    public ResponseEntity<EntityModel<Inventario>> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(toEntityModel(inventarioService.buscarPorId(id)));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Buscar ubicación de un producto en inventario")
    public ResponseEntity<EntityModel<Inventario>> buscarPorProductoId(@PathVariable Integer productoId) {
        return ResponseEntity.ok(toEntityModel(inventarioService.buscarPorProductoId(productoId)));
    }

    @PostMapping
    @Operation(summary = "Registrar ubicación de un producto en inventario")
    public ResponseEntity<EntityModel<Inventario>> guardar(@Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toEntityModel(inventarioService.guardar(request)));
    }

    @PatchMapping("/{id}/ubicacion")
    @Operation(summary = "Actualizar ubicación de un producto en inventario")
    public ResponseEntity<EntityModel<Inventario>> actualizarUbicacion(
            @PathVariable Integer id,
            @RequestParam String nuevaUbicacion) {
        return ResponseEntity.ok(toEntityModel(inventarioService.actualizarUbicacion(id, nuevaUbicacion)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar registro de inventario")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Inventario> toEntityModel(Inventario inventario) {
        return EntityModel.of(inventario,
                linkTo(methodOn(InventarioController.class).buscarPorId(inventario.getId())).withSelfRel(),
                linkTo(methodOn(InventarioController.class).listarInventario()).withRel("inventario"),
                linkTo(methodOn(InventarioController.class)
                        .buscarPorProductoId(inventario.getProductoId())).withRel("por-producto")
        );
    }
}