/**
 * Interfaz que define las operaciones CRUD básicas y específicas para la entidad Cliente.
 */
package com.emilio.orders.dao.interfaces;

import com.emilio.orders.model.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    /**
     * Inserta un nuevo cliente en la base de datos.
     *
     * @param cliente El cliente a insertar
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL
     */
    void insert(Cliente cliente) throws SQLException;

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id El ID del cliente
     * @return El cliente correspondiente al ID proporcionado, o null si no se encuentra
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL
     */
    Cliente getById(int id) throws SQLException;

    /**
     * Obtiene todos los clientes registrados en la base de datos.
     *
     * @return Una lista de todos los clientes
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL
     */
    List<Cliente> getAll() throws SQLException;

    /**
     * Actualiza la información de un cliente existente en la base de datos.
     *
     * @param cliente El cliente actualizado con nuevos datos
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL
     */
    void update(Cliente cliente) throws SQLException;

    /**
     * Elimina un cliente de la base de datos por su ID.
     *
     * @param id El ID del cliente a eliminar
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL
     */
    void delete(int id) throws SQLException;

    /**
     * Obtiene una lista de clientes que pertenecen a una zona específica.
     *
     * @param idZona El ID de la zona
     * @return Una lista de clientes en la zona especificada
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL
     */
    List<Cliente> getClientesByZona(int idZona) throws SQLException;

    /**
     * Calcula el total gastado por un cliente específico.
     *
     * @param idCliente El ID del cliente
     * @return La suma total de las compras realizadas por el cliente
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL
     */
    double getTotalGastadoPorCliente(int idCliente) throws SQLException;
}