package _store.cliente.service;

import _store.cliente.dto.ClienteRequest;
import _store.cliente.exception.ClienteNoEncontradoException;
import _store.cliente.model.Cliente;
import _store.cliente.repository.ClienteRepository;
import java.util.List;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        log.info("Listando todos los clientes registrados");
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Integer id) {
        log.info("Buscando cliente con ID: {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException("No se encontro el cliente con id: " + id)
                );
    }

    public Cliente crearDesdeRequest(ClienteRequest request) {
        log.info("Buscando cliente con RUN: {}", request.getRun());

        Cliente cliente = new Cliente();
        cliente.setRun(request.getRun());
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());

        return clienteRepository.save(cliente);
    }


    public Cliente actualizar(Integer id, ClienteRequest request){
        log.info("Actualizando datos del cliente con ID: {}", id);

        Cliente cliente = buscarPorId(id);
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setCorreo(request.getCorreo());

        return clienteRepository.save(cliente);
    }

    public void eliminar(Integer id){
        log.info("Eliminando cliente con id: {}", id);
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }



}