package _store.envio.service;

import _store.envio.dto.EnvioRequest;
import _store.envio.exception.EnvioNoEncontradoException;
import _store.envio.exception.PedidoNoValidoException;
import _store.envio.model.Envio;
import _store.envio.repository.EnvioRepository;
import _store.envio.webclient.PedidoClient;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del servicio de Envío.
 * <p>
 * EnvioRepository y PedidoClient se simulan con Mockito: estas pruebas NO
 * tocan una base de datos real ni hacen llamadas HTTP de verdad. Faker
 * (Datafaker) se usa solo para generar datos de entrada variados en cada
 * ejecución, sin tener que escribir direcciones/IDs "a mano".
 */
@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private PedidoClient pedidoClient;

    @InjectMocks
    private EnvioService envioService;

    private static final Faker faker = new Faker();

    private EnvioRequest crearRequestValido() {
        EnvioRequest request = new EnvioRequest();
        request.setDireccion(faker.address().fullAddress());
        request.setCostoEnvio(faker.number().randomDouble(2, 1000, 5000));
        request.setPedidoId(faker.number().numberBetween(1, 1000));
        return request;
    }

    private Envio crearEnvioPersistido(Integer id, EnvioRequest origen) {
        Envio envio = new Envio();
        envio.setId(id);
        envio.setDireccion(origen.getDireccion());
        envio.setCostoEnvio(origen.getCostoEnvio());
        envio.setPedidoId(origen.getPedidoId());
        envio.setFechaEnvio(LocalDate.now());
        envio.setEstadoEnvio("PENDIENTE");
        return envio;
    }

    // ------------------------------------------------------------------
    // guardarEnvio
    // ------------------------------------------------------------------

    @Test
    void guardarEnvio_conPedidoValido_debeValidarPedidoYGuardarEnvio() {
        // Given: un request válido y un pedido que SÍ existe en el microservicio Pedido
        EnvioRequest request = crearRequestValido();
        Envio envioGuardado = crearEnvioPersistido(1, request);

        when(pedidoClient.obtenerPedidoId(request.getPedidoId()))
                .thenReturn(Map.of("id", request.getPedidoId(), "estado", "PAGADO"));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioGuardado);

        // When
        Envio resultado = envioService.guardarEnvio(request);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getDireccion()).isEqualTo(request.getDireccion());
        assertThat(resultado.getPedidoId()).isEqualTo(request.getPedidoId());

        verify(pedidoClient, times(1)).obtenerPedidoId(request.getPedidoId());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    void guardarEnvio_conPedidoInexistente_debeLanzarPedidoNoValidoExceptionYNoGuardar() {
        // Given: el microservicio Pedido responde con error (pedido no existe)
        EnvioRequest request = crearRequestValido();

        when(pedidoClient.obtenerPedidoId(request.getPedidoId()))
                .thenThrow(new RuntimeException("Pedido no encontrado"));

        // When / Then
        assertThrows(PedidoNoValidoException.class, () -> envioService.guardarEnvio(request));

        // El envío NUNCA debe guardarse si el pedido no es válido
        verify(envioRepository, never()).save(any(Envio.class));
    }

    // ------------------------------------------------------------------
    // listarTodos
    // ------------------------------------------------------------------

    @Test
    void listarTodos_debeRetornarTodosLosEnviosDelRepositorio() {
        // Given
        EnvioRequest r1 = crearRequestValido();
        EnvioRequest r2 = crearRequestValido();
        List<Envio> enviosEsperados = List.of(
                crearEnvioPersistido(1, r1),
                crearEnvioPersistido(2, r2)
        );
        when(envioRepository.findAll()).thenReturn(enviosEsperados);

        // When
        List<Envio> resultado = envioService.listarTodos();

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).containsExactlyElementsOf(enviosEsperados);
    }

    // ------------------------------------------------------------------
    // buscarPorId
    // ------------------------------------------------------------------

    @Test
    void buscarPorId_conIdExistente_debeRetornarElEnvio() {
        // Given
        EnvioRequest request = crearRequestValido();
        Envio envio = crearEnvioPersistido(7, request);
        when(envioRepository.findById(7)).thenReturn(Optional.of(envio));

        // When
        Envio resultado = envioService.buscarPorId(7);

        // Then
        assertThat(resultado.getId()).isEqualTo(7);
    }

    @Test
    void buscarPorId_conIdInexistente_debeLanzarEnvioNoEncontradoException() {
        // Given
        when(envioRepository.findById(99)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EnvioNoEncontradoException.class, () -> envioService.buscarPorId(99));
    }

    // ------------------------------------------------------------------
    // confirmarEntrega
    // ------------------------------------------------------------------

    @Test
    void confirmarEntrega_conIdValido_debeCambiarEstadoYRegistrarFechaRecibido() {
        // Given: un envío PENDIENTE
        EnvioRequest request = crearRequestValido();
        Envio envioPendiente = crearEnvioPersistido(3, request);
        envioPendiente.setEstadoEnvio("PENDIENTE");

        when(envioRepository.findById(3)).thenReturn(Optional.of(envioPendiente));
        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Envio resultado = envioService.confirmarEntrega(3);

        // Then
        assertThat(resultado.getEstadoEnvio()).isEqualTo("ENTREGADO");
        assertThat(resultado.getFechaRecibido()).isEqualTo(LocalDate.now());
    }

    @Test
    void confirmarEntrega_conIdInexistente_debeLanzarEnvioNoEncontradoException() {
        // Given
        when(envioRepository.findById(123)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EnvioNoEncontradoException.class, () -> envioService.confirmarEntrega(123));
        verify(envioRepository, never()).save(any());
    }

    // ------------------------------------------------------------------
    // eliminar
    // ------------------------------------------------------------------

    @Test
    void eliminar_conIdValido_debeEliminarElEnvio() {
        // Given
        EnvioRequest request = crearRequestValido();
        Envio envio = crearEnvioPersistido(5, request);
        when(envioRepository.findById(5)).thenReturn(Optional.of(envio));

        // When
        envioService.eliminar(5);

        // Then
        verify(envioRepository, times(1)).delete(envio);
    }

    @Test
    void eliminar_conIdInexistente_debeLanzarEnvioNoEncontradoExceptionYNoEliminarNada() {
        // Given
        when(envioRepository.findById(404)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EnvioNoEncontradoException.class, () -> envioService.eliminar(404));
        verify(envioRepository, never()).delete(any());
    }
}
