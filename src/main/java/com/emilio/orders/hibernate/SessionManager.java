package com.emilio.orders.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Clase para gestionar sesiones y transacciones de Hibernate.
 * Implementa el patrón Singleton para garantizar una única instancia de SessionFactory.
 */
public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static SessionManager instance;
    private final SessionFactory sessionFactory;

    /**
     * Constructor privado que inicializa la SessionFactory.
     */
    private SessionManager() {
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

    /**
     * Obtiene la única instancia de SessionManager.
     *
     * @return La instancia de SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Obtiene la SessionFactory.
     *
     * @return La SessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Abre una nueva sesión.
     *
     * @return Una nueva sesión de Hibernate
     */
    public Session openSession() {
        return sessionFactory.openSession();
    }

    /**
     * Ejecuta una operación dentro de una transacción y devuelve un resultado.
     *
     * @param operation La operación a ejecutar
     * @param <T> El tipo de resultado
     * @return El resultado de la operación
     */
    public <T> T executeWithResult(Function<Session, T> operation) {
        Transaction transaction = null;
        try (Session session = openSession()) {
            transaction = session.beginTransaction();
            T result = operation.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error durante la transacción", e);
            throw new RuntimeException("Error durante la transacción", e);
        }
    }

    /**
     * Ejecuta una operación dentro de una transacción sin devolver resultado.
     *
     * @param operation La operación a ejecutar
     */
    public void execute(Consumer<Session> operation) {
        Transaction transaction = null;
        try (Session session = openSession()) {
            transaction = session.beginTransaction();
            operation.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.error("Error durante la transacción", e);
            throw new RuntimeException("Error durante la transacción", e);
        }
    }

    /**
     * Cierra la SessionFactory.
     */
    public void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            logger.info("Cerrando SessionFactory");
            sessionFactory.close();
            logger.info("SessionFactory cerrada correctamente");
        }
    }
}