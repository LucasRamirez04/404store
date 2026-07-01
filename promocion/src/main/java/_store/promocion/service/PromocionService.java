package _store.promocion.service;

import _store.promocion.exception.PromocionInvalidaException;
import _store.promocion.exception.PromocionNoEncontradaException;
import _store.promocion.model.Promocion;
import _store.promocion.repository.PromocionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromocionService {

    private static final Logger log = LoggerFactory.getLogger(PromocionService.class);

    @Autowired
    PromocionRepository promocionRepository;

    public List<Promocion> listarTodos() {
        log.info("Listando todas las promociones");
        return promocionRepository.findAll();
    }

    public Promocion buscarPorId(Integer id) {
        log.info("Buscando promoción con ID: {}", id);
        return promocionRepository.findById(id)
                .orElseThrow(() -> new PromocionNoEncontradaException("No se encontró la promoción con id: " + id));
    }

    public Promocion crear(Promocion promocion) {
        log.info("Creando promoción con código: {}", promocion.getCodigo());
        return promocionRepository.save(promocion);
    }

    // método clave: lo usará el microservicio "pedido" para validar un cupón antes de aplicar el descuento
    public Promocion validarCodigo(String codigo) {
        log.info("Validando código de promoción: {}", codigo);

        Promocion promocion = promocionRepository.findByCodigo(codigo)
                .orElseThrow(() -> new PromocionNoEncontradaException("No existe la promoción con código: " + codigo));

        if (!promocion.getActivo()) {
            throw new PromocionInvalidaException("La promoción con código " + codigo + " está inactiva");
        }

        if (promocion.getFechaExpiracion().isBefore(LocalDate.now())) {
            throw new PromocionInvalidaException("La promoción con código " + codigo + " ya expiró");
        }

        return promocion;
    }

    public void eliminar(Integer id) {
        log.info("Eliminando promoción con id: {}", id);
        Promocion promocion = buscarPorId(id);
        promocionRepository.delete(promocion);
    }
}