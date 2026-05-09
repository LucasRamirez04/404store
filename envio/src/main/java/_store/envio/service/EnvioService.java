package _store.envio.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import _store.envio.dto.EnvioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import _store.envio.model.Envio;
import _store.envio.repository.EnvioRepository;
import _store.envio.webclient.PedidoClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EnvioService {

    //FALTA COMPLETAR

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private PedidoClient pedidoClient;

    public List<Envio> listarTodos(){
        return envioRepository.findAll();
    }

    public Envio guardarEnvio(Envio envio){
        return envioRepository.save(envio);
    }

    public Envio confirmarEntrega(Integer id) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el envío con ID: " + id));

        // Cambiamos el estado de PENDIENTE a ENTREGADO
        envio.setEstadoEnvio("ENTREGADO");

        // Registramos la fecha actual (cumpliendo tu requerimiento de fechaRecibido)
        envio.setFechaRecibido(LocalDate.now());

        return envioRepository.save(envio);
    }
     

     public void eliminar(Integer id) {
        Envio envioExistente = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontro el envio con id: " + id));

            envioRepository.delete(envioExistente);
         }
     }


