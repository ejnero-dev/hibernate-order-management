package com.emilio.orders.ui;

/**
 * Interfaz que define los métodos necesarios para la interacción con el usuario
 */
public interface UI {
    /**
     * Inicia la interfaz de usuario y muestra el menú principal
     */
    void iniciar();

    /**
     * Muestra un mensaje informativo al usuario
     * @param mensaje El mensaje a mostrar
     */
    void mostrarMensaje(String mensaje);

    /**
     * Muestra un mensaje de error al usuario
     * @param mensaje El mensaje de error a mostrar
     */
    void mostrarError(String mensaje);

    /**
     * Muestra el menú principal y obtiene la selección del usuario
     * @return La opción seleccionada por el usuario
     */
    int mostrarMenu();

    /**
     * Gestiona todas las operaciones relacionadas con clientes
     * (crear, modificar, eliminar, listar)
     */
    void gestionarClientes();

    /**
     * Gestiona todas las operaciones relacionadas con pedidos
     * (crear, modificar, eliminar, listar)
     */
    void gestionarPedidos();

    /**
     * Muestra información sobre las zonas de envío disponibles
     */
    void consultarZonasEnvio() throws Exception;

    /**
     * Consulta y muestra los pedidos de un cliente específico
     */
    void consultarPedidosCliente() throws Exception;
}