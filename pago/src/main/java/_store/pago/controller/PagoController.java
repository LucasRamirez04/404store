package _store.pago.controller;

import _store.pago.model.Pago;
import _store.pago.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/store/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    // Ajustado con /listar igual que en PedidoController
    @GetMapping("/listar")
    public ResponseEntity<CollectionModel<EntityModel<Pago>>> listarPagos() {
        List<Pago> pagos = pagoService.listarTodos();
        if (pagos.isEmpty()) {
            CollectionModel<EntityModel<Pago>> vacio = CollectionModel.empty();
            vacio.add(linkTo(methodOn(PagoController.class).listarPagos()).withSelfRel());
            return ResponseEntity.ok(vacio);
        }

        List<EntityModel<Pago>> pagosConLinks = pagos.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pago>> collectionModel = CollectionModel.of(pagosConLinks,
                linkTo(methodOn(PagoController.class).listarPagos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Pago>> crearPago(@RequestBody Map<String, Object> datos) {
        Pago pagoGuardado = pagoService.guardar(datos);
        return ResponseEntity.status(HttpStatus.CREATED).body(toEntityModel(pagoGuardado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pago>> buscarPorId(@PathVariable Integer id) {
        Pago pago = pagoService.listarTodos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return ResponseEntity.ok(toEntityModel(pago));
    }

    @PatchMapping("/{id}/{metodo}/confirmar")
    public ResponseEntity<EntityModel<Pago>> confirmarPago(@PathVariable Integer id, @PathVariable String metodo) {
        Pago pagoActualizado = pagoService.confirmarPago(id, metodo);
        return ResponseEntity.ok(toEntityModel(pagoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Pago> toEntityModel(Pago pago) {
        EntityModel<Pago> entityModel = EntityModel.of(pago,
                linkTo(methodOn(PagoController.class).buscarPorId(pago.getId())).withSelfRel(),
                linkTo(methodOn(PagoController.class).listarPagos()).withRel("pagos"),
                linkTo(methodOn(PagoController.class).eliminar(pago.getId())).withRel("eliminar")
        );

        if (pago.getMetodoPago() != null) {
            entityModel.add(linkTo(methodOn(PagoController.class)
                    .confirmarPago(pago.getId(), pago.getMetodoPago())).withRel("confirmar"));
        }

        return entityModel;
    }
}