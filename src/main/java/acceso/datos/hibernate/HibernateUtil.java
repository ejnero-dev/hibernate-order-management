package acceso.datos.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de utilidad para manejar la SessionFactory de Hibernate.
 * Implementa el patrón Singleton para garantizar una única instancia de SessionFactory.
 */
public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    /**
     * Constructor privado para evitar la instanciación directa.
     */
    private HibernateUtil() {
    }

    /**
     * Obtiene la única instancia de SessionFactory, creándola si no existe.
     *
     * @return SessionFactory una instancia de SessionFactory
     */
    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                logger.info("Inicializando SessionFactory de Hibernate");
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                sessionFactory = configuration.buildSessionFactory();
                logger.info("SessionFactory inicializada correctamente");
            } catch (Exception e) {
                logger.error("Error al inicializar SessionFactory", e);
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    /**
     * Cierra la SessionFactory.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            logger.info("Cerrando SessionFactory");
            sessionFactory.close();
            logger.info("SessionFactory cerrada correctamente");
        }
    }
}