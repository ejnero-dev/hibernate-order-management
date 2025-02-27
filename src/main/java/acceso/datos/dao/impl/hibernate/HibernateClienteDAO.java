package acceso.datos.dao.impl.hibernate;

import acceso.datos.dao.interfaces.ClienteDAO;
import acceso.datos.model.Cliente;
import acceso.datos.hibernate.HibernateUtil;
import acceso.datos.util.DatabaseException;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Implementación de ClienteDAO que utiliza Hibernate para acceder a la base de datos.
 */
public class HibernateClienteDAO implements ClienteDAO {
    private static final Logger logger = LoggerFactory.getLogger(HibernateClienteDAO.class);

    /**
     * Inserta un nuevo cliente en la base de datos.
     *
     * @param cliente Cliente a insertar
     * @throws SQLException Si ocurre algún error al ejecutar el SQL
     */
    @Override
    public void insert(Cliente cliente) throws SQLException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            logger.debug("Insertando cliente: {}", cliente.getNombre());
            session.persist(cliente);
            transaction.commit();
            logger.info("Cliente insertado con ID: {}", cliente.getIdCliente());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al insertar cliente", e);
            throw new SQLException("Error al insertar cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param id ID del cliente
     * @return Cliente encontrado o null si no se encuentra
     * @throws SQLException Si ocurre algún error al ejecutar el SQL
     */
    @Override
    public Cliente getById(int id) throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            logger.debug("Buscando cliente con ID: {}", id);
            Cliente cliente = session.get(Cliente.class, id);
            if (cliente != null) {
                logger.debug("Cliente encontrado: {}", cliente.getNombre());
            } else {
                logger.debug("No se encontró cliente con ID: {}", id);
            }
            return cliente;
        } catch (Exception e) {
            logger.error("Error al buscar cliente por ID", e);
            throw new SQLException("Error al buscar cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todos los clientes almacenados en la base de datos.
     *
     * @return Lista de clientes
     * @throws SQLException Si ocurre algún error al ejecutar el SQL
     */
    @Override
    public List<Cliente> getAll() throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            logger.debug("Obteniendo todos los clientes");
            Query<Cliente> query = session.createQuery("FROM Cliente", Cliente.class);
            List<Cliente> clientes = query.getResultList();
            logger.debug("Se encontraron {} clientes", clientes.size());
            return clientes;
        } catch (Exception e) {
            logger.error("Error al obtener todos los clientes", e);
            throw new SQLException("Error al obtener clientes: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza la información de un cliente en la base de datos.
     *
     * @param cliente Cliente con los nuevos datos
     * @throws SQLException Si ocurre algún error al ejecutar el SQL
     */
    @Override
    public void update(Cliente cliente) throws SQLException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            logger.debug("Actualizando cliente con ID: {}", cliente.getIdCliente());
            session.merge(cliente);
            transaction.commit();
            logger.info("Cliente actualizado: {}", cliente.getNombre());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al actualizar cliente", e);
            throw new SQLException("Error al actualizar cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un cliente de la base de datos por su ID.
     *
     * @param id ID del cliente a eliminar
     * @throws SQLException Si ocurre algún error al ejecutar el SQL
     */
    @Override
    public void delete(int id) throws SQLException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            logger.debug("Eliminando cliente con ID: {}", id);
            
            Cliente cliente = session.get(Cliente.class, id);
            if (cliente != null) {
                session.remove(cliente);
                transaction.commit();
                logger.info("Cliente eliminado con ID: {}", id);
            } else {
                transaction.rollback();
                logger.warn("No se encontró cliente para eliminar con ID: {}", id);
                throw new DatabaseException("El cliente con ID " + id + " no existe");
            }
        } catch (DatabaseException e) {
            throw new SQLException(e.getMessage(), e);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error al eliminar cliente", e);
            throw new SQLException("Error al eliminar cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene una lista de clientes que pertenecen a una zona específica.
     *
     * @param idZona ID de la zona
     * @return Lista de clientes
     * @throws SQLException Si ocurre algún error al ejecutar el SQL
     */
    @Override
    public List<Cliente> getClientesByZona(int idZona) throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            logger.debug("Buscando clientes en zona con ID: {}", idZona);
            Query<Cliente> query = session.createQuery(
                    "FROM Cliente WHERE idZona = :idZona", Cliente.class);
            query.setParameter("idZona", idZona);
            List<Cliente> clientes = query.getResultList();
            logger.debug("Se encontraron {} clientes en la zona {}", clientes.size(), idZona);
            return clientes;
        } catch (Exception e) {
            logger.error("Error al buscar clientes por zona", e);
            throw new SQLException("Error al buscar clientes por zona: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el total gastado por un cliente.
     *
     * @param idCliente ID del cliente
     * @return Total gastado por el cliente
     * @throws SQLException Si ocurre algún error al ejecutar el SQL
     */
    @Override
    public double getTotalGastadoPorCliente(int idCliente) throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            logger.debug("Calculando total gastado por cliente con ID: {}", idCliente);
            Query<Double> query = session.createQuery(
                    "SELECT SUM(p.importeTotal) FROM Pedido p WHERE p.idCliente = :idCliente", Double.class);
            query.setParameter("idCliente", idCliente);
            Double total = query.uniqueResult();
            double result = (total != null) ? total : 0.0;
            logger.debug("Total gastado por cliente {}: {}", idCliente, result);
            return result;
        } catch (Exception e) {
            logger.error("Error al calcular total gastado por cliente", e);
            throw new SQLException("Error al calcular total gastado: " + e.getMessage(), e);
        }
    }
}