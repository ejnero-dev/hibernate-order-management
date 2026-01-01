package com.emilio.orders.dao.interfaces;

import com.emilio.orders.model.Pedido;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad {@link Pedido}.
 */
public interface PedidoDAO {
    
    /**
     * Inserta un nuevo pedido en el sistema.
     *
     * @param pedido El objeto {@link Pedido} a insertar.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    void insert(Pedido pedido) throws SQLException;
    
    /**
     * Obtiene un pedido por su identificador único.
     *
     * @param id El identificador del pedido a obtener.
     * @return El objeto {@link Pedido} correspondiente o null si no se encuentra.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    Pedido getById(int id) throws SQLException;
    
    /**
     * Obtiene una lista de todos los pedidos en el sistema.
     *
     * @return Una lista de objetos {@link Pedido}.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    List<Pedido> getAll() throws SQLException;
    
    /**
     * Actualiza un pedido existente en el sistema.
     *
     * @param pedido El objeto {@link Pedido} con los datos actualizados.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    void update(Pedido pedido) throws SQLException;
    
    /**
     * Elimina un pedido por su identificador único.
     *
     * @param id El identificador del pedido a eliminar.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    void delete(int id) throws SQLException;
    
    /**
     * Obtiene una lista de pedidos asociados a un cliente específico.
     *
     * @param idCliente El identificador del cliente.
     * @return Una lista de objetos {@link Pedido}.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    List<Pedido> getPedidosByCliente(int idCliente) throws SQLException;
    
    /**
     * Obtiene una lista de pedidos que corresponden a una fecha específica.
     *
     * @param fecha La fecha deseada para los pedidos.
     * @return Una lista de objetos {@link Pedido}.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    List<Pedido> getPedidosByFecha(LocalDate fecha) throws SQLException;
    
    /**
     * Calcula el total de ventas por cliente.
     *
     * @param idCliente El identificador del cliente.
     * @return El monto total de las ventas realizadas al cliente.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    double getTotalPedidosByCliente(int idCliente) throws SQLException;
}