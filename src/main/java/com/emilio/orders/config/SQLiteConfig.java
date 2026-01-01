package com.emilio.orders.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import com.emilio.orders.util.DatabaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Configuración de la base de datos SQLite utilizando HikariCP.
 * Esta clase maneja la configuración, inicialización y conexión a una base de datos SQLite.
 */
public class SQLiteConfig implements DatabaseConfig {
    private static final int DEFAULT_MAX_POOL_SIZE = 10; // Tamaño máximo del pool de conexiones
    private static final int DEFAULT_MIN_POOL_SIZE = 1; // Tamaño mínimo del pool de conexiones
    private final String url; // URL de la base de datos SQLite
    private HikariDataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(SQLiteConfig.class); // Logger para registro

    /**
     * Constructor que inicializa la configuración de la base de datos con la URL proporcionada.
     *
     * @param url URL de la base de datos SQLite
     */
    public SQLiteConfig(String url) {
        this.url = url;
        initializeDataSource(); // Inicializa el pool de conexiones y la base de datos
    }

    /**
     * Método que inicializa el pool de conexiones y configura las propiedades específicas de SQLite.
     */
    private void initializeDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + url); // Configura la URL de la base de datos
        config.setMaximumPoolSize(DEFAULT_MAX_POOL_SIZE); // Establece el tamaño máximo del pool
        config.setMinimumIdle(DEFAULT_MIN_POOL_SIZE); // Establece el tamaño mínimo del pool
        config.setAutoCommit(true); // Habilita el autocommit

        // Configuraciones específicas de SQLite
        config.addDataSourceProperty("foreign_keys", "true"); // Activa las claves foráneas
        config.addDataSourceProperty("synchronous", "normal"); // Configura la sincronización normal
        config.addDataSourceProperty("journal_mode", "WAL"); // Establece el modo de registro WAL

        dataSource = new HikariDataSource(config); // Crea la instancia del pool de conexiones

        // Inicializa la base de datos
        initializeDatabase();
    }

    /**
     * Método que inicializa la base de datos ejecutando un script SQL.
     */
    private void initializeDatabase() {
        logger.info("Iniciando inicialización de base de datos SQLite");
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            logger.debug("Leyendo script SQL de inicialización");
            String sqlScript = new String(Files.readAllBytes(Paths.get("src/main/resources/pedidos.sql"))); // Lee el script SQL

            String[] statements = sqlScript.split(";"); // Divide el script en instrucciones individuales

            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    try {
                        if (statement.startsWith("CREATE TABLE")) {
                            logger.debug("Ejecutando creación de tabla: {}", 
                                statement.substring(0, statement.indexOf("(")).trim());
                            statement = statement.replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS"); // Asegura que la tabla exista
                        }
                        stmt.execute(statement); // Ejecuta la instrucción SQL
                    } catch (SQLException e) {
                        logger.warn("Advertencia al ejecutar statement: {}", e.getMessage()); // Registra advertencias de errores
                    }
                }
            }
            logger.info("Base de datos inicializada correctamente");
        } catch (SQLException | IOException e) {
            logger.error("Error fatal al inicializar base de datos", e); // Registra errores fatales
            throw new DatabaseException("Error inicializando base de datos", e);
        }
    }

    /**
     * Obtiene una conexión a la base de datos.
     *
     * @return Una instancia de {@link Connection} para interactuar con la base de datos
     * @throws SQLException Si ocurre un error al obtener la conexión
     */
    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Obtiene el objeto {@link DataSource}.
     *
     * @return Instancia del {@link DataSource}
     */
    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Cierra el pool de conexiones.
     */
    @Override
    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    /**
     * Obtiene la URL de la base de datos.
     *
     * @return URL de la base de datos
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * Obtiene el nombre de usuario (no aplica para SQLite).
     *
     * @return Nombre de usuario, siempre vacío
     */
    @Override
    public String getUsername() {
        return ""; // SQLite no usa autenticación
    }

    /**
     * Obtiene la contraseña (no aplica para SQLite).
     *
     * @return Contraseña, siempre vacía
     */
    @Override
    public String getPassword() {
        return ""; // SQLite no usa autenticación
    }

    /**
     * Obtiene el tamaño máximo del pool de conexiones.
     *
     * @return Tamaño máximo del pool
     */
    @Override
    public int getMaxPoolSize() {
        return DEFAULT_MAX_POOL_SIZE;
    }

    /**
     * Obtiene el tamaño mínimo del pool de conexiones.
     *
     * @return Tamaño mínimo del pool
     */
    @Override
    public int getMinPoolSize() {
        return DEFAULT_MIN_POOL_SIZE;
    }
}