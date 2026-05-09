package _store.pedido.service;

import _store.pedido.dto.PedidoRequest;
import _store.pedido.exception.ClienteNoEncontradoException;
import _store.pedido.exception.ErroPagoException;
import _store.pedido.exception.PedidoNoEncontradoException;
import _store.pedido.exception.ProductoNoEncontradoException;
import _store.pedido.model.Pedido;
import _store.pedido.repository.PedidoRepository;
import _store.pedido.webclient.ClienteClient;
import _store.pedido.webclient.PagoClient;
import _store.pedido.webclient.ProductoClient;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PedidoService {


    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private ProductoClient productoClient;

    @Autowired
    private PagoClient pagoClient;

    //Metodo para listar todos los pedidos
    public List<Pedido> listarTodos() {
        log.info("Listando todos los pedidos");
        return pedidoRepository.findAll();
    }

    //Metodo para mostrar pedido segun ID


    public Pedido buscarPorId(Integer id) {
        log.info("Buscando pedido con id: {}", id);
        return pedidoRepository.findById(id).orElseThrow(() -> new PedidoNoEncontradoException("No se encontro el pedido con id: " + id));
    }

    public Pedido guardar(PedidoRequest request){
        //Validar si el cliente existe
        log.info("Buscando pedido para el cliente con id {} y el producto con id {}",request.getClienteId(),request.getProductoId());
        Map<String, Object> cliente = clienteClient.obtenerClienteId(request.getClienteId());
        if(cliente == null || cliente.isEmpty()){
            throw new ClienteNoEncontradoException("Cliente no encontrado, no se puede agregar el pedido");
        }

        //Validar si el producto existe en inventario
        Map<String,Object> producto = productoClient.obtenerProductoId(request.getProductoId());
        if(producto == null || producto.isEmpty()){
            throw new ProductoNoEncontradoException("Producto no encontrado, no se puede agregar el pedido");
        }

        //Obtener precio de producto
        Double precioProducto = (Double) producto.get("precio");

        //pasar de request a model para setear el monto total
        Pedido pedido = new Pedido();
        pedido.setClienteId(request.getClienteId());
        pedido.setProductoId(request.getProductoId());
        pedido.setCantidad(request.getCantidad());
        //set monto total
        pedido.setTotal(precioProducto * request.getCantidad());

        //crear pedido para obtener el ID
        Pedido pedidoGuardado =  pedidoRepository.save(pedido);

        //Creación de pago automático
        // 4. LLAMADA AUTOMÁTICA AL PAGO
        try {
            pagoClient.registrarPagoAutomatico(pedidoGuardado.getId(), pedidoGuardado.getTotal());
            log.info("Pago automático registrado para el pedido {}", pedidoGuardado.getId());
        } catch (Exception e) {
            log.error("Fallo crítico: No se pudo conectar con el microservicio de Pago para el pedido {}", pedidoGuardado.getId());
            throw new ErroPagoException("Error al procesar el pago automático. El pedido no pudo completarse.");
        }

        //validar que el stock sea suficiente
        productoClient.descontarStock(request.getProductoId(),request.getCantidad());


        log.info("Pedido {} guardado exitosamente tras validar stock y cliente", pedidoGuardado.getId());

        return pedidoGuardado;
     }

     //Metodo para obtener la direccion del cliente
     public Map<String, Object> obtenerDetalleEnvio(Integer id) {
         Pedido pedido = pedidoRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

         // Obtenemos el cliente como un mapa
         Map<String, Object> clienteMap = clienteClient.obtenerClienteId(pedido.getClienteId());

         // Sacamos la dirección (Casteamos a String porque el Map es de Object)
         String direccion = (String) clienteMap.get("direccion");

         Map<String, Object> respuesta = new HashMap<>();
         respuesta.put("direccion", direccion);

         return respuesta;
     }


}
