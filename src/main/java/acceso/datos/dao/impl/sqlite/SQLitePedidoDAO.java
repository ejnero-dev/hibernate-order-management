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

public class SQLitePedidoDAO implements PedidoDAO {
    private final DatabaseConfig databaseConfig;

    public SQLitePedidoDAO(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

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

    // Método auxiliar para extraer un pedido de un ResultSet
    private Pedido extractPedidoFromResultSet(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(rs.getInt("id_pedido"));
        pedido.setFecha(rs.getDate("fecha").toLocalDate());
        pedido.setImporteTotal(rs.getDouble("importe_total"));
        pedido.setIdCliente(rs.getInt("id_cliente"));
        return pedido;
    }
}