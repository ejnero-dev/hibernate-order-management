package com.emilio.orders.factory;

import java.sql.Connection;
import java.sql.SQLException;

import com.emilio.orders.config.DatabaseConfig;
import com.emilio.orders.config.DatabaseType;
import com.emilio.orders.dao.interfaces.ClienteDAO;
import com.emilio.orders.dao.interfaces.PedidoDAO;
import com.emilio.orders.dao.interfaces.ZonaEnvioDAO;

/**
 * Clase abstracta DAOFactory que proporciona una factoría para crear diferentes DAOs.
 */
public abstract class DAOFactory {
    /**
     * Configuración de la base de datos.
     */
    protected DatabaseConfig databaseConfig;

    /**
     * Constructor protegido para inicializar la configuración de la base de datos.
     *
     * @param databaseConfig La configuración de la base de datos.
     */
    protected DAOFactory(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    /**
     * Método estático que devuelve una instancia de {@link DAOFactory} según el tipo de base de datos especificado.
     *
     * @param dbType Tipo de base de datos.
     * @param config Configuración de la base de datos.
     * @return Una instancia de {@link DAOFactory}.
     */
    public static DAOFactory getDAOFactory(DatabaseType dbType, DatabaseConfig config) {
        return switch (dbType) {
            case SQLITE -> new SQLiteDAOFactory(config);
            case MYSQL -> throw new UnsupportedOperationException("MySQL no implementado aún");
            case POSTGRESQL -> throw new UnsupportedOperationException("PostgreSQL no implementado aún");
            case HIBERNATE -> new HibernateDAOFactory(config);
        };
    }

    /**
     * Método abstracto para crear un {@link ClienteDAO}.
     *
     * @return Un nuevo {@link ClienteDAO}.
     */
    public abstract ClienteDAO createClienteDAO();

    /**
     * Método abstracto para crear un {@link PedidoDAO}.
     *
     * @return Un nuevo {@link PedidoDAO}.
     */
    public abstract PedidoDAO createPedidoDAO();

    /**
     * Método abstracto para crear un {@link ZonaEnvioDAO}.
     *
     * @return Un nuevo {@link ZonaEnvioDAO}.
     */
    public abstract ZonaEnvioDAO createZonaEnvioDAO();

    /**
     * Método que devuelve una conexión a la base de datos.
     *
     * @return Una instancia de {@link Connection}.
     * @throws SQLException Si ocurre un error al obtener la conexión.
     */
    public Connection getConnection() throws SQLException {
        return databaseConfig.getConnection();
    }
}