package acceso.datos.factory;

import java.sql.Connection;
import java.sql.SQLException;

import acceso.datos.config.DatabaseConfig;
import acceso.datos.config.DatabaseType;
import acceso.datos.dao.interfaces.ClienteDAO;
import acceso.datos.dao.interfaces.PedidoDAO;
import acceso.datos.dao.interfaces.ZonaEnvioDAO;

public abstract class DAOFactory {
    protected DatabaseConfig databaseConfig;

    protected DAOFactory(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public static DAOFactory getDAOFactory(DatabaseType dbType, DatabaseConfig config) {
        return switch (dbType) {
            case SQLITE -> new SQLiteDAOFactory(config);
            case MYSQL -> throw new UnsupportedOperationException("MySQL no implementado aún");
            case POSTGRESQL -> throw new UnsupportedOperationException("PostgreSQL no implementado aún");
        };
    }

    // Métodos abstractos para crear los DAOs
    public abstract ClienteDAO createClienteDAO();

    public abstract PedidoDAO createPedidoDAO();

    public abstract ZonaEnvioDAO createZonaEnvioDAO();

    // Método para obtener la conexión
    public Connection getConnection() throws SQLException {
        return databaseConfig.getConnection();
    }
}