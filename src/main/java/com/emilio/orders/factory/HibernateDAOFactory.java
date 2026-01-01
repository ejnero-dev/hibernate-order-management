package com.emilio.orders.factory;

import com.emilio.orders.config.DatabaseConfig;
import com.emilio.orders.dao.impl.hibernate.HibernateClienteDAO;
import com.emilio.orders.dao.impl.hibernate.HibernatePedidoDAO;
import com.emilio.orders.dao.impl.hibernate.HibernateZonaEnvioDAO;
import com.emilio.orders.dao.interfaces.ClienteDAO;
import com.emilio.orders.dao.interfaces.PedidoDAO;
import com.emilio.orders.dao.interfaces.ZonaEnvioDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Factoría para crear DAOs que utilizan Hibernate.
 */
public class HibernateDAOFactory extends DAOFactory {

    /**
     * Constructor que recibe una configuración de base de datos.
     *
     * @param databaseConfig Configuración de base de datos (no utilizada directamente en Hibernate)
     */
    public HibernateDAOFactory(DatabaseConfig databaseConfig) {
        super(databaseConfig);
    }

    /**
     * Crea una instancia de ClienteDAO que utiliza Hibernate.
     *
     * @return Una instancia de ClienteDAO para Hibernate
     */
    @Override
    public ClienteDAO createClienteDAO() {
        return new HibernateClienteDAO();
    }

    /**
     * Crea una instancia de PedidoDAO que utiliza Hibernate.
     *
     * @return Una instancia de PedidoDAO para Hibernate
     */
    @Override
    public PedidoDAO createPedidoDAO() {
        return new HibernatePedidoDAO();
    }

    /**
     * Crea una instancia de ZonaEnvioDAO que utiliza Hibernate.
     *
     * @return Una instancia de ZonaEnvioDAO para Hibernate
     */
    @Override
    public ZonaEnvioDAO createZonaEnvioDAO() {
        return new HibernateZonaEnvioDAO();
    }

    /**
     * Obtiene una conexión utilizando Hibernate.
     * Nota: En Hibernate generalmente se trabaja con sesiones en lugar de conexiones.
     * Este método se implementa para mantener la compatibilidad con la interfaz.
     *
     * @return Una conexión a la base de datos
     * @throws SQLException Si ocurre un error al obtener la conexión
     */
    @Override
    public Connection getConnection() throws SQLException {
        // Con Hibernate normalmente no necesitamos acceder directamente a la conexión JDBC
        // Pero implementamos este método para cumplir con la interfaz
        return databaseConfig.getConnection();
    }
}