package acceso.datos.factory;

import acceso.datos.dao.interfaces.ClienteDAO;
import acceso.datos.dao.interfaces.PedidoDAO;
import acceso.datos.dao.interfaces.ZonaEnvioDAO;
import acceso.datos.config.DatabaseConfig;
import acceso.datos.dao.impl.sqlite.SQLiteClienteDAO;
import acceso.datos.dao.impl.sqlite.SQLitePedidoDAO;
import acceso.datos.dao.impl.sqlite.SQLiteZonaEnvioDAO;

public class SQLiteDAOFactory extends DAOFactory {

    public SQLiteDAOFactory(DatabaseConfig databaseConfig) {
        super(databaseConfig);
    }

    @Override
    public ClienteDAO createClienteDAO() {
        return new SQLiteClienteDAO(databaseConfig);
    }

    @Override
    public PedidoDAO createPedidoDAO() {
        return new SQLitePedidoDAO(databaseConfig);
    }

    @Override
    public ZonaEnvioDAO createZonaEnvioDAO() {
        return new SQLiteZonaEnvioDAO(databaseConfig);
    }
}