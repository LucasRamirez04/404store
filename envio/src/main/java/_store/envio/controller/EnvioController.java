package _store.envio.controller;

import _store.envio.assembler.EnvioModelAssembler;
import _store.envio.dto.EnvioRequest;
import _store.envio.model.Envio;
import _store.envio.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/store/envios")
@RequiredArgsConstructor
@Tag(name = "Envíos", description = "Logística y seguimiento de entregas")
public class EnvioController {

    private final EnvioService envioService;
    private final EnvioModelAssembler envioModelAssembler;

    @Operation(summary = "Listar todos los envíos registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Listado de envíos")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> listarEnvios() {
        List<EntityModel<Envio>> envios = envioService.listarTodos().stream()
                .map(envioModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Envio>> coleccion = CollectionModel.of(envios);
        return ResponseEntity.ok(coleccion);
    }

    @Operation(summary = "Buscar un envío por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envío encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un envío con ese ID", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Envio>> buscarPorId(
            @Parameter(description = "ID del envío") @PathVariable Integer id) {
        Envio envio = envioService.buscarPorId(id);
        return ResponseEntity.ok(envioModelAssembler.toModel(envio));
    }

    @Operation(summary = "Registrar un nuevo envío",
            description = "Lo utiliza el microservicio Pago luego de confirmar un pago. "
                    + "Valida contra el microservicio Pedido que el pedidoId exista antes de crear el envío.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Envío creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "El pedido referenciado no existe", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EntityModel<Envio>> guardar(
            @Valid @RequestBody @Schema(description = "Datos del envío a crear") EnvioRequest request) {
        Envio envioGuardado = envioService.guardarEnvio(request);
        EntityModel<Envio> model = envioModelAssembler.toModel(envioGuardado);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(model);
    }

    @Operation(summary = "Eliminar un envío por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Envío eliminado"),
            @ApiResponse(responseCode = "404", description = "No existe un envío con ese ID", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del envío") @PathVariable Integer id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Confirmar la entrega de un envío",
            description = "Registra la fecha de recibido y cambia el estado a ENTREGADO.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entrega confirmada"),
            @ApiResponse(responseCode = "404", description = "No existe un envío con ese ID", content = @Content)
    })
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<EntityModel<Envio>> confirmarEntrega(
            @Parameter(description = "ID del envío") @PathVariable Integer id) {
        Envio envioActualizado = envioService.confirmarEntrega(id);
        return ResponseEntity.ok(envioModelAssembler.toModel(envioActualizado));
    }
}
