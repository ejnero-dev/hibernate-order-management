/**
 * Excepción personalizada para manejar errores relacionados con la base de datos.
 *
 * Esta excepción extiende {@link RuntimeException} y se utiliza para representar
 * cualquier error o situación inesperada que pueda surgir durante las operaciones
 * con la base de datos. Proporciona tres constructores para cubrir diferentes
 * escenarios de uso:
 *
 * 1. Un constructor que toma solo un mensaje de error.
 * 2. Un constructor que toma tanto un mensaje de error como una causa subyacente.
 * 3. Un constructor que toma solo una causa subyacente.
 *
 * Estos constructores permiten capturar y propagar errores en la lógica de acceso
 * a los datos, facilitando el diagnóstico y la depuración durante el desarrollo y
 * el mantenimiento del sistema.
 */
package com.emilio.orders.util;

public class DatabaseException extends RuntimeException {
    /**
     * Crea una nueva instancia de DatabaseException con un mensaje de error específico.
     *
     * @param message El mensaje que describe el error.
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Crea una nueva instancia de DatabaseException con un mensaje de error y una causa subyacente.
     *
     * @param message El mensaje que describe el error.
     * @param cause   La causa del error, que puede ser cualquier objeto {@link Throwable}.
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crea una nueva instancia de DatabaseException con una causa subyacente.
     *
     * @param cause La causa del error, que puede ser cualquier objeto {@link Throwable}.
     */
    public DatabaseException(Throwable cause) {
        super(cause);
    }
}