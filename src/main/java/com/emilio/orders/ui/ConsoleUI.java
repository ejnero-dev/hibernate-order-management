package com.emilio.orders.ui;

import com.emilio.orders.dao.interfaces.ClienteDAO;
import com.emilio.orders.dao.interfaces.PedidoDAO;
import com.emilio.orders.dao.interfaces.ZonaEnvioDAO;
import com.emilio.orders.model.Cliente;
import com.emilio.orders.model.Pedido;
import com.emilio.orders.model.ZonaEnvio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que implementa la interfaz ConsoleUI para proporcionar una interfaz de usuario por consola.
 */
public class ConsoleUI implements UI {

    private final Scanner scanner;
    private final ClienteDAO clienteDAO;
    private final PedidoDAO pedidoDAO;
    private final ZonaEnvioDAO zonaEnvioDAO;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constructor de la clase ConsoleUI.
     *
     * @param clienteDAO DAO para operaciones con clientes
     * @param pedidoDAO  DAO para operaciones con pedidos
     * @param zonaEnvioDAO DAO para operaciones con zonas de envío
     */
    public ConsoleUI(ClienteDAO clienteDAO, PedidoDAO pedidoDAO, ZonaEnvioDAO zonaEnvioDAO) {
        this.scanner = new Scanner(System.in);
        this.clienteDAO = clienteDAO;
        this.pedidoDAO = pedidoDAO;
        this.zonaEnvioDAO = zonaEnvioDAO;
    }

    /**
     * Método que inicia la interfaz de usuario y muestra un menú principal.
     */
    @Override
    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            int opcion = mostrarMenu();
            try {
                switch (opcion) {
                    case 1 -> gestionarClientes();
                    case 2 -> gestionarPedidos();
                    case 3 -> consultarZonasEnvio();
                    case 4 -> consultarPedidosCliente();
                    case 0 -> salir = true;
                    default -> mostrarError("Opción no válida");
                }
            } catch (Exception e) {
                mostrarError("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Método que muestra un mensaje en la consola.
     *
     * @param mensaje El mensaje a mostrar
     */
    @Override
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /**
     * Método que muestra un error en la consola.
     *
     * @param mensaje El error a mostrar
     */
    @Override
    public void mostrarError(String mensaje) {
        System.err.println("ERROR: " + mensaje);
    }

    /**
     * Método que muestra el menú principal y solicita una opción al usuario.
     *
     * @return La opción seleccionada por el usuario
     */
    @Override
    public int mostrarMenu() {
        System.out.println("\n=== GESTIÓN DE PEDIDOS ===");
        System.out.println("1. Gestionar Clientes");
        System.out.println("2. Gestionar Pedidos");
        System.out.println("3. Consultar Zonas de Envío");
        System.out.println("4. Consultar Pedidos de Cliente");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
        return Integer.parseInt(scanner.nextLine());
    }

    /**
     * Método que gestiona las operaciones relacionadas con clientes.
     */
    @Override
    public void gestionarClientes() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n=== GESTIÓN DE CLIENTES ===");
            System.out.println("1. Nuevo Cliente");
            System.out.println("2. Modificar Cliente");
            System.out.println("3. Eliminar Cliente");
            System.out.println("4. Listar Clientes");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            int opcion = Integer.parseInt(scanner.nextLine());
            try {
                switch (opcion) {
                    case 1 -> nuevoCliente();
                    case 2 -> modificarCliente();
                    case 3 -> eliminarCliente();
                    case 4 -> listarClientes();
                    case 0 -> volver = true;
                    default -> mostrarError("Opción no válida");
                }
            } catch (Exception e) {
                mostrarError("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Método que permite crear un nuevo cliente.
     */
    private void nuevoCliente() throws Exception {
        System.out.println("\nNUEVO CLIENTE");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        // Mostrar zonas disponibles
        List<ZonaEnvio> zonas = zonaEnvioDAO.getAll();
        System.out.println("\nZonas disponibles:");
        for (ZonaEnvio zona : zonas) {
            System.out.println(zona.getIdZona() + ". " + zona.getNombreZona());
        }
        System.out.print("Seleccione zona: ");
        int idZona = Integer.parseInt(scanner.nextLine());

        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setEmail(email);
        cliente.setTelefono(telefono);
        cliente.setIdZona(idZona);

        clienteDAO.insert(cliente);
        mostrarMensaje("Cliente creado correctamente");
    }

    /**
     * Método que permite modificar un cliente existente.
     */
    private void modificarCliente() throws Exception {
        listarClientes();
        System.out.print("\nID del cliente a modificar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Cliente cliente = clienteDAO.getById(id);
        if (cliente == null) {
            mostrarError("Cliente no encontrado");
            return;
        }

        System.out.println("Deje en blanco para mantener el valor actual");

        System.out.print("Nombre [" + cliente.getNombre() + "]: ");
        String nombre = scanner.nextLine();
        if (!nombre.isEmpty())
            cliente.setNombre(nombre);

        System.out.print("Email [" + cliente.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.isEmpty())
            cliente.setEmail(email);

        System.out.print("Teléfono [" + cliente.getTelefono() + "]: ");
        String telefono = scanner.nextLine();
        if (!telefono.isEmpty())
            cliente.setTelefono(telefono);

        List<ZonaEnvio> zonas = zonaEnvioDAO.getAll();
        System.out.println("\nZonas disponibles:");
        for (ZonaEnvio zona : zonas) {
            System.out.println(zona.getIdZona() + ". " + zona.getNombreZona());
        }
        System.out.print("Nueva zona [" + cliente.getIdZona() + "]: ");
        String zonaStr = scanner.nextLine();
        if (!zonaStr.isEmpty()) {
            cliente.setIdZona(Integer.parseInt(zonaStr));
        }

        clienteDAO.update(cliente);
        mostrarMensaje("Cliente actualizado correctamente");
    }

    /**
     * Método que permite eliminar un cliente.
     */
    private void eliminarCliente() throws Exception {
        listarClientes();
        System.out.print("\nID del cliente a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("¿Está seguro de eliminar este cliente? (s/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("s")) {
            clienteDAO.delete(id);
            mostrarMensaje("Cliente eliminado correctamente");
        }
    }

    /**
     * Método que lista todos los clientes.
     */
    private void listarClientes() throws Exception {
        List<Cliente> clientes = clienteDAO.getAll();
        System.out.println("\nLISTA DE CLIENTES");
        System.out.println("================");
        for (Cliente cliente : clientes) {
            System.out.printf("ID: %d | Nombre: %s | Email: %s | Teléfono: %s | Zona: %d%n",
                    cliente.getIdCliente(),
                    cliente.getNombre(),
                    cliente.getEmail(),
                    cliente.getTelefono(),
                    cliente.getIdZona());
        }
    }

    /**
     * Método que gestiona las operaciones relacionadas con pedidos.
     */
    @Override
    public void gestionarPedidos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n=== GESTIÓN DE PEDIDOS ===");
            System.out.println("1. Nuevo Pedido");
            System.out.println("2. Modificar Pedido");
            System.out.println("3. Eliminar Pedido");
            System.out.println("4. Listar Pedidos");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            int opcion = Integer.parseInt(scanner.nextLine());
            try {
                switch (opcion) {
                    case 1 -> nuevoPedido();
                    case 2 -> modificarPedido();
                    case 3 -> eliminarPedido();
                    case 4 -> listarPedidos();
                    case 0 -> volver = true;
                    default -> mostrarError("Opción no válida");
                }
            } catch (Exception e) {
                mostrarError("Error: " + e.getMessage());
            }
        }
    }

    /**
     * Método que permite crear un nuevo pedido.
     */
    private void nuevoPedido() throws Exception {
        listarClientes();
        System.out.print("\nID del cliente: ");
        int idCliente = Integer.parseInt(scanner.nextLine());

        System.out.print("Fecha (dd/MM/yyyy): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine(), dateFormatter);

        System.out.print("Importe total: ");
        double importeTotal = Double.parseDouble(scanner.nextLine());

        Pedido pedido = new Pedido();
        pedido.setIdCliente(idCliente);
        pedido.setFecha(fecha);
        pedido.setImporteTotal(importeTotal);

        pedidoDAO.insert(pedido);
        mostrarMensaje("Pedido creado correctamente");
    }

    /**
     * Método que permite modificar un pedido existente.
     */
    private void modificarPedido() throws Exception {
        listarPedidos();
        System.out.print("\nID del pedido a modificar: ");
        int id = Integer.parseInt(scanner.nextLine());

        Pedido pedido = pedidoDAO.getById(id);
        if (pedido == null) {
            mostrarError("Pedido no encontrado");
            return;
        }

        System.out.print("Nueva fecha [" + pedido.getFecha().format(dateFormatter) + "]: ");
        String fechaStr = scanner.nextLine();
        if (!fechaStr.isEmpty()) {
            pedido.setFecha(LocalDate.parse(fechaStr, dateFormatter));
        }

        System.out.print("Nuevo importe [" + pedido.getImporteTotal() + "]: ");
        String importeStr = scanner.nextLine();
        if (!importeStr.isEmpty()) {
            pedido.setImporteTotal(Double.parseDouble(importeStr));
        }

        pedidoDAO.update(pedido);
        mostrarMensaje("Pedido actualizado correctamente");
    }

    /**
     * Método que permite eliminar un pedido.
     */
    private void eliminarPedido() throws Exception {
        listarPedidos();
        System.out.print("\nID del pedido a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("¿Está seguro de eliminar este pedido? (s/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("s")) {
            pedidoDAO.delete(id);
            mostrarMensaje("Pedido eliminado correctamente");
        }
    }

    /**
     * Método que lista todos los pedidos.
     */
    private void listarPedidos() throws Exception {
        List<Pedido> pedidos = pedidoDAO.getAll();
        System.out.println("\nLISTA DE PEDIDOS");
        System.out.println("===============");
        for (Pedido pedido : pedidos) {
            System.out.printf("ID: %d | Cliente: %d | Fecha: %s | Importe: %.2f€%n",
                    pedido.getIdPedido(),
                    pedido.getIdCliente(),
                    pedido.getFecha().format(dateFormatter),
                    pedido.getImporteTotal());
        }
    }

    /**
     * Método que consulta todas las zonas de envío.
     */
    @Override
    public void consultarZonasEnvio() throws Exception {
        List<ZonaEnvio> zonas = zonaEnvioDAO.getAll();
        System.out.println("\nZONAS DE ENVÍO");
        System.out.println("=============");
        for (ZonaEnvio zona : zonas) {
            System.out.printf("ID: %d | Nombre: %s | Tarifa: %.2f€%n",
                    zona.getIdZona(),
                    zona.getNombreZona(),
                    zona.getTarifaEnvio());
        }
    }

    /**
     * Método que consulta los pedidos de un cliente específico.
     */
    @Override
    public void consultarPedidosCliente() throws Exception {
        listarClientes();
        System.out.print("\nID del cliente: ");
        int idCliente = Integer.parseInt(scanner.nextLine());

        // Verificar si el cliente existe
        Cliente cliente = clienteDAO.getById(idCliente);
        if (cliente == null) {
            mostrarError("Cliente no encontrado");
            return;
        }

        List<Pedido> pedidos = pedidoDAO.getPedidosByCliente(idCliente);
        double totalGastado = pedidoDAO.getTotalPedidosByCliente(idCliente);

        System.out.println("\nPEDIDOS DEL CLIENTE");
        System.out.println("==================");
        for (Pedido pedido : pedidos) {
            System.out.printf("ID: %d | Fecha: %s | Importe: %.2f€%n",
                    pedido.getIdPedido(),
                    pedido.getFecha().format(dateFormatter),
                    pedido.getImporteTotal());
        }
        System.out.printf("\nTotal gastado: %.2f€%n", totalGastado);
    }
}