package acceso.datos.config;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConfig {
    // Métodos principales que toda configuración debe implementar
    Connection getConnection() throws SQLException;
    DataSource getDataSource();
    void closePool();
    
    // Propiedades básicas que toda base de datos necesita
    String getUrl();
    String getUsername();
    String getPassword();
    int getMaxPoolSize();
    int getMinPoolSize();
    
    // Método de fábrica estático
    static DatabaseConfig forType(DatabaseType type, String url, String username, String password) {
        return switch (type) {
            case SQLITE -> new SQLiteConfig(url);
            case MYSQL -> throw new UnsupportedOperationException("MySQL not implemented yet");
            case POSTGRESQL -> throw new UnsupportedOperationException("PostgreSQL not implemented yet");
        };
    }
}