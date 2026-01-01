package com.emilio.orders;

import com.emilio.orders.config.DatabaseConfig;
import com.emilio.orders.config.DatabaseConfigFactory;
import com.emilio.orders.config.DatabaseProperties;
import com.emilio.orders.config.DatabaseType;
import com.emilio.orders.dao.interfaces.ClienteDAO;
import com.emilio.orders.dao.interfaces.PedidoDAO;
import com.emilio.orders.dao.interfaces.ZonaEnvioDAO;
import com.emilio.orders.factory.DAOFactory;
import com.emilio.orders.ui.ConsoleUI;
import com.emilio.orders.ui.GraphicalUI;
import com.emilio.orders.ui.UI;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase principal de la aplicación de gestión de pedidos.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Método principal que inicia la ejecución de la aplicación.
     *
     * @param args Argumentos de línea de comandos proporcionados por el usuario
     */
    public static void main(String[] args) {
        logger.info("Iniciando aplicación de gestión de pedidos");
        
        try {
            // Configuración de opciones
            logger.debug("Configurando opciones de línea de comandos");
            Options options = new Options();
            options.addOption("i", "interfaz", true, "Interfaz a usar (consola/grafica)");
            options.addOption("db", "database", true, "Tipo de base de datos (sqlite/hibernate)");
            
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            
            // Obtener la interfaz seleccionada
            String interfaz = cmd.getOptionValue("i", "consola");
            logger.info("Interfaz seleccionada: {}", interfaz);
            
            // Obtener el tipo de base de datos seleccionada
            String dbType = cmd.getOptionValue("db", "sqlite").toUpperCase();
            DatabaseType databaseType;
            try {
                databaseType = DatabaseType.valueOf(dbType);
                logger.info("Tipo de base de datos seleccionada: {}", databaseType);
            } catch (IllegalArgumentException e) {
                logger.warn("Tipo de base de datos no válido: {}. Usando SQLite por defecto.", dbType);
                databaseType = DatabaseType.SQLITE;
            }
            
            // Configuración de base de datos
            logger.debug("Inicializando configuración de base de datos");
            DatabaseProperties properties = new DatabaseProperties.Builder()
                .url("src/main/resources/pedidos.db")
                .maxPoolSize(5)
                .build();
            
            DatabaseConfig databaseConfig = DatabaseConfigFactory.createConfig(databaseType, properties);
            logger.info("Conexión a base de datos establecida");

            // Creación de DAOs
            logger.debug("Inicializando factories y DAOs");
            DAOFactory daoFactory = DAOFactory.getDAOFactory(databaseType, databaseConfig);
            ClienteDAO clienteDAO = daoFactory.createClienteDAO();
            PedidoDAO pedidoDAO = daoFactory.createPedidoDAO();
            ZonaEnvioDAO zonaEnvioDAO = daoFactory.createZonaEnvioDAO();

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
        }
    }
}