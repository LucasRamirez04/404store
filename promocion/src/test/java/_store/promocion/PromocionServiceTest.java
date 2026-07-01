package _store.promocion;

import _store.promocion.exception.PromocionInvalidaException;
import _store.promocion.exception.PromocionNoEncontradaException;
import _store.promocion.model.Promocion;
import _store.promocion.repository.PromocionRepository;
import _store.promocion.service.PromocionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromocionServiceTest {

    @Mock
    PromocionRepository promocionRepository;

    @InjectMocks
    PromocionService promocionService;

    @Test
    void validarCodigo_cuponValido_retornaPromocion() {
        Promocion p = new Promocion();
        p.setCodigo("VERANO2026");
        p.setActivo(true);
        p.setFechaExpiracion(LocalDate.now().plusDays(10));
        p.setPorcentajeDescuento(15.0);

        when(promocionRepository.findByCodigo("VERANO2026")).thenReturn(Optional.of(p));

        Promocion resultado = promocionService.validarCodigo("VERANO2026");
        assertEquals(15.0, resultado.getPorcentajeDescuento());
    }

    @Test
    void validarCodigo_cuponInexistente_lanzaExcepcion() {
        when(promocionRepository.findByCodigo("FALSO")).thenReturn(Optional.empty());
        assertThrows(PromocionNoEncontradaException.class, () -> promocionService.validarCodigo("FALSO"));
    }

    @Test
    void validarCodigo_cuponExpirado_lanzaExcepcion() {
        Promocion p = new Promocion();
        p.setCodigo("VIEJO2020");
        p.setActivo(true);
        p.setFechaExpiracion(LocalDate.now().minusDays(1));

        when(promocionRepository.findByCodigo("VIEJO2020")).thenReturn(Optional.of(p));
        assertThrows(PromocionInvalidaException.class, () -> promocionService.validarCodigo("VIEJO2020"));
    }

    @Test
    void validarCodigo_cuponInactivo_lanzaExcepcion() {
        Promocion p = new Promocion();
        p.setCodigo("INACTIVO");
        p.setActivo(false);
        p.setFechaExpiracion(LocalDate.now().plusDays(5));

        when(promocionRepository.findByCodigo("INACTIVO")).thenReturn(Optional.of(p));
        assertThrows(PromocionInvalidaException.class, () -> promocionService.validarCodigo("INACTIVO"));
    }
}