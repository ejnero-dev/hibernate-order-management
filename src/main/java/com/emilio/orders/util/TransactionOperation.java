/**
 * Interface que define una operación de transacción.
 * Esta interfaz se utiliza para encapsular operaciones que deben ser ejecutadas dentro de un contexto de transacción.
 */
package com.emilio.orders.util;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface TransactionOperation {
    /**
     * Ejecuta la operación de transacción utilizando la conexión proporcionada.
     *
     * @param conn La conexión a la base de datos dentro del contexto de una transacción.
     * @throws SQLException Si ocurre un error durante la ejecución de la operación de transacción.
     */
    void execute(Connection conn) throws SQLException;
}