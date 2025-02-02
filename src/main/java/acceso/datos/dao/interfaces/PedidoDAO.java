package acceso.datos.dao.interfaces;

import acceso.datos.model.Pedido;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;

public interface PedidoDAO {
    // Operaciones CRUD básicas
    void insert(Pedido pedido) throws SQLException;
    Pedido getById(int id) throws SQLException;
    List<Pedido> getAll() throws SQLException;
    void update(Pedido pedido) throws SQLException;
    void delete(int id) throws SQLException;
    
    // Operaciones específicas
    List<Pedido> getPedidosByCliente(int idCliente) throws SQLException;
    List<Pedido> getPedidosByFecha(LocalDate fecha) throws SQLException;
    double getTotalPedidosByCliente(int idCliente) throws SQLException;
}