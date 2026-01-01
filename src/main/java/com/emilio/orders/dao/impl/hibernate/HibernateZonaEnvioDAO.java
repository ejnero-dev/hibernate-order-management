package com.emilio.orders.dao.impl.hibernate;

import com.emilio.orders.dao.interfaces.ZonaEnvioDAO;
import com.emilio.orders.model.ZonaEnvio;
import com.emilio.orders.hibernate.SessionManager;
import com.emilio.orders.util.DatabaseException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementación de ZonaEnvioDAO que utiliza Hibernate para acceder a la base de datos.
 */
public class HibernateZonaEnvioDAO implements ZonaEnvioDAO {
    private static final Logger logger = LoggerFactory.getLogger(HibernateZonaEnvioDAO.class);

    /**
     * Inserta una nueva zona en la base de datos.
     *
     * @param zonaEnvio La zona a insertar
     * @throws SQLException Si ocurre un error durante la operación
     */
    @Override
    public void insert(ZonaEnvio zonaEnvio) throws SQLException {
        Transaction transaction = null;
        try (Session session = SessionManager.getInstance().openSession()) {
            transaction = session.beginTransaction();
            logger.debug("Insertando zona de envío: {}", zonaEnvio.getNombreZona());
            session.persist(zonaEnvio);
            transaction.commit();
            logger.info("Zona de envío insertada con ID: {}", zonaEnvio.getIdZona());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al insertar zona de envío", e);
            throw new SQLException("Error al insertar zona de envío: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene una zona por su identificador.
     *
     * @param id El identificador de la zona
     * @return La zona correspondiente o null si no se encuentra
     * @throws SQLException Si ocurre un error durante la operación
     */
    @Override
    public ZonaEnvio getById(int id) throws SQLException {
        try (Session session = SessionManager.getInstance().openSession()) {
            logger.debug("Buscando zona de envío con ID: {}", id);
            ZonaEnvio zona = session.get(ZonaEnvio.class, id);
            if (zona != null) {
                logger.debug("Zona de envío encontrada: {}", zona.getNombreZona());
            } else {
                logger.debug("No se encontró zona de envío con ID: {}", id);
            }
            return zona;
        } catch (Exception e) {
            logger.error("Error al buscar zona de envío por ID", e);
            throw new SQLException("Error al buscar zona de envío: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todas las zonas en la base de datos.
     *
     * @return La lista de zonas
     * @throws SQLException Si ocurre un error durante la operación
     */
    @Override
    public List<ZonaEnvio> getAll() throws SQLException {
        try (Session session = SessionManager.getInstance().openSession()) {
            logger.debug("Obteniendo todas las zonas de envío");
            Query<ZonaEnvio> query = session.createQuery("FROM ZonaEnvio", ZonaEnvio.class);
            List<ZonaEnvio> zonas = query.getResultList();
            logger.debug("Se encontraron {} zonas de envío", zonas.size());
            return zonas;
        } catch (Exception e) {
            logger.error("Error al obtener todas las zonas de envío", e);
            throw new SQLException("Error al obtener zonas de envío: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza una zona en la base de datos.
     *
     * @param zonaEnvio La zona a actualizar
     * @throws SQLException Si ocurre un error durante la operación
     */
    @Override
    public void update(ZonaEnvio zonaEnvio) throws SQLException {
        Transaction transaction = null;
        try (Session session = SessionManager.getInstance().openSession()) {
            transaction = session.beginTransaction();
            logger.debug("Actualizando zona de envío con ID: {}", zonaEnvio.getIdZona());
            session.merge(zonaEnvio);
            transaction.commit();
            logger.info("Zona de envío actualizada: {}", zonaEnvio.getNombreZona());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al actualizar zona de envío", e);
            throw new SQLException("Error al actualizar zona de envío: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una zona por su identificador.
     *
     * @param id El identificador de la zona a eliminar
     * @throws SQLException Si ocurre un error durante la operación
     */
    @Override
    public void delete(int id) throws SQLException {
        Transaction transaction = null;
        try (Session session = SessionManager.getInstance().openSession()) {
            transaction = session.beginTransaction();
            logger.debug("Eliminando zona de envío con ID: {}", id);
            
            ZonaEnvio zona = session.get(ZonaEnvio.class, id);
            if (zona != null) {
                session.remove(zona);
                transaction.commit();
                logger.info("Zona de envío eliminada con ID: {}", id);
            } else {
                transaction.rollback();
                logger.warn("No se encontró zona de envío para eliminar con ID: {}", id);
                throw new DatabaseException("La zona de envío con ID " + id + " no existe");
            }
        } catch (DatabaseException e) {
            throw new SQLException(e.getMessage(), e);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al eliminar zona de envío", e);
            throw new SQLException("Error al eliminar zona de envío: " + e.getMessage(), e);
        }
    }
}