package acceso.datos.dao.interfaces;

import acceso.datos.model.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    // Operaciones CRUD básicas
    void insert(Cliente cliente) throws SQLException;
    Cliente getById(int id) throws SQLException;
    List<Cliente> getAll() throws SQLException;
    void update(Cliente cliente) throws SQLException;
    void delete(int id) throws SQLException;
    
    // Operaciones específicas
    List<Cliente> getClientesByZona(int idZona) throws SQLException;
    double getTotalGastadoPorCliente(int idCliente) throws SQLException;
}