package acceso.datos.dao.impl.sqlite;

import acceso.datos.config.DatabaseConfig;
import acceso.datos.dao.interfaces.ClienteDAO;
import acceso.datos.model.Cliente;
import acceso.datos.util.DatabaseException;
import acceso.datos.util.QueryUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteClienteDAO implements ClienteDAO {
    private final DatabaseConfig databaseConfig;

    public SQLiteClienteDAO(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    @Override
    public void insert(Cliente cliente) throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.INSERT_CLIENTE, 
                     Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setInt(4, cliente.getIdZona());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("La creación del cliente falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("La creación del cliente falló, no se obtuvo el ID.");
                }
            }
        }
    }

    @Override
    public Cliente getById(int id) throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.SELECT_CLIENTE_BY_ID)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractClienteFromResultSet(rs);
                }
            }
        }
        return null;
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
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.DELETE_CLIENTE)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("El borrado del cliente falló, ninguna fila afectada.");
            }
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