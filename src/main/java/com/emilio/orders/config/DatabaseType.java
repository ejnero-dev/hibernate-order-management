package com.emilio.orders.config;

/**
 * Enumeración que define diferentes tipos de bases de datos compatibles.
 */
public enum DatabaseType {
    /**
     * Tipo de base de datos SQLite.
     */
    SQLITE,
    
    /**
     * Tipo de base de datos MySQL.
     */
    MYSQL,
    
    /**
     * Tipo de base de datos PostgreSQL.
     */
    POSTGRESQL,
    
    /**
     * Tipo de acceso a datos mediante Hibernate ORM.
     */
    HIBERNATE
}