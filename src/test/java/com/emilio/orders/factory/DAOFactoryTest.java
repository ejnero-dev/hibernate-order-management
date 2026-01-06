package com.emilio.orders.factory;

import com.emilio.orders.config.DatabaseConfig;
import com.emilio.orders.config.DatabaseType;
import com.emilio.orders.dao.interfaces.ClienteDAO;
import com.emilio.orders.dao.interfaces.PedidoDAO;
import com.emilio.orders.dao.interfaces.ZonaEnvioDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase DAOFactory.
 * Verifica que la factoría crea correctamente los DAOs según el tipo de base de datos.
 */
class DAOFactoryTest {

    @Test
    @DisplayName("Test: getDAOFactory con SQLite retorna SQLiteDAOFactory")
    void testGetDAOFactorySQLite() {
        DatabaseConfig config = Mockito.mock(DatabaseConfig.class);
        DAOFactory factory = DAOFactory.getDAOFactory(DatabaseType.SQLITE, config);

        assertNotNull(factory);
        assertTrue(factory instanceof SQLiteDAOFactory);
    }

    @Test
    @DisplayName("Test: getDAOFactory con Hibernate retorna HibernateDAOFactory")
    void testGetDAOFactoryHibernate() {
        DatabaseConfig config = Mockito.mock(DatabaseConfig.class);
        DAOFactory factory = DAOFactory.getDAOFactory(DatabaseType.HIBERNATE, config);

        assertNotNull(factory);
        assertTrue(factory instanceof HibernateDAOFactory);
    }

    @Test
    @DisplayName("Test: getDAOFactory con MySQL lanza UnsupportedOperationException")
    void testGetDAOFactoryMySQL() {
        DatabaseConfig config = Mockito.mock(DatabaseConfig.class);

        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> DAOFactory.getDAOFactory(DatabaseType.MYSQL, config)
        );
        assertEquals("MySQL no implementado aún", exception.getMessage());
    }

    @Test
    @DisplayName("Test: getDAOFactory con PostgreSQL lanza UnsupportedOperationException")
    void testGetDAOFactoryPostgreSQL() {
        DatabaseConfig config = Mockito.mock(DatabaseConfig.class);

        UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class,
            () -> DAOFactory.getDAOFactory(DatabaseType.POSTGRESQL, config)
        );
        assertEquals("PostgreSQL no implementado aún", exception.getMessage());
    }

    @Test
    @DisplayName("Test: SQLiteDAOFactory crea ClienteDAO correctamente")
    void testSQLiteFactoryCreatesClienteDAO() {
        DatabaseConfig config = Mockito.mock(DatabaseConfig.class);
        DAOFactory factory = DAOFactory.getDAOFactory(DatabaseType.SQLITE, config);

        ClienteDAO clienteDAO = factory.createClienteDAO();
        assertNotNull(clienteDAO);
    }

    @Test
    @DisplayName("Test: SQLiteDAOFactory crea PedidoDAO correctamente")
    void testSQLiteFactoryCreatesPedidoDAO() {
        DatabaseConfig config = Mockito.mock(DatabaseConfig.class);
        DAOFactory factory = DAOFactory.getDAOFactory(DatabaseType.SQLITE, config);

        PedidoDAO pedidoDAO = factory.createPedidoDAO();
        assertNotNull(pedidoDAO);
    }

    @Test
    @DisplayName("Test: SQLiteDAOFactory crea ZonaEnvioDAO correctamente")
    void testSQLiteFactoryCreatesZonaEnvioDAO() {
        DatabaseConfig config = Mockito.mock(DatabaseConfig.class);
        DAOFactory factory = DAOFactory.getDAOFactory(DatabaseType.SQLITE, config);

        ZonaEnvioDAO zonaEnvioDAO = factory.createZonaEnvioDAO();
        assertNotNull(zonaEnvioDAO);
    }

    @Test
    @DisplayName("Test: HibernateDAOFactory crea ClienteDAO correctamente")
    void testHibernateFactoryCreatesClienteDAO() {
        DatabaseConfig config = Mockito.mock(DatabaseConfig.class);
        DAOFactory factory = DAOFactory.getDAOFactory(DatabaseType.HIBERNATE, config);

        ClienteDAO clienteDAO = factory.createClienteDAO();
        assertNotNull(clienteDAO);
    }

    @Test
    @DisplayName("Test: HibernateDAOFactory crea PedidoDAO correctamente")
    void testHibernateFactoryCreatesPedidoDAO() {
        DatabaseConfig config = Mockito.mock(DatabaseConfig.class);
        DAOFactory factory = DAOFactory.getDAOFactory(DatabaseType.HIBERNATE, config);

        PedidoDAO pedidoDAO = factory.createPedidoDAO();
        assertNotNull(pedidoDAO);
    }

    @Test
    @DisplayName("Test: HibernateDAOFactory crea ZonaEnvioDAO correctamente")
    void testHibernateFactoryCreatesZonaEnvioDAO() {
        DatabaseConfig config = Mockito.mock(DatabaseConfig.class);
        DAOFactory factory = DAOFactory.getDAOFactory(DatabaseType.HIBERNATE, config);

        ZonaEnvioDAO zonaEnvioDAO = factory.createZonaEnvioDAO();
        assertNotNull(zonaEnvioDAO);
    }
}
