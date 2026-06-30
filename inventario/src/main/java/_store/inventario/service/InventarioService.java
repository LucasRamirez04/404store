package _store.inventario.service;

import _store.inventario.dto.InventarioRequest;
import _store.inventario.exception.InventarioNoEncontradoException;
import _store.inventario.exception.ProductoNoEncontradoException;
import _store.inventario.model.Inventario;
import _store.inventario.repository.InventarioRepository;
import _store.inventario.webclient.ProductoClient;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InventarioService {

    private static final Logger log = LoggerFactory.getLogger(InventarioService.class);

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoClient productoClient;

    public List<Inventario> listarTodos() {
        log.info("Listando todos los registros de inventario");
        return inventarioRepository.findAll();
    }

    public Inventario buscarPorId(Integer id) {
        log.info("Buscando inventario con id: {}", id);
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new InventarioNoEncontradoException(
                        "No se encontró inventario con id: " + id));
    }

    public Inventario buscarPorProductoId(Integer productoId) {
        log.info("Buscando inventario para productoId: {}", productoId);
        return inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new InventarioNoEncontradoException(
                        "No se encontró inventario para productoId: " + productoId));
    }

    public Inventario guardar(InventarioRequest request) {
        log.info("Verificando existencia del producto con id: {}", request.getProductoId());
        Map<String, Object> producto = productoClient.obtenerProductoPorId(request.getProductoId());
        if (producto == null || producto.isEmpty()) {
            throw new ProductoNoEncontradoException(
                    "Producto no encontrado, no se puede registrar en inventario");
        }

        Inventario inventario = Inventario.builder()
                .productoId(request.getProductoId())
                .ubicacion(request.getUbicacion())
                .build();

        log.info("Guardando inventario para productoId: {} en ubicación: {}",
                request.getProductoId(), request.getUbicacion());
        return inventarioRepository.save(inventario);
    }

    public Inventario actualizarUbicacion(Integer id, String nuevaUbicacion) {
        log.info("Actualizando ubicación del inventario con id: {}", id);
        Inventario inventario = buscarPorId(id);
        inventario.setUbicacion(nuevaUbicacion);
        return inventarioRepository.save(inventario);
    }

    public void eliminar(Integer id) {
        log.info("Eliminando inventario con id: {}", id);
        buscarPorId(id);
        inventarioRepository.deleteById(id);
    }
}