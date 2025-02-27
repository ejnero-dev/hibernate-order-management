package acceso.datos;

import acceso.datos.dao.interfaces.ClienteDAO;
import acceso.datos.dao.interfaces.PedidoDAO;
import acceso.datos.dao.interfaces.ZonaEnvioDAO;
import acceso.datos.factory.DirectDAOFactory;
import acceso.datos.hibernate.SessionManager;
import acceso.datos.ui.ConsoleUI;
import acceso.datos.ui.GraphicalUI;
import acceso.datos.ui.UI;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase principal de la aplicación de gestión de pedidos usando directamente Hibernate.
 * Esta implementación corresponde a la opción A del enunciado.
 */
public class DirectMain {
    private static final Logger logger = LoggerFactory.getLogger(DirectMain.class);

    /**
     * Método principal que inicia la ejecución de la aplicación.
     *
     * @param args Argumentos de línea de comandos proporcionados por el usuario
     */
    public static void main(String[] args) {
        logger.info("Iniciando aplicación de gestión de pedidos (implementación directa Hibernate)");
        
        try {
            // Configuración de opciones
            logger.debug("Configurando opciones de línea de comandos");
            Options options = new Options();
            options.addOption("i", "interfaz", true, "Interfaz a usar (consola/grafica)");
            
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            
            // Obtener la interfaz seleccionada
            String interfaz = cmd.getOptionValue("i", "consola");
            logger.info("Interfaz seleccionada: {}", interfaz);
            
            // Obtener las instancias de DAO
            DirectDAOFactory daoFactory = DirectDAOFactory.getInstance();
            ClienteDAO clienteDAO = daoFactory.getClienteDAO();
            PedidoDAO pedidoDAO = daoFactory.getPedidoDAO();
            ZonaEnvioDAO zonaEnvioDAO = daoFactory.getZonaEnvioDAO();
            
            // Seleccionar interfaz
            UI ui;
            if ("grafica".equalsIgnoreCase(interfaz)) {
                logger.info("Iniciando interfaz gráfica");
                ui = new GraphicalUI(clienteDAO, pedidoDAO, zonaEnvioDAO);
            } else {
                logger.info("Iniciando interfaz de consola");
                ui = new ConsoleUI(clienteDAO, pedidoDAO, zonaEnvioDAO);
            }

            ui.iniciar();
            logger.info("Aplicación iniciada correctamente");

        } catch (ParseException e) {
            logger.error("Error al parsear argumentos de línea de comandos: {}", e.getMessage());
            System.err.println("Error al parsear argumentos: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error fatal en la aplicación", e);
            System.err.println("Error fatal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar la SessionFactory al terminar
            try {
                SessionManager.getInstance().shutdown();
            } catch (Exception e) {
                logger.error("Error al cerrar SessionFactory", e);
            }
        }
    }
}