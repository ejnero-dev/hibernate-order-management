package acceso.datos.ui;

import acceso.datos.dao.interfaces.ClienteDAO;
import acceso.datos.dao.interfaces.PedidoDAO;
import acceso.datos.dao.interfaces.ZonaEnvioDAO;
import acceso.datos.model.Cliente;
import acceso.datos.model.Pedido;
import acceso.datos.model.ZonaEnvio;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GraphicalUI implements UI {
    private SwingMenuBuilder gui;
    private final ClienteDAO clienteDAO;
    private final PedidoDAO pedidoDAO;
    private final ZonaEnvioDAO zonaEnvioDAO;
    private volatile int selectedOption = -1;
    private final Object lock = new Object();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public GraphicalUI(ClienteDAO clienteDAO, PedidoDAO pedidoDAO, ZonaEnvioDAO zonaEnvioDAO) {
        this.clienteDAO = clienteDAO;
        this.pedidoDAO = pedidoDAO;
        this.zonaEnvioDAO = zonaEnvioDAO;
        this.gui = new SwingMenuBuilder("Sistema de Gestión de Pedidos");
        configureGUI();
    }

    private void configureGUI() {
        gui.addMenuOption("Gestionar Clientes", () -> {
            gui.setStatus(SwingMenuBuilder.STATUS_PROCESSING);
            synchronized (lock) {
                selectedOption = 1;
                lock.notify();
            }
        });

        gui.addMenuOption("Gestionar Pedidos", () -> {
            gui.setStatus(SwingMenuBuilder.STATUS_PROCESSING);
            synchronized (lock) {
                selectedOption = 2;
                lock.notify();
            }
        });

        gui.addMenuOption("Consultar Zonas de Envío", () -> {
            gui.setStatus(SwingMenuBuilder.STATUS_PROCESSING);
            synchronized (lock) {
                selectedOption = 3;
                lock.notify();
            }
        });

        gui.addMenuOption("Consultar Pedidos de Cliente", () -> {
            gui.setStatus(SwingMenuBuilder.STATUS_PROCESSING);
            synchronized (lock) {
                selectedOption = 4;
                lock.notify();
            }
        });

        gui.addMenuOption("Salir", () -> {
            gui.setStatus(SwingMenuBuilder.STATUS_CLOSING);
            synchronized (lock) {
                selectedOption = 0;
                lock.notify();
            }
        });
    }

    @Override
    public void iniciar() {
        SwingUtilities.invokeLater(() -> {
            gui.setStatus(SwingMenuBuilder.STATUS_READY);
            gui.displayGUI();
        });

        boolean continuar = true;
        while (continuar) {
            int opcion = mostrarMenu();
            try {
                switch (opcion) {
                    case 1 -> gestionarClientes();
                    case 2 -> gestionarPedidos();
                    case 3 -> consultarZonasEnvio();
                    case 4 -> consultarPedidosCliente();
                    case 0 -> continuar = false;
                    default -> mostrarError("Opción no válida");
                }
            } catch (Exception e) {
                mostrarError("Error: " + e.getMessage());
            }
        }
    }

    @Override
    public int mostrarMenu() {
        synchronized (lock) {
            selectedOption = -1;
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return selectedOption;
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            SwingMenuBuilder.showMessageDialog(mensaje);
            gui.setStatus(SwingMenuBuilder.STATUS_READY);
        });
    }

    @Override
    public void mostrarError(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            SwingMenuBuilder.showErrorDialog(mensaje);
            gui.setStatus(SwingMenuBuilder.STATUS_READY);
        });
    }

    @Override
    public void gestionarClientes() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton nuevoButton = new JButton("Nuevo Cliente");
        JButton modificarButton = new JButton("Modificar Cliente");
        JButton eliminarButton = new JButton("Eliminar Cliente");
        JButton listarButton = new JButton("Listar Clientes");
        JButton volverButton = new JButton("Volver");

        panel.add(nuevoButton);
        panel.add(modificarButton);
        panel.add(eliminarButton);
        panel.add(listarButton);
        panel.add(volverButton);

        JDialog dialog = new JDialog(gui, "Gestión de Clientes", true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(gui);

        nuevoButton.addActionListener(e -> {
            dialog.setVisible(false);
            nuevoCliente();
        });

        modificarButton.addActionListener(e -> {
            dialog.setVisible(false);
            modificarCliente();
        });

        eliminarButton.addActionListener(e -> {
            dialog.setVisible(false);
            eliminarCliente();
        });

        listarButton.addActionListener(e -> {
            dialog.setVisible(false);
            listarClientes();
        });

        volverButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void nuevoCliente() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nombre:"));
        JTextField nombreField = new JTextField();
        panel.add(nombreField);

        panel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Teléfono:"));
        JTextField telefonoField = new JTextField();
        panel.add(telefonoField);

        panel.add(new JLabel("Zona:"));
        JComboBox<ZonaEnvio> zonaCombo = new JComboBox<>();
        try {
            List<ZonaEnvio> zonas = zonaEnvioDAO.getAll();
            for (ZonaEnvio zona : zonas) {
                zonaCombo.addItem(zona);
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar las zonas: " + ex.getMessage());
            return;
        }
        panel.add(zonaCombo);

        int result = JOptionPane.showConfirmDialog(gui, panel, "Nuevo Cliente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Cliente cliente = new Cliente();
                cliente.setNombre(nombreField.getText());
                cliente.setEmail(emailField.getText());
                cliente.setTelefono(telefonoField.getText());
                cliente.setIdZona(((ZonaEnvio) zonaCombo.getSelectedItem()).getIdZona());

                clienteDAO.insert(cliente);
                mostrarMensaje("Cliente creado correctamente");
            } catch (SQLException ex) {
                mostrarError("Error al crear el cliente: " + ex.getMessage());
            }
        }
    }

    private void modificarCliente() {
        // Primero seleccionar el cliente a modificar
        JComboBox<Cliente> clienteCombo = new JComboBox<>();
        try {
            List<Cliente> clientes = clienteDAO.getAll();
            for (Cliente cliente : clientes) {
                clienteCombo.addItem(cliente);
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar los clientes: " + ex.getMessage());
            return;
        }

        int seleccion = JOptionPane.showConfirmDialog(gui, clienteCombo, "Seleccione Cliente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (seleccion == JOptionPane.OK_OPTION) {
            Cliente clienteSeleccionado = (Cliente) clienteCombo.getSelectedItem();

            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));

            panel.add(new JLabel("Nombre:"));
            JTextField nombreField = new JTextField(clienteSeleccionado.getNombre());
            panel.add(nombreField);

            panel.add(new JLabel("Email:"));
            JTextField emailField = new JTextField(clienteSeleccionado.getEmail());
            panel.add(emailField);

            panel.add(new JLabel("Teléfono:"));
            JTextField telefonoField = new JTextField(clienteSeleccionado.getTelefono());
            panel.add(telefonoField);

            panel.add(new JLabel("Zona:"));
            JComboBox<ZonaEnvio> zonaCombo = new JComboBox<>();
            try {
                List<ZonaEnvio> zonas = zonaEnvioDAO.getAll();
                for (ZonaEnvio zona : zonas) {
                    zonaCombo.addItem(zona);
                    if (zona.getIdZona() == clienteSeleccionado.getIdZona()) {
                        zonaCombo.setSelectedItem(zona);
                    }
                }
            } catch (SQLException ex) {
                mostrarError("Error al cargar las zonas: " + ex.getMessage());
                return;
            }
            panel.add(zonaCombo);

            int result = JOptionPane.showConfirmDialog(gui, panel, "Modificar Cliente",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    clienteSeleccionado.setNombre(nombreField.getText());
                    clienteSeleccionado.setEmail(emailField.getText());
                    clienteSeleccionado.setTelefono(telefonoField.getText());
                    clienteSeleccionado.setIdZona(((ZonaEnvio) zonaCombo.getSelectedItem()).getIdZona());

                    clienteDAO.update(clienteSeleccionado);
                    mostrarMensaje("Cliente modificado correctamente");
                } catch (SQLException ex) {
                    mostrarError("Error al modificar el cliente: " + ex.getMessage());
                }
            }
        }
    }

    private void eliminarCliente() {
        JComboBox<Cliente> clienteCombo = new JComboBox<>();
        try {
            List<Cliente> clientes = clienteDAO.getAll();
            for (Cliente cliente : clientes) {
                clienteCombo.addItem(cliente);
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar los clientes: " + ex.getMessage());
            return;
        }

        int seleccion = JOptionPane.showConfirmDialog(gui, clienteCombo, "Seleccione Cliente a Eliminar",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (seleccion == JOptionPane.OK_OPTION) {
            Cliente clienteSeleccionado = (Cliente) clienteCombo.getSelectedItem();

            int confirmacion = JOptionPane.showConfirmDialog(gui,
                    "¿Está seguro de eliminar al cliente " + clienteSeleccionado.getNombre() + "?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    clienteDAO.delete(clienteSeleccionado.getIdCliente());
                    mostrarMensaje("Cliente eliminado correctamente");
                } catch (SQLException ex) {
                    mostrarError("Error al eliminar el cliente: " + ex.getMessage());
                }
            }
        }
    }

    private void listarClientes() {
        try {
            List<Cliente> clientes = clienteDAO.getAll();
            StringBuilder sb = new StringBuilder();
            sb.append("LISTADO DE CLIENTES\n");
            sb.append("===================\n\n");

            for (Cliente cliente : clientes) {
                sb.append(String.format("ID: %d\n", cliente.getIdCliente()));
                sb.append(String.format("Nombre: %s\n", cliente.getNombre()));
                sb.append(String.format("Email: %s\n", cliente.getEmail()));
                sb.append(String.format("Teléfono: %s\n", cliente.getTelefono()));
                sb.append(String.format("Zona: %d\n", cliente.getIdZona()));
                sb.append("-------------------\n");
            }

            SwingMenuBuilder.showLongTextDialog("Listado de Clientes", sb.toString());
        } catch (SQLException ex) {
            mostrarError("Error al listar los clientes: " + ex.getMessage());
        }
    }

    @Override
    public void gestionarPedidos() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton nuevoButton = new JButton("Nuevo Pedido");
        JButton modificarButton = new JButton("Modificar Pedido");
        JButton eliminarButton = new JButton("Eliminar Pedido");
        JButton listarButton = new JButton("Listar Pedidos");
        JButton volverButton = new JButton("Volver");

        panel.add(nuevoButton);
        panel.add(modificarButton);
        panel.add(eliminarButton);
        panel.add(listarButton);
        panel.add(volverButton);

        JDialog dialog = new JDialog(gui, "Gestión de Pedidos", true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(gui);

        nuevoButton.addActionListener(e -> {
            dialog.setVisible(false);
            nuevoPedido();
        });

        modificarButton.addActionListener(e -> {
            dialog.setVisible(false);
            modificarPedido();
        });

        eliminarButton.addActionListener(e -> {
            dialog.setVisible(false);
            eliminarPedido();
        });

        listarButton.addActionListener(e -> {
            dialog.setVisible(false);
            listarPedidos();
        });

        volverButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void nuevoPedido() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Cliente:"));
        JComboBox<Cliente> clienteCombo = new JComboBox<>();
        try {
            List<Cliente> clientes = clienteDAO.getAll();
            for (Cliente cliente : clientes) {
                clienteCombo.addItem(cliente);
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar los clientes: " + ex.getMessage());
            return;
        }
        panel.add(clienteCombo);

        panel.add(new JLabel("Fecha (dd/MM/yyyy):"));
        JTextField fechaField = new JTextField(LocalDate.now().format(dateFormatter));
        panel.add(fechaField);

        panel.add(new JLabel("Importe Total:"));
        JTextField importeField = new JTextField();
        panel.add(importeField);

        int result = JOptionPane.showConfirmDialog(gui, panel, "Nuevo Pedido",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Pedido pedido = new Pedido();
                pedido.setIdCliente(((Cliente) clienteCombo.getSelectedItem()).getIdCliente());
                pedido.setFecha(LocalDate.parse(fechaField.getText(), dateFormatter));
                pedido.setImporteTotal(Double.parseDouble(importeField.getText()));

                pedidoDAO.insert(pedido);
                mostrarMensaje("Pedido creado correctamente");
            } catch (SQLException ex) {
                mostrarError("Error al crear el pedido: " + ex.getMessage());
            } catch (Exception ex) {
                mostrarError("Error en los datos introducidos: " + ex.getMessage());
            }
        }
    }

    private void modificarPedido() {
        JComboBox<Pedido> pedidoCombo = new JComboBox<>();
        try {
            List<Pedido> pedidos = pedidoDAO.getAll();
            for (Pedido pedido : pedidos) {
                pedidoCombo.addItem(pedido);
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar los pedidos: " + ex.getMessage());
            return;
        }

        int seleccion = JOptionPane.showConfirmDialog(gui, pedidoCombo, "Seleccione Pedido",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (seleccion == JOptionPane.OK_OPTION) {
            Pedido pedidoSeleccionado = (Pedido) pedidoCombo.getSelectedItem();

            JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));

            panel.add(new JLabel("Fecha (dd/MM/yyyy):"));
            JTextField fechaField = new JTextField(pedidoSeleccionado.getFecha().format(dateFormatter));
            panel.add(fechaField);

            panel.add(new JLabel("Importe Total:"));
            JTextField importeField = new JTextField(String.valueOf(pedidoSeleccionado.getImporteTotal()));
            panel.add(importeField);

            int result = JOptionPane.showConfirmDialog(gui, panel, "Modificar Pedido",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    pedidoSeleccionado.setFecha(LocalDate.parse(fechaField.getText(), dateFormatter));
                    pedidoSeleccionado.setImporteTotal(Double.parseDouble(importeField.getText()));

                    pedidoDAO.update(pedidoSeleccionado);
                    mostrarMensaje("Pedido modificado correctamente");
                } catch (SQLException ex) {
                    mostrarError("Error al modificar el pedido: " + ex.getMessage());
                } catch (Exception ex) {
                    mostrarError("Error en los datos introducidos: " + ex.getMessage());
                }
            }
        }
    }

    private void eliminarPedido() {
        JComboBox<Pedido> pedidoCombo = new JComboBox<>();
        try {
            List<Pedido> pedidos = pedidoDAO.getAll();
            for (Pedido pedido : pedidos) {
                pedidoCombo.addItem(pedido);
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar los pedidos: " + ex.getMessage());
            return;
        }

        int seleccion = JOptionPane.showConfirmDialog(gui, pedidoCombo, "Seleccione Pedido a Eliminar",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (seleccion == JOptionPane.OK_OPTION) {
            Pedido pedidoSeleccionado = (Pedido) pedidoCombo.getSelectedItem();

            int confirmacion = JOptionPane.showConfirmDialog(gui,
                    "¿Está seguro de eliminar el pedido ID: " + pedidoSeleccionado.getIdPedido() + "?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    pedidoDAO.delete(pedidoSeleccionado.getIdPedido());
                    mostrarMensaje("Pedido eliminado correctamente");
                } catch (SQLException ex) {
                    mostrarError("Error al eliminar el pedido: " + ex.getMessage());
                }
            }
        }
    }

    private void listarPedidos() {
        try {
            List<Pedido> pedidos = pedidoDAO.getAll();
            StringBuilder sb = new StringBuilder();
            sb.append("LISTADO DE PEDIDOS\n");
            sb.append("==================\n\n");

            for (Pedido pedido : pedidos) {
                sb.append(String.format("ID: %d\n", pedido.getIdPedido()));
                sb.append(String.format("Cliente: %d\n", pedido.getIdCliente()));
                sb.append(String.format("Fecha: %s\n", pedido.getFecha().format(dateFormatter)));
                sb.append(String.format("Importe: %.2f€\n", pedido.getImporteTotal()));
                sb.append("-------------------\n");
            }

            SwingMenuBuilder.showLongTextDialog("Listado de Pedidos", sb.toString());
        } catch (SQLException ex) {
            mostrarError("Error al listar los pedidos: " + ex.getMessage());
        }
    }

    // En GraphicalUI.java

    @Override
    public void consultarZonasEnvio() throws SQLException {
        try {
            List<ZonaEnvio> zonas = zonaEnvioDAO.getAll();
            StringBuilder sb = new StringBuilder();
            sb.append("ZONAS DE ENVÍO\n");
            sb.append("==============\n\n");

            for (ZonaEnvio zona : zonas) {
                sb.append(String.format("ID: %d\n", zona.getIdZona()));
                sb.append(String.format("Nombre: %s\n", zona.getNombreZona()));
                sb.append(String.format("Tarifa: %.2f€\n", zona.getTarifaEnvio()));

                // Obtener y mostrar clientes en esta zona
                List<Cliente> clientesZona = clienteDAO.getClientesByZona(zona.getIdZona());
                if (!clientesZona.isEmpty()) {
                    sb.append("Clientes en esta zona:\n");
                    for (Cliente cliente : clientesZona) {
                        sb.append(String.format("  - %s\n", cliente.getNombre()));
                    }
                }

                sb.append("-------------------\n");
            }

            SwingMenuBuilder.showLongTextDialog("Zonas de Envío", sb.toString());
        } catch (SQLException ex) {
            mostrarError("Error al consultar las zonas de envío: " + ex.getMessage());
        }
    }

    @Override
    public void consultarPedidosCliente() throws SQLException {
        // Primero seleccionar el cliente
        JComboBox<Cliente> clienteCombo = new JComboBox<>();
        try {
            List<Cliente> clientes = clienteDAO.getAll();
            for (Cliente cliente : clientes) {
                clienteCombo.addItem(cliente);
            }
        } catch (SQLException ex) {
            mostrarError("Error al cargar los clientes: " + ex.getMessage());
            return;
        }

        int seleccion = JOptionPane.showConfirmDialog(gui, clienteCombo,
                "Seleccione Cliente para ver sus pedidos",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (seleccion == JOptionPane.OK_OPTION) {
            Cliente clienteSeleccionado = (Cliente) clienteCombo.getSelectedItem();

            try {
                List<Pedido> pedidos = pedidoDAO.getPedidosByCliente(clienteSeleccionado.getIdCliente());
                double totalGastado = pedidoDAO.getTotalPedidosByCliente(clienteSeleccionado.getIdCliente());

                StringBuilder sb = new StringBuilder();
                sb.append(String.format("PEDIDOS DE %s\n", clienteSeleccionado.getNombre().toUpperCase()));
                sb.append("=======================\n\n");

                if (pedidos.isEmpty()) {
                    sb.append("Este cliente no tiene pedidos registrados.\n");
                } else {
                    for (Pedido pedido : pedidos) {
                        sb.append(String.format("ID Pedido: %d\n", pedido.getIdPedido()));
                        sb.append(String.format("Fecha: %s\n", pedido.getFecha().format(dateFormatter)));
                        sb.append(String.format("Importe: %.2f€\n", pedido.getImporteTotal()));
                        sb.append("-------------------\n");
                    }

                    sb.append("\nRESUMEN DEL CLIENTE\n");
                    sb.append("==================\n");
                    sb.append(String.format("Total de pedidos: %d\n", pedidos.size()));
                    sb.append(String.format("Importe total gastado: %.2f€\n", totalGastado));

                    // Obtener y mostrar información de la zona del cliente
                    ZonaEnvio zona = zonaEnvioDAO.getById(clienteSeleccionado.getIdZona());
                    if (zona != null) {
                        sb.append(String.format("Zona de envío: %s\n", zona.getNombreZona()));
                        sb.append(String.format("Tarifa de envío: %.2f€\n", zona.getTarifaEnvio()));
                    }
                }

                SwingMenuBuilder.showLongTextDialog(
                        "Consulta de Pedidos - " + clienteSeleccionado.getNombre(),
                        sb.toString());

            } catch (SQLException ex) {
                mostrarError("Error al consultar los pedidos: " + ex.getMessage());
            }
        }
    }

    // Método auxiliar para formatear moneda
    private String formatMoney(double amount) {
        return String.format("%.2f€", amount);
    }

    // Método auxiliar para formatear fecha
    private String formatDate(LocalDate date) {
        return date.format(dateFormatter);
    }
}