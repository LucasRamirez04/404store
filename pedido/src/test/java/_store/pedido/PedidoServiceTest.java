package _store.pedido;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import _store.pedido.dto.PedidoRequest;
import _store.pedido.exception.ClienteNoEncontradoException;
import _store.pedido.exception.ErroPagoException;
import _store.pedido.exception.PedidoNoEncontradoException;
import _store.pedido.exception.ProductoNoEncontradoException;
import _store.pedido.model.Pedido;
import _store.pedido.repository.PedidoRepository;
import _store.pedido.service.PedidoService;
import _store.pedido.webclient.ClienteClient;
import _store.pedido.webclient.PagoClient;
import _store.pedido.webclient.ProductoClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PedidoServiceTest {

    // 1. Declaramos TODOS los mocks que usa tu método
    @Mock
    private ClienteClient clienteClient;

    @Mock
    private ProductoClient productoClient;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PagoClient pagoClient;

    // 2. Inyectamos los mocks en tu servicio
    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void testguardar() {
        // --- ARRANGE (Preparar Datos de Prueba) ---
        PedidoRequest request = new PedidoRequest();
        request.setClienteId(1);
        request.setProductoId(100);
        request.setCantidad(3);

        // Mock Cliente: Simula que existe devolviendo un mapa con datos ficticios
        Map<String, Object> clienteMock = new HashMap<>();
        clienteMock.put("id", 1);
        when(clienteClient.obtenerClienteId(1)).thenReturn(clienteMock);

        // Mock Producto: Simula que existe y tiene precio de 1500.0
        Map<String, Object> productoMock = new HashMap<>();
        productoMock.put("id", 100);
        productoMock.put("precio", 1500.0);
        when(productoClient.obtenerProductoId(100)).thenReturn(productoMock);

        // Mock Repository: Simula el comportamiento de guardar y asigna un ID simulado (ej: 55)
        Pedido pedidoGuardadoMock = new Pedido();
        pedidoGuardadoMock.setId(55);
        pedidoGuardadoMock.setClienteId(1);
        pedidoGuardadoMock.setProductoId(100);
        pedidoGuardadoMock.setCantidad(3);
        pedidoGuardadoMock.setTotal(4500.0); // 1500.0 * 3

        // Usamos any(Pedido.class) para aceptar cualquier instancia de Pedido que se intente guardar
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoGuardadoMock);

        // Nota: pagoClient y productoClient.descontarStock devuelven void por defecto,
        // Mockito asume que no hacen nada (doNothing), lo cual es ideal para el camino feliz.

        // --- ACT (Actuar) ---
        Pedido resultado = pedidoService.guardar(request);

        // --- ASSERT (Afirmar y Verificar) ---
        assertNotNull(resultado);
        assertEquals(55, resultado.getId());
        assertEquals(4500.0, resultado.getTotal()); // Verificamos que el cálculo de la multiplicación funcionó

        // Verificaciones de comportamiento: Asegurarnos de que se llamó a cada microservicio
        verify(clienteClient, times(1)).obtenerClienteId(1);
        verify(productoClient, times(1)).obtenerProductoId(100);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(pagoClient, times(1)).registrarPagoAutomatico(55, 4500.0);
        verify(productoClient, times(1)).descontarStock(100, 3);
    }

    @Test
    void testGuardar_ClienteNoEncontrado() {
        // GIVEN
        PedidoRequest request = new PedidoRequest();
        request.setClienteId(99);
        request.setProductoId(100);
        request.setCantidad(2);

        when(clienteClient.obtenerClienteId(99)).thenReturn(null);

        // WHEN - THEN
        assertThrows(ClienteNoEncontradoException.class, () -> pedidoService.guardar(request));
        verify(productoClient, never()).obtenerProductoId(any());
    }

    @Test
    void testGuardar_ProductoNoEncontrado() {
        // GIVEN
        PedidoRequest request = new PedidoRequest();
        request.setClienteId(1);
        request.setProductoId(99);
        request.setCantidad(2);

        Map<String, Object> clienteMock = new HashMap<>();
        clienteMock.put("id", 1);
        when(clienteClient.obtenerClienteId(1)).thenReturn(clienteMock);
        when(productoClient.obtenerProductoId(99)).thenReturn(null);

        // WHEN - THEN
        assertThrows(ProductoNoEncontradoException.class, () -> pedidoService.guardar(request));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testGuardar_FalloPago() {
        // GIVEN
        PedidoRequest request = new PedidoRequest();
        request.setClienteId(1);
        request.setProductoId(100);
        request.setCantidad(2);

        Map<String, Object> clienteMock = new HashMap<>();
        clienteMock.put("id", 1);
        when(clienteClient.obtenerClienteId(1)).thenReturn(clienteMock);

        Map<String, Object> productoMock = new HashMap<>();
        productoMock.put("id", 100);
        productoMock.put("precio", 1000.0);
        when(productoClient.obtenerProductoId(100)).thenReturn(productoMock);

        Pedido pedidoGuardadoMock = new Pedido();
        pedidoGuardadoMock.setId(10);
        pedidoGuardadoMock.setTotal(2000.0);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoGuardadoMock);

        doThrow(new RuntimeException("Error de conexión"))
                .when(pagoClient).registrarPagoAutomatico(10, 2000.0);

        // WHEN - THEN
        assertThrows(ErroPagoException.class, () -> pedidoService.guardar(request));
    }

    @Test
    void testListarTodos() {
        // GIVEN
        Pedido p1 = new Pedido();
        p1.setId(1);
        Pedido p2 = new Pedido();
        p2.setId(2);
        when(pedidoRepository.findAll()).thenReturn(List.of(p1, p2));

        // WHEN
        List<Pedido> resultado = pedidoService.listarTodos();

        // THEN
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId_Existe() {
        // GIVEN
        Pedido pedido = new Pedido();
        pedido.setId(1);
        when(pedidoRepository.findById(1)).thenReturn(java.util.Optional.of(pedido));

        // WHEN
        Pedido resultado = pedidoService.buscarPorId(1);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void testBuscarPorId_NoExiste() {
        // GIVEN
        when(pedidoRepository.findById(99)).thenReturn(java.util.Optional.empty());

        // WHEN - THEN
        assertThrows(PedidoNoEncontradoException.class, () -> pedidoService.buscarPorId(99));
    }

    @Test
    void testObtenerDetalleEnvio() {
        // GIVEN
        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setClienteId(5);
        when(pedidoRepository.findById(1)).thenReturn(java.util.Optional.of(pedido));

        Map<String, Object> clienteMock = new HashMap<>();
        clienteMock.put("id", 5);
        clienteMock.put("direccion", "Calle Falsa 123");
        when(clienteClient.obtenerClienteId(5)).thenReturn(clienteMock);

        // WHEN
        Map<String, Object> resultado = pedidoService.obtenerDetalleEnvio(1);

        // THEN
        assertNotNull(resultado);
        assertEquals("Calle Falsa 123", resultado.get("direccion"));
    }



}
