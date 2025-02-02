package acceso.datos.dao.impl.sqlite;

import acceso.datos.config.DatabaseConfig;
import acceso.datos.dao.interfaces.ZonaEnvioDAO;
import acceso.datos.model.ZonaEnvio;
import acceso.datos.util.DatabaseException;
import acceso.datos.util.QueryUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa la interfaz ZonaEnvioDAO para interactuar con una base de datos SQLite.
 */
public class SQLiteZonaEnvioDAO implements ZonaEnvioDAO {
    private final DatabaseConfig databaseConfig;

    /**
     * Constructor que recibe una instancia de DatabaseConfig.
     *
     * @param databaseConfig Configuración de la base de datos
     */
    public SQLiteZonaEnvioDAO(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    /**
     * Inserta una nueva zona en la base de datos.
     *
     * @param zonaEnvio La zona a insertar
     * @throws SQLException Si ocurre un error durante la inserción
     */
    @Override
    public void insert(ZonaEnvio zonaEnvio) throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.INSERT_ZONA, 
                     Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, zonaEnvio.getNombreZona());
            stmt.setDouble(2, zonaEnvio.getTarifaEnvio());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("La creación de la zona falló, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    zonaEnvio.setIdZona(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("La creación de la zona falló, no se obtuvo el ID.");
                }
            }
        }
    }

    /**
     * Obtiene una zona por su identificador.
     *
     * @param id El identificador de la zona
     * @return La zona correspondiente o null si no se encuentra
     * @throws SQLException Si ocurre un error durante la consulta
     */
    @Override
    public ZonaEnvio getById(int id) throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.SELECT_ZONA_BY_ID)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractZonaFromResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Obtiene todas las zonas en la base de datos.
     *
     * @return La lista de zonas
     * @throws SQLException Si ocurre un error durante la consulta
     */
    @Override
    public List<ZonaEnvio> getAll() throws SQLException {
        List<ZonaEnvio> zonas = new ArrayList<>();
        
        try (Connection conn = databaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(QueryUtils.SELECT_ALL_ZONAS)) {
            
            while (rs.next()) {
                zonas.add(extractZonaFromResultSet(rs));
            }
        }
        
        return zonas;
    }

    /**
     * Actualiza una zona en la base de datos.
     *
     * @param zonaEnvio La zona a actualizar
     * @throws SQLException Si ocurre un error durante la actualización
     */
    @Override
    public void update(ZonaEnvio zonaEnvio) throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.UPDATE_ZONA)) {
            
            stmt.setString(1, zonaEnvio.getNombreZona());
            stmt.setDouble(2, zonaEnvio.getTarifaEnvio());
            stmt.setInt(3, zonaEnvio.getIdZona());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("La actualización de la zona falló, ninguna fila afectada.");
            }
        }
    }

    /**
     * Elimina una zona por su identificador.
     *
     * @param id El identificador de la zona a eliminar
     * @throws SQLException Si ocurre un error durante el borrado
     */
    @Override
    public void delete(int id) throws SQLException {
        try (Connection conn = databaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QueryUtils.DELETE_ZONA)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("El borrado de la zona falló, ninguna fila afectada.");
            }
        }
    }

    /**
     * Extrae una zona desde un ResultSet.
     *
     * @param rs El ResultSet del que extraer la zona
     * @return La zona extraída
     * @throws SQLException Si ocurre un error durante la extracción
     */
    private ZonaEnvio extractZonaFromResultSet(ResultSet rs) throws SQLException {
        ZonaEnvio zona = new ZonaEnvio();
        zona.setIdZona(rs.getInt("id_zona"));
        zona.setNombreZona(rs.getString("nombre_zona"));
        zona.setTarifaEnvio(rs.getDouble("tarifa_envio"));
        return zona;
    }
}