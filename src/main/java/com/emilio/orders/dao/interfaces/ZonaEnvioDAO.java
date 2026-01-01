package com.emilio.orders.dao.interfaces;

import com.emilio.orders.model.ZonaEnvio;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO (Data Access Object) para la entidad ZonaEnvio.
 * Proporciona métodos CRUD básicos para interactuar con la base de datos.
 */
public interface ZonaEnvioDAO {
    /**
     * Inserta una nueva zona de envío en la base de datos.
     *
     * @param zonaEnvio La zona de envío a insertar.
     * @throws SQLException Si ocurre un error en la operación de inserción.
     */
    void insert(ZonaEnvio zonaEnvio) throws SQLException;

    /**
     * Obtiene una zona de envío por su ID.
     *
     * @param id El ID de la zona de envío a obtener.
     * @return La zona de envío correspondiente.
     * @throws SQLException Si ocurre un error en la operación de consulta.
     */
    ZonaEnvio getById(int id) throws SQLException;

    /**
     * Obtiene una lista de todas las zonas de envío.
     *
     * @return Una lista de todas las zonas de envío.
     * @throws SQLException Si ocurre un error en la operación de consulta.
     */
    List<ZonaEnvio> getAll() throws SQLException;

    /**
     * Actualiza una zona de envío existente en la base de datos.
     *
     * @param zonaEnvio La zona de envío con los nuevos valores a actualizar.
     * @throws SQLException Si ocurre un error en la operación de actualización.
     */
    void update(ZonaEnvio zonaEnvio) throws SQLException;

    /**
     * Elimina una zona de envío por su ID.
     *
     * @param id El ID de la zona de envío a eliminar.
     * @throws SQLException Si ocurre un error en la operación de eliminación.
     */
    void delete(int id) throws SQLException;
}