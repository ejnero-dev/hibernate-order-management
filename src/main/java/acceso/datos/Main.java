package acceso.datos;

import acceso.datos.config.DatabaseConfig;
import acceso.datos.config.DatabaseConfigFactory;
import acceso.datos.config.DatabaseProperties;
import acceso.datos.config.DatabaseType;
import acceso.datos.dao.interfaces.ClienteDAO;
import acceso.datos.dao.interfaces.PedidoDAO;
import acceso.datos.dao.interfaces.ZonaEnvioDAO;
import acceso.datos.factory.DAOFactory;
import acceso.datos.ui.ConsoleUI;
import acceso.datos.ui.GraphicalUI;
import acceso.datos.ui.UI;

import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("i", "interfaz", true, "Interfaz a usar (consola/grafica)");
        
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            
            // Obtener la interfaz seleccionada
            String interfaz = cmd.getOptionValue("i", "consola");
            
            // Configuración de base de datos
            DatabaseProperties properties = new DatabaseProperties.Builder()
                .url("src/main/resources/pedidos.db")
                .maxPoolSize(5)
                .build();
            
            DatabaseConfig databaseConfig = DatabaseConfigFactory.createConfig(DatabaseType.SQLITE, properties);

            DAOFactory daoFactory = DAOFactory.getDAOFactory(DatabaseType.SQLITE, databaseConfig);

            ClienteDAO clienteDAO = daoFactory.createClienteDAO();
            PedidoDAO pedidoDAO = daoFactory.createPedidoDAO();
            ZonaEnvioDAO zonaEnvioDAO = daoFactory.createZonaEnvioDAO();

            // Seleccionar interfaz
            UI ui;
            if ("grafica".equalsIgnoreCase(interfaz)) {
                ui = new GraphicalUI(clienteDAO, pedidoDAO, zonaEnvioDAO);
            } else {
                ui = new ConsoleUI(clienteDAO, pedidoDAO, zonaEnvioDAO);
            }

            ui.iniciar();

        } catch (ParseException e) {
            System.err.println("Error al parsear argumentos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error fatal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}