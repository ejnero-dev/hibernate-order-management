package acceso.datos.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import acceso.datos.util.DatabaseException;

import javax.sql.DataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConfig implements DatabaseConfig {
    private static final int DEFAULT_MAX_POOL_SIZE = 10;
    private static final int DEFAULT_MIN_POOL_SIZE = 1;
    private final String url;
    private HikariDataSource dataSource;

    public SQLiteConfig(String url) {
        this.url = url;
        initializeDataSource();
    }

private void initializeDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:sqlite:" + url);
    config.setMaximumPoolSize(DEFAULT_MAX_POOL_SIZE);
    config.setMinimumIdle(DEFAULT_MIN_POOL_SIZE);
    config.setAutoCommit(true);
    
    // Configuraciones específicas de SQLite
    config.addDataSourceProperty("foreign_keys", "true");
    config.addDataSourceProperty("synchronous", "normal");
    config.addDataSourceProperty("journal_mode", "WAL");
    
    dataSource = new HikariDataSource(config);

    // Inicializar base de datos
    initializeDatabase();
}

private void initializeDatabase() {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
        
        // Leer el contenido del script SQL
        String sqlScript = new String(Files.readAllBytes(Paths.get("src/main/resources/pedidos.sql")));
        
        // Dividir el script en sentencias individuales
        String[] statements = sqlScript.split(";");
        
        for (String statement : statements) {
            statement = statement.trim();
            if (!statement.isEmpty()) {
                try {
                    // Añadir IF NOT EXISTS o usar operaciones que no fallen si ya existen
                    if (statement.startsWith("CREATE TABLE")) {
                        statement = statement.replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS");
                    }
                    stmt.execute(statement);
                } catch (SQLException e) {
                    // Ignorar errores de tablas ya existentes
                    System.out.println("Advertencia: " + e.getMessage());
                }
            }
        }
    } catch (SQLException | IOException e) {
        throw new DatabaseException("Error inicializando base de datos", e);
    }
}

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUsername() {
        return ""; // SQLite no usa autenticación
    }

    @Override
    public String getPassword() {
        return ""; // SQLite no usa autenticación
    }

    @Override
    public int getMaxPoolSize() {
        return DEFAULT_MAX_POOL_SIZE;
    }

    @Override
    public int getMinPoolSize() {
        return DEFAULT_MIN_POOL_SIZE;
    }
}