package acceso.datos.dao.interfaces;

import acceso.datos.model.ZonaEnvio;
import java.sql.SQLException;
import java.util.List;

public interface ZonaEnvioDAO {
    // Operaciones CRUD básicas
    void insert(ZonaEnvio zonaEnvio) throws SQLException;
    ZonaEnvio getById(int id) throws SQLException;
    List<ZonaEnvio> getAll() throws SQLException;
    void update(ZonaEnvio zonaEnvio) throws SQLException;
    void delete(int id) throws SQLException;
}