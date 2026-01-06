package com.emilio.orders.dao;

import com.emilio.orders.dao.interfaces.ClienteDAO;
import com.emilio.orders.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests para la interfaz ClienteDAO.
 * Usa mocks para verificar el comportamiento esperado sin depender de la base de datos.
 */
class ClienteDAOTest {

    @Mock
    private ClienteDAO clienteDAO;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente(1, "Juan Pérez", "juan@example.com", "612345678", 1);
    }

    @Test
    @DisplayName("Test: insert guarda un cliente correctamente")
    void testInsertCliente() throws SQLException {
        doNothing().when(clienteDAO).insert(cliente);

        assertDoesNotThrow(() -> clienteDAO.insert(cliente));
        verify(clienteDAO, times(1)).insert(cliente);
    }

    @Test
    @DisplayName("Test: getById retorna el cliente correcto")
    void testGetById() throws SQLException {
        when(clienteDAO.getById(1)).thenReturn(cliente);

        Cliente resultado = clienteDAO.getById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCliente());
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("juan@example.com", resultado.getEmail());
        verify(clienteDAO, times(1)).getById(1);
    }

    @Test
    @DisplayName("Test: getById retorna null cuando el cliente no existe")
    void testGetByIdNoExiste() throws SQLException {
        when(clienteDAO.getById(999)).thenReturn(null);

        Cliente resultado = clienteDAO.getById(999);

        assertNull(resultado);
        verify(clienteDAO, times(1)).getById(999);
    }

    @Test
    @DisplayName("Test: getAll retorna lista de clientes")
    void testGetAll() throws SQLException {
        Cliente cliente2 = new Cliente(2, "María García", "maria@example.com", "623456789", 1);
        List<Cliente> clientes = Arrays.asList(cliente, cliente2);

        when(clienteDAO.getAll()).thenReturn(clientes);

        List<Cliente> resultado = clienteDAO.getAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getNombre());
        assertEquals("María García", resultado.get(1).getNombre());
        verify(clienteDAO, times(1)).getAll();
    }

    @Test
    @DisplayName("Test: update actualiza un cliente correctamente")
    void testUpdateCliente() throws SQLException {
        cliente.setNombre("Juan Pérez Actualizado");
        doNothing().when(clienteDAO).update(cliente);

        assertDoesNotThrow(() -> clienteDAO.update(cliente));
        verify(clienteDAO, times(1)).update(cliente);
    }

    @Test
    @DisplayName("Test: delete elimina un cliente correctamente")
    void testDeleteCliente() throws SQLException {
        doNothing().when(clienteDAO).delete(1);

        assertDoesNotThrow(() -> clienteDAO.delete(1));
        verify(clienteDAO, times(1)).delete(1);
    }

    @Test
    @DisplayName("Test: getClientesByZona retorna clientes de una zona específica")
    void testGetClientesByZona() throws SQLException {
        Cliente cliente2 = new Cliente(2, "María García", "maria@example.com", "623456789", 1);
        List<Cliente> clientesZona1 = Arrays.asList(cliente, cliente2);

        when(clienteDAO.getClientesByZona(1)).thenReturn(clientesZona1);

        List<Cliente> resultado = clienteDAO.getClientesByZona(1);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(c -> c.getIdZona() == 1));
        verify(clienteDAO, times(1)).getClientesByZona(1);
    }

    @Test
    @DisplayName("Test: getTotalGastadoPorCliente retorna el total correcto")
    void testGetTotalGastadoPorCliente() throws SQLException {
        when(clienteDAO.getTotalGastadoPorCliente(1)).thenReturn(1500.75);

        double total = clienteDAO.getTotalGastadoPorCliente(1);

        assertEquals(1500.75, total, 0.01);
        verify(clienteDAO, times(1)).getTotalGastadoPorCliente(1);
    }

    @Test
    @DisplayName("Test: getTotalGastadoPorCliente retorna 0 si el cliente no tiene pedidos")
    void testGetTotalGastadoPorClienteSinPedidos() throws SQLException {
        when(clienteDAO.getTotalGastadoPorCliente(1)).thenReturn(0.0);

        double total = clienteDAO.getTotalGastadoPorCliente(1);

        assertEquals(0.0, total, 0.01);
        verify(clienteDAO, times(1)).getTotalGastadoPorCliente(1);
    }

    @Test
    @DisplayName("Test: insert lanza SQLException cuando hay error de base de datos")
    void testInsertConError() throws SQLException {
        doThrow(new SQLException("Error de conexión")).when(clienteDAO).insert(cliente);

        SQLException exception = assertThrows(
            SQLException.class,
            () -> clienteDAO.insert(cliente)
        );
        assertEquals("Error de conexión", exception.getMessage());
        verify(clienteDAO, times(1)).insert(cliente);
    }
}
