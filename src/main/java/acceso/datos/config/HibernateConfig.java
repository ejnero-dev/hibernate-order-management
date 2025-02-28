package acceso.datos.config;

import acceso.datos.hibernate.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Configuración de base de datos para Hibernate.
 * Esta clase implementa {@link DatabaseConfig} para mantener compatibilidad con la interfaz existente.
 */
public class HibernateConfig implements DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(HibernateConfig.class);
    private final String url;
    private final String username;
    private final String password;
    private final int maxPoolSize;
    private final int minPoolSize;
    private final SessionFactory sessionFactory;

    /**
     * Constructor que inicializa la configuración de Hibernate.
     *
     * @param url URL de la base de datos (usado solo para información)
     * @param username Nombre de usuario (puede ser vacío para SQLite)
     * @param password Contraseña (puede ser vacía para SQLite)
     * @param maxPoolSize Tamaño máximo del pool (no aplica directamente en Hibernate)
     * @param minPoolSize Tamaño mínimo del pool (no aplica directamente en Hibernate)
     */
    public HibernateConfig(String url, String username, String password, int maxPoolSize, int minPoolSize) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.maxPoolSize = maxPoolSize;
        this.minPoolSize = minPoolSize;
        
        // Inicializar la SessionFactory de Hibernate
        this.sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Configuración de Hibernate inicializada");
    }

    /**
     * Constructor simplificado para SQLite.
     *
     * @param url URL de la base de datos SQLite
     */
    public HibernateConfig(String url) {
        this(url, "", "", 10, 1);
    }

    /**
     * Obtiene una conexión a la base de datos utilizando Hibernate.
     * Nota: En Hibernate normalmente trabajamos con sesiones, 
     * pero proporcionamos este método para mantener compatibilidad con la interfaz.
     *
     * @return Una conexión JDBC obtenida de una sesión de Hibernate
     * @throws SQLException Si hay un error al obtener la conexión
     */
    @Override
    public Connection getConnection() throws SQLException {
        try {
            Session session = sessionFactory.openSession();
            return session.doReturningWork(connection -> connection);
        } catch (Exception e) {
            logger.error("Error al obtener conexión desde Hibernate", e);
            throw new SQLException("Error al obtener conexión desde Hibernate", e);
        }
    }

    /**
     * Este método no aplica directamente a Hibernate, 
     * pero se implementa para cumplir con la interfaz.
     *
     * @return null ya que Hibernate no utiliza DataSource directamente
     */
    @Override
    public DataSource getDataSource() {
        return null;
    }

    /**
     * Cierra la SessionFactory de Hibernate.
     */
    @Override
    public void closePool() {
        HibernateUtil.shutdown();
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    @Override
    public int getMinPoolSize() {
        return minPoolSize;
    }
}