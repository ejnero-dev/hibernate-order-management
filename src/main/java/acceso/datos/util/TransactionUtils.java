package acceso.datos.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase utilitaria para ejecutar operaciones de base de datos dentro de una transacción.
 * En este proyecto tan sencillo solo llegamos a utilizarla a la hora de eliminar un cliente de la base de datos,
 * pero se puede usar para operaciones complejas más adelante. Pensé que era importante implementarla.
 */
public class TransactionUtils {

    /**
     * Ejecuta la operación proporcionada {@link TransactionOperation} dentro de una transacción de la base de datos.
     *
     * @param conn Conexión a la base de datos a utilizar.
     * @param operation Operación {@link TransactionOperation} a ejecutar.
     * @throws SQLException Si ocurre un error durante la ejecución de la transacción.
     */
    public static void executeInTransaction(Connection conn, TransactionOperation operation) 
            throws SQLException {
        // Almacenar el estado actual del auto-commit
        boolean autoCommit = conn.getAutoCommit();
        
        try {
            // Desactivar el auto-commit para permitir un control manual de las fronteras de la transacción
            conn.setAutoCommit(false);
            
            // Ejecutar la operación proporcionada dentro de la transacción
            operation.execute(conn);
            
            // Confirmar la transacción si no ocurren errores
            conn.commit();
        } catch (SQLException e) {
            // Revertir la transacción en caso de error
            conn.rollback();
            
            // Re-lanzar la excepción para propagarla por encima de la pila de llamadas
            throw e;
        } finally {
            // Restablecer el estado original del auto-commit
            conn.setAutoCommit(autoCommit);
        }
    }
}