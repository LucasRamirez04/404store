package _store.producto.controller;

import _store.producto.dto.ProductoRequest;
import _store.producto.model.Producto;
import _store.producto.service.ProductoService;
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

// Reemplaza al ProductoController original. Misma logica, ahora con HATEOAS,
// documentacion Swagger y la misma convencion que el microservicio Pedido
// (referencia del equipo: /listar para listados, metodo privado para EntityModel).
@RestController
@RequestMapping("/api/store/productos")
@Tag(name = "Productos", description = "Gestion de inventario y catalogo")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @GetMapping("/listar")
    @Operation(summary = "Listar todos los productos")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listarProductos(){
        List<Producto> productos = productoService.listarTodos();
        if (productos.isEmpty()){
            CollectionModel<EntityModel<Producto>> vacio = CollectionModel.empty();
            vacio.add(linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());
            return ResponseEntity.ok(vacio);
        }

        List<EntityModel<Producto>> modelos = productos.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Producto>> coleccion = CollectionModel.of(modelos,
                linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());

        return ResponseEntity.ok(coleccion);
    }

    @Operation(summary = "Buscar un producto por su id")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> buscarPorID(@PathVariable Integer id){
        Producto productoEncontrado = productoService.buscarPorId(id);
        return ResponseEntity.ok(toEntityModel(productoEncontrado));
    }

    @Operation(summary = "Buscar productos por categoria")
    @GetMapping("/categoria/{nombreCategoria}")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> obtenerPorCategoria(@PathVariable String nombreCategoria) {
        List<Producto> productos = productoService.buscarPorCategoria(nombreCategoria);

        if (productos.isEmpty()) {
            CollectionModel<EntityModel<Producto>> vacio = CollectionModel.empty();
            vacio.add(linkTo(methodOn(ProductoController.class).obtenerPorCategoria(nombreCategoria)).withSelfRel());
            return ResponseEntity.ok(vacio);
        }

        List<EntityModel<Producto>> modelos = productos.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Producto>> coleccion = CollectionModel.of(modelos,
                linkTo(methodOn(ProductoController.class).obtenerPorCategoria(nombreCategoria)).withSelfRel());

        return ResponseEntity.ok(coleccion);
    }

    @Operation(summary = "Registrar un nuevo producto")
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> guardar(@Valid @RequestBody ProductoRequest request){
        Producto productoGuardado = productoService.crearDesdeRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toEntityModel(productoGuardado));
    }

    @Operation(summary = "Actualizar un producto")
    @PutMapping("{id}")
    public ResponseEntity<EntityModel<Producto>> actualizar(@PathVariable Integer id, @RequestBody ProductoRequest request){
        Producto productoActualizado = productoService.actualizar(id,request);
        return ResponseEntity.ok(toEntityModel(productoActualizado));
    }

    @Operation(summary = "Eliminar un producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Descontar stock de un producto (uso interno entre microservicios)")
    @PutMapping("/{id}/descontar")
    public ResponseEntity<Void> descontar(@PathVariable Integer id, @RequestParam Integer cantidad){
        productoService.descontarInventario(id,cantidad);
        return ResponseEntity.ok().build();
    }

    // Metodo auxiliar para construir el EntityModel con sus enlaces HATEOAS
    // (mismo patron que PedidoController, sin clase Assembler aparte).
    private EntityModel<Producto> toEntityModel(Producto producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).buscarPorID(producto.getId())).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos"),
                linkTo(methodOn(ProductoController.class).obtenerPorCategoria(producto.getCategoria())).withRel("misma-categoria"));
    }
}
