package com.emilio.orders.factory;

import com.emilio.orders.dao.interfaces.ClienteDAO;
import com.emilio.orders.dao.interfaces.PedidoDAO;
import com.emilio.orders.dao.interfaces.ZonaEnvioDAO;
import com.emilio.orders.config.DatabaseConfig;
import com.emilio.orders.dao.impl.sqlite.SQLiteClienteDAO;
import com.emilio.orders.dao.impl.sqlite.SQLitePedidoDAO;
import com.emilio.orders.dao.impl.sqlite.SQLiteZonaEnvioDAO;

/**
 * Clase de fábrica que crea instancias de DAO para SQLite.
 */
public class SQLiteDAOFactory extends DAOFactory {

    /**
     * Constructor de la clase SQLiteDAOFactory.
     *
     * @param databaseConfig La configuración de la base de datos.
     */
    public SQLiteDAOFactory(DatabaseConfig databaseConfig) {
        super(databaseConfig);
    }

    /**
     * Crea una instancia del DAO para clientes usando SQLite.
     *
     * @return Una instancia de {@link ClienteDAO}.
     */
    @Override
    public ClienteDAO createClienteDAO() {
        return new SQLiteClienteDAO(databaseConfig);
    }

    /**
     * Crea una instancia del DAO para pedidos usando SQLite.
     *
     * @return Una instancia de {@link PedidoDAO}.
     */
    @Override
    public PedidoDAO createPedidoDAO() {
        return new SQLitePedidoDAO(databaseConfig);
    }

    /**
     * Crea una instancia del DAO para zonas de envío usando SQLite.
     *
     * @return Una instancia de {@link ZonaEnvioDAO}.
     */
    @Override
    public ZonaEnvioDAO createZonaEnvioDAO() {
        return new SQLiteZonaEnvioDAO(databaseConfig);
    }
}