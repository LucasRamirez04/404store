package _store.envio.controller;
//importacion clases necesarias

import _store.envio.dto.EnvioRequest;
import _store.envio.model.Envio;
import _store.envio.service.EnvioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

//ruta base del endpoints del controlador
@RequestMapping("/api/store/envios")
public class EnvioController {

    @Autowired
    EnvioService envioService;

    //GET para listar todos los envios
    @GetMapping
    public ResponseEntity<List<Envio>> listarEnvios(){

        // Obtiene la lista de envíos desde el servicio
        List<Envio> envios = envioService.listarTodos();
        if(envios.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(envios);
    }
    // Endpoint POST para guardar un nuevo envío
    @PostMapping
    public ResponseEntity<Envio> guardar(@Valid @RequestBody Envio envio){
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.guardarEnvio(envio));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id){
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Envio> confirmarEntrega(@PathVariable Integer id){
        Envio envioActualizado = envioService.confirmarEntrega(id);
        return ResponseEntity.ok(envioActualizado);
    }

}