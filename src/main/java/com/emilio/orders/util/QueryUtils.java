package com.emilio.orders.util;

/**
 * Clase utils para manejar las consultas SQL.
 */
public class QueryUtils {
    // Consultas para ZonaEnvio
    public static final String INSERT_ZONA = 
        "INSERT INTO Zonas_Envio (nombre_zona, tarifa_envio) VALUES (?, ?)";
    public static final String SELECT_ZONA_BY_ID = 
        "SELECT * FROM Zonas_Envio WHERE id_zona = ?";
    public static final String SELECT_ALL_ZONAS = 
        "SELECT * FROM Zonas_Envio";
    public static final String UPDATE_ZONA = 
        "UPDATE Zonas_Envio SET nombre_zona = ?, tarifa_envio = ? WHERE id_zona = ?";
    public static final String DELETE_ZONA = 
        "DELETE FROM Zonas_Envio WHERE id_zona = ?";

    // Consultas para Cliente
    public static final String INSERT_CLIENTE = 
        "INSERT INTO Clientes (nombre, email, telefono, id_zona) VALUES (?, ?, ?, ?)";
    public static final String SELECT_CLIENTE_BY_ID = 
        "SELECT * FROM Clientes WHERE id_cliente = ?";
    public static final String SELECT_ALL_CLIENTES = 
        "SELECT * FROM Clientes";
    public static final String UPDATE_CLIENTE = 
        "UPDATE Clientes SET nombre = ?, email = ?, telefono = ?, id_zona = ? WHERE id_cliente = ?";
    public static final String DELETE_CLIENTE = 
        "DELETE FROM Clientes WHERE id_cliente = ?";
    public static final String SELECT_CLIENTES_BY_ZONA = 
        "SELECT * FROM Clientes WHERE id_zona = ?";
    public static final String SELECT_TOTAL_CLIENTE = 
        "SELECT SUM(importe_total) as total FROM Pedidos WHERE id_cliente = ?";

    // Consultas para Pedido
    public static final String INSERT_PEDIDO = 
        "INSERT INTO Pedidos (fecha, importe_total, id_cliente) VALUES (?, ?, ?)";
    public static final String SELECT_PEDIDO_BY_ID = 
        "SELECT * FROM Pedidos WHERE id_pedido = ?";
    public static final String SELECT_ALL_PEDIDOS = 
        "SELECT * FROM Pedidos";
    public static final String UPDATE_PEDIDO = 
        "UPDATE Pedidos SET fecha = ?, importe_total = ?, id_cliente = ? WHERE id_pedido = ?";
    public static final String DELETE_PEDIDO = 
        "DELETE FROM Pedidos WHERE id_pedido = ?";
    public static final String SELECT_PEDIDOS_BY_CLIENTE = 
        "SELECT * FROM Pedidos WHERE id_cliente = ?";
    public static final String SELECT_PEDIDOS_BY_FECHA = 
        "SELECT * FROM Pedidos WHERE fecha = ?";
}