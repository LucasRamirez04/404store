package _store.inventario.service;

import _store.inventario.dto.InventarioRequest;
import _store.inventario.exception.InventarioNoEncontradoException;
import _store.inventario.exception.ProductoNoEncontradoException;
import _store.inventario.model.Inventario;
import _store.inventario.repository.InventarioRepository;
import _store.inventario.webclient.ProductoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario inventario;
    private InventarioRequest request;

    @BeforeEach
    void setUp() {
        inventario = Inventario.builder()
                .id(1)
                .productoId(1)
                .ubicacion("Bodega A")
                .build();

        request = new InventarioRequest();
        request.setProductoId(1);
        request.setUbicacion("Bodega A");
    }

    @Test
    void listarTodos_debeRetornarLista() {
        // Given
        when(inventarioRepository.findAll()).thenReturn(List.of(inventario));
        // When
        List<Inventario> resultado = inventarioService.listarTodos();
        // Then
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(inventarioRepository).findAll();
    }

    @Test
    void listarTodos_debeRetornarListaVacia() {
        // Given
        when(inventarioRepository.findAll()).thenReturn(List.of());
        // When
        List<Inventario> resultado = inventarioService.listarTodos();
        // Then
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorId_debeRetornarInventario() {
        // Given
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));
        // When
        Inventario resultado = inventarioService.buscarPorId(1);
        // Then
        assertNotNull(resultado);
        assertEquals("Bodega A", resultado.getUbicacion());
    }

    @Test
    void buscarPorId_debeArrojarExcepcionSiNoExiste() {
        // Given
        when(inventarioRepository.findById(99)).thenReturn(Optional.empty());
        // When & Then
        assertThrows(InventarioNoEncontradoException.class,
                () -> inventarioService.buscarPorId(99));
    }

    @Test
    void guardar_debeGuardarInventarioCuandoProductoExiste() {
        // Given
        when(productoClient.obtenerProductoPorId(1))
                .thenReturn(Map.of("id", 1, "nombre", "Producto Test"));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);
        // When
        Inventario resultado = inventarioService.guardar(request);
        // Then
        assertNotNull(resultado);
        assertEquals("Bodega A", resultado.getUbicacion());
        verify(inventarioRepository).save(any(Inventario.class));
    }

    @Test
    void guardar_debeArrojarExcepcionSiProductoNoExiste() {
        // Given
        when(productoClient.obtenerProductoPorId(1)).thenReturn(null);
        // When & Then
        assertThrows(ProductoNoEncontradoException.class,
                () -> inventarioService.guardar(request));
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void actualizarUbicacion_debeActualizarCorrectamente() {
        // Given
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);
        // When
        Inventario resultado = inventarioService.actualizarUbicacion(1, "Estante 2");
        // Then
        assertNotNull(resultado);
        verify(inventarioRepository).save(any(Inventario.class));
    }

    @Test
    void eliminar_debeEliminarInventarioExistente() {
        // Given
        when(inventarioRepository.findById(1)).thenReturn(Optional.of(inventario));
        doNothing().when(inventarioRepository).deleteById(1);
        // When
        inventarioService.eliminar(1);
        // Then
        verify(inventarioRepository).deleteById(1);
    }
}