package acceso.datos.dao.impl.sqlite;

import acceso.datos.config.DatabaseConfig;
import acceso.datos.dao.interfaces.ClienteDAO;
import acceso.datos.model.Cliente;
import acceso.datos.util.DatabaseException;
import acceso.datos.util.QueryUtils;
import acceso.datos.util.TransactionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLiteClienteDAO implements ClienteDAO {
    private static final Logger logger = LoggerFactory.getLogger(SQLiteClienteDAO.class);
    private final DatabaseConfig databaseConfig;

    public SQLiteClienteDAO(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
        logger.debug("SQLiteClienteDAO inicializado");
    }

    @Override
    public void insert(Cliente cliente) throws SQLException {
        logger.debug("Intentando insertar cliente: {}", cliente.getNombre());
        try (Connection conn = databaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(QueryUtils.INSERT_CLIENTE,
                        Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setInt(4, cliente.getIdZona());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                logger.error("Fallo al crear cliente: ninguna fila afectada");
                throw new DatabaseException("La creación del cliente falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getInt(1));
                    logger.info("Cliente insertado correctamente con ID: {}", cliente.getIdCliente());
                } else {
                    logger.error("Fallo al crear cliente: no se obtuvo ID");
                    throw new DatabaseException("La creación del cliente falló, no se obtuvo el ID.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error al insertar cliente: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Cliente getById(int id) throws SQLException {
        logger.debug("Buscando cliente con ID: {}", id);
        try (Connection conn = databaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(QueryUtils.SELECT_CLIENTE_BY_ID)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = extractClienteFromResultSet(rs);
                    logger.debug("Cliente encontrado: {}", cliente.getNombre());
                    return cliente;
                }
            }
            logger.debug("No se encontró cliente con ID: {}", id);
            return null;
        } catch (SQLException e) {
            logger.error("Error al buscar cliente por ID: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Cliente> getAll() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(QueryUtils.SELECT_ALL_CLIENTES)) {

            while (rs.next()) {
                clientes.add(extractClienteFromResultSet(rs));
            }
        }

        return clientes;
    }

    @Override
    public void update(Cliente cliente) throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(QueryUtils.UPDATE_CLIENTE)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setInt(4, cliente.getIdZona());
            stmt.setInt(5, cliente.getIdCliente());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("La actualización del cliente falló, ninguna fila afectada.");
            }
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (Connection conn = databaseConfig.getConnection()) {
            TransactionUtils.executeInTransaction(conn, connection -> {
                try (PreparedStatement stmt = connection.prepareStatement(QueryUtils.DELETE_CLIENTE)) {
                    stmt.setInt(1, id);
                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows == 0) {
                        throw new DatabaseException("El borrado del cliente falló, ninguna fila afectada.");
                    }
                }
            });
        }
    }

    @Override
    public List<Cliente> getClientesByZona(int idZona) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = databaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(QueryUtils.SELECT_CLIENTES_BY_ZONA)) {

            stmt.setInt(1, idZona);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(extractClienteFromResultSet(rs));
                }
            }
        }

        return clientes;
    }

    @Override
    public double getTotalGastadoPorCliente(int idCliente) throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
                PreparedStatement stmt = conn.prepareStatement(QueryUtils.SELECT_TOTAL_CLIENTE)) {

            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0.0;
    }

    // Método auxiliar para extraer un cliente de un ResultSet
    private Cliente extractClienteFromResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("id_cliente"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setIdZona(rs.getInt("id_zona"));
        return cliente;
    }
}