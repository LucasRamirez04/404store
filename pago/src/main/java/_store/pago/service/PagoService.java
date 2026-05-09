package _store.pago.service;

import _store.pago.exception.PagoNoEncontradoException;
import _store.pago.model.Pago;
import _store.pago.repository.PagoRepository;
import _store.pago.webclient.EnvioClient;
import _store.pago.webclient.PedidoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    @Autowired
    PagoRepository pagoRepository;

    @Autowired
    PedidoClient pedidoClient;

    @Autowired
    EnvioClient envioClient;

    //metodo para listar todos los pagos
    public List<Pago> listarTodos(){
        log.info("Listando todos los pagos");
        return pagoRepository.findAll();
    }

    public Pago buscarPorId(Integer id) {
        log.info("Buscando cliente con ID: {}", id);
        return pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNoEncontradoException("No se encontro el pago con id: " + id)
                );
    }

    public Pago guardar(Map<String, Object> datos) {
        Pago pago = new Pago();
        pago.setPedidoId(((Number) datos.get("pedidoId")).intValue());
        pago.setMontoTotal(((Number) datos.get("montoTotal")).doubleValue());
        pago.setEstadoPago((String) datos.get("estadoPago"));

        log.info("Registrando pago automático para pedido ID: {}", pago.getPedidoId());
        return pagoRepository.save(pago);
    }

    //metodo para simular la realización del pago
    public Pago confirmarPago(Integer id, String metodo){
        Pago pago = pagoRepository.findById(id).orElseThrow(() -> new PagoNoEncontradoException("No se encontró el pago con ID " + id));
        pago.setMetodoPago(metodo.toUpperCase());
        pago.setFechaPago(LocalDate.now());
        pago.setEstadoPago("REALIZADO");

        log.info("Pago ID {} confirmado usando {}. Fecha: {}",id,metodo,pago.getFechaPago());

        String direccionPedido = pedidoClient.obtenerDireccionPedido(pago.getPedidoId());


        Double costoEnvio = pago.getMontoTotal() * 0.05;


        envioClient.crearNuevoEnvio(pago.getPedidoId(), direccionPedido, costoEnvio);

        return pagoRepository.save(pago);
    }

    //metodo para eliminar un pago
    public void eliminar(Integer id){
        log.info("Eliminando pago con id: {}", id);
        Pago pago = buscarPorId(id);
        pagoRepository.delete(pago);

    }

}
