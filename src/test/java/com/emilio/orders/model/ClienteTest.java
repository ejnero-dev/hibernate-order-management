package com.emilio.orders.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Cliente.
 * Verifica la creación, getters/setters y validaciones del modelo.
 */
class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
    }

    @Test
    @DisplayName("Test: Constructor vacío crea instancia correctamente")
    void testConstructorVacio() {
        assertNotNull(cliente);
        assertEquals(0, cliente.getIdCliente());
    }

    @Test
    @DisplayName("Test: Constructor completo inicializa todos los campos")
    void testConstructorCompleto() {
        Cliente c = new Cliente(1, "Juan Pérez", "juan@example.com", "612345678", 1);

        assertEquals(1, c.getIdCliente());
        assertEquals("Juan Pérez", c.getNombre());
        assertEquals("juan@example.com", c.getEmail());
        assertEquals("612345678", c.getTelefono());
        assertEquals(1, c.getIdZona());
    }

    @Test
    @DisplayName("Test: Setter de nombre válido funciona correctamente")
    void testSetNombreValido() {
        cliente.setNombre("María García");
        assertEquals("María García", cliente.getNombre());
    }

    @Test
    @DisplayName("Test: Setter de nombre elimina espacios extras")
    void testSetNombreEliminaEspacios() {
        cliente.setNombre("  Pedro López  ");
        assertEquals("Pedro López", cliente.getNombre());
    }

    @Test
    @DisplayName("Test: Setter de nombre null lanza IllegalArgumentException")
    void testSetNombreNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cliente.setNombre(null)
        );
        assertEquals("El nombre no puede estar vacío", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de nombre vacío lanza IllegalArgumentException")
    void testSetNombreVacio() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cliente.setNombre("   ")
        );
        assertEquals("El nombre no puede estar vacío", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de nombre muy largo lanza IllegalArgumentException")
    void testSetNombreMuyLargo() {
        String nombreLargo = "a".repeat(101);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cliente.setNombre(nombreLargo)
        );
        assertEquals("El nombre no puede exceder 100 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de email válido funciona correctamente")
    void testSetEmailValido() {
        cliente.setEmail("test@example.com");
        assertEquals("test@example.com", cliente.getEmail());
    }

    @Test
    @DisplayName("Test: Setter de email null lanza IllegalArgumentException")
    void testSetEmailNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cliente.setEmail(null)
        );
        assertEquals("El email no puede estar vacío", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de email con formato inválido lanza IllegalArgumentException")
    void testSetEmailFormatoInvalido() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cliente.setEmail("correo-invalido")
        );
        assertEquals("Formato de email inválido", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de teléfono válido funciona correctamente")
    void testSetTelefonoValido() {
        cliente.setTelefono("612345678");
        assertEquals("612345678", cliente.getTelefono());
    }

    @Test
    @DisplayName("Test: Setter de teléfono con formato inválido lanza IllegalArgumentException")
    void testSetTelefonoInvalido() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cliente.setTelefono("12345")
        );
        assertEquals("El teléfono debe tener 9 dígitos", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de teléfono con letras lanza IllegalArgumentException")
    void testSetTelefonoConLetras() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cliente.setTelefono("abc123456")
        );
        assertEquals("El teléfono debe tener 9 dígitos", exception.getMessage());
    }

    @Test
    @DisplayName("Test: Setter de teléfono null es válido")
    void testSetTelefonoNull() {
        assertDoesNotThrow(() -> cliente.setTelefono(null));
        assertNull(cliente.getTelefono());
    }

    @Test
    @DisplayName("Test: Setter de idZona válido funciona correctamente")
    void testSetIdZonaValido() {
        cliente.setIdZona(5);
        assertEquals(5, cliente.getIdZona());
    }

    @Test
    @DisplayName("Test: Setter de idZona cero o negativo lanza IllegalArgumentException")
    void testSetIdZonaInvalido() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> cliente.setIdZona(0)
        );
        assertEquals("El ID de zona debe ser positivo", exception.getMessage());

        exception = assertThrows(
            IllegalArgumentException.class,
            () -> cliente.setIdZona(-1)
        );
        assertEquals("El ID de zona debe ser positivo", exception.getMessage());
    }

    @Test
    @DisplayName("Test: toString retorna formato esperado")
    void testToString() {
        cliente.setNombre("Ana Martínez");
        cliente.setEmail("ana@example.com");

        String expected = "Ana Martínez (ana@example.com)";
        assertEquals(expected, cliente.toString());
    }
}
