package _store.producto;

import _store.producto.dto.ProductoRequest;
import _store.producto.exception.NoSuficienteStockException;
import _store.producto.exception.ProductoNoEncontradoException;
import _store.producto.model.Producto;
import _store.producto.repository.ProductoRepository;
import _store.producto.service.ProductoService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Pruebas unitarias de ProductoService con Datafaker (datos) + Mockito (repositorio simulado).
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private final Faker faker = new Faker();
    private ProductoRequest request;

    @BeforeEach
    void setUp() {
        request = new ProductoRequest();
        request.setNombre(faker.commerce().productName());
        request.setDescripcion(faker.lorem().sentence());
        request.setPrecio(Double.valueOf(faker.commerce().price().replace(",", ".")));
        request.setStock(faker.number().numberBetween(5, 100));
        request.setCategoria(faker.commerce().department());
        request.setMarca(faker.company().name());
    }

    @Test
    void crearDesdeRequest_debeGuardarProducto_conDatosDelRequest() {
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        Producto resultado = productoService.crearDesdeRequest(request);

        assertEquals(request.getNombre(), resultado.getNombre());
        assertEquals(request.getStock(), resultado.getStock());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void buscarPorId_debeLanzarExcepcion_cuandoNoExiste() {
        int idFalso = faker.number().numberBetween(5000, 9000);
        when(productoRepository.findById(idFalso)).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class, () -> productoService.buscarPorId(idFalso));
    }

    @Test
    void descontarInventario_debeLanzarExcepcion_cuandoNoHayStockSuficiente() {
        Integer id = 1;
        Integer cantidad = faker.number().numberBetween(1, 10);

        // Given: la actualizacion condicional (WHERE stock >= cantidad) no afecta filas
        when(productoRepository.descontarStock(id, cantidad)).thenReturn(0);

        // When / Then
        assertThrows(NoSuficienteStockException.class,
                () -> productoService.descontarInventario(id, cantidad));
    }

    @Test
    void descontarInventario_debeFuncionar_cuandoHayStockSuficiente() {
        Integer id = 1;
        Integer cantidad = faker.number().numberBetween(1, 10);

        when(productoRepository.descontarStock(id, cantidad)).thenReturn(1);

        assertDoesNotThrow(() -> productoService.descontarInventario(id, cantidad));
    }
}
