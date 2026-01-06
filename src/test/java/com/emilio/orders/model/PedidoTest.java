package com.emilio.orders.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Pedido.
 * Verifica la creación, getters/setters y validaciones del modelo.
 */
class PedidoTest {

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
    }

    @Test
    @DisplayName("Test: Constructor vacío crea instancia correctamente")
    void testConstructorVacio() {
        assertNotNull(pedido);
        assertEquals(0, pedido.getIdPedido());
    }

    @Test
    @DisplayName("Test: Constructor completo inicializa todos los campos")
    void testConstructorCompleto() {
        LocalDate fecha = LocalDate.of(2024, 1, 15);
        Pedido p = new Pedido(1, fecha, 150.50, 10);

        assertEquals(1, p.getIdPedido());
        assertEquals(fecha, p.getFecha());
        assertEquals(150.50, p.getImporteTotal(), 0.01);
        assertEquals(10, p.getIdCliente());
    }

    @Test
    @DisplayName("Test: Setter de fecha válida funciona correctamente")
    void testSetFechaValida() {
        LocalDate fecha = LocalDate.of(2024, 1, 1);
        pedido.setFecha(fecha);
        assertEquals(fecha, pedido.getFecha());
    }

    @Test
    @DisplayName("Test: Setter de fecha actual es válido")
    void testSetFechaActual() {
        LocalDate hoy = LocalDate.now();
        assertDoesNotThrow(() -> pedido.setFecha(hoy));
        assertEquals(hoy, pedido.getFecha());
    }

    @Test
    @DisplayName("Test: Setter de fecha null lanza IllegalArgumentException")
    void testSetFechaNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> pedido.setFecha(null)
        );
        assertEquals("La fecha no puede ser nula", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de fecha futura lanza IllegalArgumentException")
    void testSetFechaFutura() {
        LocalDate fechaFutura = LocalDate.now().plusDays(1);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> pedido.setFecha(fechaFutura)
        );
        assertEquals("La fecha no puede ser futura", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de importe válido funciona correctamente")
    void testSetImporteTotalValido() {
        pedido.setImporteTotal(250.75);
        assertEquals(250.75, pedido.getImporteTotal(), 0.01);
    }

    @Test
    @DisplayName("Test: Setter de importe cero es válido")
    void testSetImporteTotalCero() {
        assertDoesNotThrow(() -> pedido.setImporteTotal(0.0));
        assertEquals(0.0, pedido.getImporteTotal(), 0.01);
    }

    @Test
    @DisplayName("Test: Setter de importe negativo lanza IllegalArgumentException")
    void testSetImporteTotalNegativo() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> pedido.setImporteTotal(-10.50)
        );
        assertEquals("El importe no puede ser negativo", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de importe excesivo lanza IllegalArgumentException")
    void testSetImporteTotalExcesivo() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> pedido.setImporteTotal(1000000.00)
        );
        assertEquals("El importe excede el máximo permitido", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de importe máximo permitido es válido")
    void testSetImporteTotalMaximo() {
        assertDoesNotThrow(() -> pedido.setImporteTotal(999999.99));
        assertEquals(999999.99, pedido.getImporteTotal(), 0.01);
    }

    @Test
    @DisplayName("Test: Setter de idCliente válido funciona correctamente")
    void testSetIdClienteValido() {
        pedido.setIdCliente(5);
        assertEquals(5, pedido.getIdCliente());
    }

    @Test
    @DisplayName("Test: Setter de idCliente cero o negativo lanza IllegalArgumentException")
    void testSetIdClienteInvalido() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> pedido.setIdCliente(0)
        );
        assertEquals("El ID de cliente debe ser positivo", exception.getMessage());

        exception = assertThrows(
            IllegalArgumentException.class,
            () -> pedido.setIdCliente(-1)
        );
        assertEquals("El ID de cliente debe ser positivo", exception.getMessage());
    }

    @Test
    @DisplayName("Test: toString retorna formato esperado")
    void testToString() {
        LocalDate fecha = LocalDate.of(2024, 1, 15);
        pedido.setIdPedido(1);
        pedido.setFecha(fecha);
        pedido.setImporteTotal(150.50);
        pedido.setIdCliente(10);

        String resultado = pedido.toString();
        assertTrue(resultado.contains("idPedido=1"));
        assertTrue(resultado.contains("fecha=2024-01-15"));
        assertTrue(resultado.contains("importeTotal=150.5"));
        assertTrue(resultado.contains("idCliente=10"));
    }
}
