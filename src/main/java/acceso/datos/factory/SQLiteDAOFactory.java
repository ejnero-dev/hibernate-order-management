package acceso.datos.factory;

import acceso.datos.dao.interfaces.ClienteDAO;
import acceso.datos.dao.interfaces.PedidoDAO;
import acceso.datos.dao.interfaces.ZonaEnvioDAO;
import acceso.datos.config.DatabaseConfig;
import acceso.datos.dao.impl.sqlite.SQLiteClienteDAO;
import acceso.datos.dao.impl.sqlite.SQLitePedidoDAO;
import acceso.datos.dao.impl.sqlite.SQLiteZonaEnvioDAO;

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