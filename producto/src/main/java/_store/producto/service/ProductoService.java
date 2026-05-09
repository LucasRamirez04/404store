package _store.producto.service;

import _store.producto.dto.ProductoRequest;
import _store.producto.exception.NoSuficienteStockException;
import _store.producto.exception.ProductoNoEncontradoException;
import _store.producto.model.Producto;
import _store.producto.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        log.info("Listando todos los productos");
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Integer id) {
        log.info("Buscando producto con Id: {}", id);
        return productoRepository.findById(id).orElseThrow(() -> new ProductoNoEncontradoException("No se encontro el producto con id: " + id));
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        log.info("Buscando productos con categoria: {}", categoria);
        return productoRepository.findByCategoria(categoria);
    }


    public Producto crearDesdeRequest(ProductoRequest request) {
        log.info("Creando producto con Nombre: {}", request.getNombre());

        Producto producto = new Producto();

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setCategoria(request.getCategoria());
        producto.setMarca(request.getMarca());

        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, ProductoRequest request) {
        log.info("Actualizar producto con id: {}", id);
        
        Producto producto = buscarPorId(id);

        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());

        return productoRepository.save(producto);
    }

    public void eliminar(Integer id) {
        log.info("Eliminar producto con id: {}", id);
        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);
    }

    //metodo para reducir el stock
    @Transactional
    public void descontarInventario(Integer id, Integer cantidad){
        log.info("Procesando descuento de stock {} unidades para el ID {}",cantidad,id);
        int filasActualizadas = productoRepository.descontarStock(id,cantidad);

        if(filasActualizadas == 0){
            log.error("Fallo al descontar: Stock insuficinete para el producto {}", id);
            throw new NoSuficienteStockException("No hay suficiente stock disponible");
        }
    }

}
