package acceso.datos.factory;

import acceso.datos.dao.direct.DirectClienteDAO;
import acceso.datos.dao.direct.DirectPedidoDAO;
import acceso.datos.dao.direct.DirectZonaEnvioDAO;
import acceso.datos.dao.interfaces.ClienteDAO;
import acceso.datos.dao.interfaces.PedidoDAO;
import acceso.datos.dao.interfaces.ZonaEnvioDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factoría para crear DAOs que utilizan directamente Hibernate.
 * Esta clase proporciona un punto único para obtener instancias de los DAOs.
 */
public class DirectDAOFactory {
    private static final Logger logger = LoggerFactory.getLogger(DirectDAOFactory.class);
    private static DirectDAOFactory instance;
    
    private final ClienteDAO clienteDAO;
    private final PedidoDAO pedidoDAO;
    private final ZonaEnvioDAO zonaEnvioDAO;

    /**
     * Constructor privado que inicializa los DAOs.
     */
    private DirectDAOFactory() {
        logger.debug("Inicializando DirectDAOFactory");
        this.clienteDAO = new DirectClienteDAO();
        this.pedidoDAO = new DirectPedidoDAO();
        this.zonaEnvioDAO = new DirectZonaEnvioDAO();
        logger.info("DirectDAOFactory inicializado correctamente");
    }

    /**
     * Obtiene la única instancia de DirectDAOFactory.
     *
     * @return La instancia de DirectDAOFactory
     */
    public static synchronized DirectDAOFactory getInstance() {
        if (instance == null) {
            instance = new DirectDAOFactory();
        }
        return instance;
    }

    /**
     * Obtiene un DAO para gestionar clientes.
     *
     * @return Una instancia de ClienteDAO
     */
    public ClienteDAO getClienteDAO() {
        return clienteDAO;
    }

    /**
     * Obtiene un DAO para gestionar pedidos.
     *
     * @return Una instancia de PedidoDAO
     */
    public PedidoDAO getPedidoDAO() {
        return pedidoDAO;
    }

    /**
     * Obtiene un DAO para gestionar zonas de envío.
     *
     * @return Una instancia de ZonaEnvioDAO
     */
    public ZonaEnvioDAO getZonaEnvioDAO() {
        return zonaEnvioDAO;
    }
}