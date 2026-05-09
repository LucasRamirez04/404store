package _store.producto.controller;

import _store.producto.dto.ProductoRequest;
import _store.producto.model.Producto;

import _store.producto.service.ProductoService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/store/productos")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarClientes(){
        List<Producto> productos = productoService.listarTodos();
        if (productos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorID(@PathVariable Integer id){
        Producto productoEncontrado = productoService.buscarPorId(id);
        return ResponseEntity.ok().body(productoEncontrado);
    }

    @GetMapping("/categoria/{nombreCategoria}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(@PathVariable String nombreCategoria) {
        List<Producto> productos = productoService.buscarPorCategoria(nombreCategoria);

        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 si no hay productos
        }

        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<Producto> guardar(@Valid @RequestBody ProductoRequest request){
        Producto productoGuardado = productoService.crearDesdeRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
    }

    @PutMapping("{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody ProductoRequest request){
        Producto productoActualizado = productoService.actualizar(id,request);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/descontar")
    public ResponseEntity<Void> descontar(@PathVariable Integer id, @RequestParam Integer cantidad){
        productoService.descontarInventario(id,cantidad);
        return ResponseEntity.ok().build();
    }

}
