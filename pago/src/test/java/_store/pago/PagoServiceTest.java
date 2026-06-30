package _store.pago;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import _store.pago.exception.PagoNoEncontradoException;
import _store.pago.model.Pago;
import _store.pago.repository.PagoRepository;
import _store.pago.service.PagoService;
import _store.pago.webclient.EnvioClient;
import _store.pago.webclient.PedidoClient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PedidoClient pedidoClient;

    @Mock
    private EnvioClient envioClient;

    @InjectMocks
    private PagoService pagoService;

    @Test
    void testGuardar() {

        // ARRANGE
        Map<String, Object> datos = new HashMap<>();
        datos.put("pedidoId", 10);
        datos.put("montoTotal", 25000.0);
        datos.put("estadoPago", "PENDIENTE");

        Pago pagoGuardado = new Pago();
        pagoGuardado.setId(1);
        pagoGuardado.setPedidoId(10);
        pagoGuardado.setMontoTotal(25000.0);
        pagoGuardado.setEstadoPago("PENDIENTE");

        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoGuardado);

        // ACT
        Pago resultado = pagoService.guardar(datos);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(10, resultado.getPedidoId());
        assertEquals(25000.0, resultado.getMontoTotal());

        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testListarTodos() {

        Pago p1 = new Pago();
        p1.setId(1);

        Pago p2 = new Pago();
        p2.setId(2);

        when(pagoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Pago> resultado = pagoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_Existe() {

        Pago pago = new Pago();
        pago.setId(1);

        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));

        Pago resultado = pagoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void testBuscarPorId_NoExiste() {

        when(pagoRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(PagoNoEncontradoException.class,
                () -> pagoService.buscarPorId(100));
    }

    @Test
    void testConfirmarPago() {

        // ARRANGE
        Pago pago = new Pago();
        pago.setId(1);
        pago.setPedidoId(50);
        pago.setMontoTotal(10000.0);
        pago.setEstadoPago("PENDIENTE");

        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));

        when(pedidoClient.obtenerDireccionPedido(50))
                .thenReturn("Av. Siempre Viva 742");

        when(pagoRepository.save(any(Pago.class))).thenAnswer(i -> i.getArgument(0));

        // ACT
        Pago resultado = pagoService.confirmarPago(1, "debito");

        // ASSERT
        assertEquals("REALIZADO", resultado.getEstadoPago());
        assertEquals("DEBITO", resultado.getMetodoPago());
        assertEquals(LocalDate.now(), resultado.getFechaPago());

        verify(pedidoClient, times(1)).obtenerDireccionPedido(50);

        verify(envioClient, times(1))
                .crearNuevoEnvio(50, "Av. Siempre Viva 742", 500.0);

        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testConfirmarPago_NoExiste() {

        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(PagoNoEncontradoException.class,
                () -> pagoService.confirmarPago(99, "credito"));

        verify(envioClient, never())
                .crearNuevoEnvio(anyInt(), anyString(), anyDouble());
    }

    @Test
    void testEliminar() {

        Pago pago = new Pago();
        pago.setId(1);

        when(pagoRepository.findById(1)).thenReturn(Optional.of(pago));

        pagoService.eliminar(1);

        verify(pagoRepository, times(1)).delete(pago);
    }

    @Test
    void testEliminar_NoExiste() {

        when(pagoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(PagoNoEncontradoException.class,
                () -> pagoService.eliminar(99));

        verify(pagoRepository, never()).delete(any());
    }

}