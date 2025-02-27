package acceso.datos.config;

/**
 * Factoría para crear configuraciones de base de datos según el tipo especificado.
 */
public class DatabaseConfigFactory {
    /**
     * Crea una configuración de base de datos según el tipo proporcionado y las propiedades dadas.
     *
     * @param type         Tipo de la base de datos (SQLITE, MYSQL, POSTGRESQL, HIBERNATE).
     * @param properties   Propiedades necesarias para configurar la base de datos.
     * @return Una instancia de {@link DatabaseConfig} configurada según el tipo especificado.
     */
    public static DatabaseConfig createConfig(DatabaseType type, DatabaseProperties properties) {
        return switch (type) {
            case SQLITE -> createSQLiteConfig(properties);
            case MYSQL -> createMySQLConfig(properties);
            case POSTGRESQL -> createPostgreSQLConfig(properties);
            case HIBERNATE -> createHibernateConfig(properties);
        };
    }

    /**
     * Crea una configuración de base de datos para SQLite.
     *
     * @param properties Propiedades necesarias para configurar la base de datos de SQLite.
     * @return Una instancia de {@link DatabaseConfig} configurada para SQLite.
     */
    private static DatabaseConfig createSQLiteConfig(DatabaseProperties properties) {
        return new SQLiteConfig(properties.getUrl());
    }

    /**
     * Crea una configuración de base de datos para MySQL. Actualmente no está implementado.
     *
     * @param properties Propiedades necesarias para configurar la base de datos de MySQL.
     * @return Una instancia de {@link DatabaseConfig} configurada para MySQL.
     */
    private static DatabaseConfig createMySQLConfig(DatabaseProperties properties) {
        throw new UnsupportedOperationException("MySQL not implemented yet");
    }

    /**
     * Crea una configuración de base de datos para PostgreSQL. Actualmente no está implementado.
     *
     * @param properties Propiedades necesarias para configurar la base de datos de PostgreSQL.
     * @return Una instancia de {@link DatabaseConfig} configurada para PostgreSQL.
     */
    private static DatabaseConfig createPostgreSQLConfig(DatabaseProperties properties) {
        throw new UnsupportedOperationException("PostgreSQL not implemented yet");
    }
    
    /**
     * Crea una configuración de base de datos para Hibernate.
     *
     * @param properties Propiedades necesarias para configurar la base de datos con Hibernate.
     * @return Una instancia de {@link DatabaseConfig} configurada para Hibernate.
     */
    private static DatabaseConfig createHibernateConfig(DatabaseProperties properties) {
        return new HibernateConfig(
            properties.getUrl(),
            properties.getUsername(),
            properties.getPassword(),
            properties.getMaxPoolSize(),
            properties.getMinPoolSize()
        );
    }
}