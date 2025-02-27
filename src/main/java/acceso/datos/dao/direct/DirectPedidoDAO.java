package acceso.datos.dao.direct;

import acceso.datos.dao.interfaces.PedidoDAO;
import acceso.datos.hibernate.SessionManager;
import acceso.datos.model.Pedido;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementación de PedidoDAO que utiliza directamente la API de Hibernate.
 */
public class DirectPedidoDAO implements PedidoDAO {
    private static final Logger logger = LoggerFactory.getLogger(DirectPedidoDAO.class);
    private final SessionManager sessionManager;

    /**
     * Constructor que inicializa el DAO.
     */
    public DirectPedidoDAO() {
        this.sessionManager = SessionManager.getInstance();
        logger.debug("DirectPedidoDAO inicializado");
    }

    /**
     * Inserta un pedido en la base de datos y asigna el ID generado.
     *
     * @param pedido Pedido a insertar.
     * @throws SQLException Si ocurre un error durante la inserción del pedido.
     */
    @Override
    public void insert(Pedido pedido) throws SQLException {
        try {
            sessionManager.execute(session -> {
                logger.debug("Insertando pedido para cliente con ID: {}", pedido.getIdCliente());
                session.persist(pedido);
                logger.info("Pedido insertado con ID: {}", pedido.getIdPedido());
            });
        } catch (Exception e) {
            logger.error("Error al insertar pedido", e);
            throw new SQLException("Error al insertar pedido: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene un pedido por su ID.
     *
     * @param id Identificador del pedido a obtener.
     * @return Pedido correspondiente al ID proporcionado o null si no existe.
     * @throws SQLException Si ocurre un error durante la obtención del pedido.
     */
    @Override
    public Pedido getById(int id) throws SQLException {
        try {
            return sessionManager.executeWithResult(session -> {
                logger.debug("Buscando pedido con ID: {}", id);
                Pedido pedido = session.get(Pedido.class, id);
                if (pedido != null) {
                    logger.debug("Pedido encontrado con ID: {}", id);
                } else {
                    logger.debug("No se encontró pedido con ID: {}", id);
                }
                return pedido;
            });
        } catch (Exception e) {
            logger.error("Error al buscar pedido por ID", e);
            throw new SQLException("Error al buscar pedido: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todos los pedidos de la base de datos.
     *
     * @return Lista de todos los pedidos.
     * @throws SQLException Si ocurre un error durante la obtención de los pedidos.
     */
    @Override
    public List<Pedido> getAll() throws SQLException {
        try {
            return sessionManager.executeWithResult(session -> {
                logger.debug("Obteniendo todos los pedidos");
                Query<Pedido> query = session.createQuery("FROM Pedido", Pedido.class);
                List<Pedido> pedidos = query.getResultList();
                logger.debug("Se encontraron {} pedidos", pedidos.size());
                return pedidos;
            });
        } catch (Exception e) {
            logger.error("Error al obtener todos los pedidos", e);
            throw new SQLException("Error al obtener pedidos: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza un pedido en la base de datos.
     *
     * @param pedido Pedido a actualizar.
     * @throws SQLException Si ocurre un error durante la actualización del pedido.
     */
    @Override
    public void update(Pedido pedido) throws SQLException {
        try {
            sessionManager.execute(session -> {
                logger.debug("Actualizando pedido con ID: {}", pedido.getIdPedido());
                session.merge(pedido);
                logger.info("Pedido actualizado con ID: {}", pedido.getIdPedido());
            });
        } catch (Exception e) {
            logger.error("Error al actualizar pedido", e);
            throw new SQLException("Error al actualizar pedido: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un pedido por su ID.
     *
     * @param id Identificador del pedido a eliminar.
     * @throws SQLException Si ocurre un error durante la eliminación del pedido.
     */
    @Override
    public void delete(int id) throws SQLException {
        try {
            sessionManager.execute(session -> {
                logger.debug("Eliminando pedido con ID: {}", id);
                Pedido pedido = session.get(Pedido.class, id);
                if (pedido != null) {
                    session.remove(pedido);
                    logger.info("Pedido eliminado con ID: {}", id);
                } else {
                    logger.warn("No se encontró pedido para eliminar con ID: {}", id);
                    throw new RuntimeException("El pedido con ID " + id + " no existe");
                }
            });
        } catch (RuntimeException e) {
            logger.error("Error al eliminar pedido", e);
            throw new SQLException("Error al eliminar pedido: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todos los pedidos de un cliente específico.
     *
     * @param idCliente Identificador del cliente cuyos pedidos se quieren obtener.
     * @return Lista de pedidos del cliente especificado.
     * @throws SQLException Si ocurre un error durante la obtención de los pedidos.
     */
    @Override
    public List<Pedido> getPedidosByCliente(int idCliente) throws SQLException {
        try {
            return sessionManager.executeWithResult(session -> {
                logger.debug("Buscando pedidos para cliente con ID: {}", idCliente);
                Query<Pedido> query = session.createQuery(
                        "FROM Pedido WHERE idCliente = :idCliente", Pedido.class);
                query.setParameter("idCliente", idCliente);
                List<Pedido> pedidos = query.getResultList();
                logger.debug("Se encontraron {} pedidos para el cliente {}", pedidos.size(), idCliente);
                return pedidos;
            });
        } catch (Exception e) {
            logger.error("Error al buscar pedidos por cliente", e);
            throw new SQLException("Error al buscar pedidos por cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todos los pedidos realizados en una fecha específica.
     *
     * @param fecha Fecha cuyos pedidos se quieren obtener.
     * @return Lista de pedidos realizados en la fecha especificada.
     * @throws SQLException Si ocurre un error durante la obtención de los pedidos.
     */
    @Override
    public List<Pedido> getPedidosByFecha(LocalDate fecha) throws SQLException {
        try {
            return sessionManager.executeWithResult(session -> {
                logger.debug("Buscando pedidos para la fecha: {}", fecha);
                Query<Pedido> query = session.createQuery(
                        "FROM Pedido WHERE fecha = :fecha", Pedido.class);
                query.setParameter("fecha", fecha);
                List<Pedido> pedidos = query.getResultList();
                logger.debug("Se encontraron {} pedidos para la fecha {}", pedidos.size(), fecha);
                return pedidos;
            });
        } catch (Exception e) {
            logger.error("Error al buscar pedidos por fecha", e);
            throw new SQLException("Error al buscar pedidos por fecha: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el total de importe de los pedidos realizados por un cliente específico.
     *
     * @param idCliente Identificador del cliente cuyos pedidos se quieren sumar.
     * @return Total de importe de los pedidos del cliente especificado.
     * @throws SQLException Si ocurre un error durante la obtención de los datos.
     */
    @Override
    public double getTotalPedidosByCliente(int idCliente) throws SQLException {
        try {
            return sessionManager.executeWithResult(session -> {
                logger.debug("Calculando total de pedidos para cliente con ID: {}", idCliente);
                Query<Double> query = session.createQuery(
                        "SELECT SUM(p.importeTotal) FROM Pedido p WHERE p.idCliente = :idCliente", Double.class);
                query.setParameter("idCliente", idCliente);
                Double total = query.uniqueResult();
                double result = (total != null) ? total : 0.0;
                logger.debug("Total de pedidos para cliente {}: {}", idCliente, result);
                return result;
            });
        } catch (Exception e) {
            logger.error("Error al calcular total de pedidos por cliente", e);
            throw new SQLException("Error al calcular total de pedidos: " + e.getMessage(), e);
        }
    }
}