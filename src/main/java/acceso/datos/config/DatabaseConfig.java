package acceso.datos.config;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interfaz que define los métodos y propiedades comunes para configuraciones de bases de datos.
 */
public interface DatabaseConfig {

    /**
     * Obtiene una conexión a la base de datos.
     *
     * @return Una conexión a la base de datos o lanzará una excepción si hay un problema.
     * @throws SQLException Si no se puede obtener una conexión.
     */
    Connection getConnection() throws SQLException;

    /**
     * Obtiene el objeto DataSource utilizado para gestionar las conexiones a la base de datos.
     *
     * @return Un objeto DataSource que gestiona las conexiones.
     */
    DataSource getDataSource();

    /**
     * Cierra el pool de conexiones.
     */
    void closePool();

    /**
     * Obtiene la URL de conexión para esta configuración de base de datos.
     *
     * @return La URL de conexión.
     */
    String getUrl();

    /**
     * Obtiene el nombre de usuario utilizado para autenticarse en la base de datos.
     *
     * @return El nombre de usuario.
     */
    String getUsername();

    /**
     * Obtiene la contraseña utilizada para autenticarse en la base de datos.
     *
     * @return La contraseña.
     */
    String getPassword();

    /**
     * Obtiene el tamaño máximo del pool de conexiones.
     *
     * @return El tamaño máximo del pool de conexiones.
     */
    int getMaxPoolSize();

    /**
     * Obtiene el tamaño mínimo del pool de conexiones.
     *
     * @return El tamaño mínimo del pool de conexiones.
     */
    int getMinPoolSize();

    /**
     * Método estático que crea una instancia de configuración de base de datos según el tipo especificado.
     *
     * @param type El tipo de base de datos (SQLITE, MYSQL, POSTGRESQL, HIBERNATE).
     * @param url La URL de conexión a la base de datos.
     * @param username El nombre de usuario para autenticarse en la base de datos.
     * @param password La contraseña para autenticarse en la base de datos.
     * @return Una instancia de configuración de base de datos correspondiente al tipo especificado, o lanza una excepción si el tipo no está soportado.
     */
    static DatabaseConfig forType(DatabaseType type, String url, String username, String password) {
        return switch (type) {
            case SQLITE -> new SQLiteConfig(url);
            case MYSQL -> throw new UnsupportedOperationException("MySQL not implemented yet");
            case POSTGRESQL -> throw new UnsupportedOperationException("PostgreSQL not implemented yet");
            case HIBERNATE -> new HibernateConfig(url, username, password, 10, 1);
        };
    }
}