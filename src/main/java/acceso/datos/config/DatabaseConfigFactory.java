package acceso.datos.config;

public class DatabaseConfigFactory {
    public static DatabaseConfig createConfig(DatabaseType type, DatabaseProperties properties) {
        return switch (type) {
            case SQLITE -> createSQLiteConfig(properties);
            case MYSQL -> createMySQLConfig(properties);
            case POSTGRESQL -> createPostgreSQLConfig(properties);
        };
    }

    private static DatabaseConfig createSQLiteConfig(DatabaseProperties properties) {
        return new SQLiteConfig(properties.getUrl());
    }

    private static DatabaseConfig createMySQLConfig(DatabaseProperties properties) {
        throw new UnsupportedOperationException("MySQL not implemented yet");
    }

    private static DatabaseConfig createPostgreSQLConfig(DatabaseProperties properties) {
        throw new UnsupportedOperationException("PostgreSQL not implemented yet");
    }
}