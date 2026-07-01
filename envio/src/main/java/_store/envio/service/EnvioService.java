package _store.envio.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import _store.envio.dto.EnvioRequest;
import _store.envio.exception.EnvioNoEncontradoException;
import _store.envio.exception.PedidoNoValidoException;
import _store.envio.model.Envio;
import _store.envio.repository.EnvioRepository;
import _store.envio.webclient.PedidoClient;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final PedidoClient pedidoClient;

    public List<Envio> listarTodos() {
        return envioRepository.findAll();
    }

    public Envio buscarPorId(Integer id) {
        return envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNoEncontradoException("No se encontró el envío con ID: " + id));
    }

    /**
     * Crea un nuevo envío a partir de la solicitud del cliente (microservicio Pago).
     * Antes de guardar, valida contra el microservicio Pedido que el pedidoId
     * referenciado exista de verdad: este es el "FALTA COMPLETAR" que tenía el
     * servicio antes, donde PedidoClient se inyectaba pero nunca se usaba.
     */
    public Envio guardarEnvio(EnvioRequest request) {
        validarPedidoExistente(request.getPedidoId());

        Envio envio = mapearAEntidad(request);
        return envioRepository.save(envio);
    }

    public Envio confirmarEntrega(Integer id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNoEncontradoException("No se encontró el envío con ID: " + id));

        // Cambiamos el estado de PENDIENTE a ENTREGADO
        envio.setEstadoEnvio("ENTREGADO");

        // Registramos la fecha actual (cumpliendo tu requerimiento de fechaRecibido)
        envio.setFechaRecibido(LocalDate.now());

        return envioRepository.save(envio);
    }

    public void eliminar(Integer id) {
        Envio envioExistente = envioRepository.findById(id)
                .orElseThrow(() -> new EnvioNoEncontradoException("No se encontró el envío con ID: " + id));

        envioRepository.delete(envioExistente);
    }

    // ------------------------------------------------------------------
    // Métodos privados de apoyo
    // ------------------------------------------------------------------

    private void validarPedidoExistente(Integer pedidoId) {
        try {
            pedidoClient.obtenerPedidoId(pedidoId);
        } catch (Exception ex) {
            throw new PedidoNoValidoException(
                    "No se pudo validar el pedido con ID " + pedidoId + ": " + ex.getMessage());
        }
    }

    private Envio mapearAEntidad(EnvioRequest request) {
        Envio envio = new Envio();
        envio.setDireccion(request.getDireccion());
        envio.setCostoEnvio(request.getCostoEnvio());
        envio.setPedidoId(request.getPedidoId());
        // fechaEnvio y estadoEnvio los completa Envio#onCreate() (@PrePersist)
        return envio;
    }
}
