package acceso.datos.dao.impl.sqlite;

import acceso.datos.config.DatabaseConfig;
import acceso.datos.dao.interfaces.PedidoDAO;
import acceso.datos.model.Pedido;
import acceso.datos.util.DatabaseException;
import acceso.datos.util.QueryUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa la interfaz {@link PedidoDAO} para interactuar con la base de datos SQLite.
 */
public class SQLitePedidoDAO implements PedidoDAO {
    private final DatabaseConfig databaseConfig;

    /**
     * Constructor de la clase.
     *
     * @param databaseConfig Configuración de la base de datos.
     */
    public SQLitePedidoDAO(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    /**
     * Inserta un pedido en la base de datos y asigna el ID generado.
     *
     * @param pedido Pedido a insertar.
     * @throws SQLException Si ocurre un error durante la inserción del pedido.
     */
    @Override
    public void insert(Pedido pedido) throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.INSERT_PEDIDO, 
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(pedido.getFecha()));
            stmt.setDouble(2, pedido.getImporteTotal());
            stmt.setInt(3, pedido.getIdCliente());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("La creación del pedido falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pedido.setIdPedido(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("La creación del pedido falló, no se obtuvo el ID.");
                }
            }
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
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.SELECT_PEDIDO_BY_ID)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPedidoFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Obtiene todos los pedidos de la base de datos.
     *
     * @return Lista de todos los pedidos.
     * @throws SQLException Si ocurre un error durante la obtención de los pedidos.
     */
    @Override
    public List<Pedido> getAll() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QueryUtils.SELECT_ALL_PEDIDOS)) {

            while (rs.next()) {
                pedidos.add(extractPedidoFromResultSet(rs));
            }
        }

        return pedidos;
    }

    /**
     * Actualiza un pedido en la base de datos.
     *
     * @param pedido Pedido a actualizar.
     * @throws SQLException Si ocurre un error durante la actualización del pedido.
     */
    @Override
    public void update(Pedido pedido) throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.UPDATE_PEDIDO)) {

            stmt.setDate(1, Date.valueOf(pedido.getFecha()));
            stmt.setDouble(2, pedido.getImporteTotal());
            stmt.setInt(3, pedido.getIdCliente());
            stmt.setInt(4, pedido.getIdPedido());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("La actualización del pedido falló, ninguna fila afectada.");
            }
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
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.DELETE_PEDIDO)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("El borrado del pedido falló, ninguna fila afectada.");
            }
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
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.SELECT_PEDIDOS_BY_CLIENTE)) {

            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(extractPedidoFromResultSet(rs));
                }
            }
        }

        return pedidos;
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
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.SELECT_PEDIDOS_BY_FECHA)) {

            stmt.setDate(1, Date.valueOf(fecha));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(extractPedidoFromResultSet(rs));
                }
            }
        }

        return pedidos;
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
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT SUM(importe_total) as total FROM Pedidos WHERE id_cliente = ?")) {

            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0.0;
    }

    /**
     * Extrae un pedido de un ResultSet.
     *
     * @param rs ResultSet que contiene los datos del pedido.
     * @return Pedido extraído.
     * @throws SQLException Si ocurre un error durante la extracción de los datos.
     */
    private Pedido extractPedidoFromResultSet(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("id_pedido"));
        pedido.setFecha(rs.getDate("fecha").toLocalDate());
        pedido.setImporteTotal(rs.getDouble("importe_total"));
        pedido.setIdCliente(rs.getInt("id_cliente"));
        return pedido;
    }
}