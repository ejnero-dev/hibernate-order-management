# Sistema de Gestión de Pedidos con Hibernate ORM (Tarea 5.1 - Opción B)

Este proyecto implementa un sistema de gestión de pedidos utilizando Hibernate ORM como capa de acceso a datos. La implementación corresponde a la **Opción B** del enunciado, que consiste en adaptar el sistema existente para usar Hibernate **sin modificar** su estructura original.

## Descripción

El sistema permite gestionar:
- Zonas de envío con tarifas asociadas
- Clientes pertenecientes a distintas zonas
- Pedidos realizados por los clientes

La arquitectura del sistema se ha mantenido intacta, añadiendo solo las anotaciones JPA necesarias a las clases de modelo y creando adaptadores entre la API original y Hibernate.

## Requisitos

- Java JDK 17 o superior
- Maven 3.6 o superior
- SQLite (incluido como dependencia)

## Tecnologías utilizadas

- **Hibernate ORM**: Framework de mapeo objeto-relacional
- **JPA**: API de persistencia de Java
- **SQLite**: Base de datos ligera para almacenamiento local
- **Swing**: Para la interfaz gráfica
- **SLF4J/Logback**: Para el sistema de logs

## Estructura del proyecto

La estructura del proyecto se ha mantenido, añadiendo solo las clases y archivos necesarios para la integración con Hibernate:

```
src/
  main/
    java/
      acceso/
        datos/
          config/
            DatabaseConfig.java            # Interfaz actualizada para soportar Hibernate
            DatabaseConfigFactory.java     # Factoría actualizada para crear config. de Hibernate
            DatabaseType.java              # Enum actualizado con tipo HIBERNATE
            HibernateConfig.java           # Nueva implementación para Hibernate
            SQLiteConfig.java              # Configuración original de SQLite
            ...
          dao/
            impl/
              hibernate/                   # Nuevas implementaciones para Hibernate
                HibernateClienteDAO.java
                HibernatePedidoDAO.java
                HibernateZonaEnvioDAO.java
              sqlite/                      # Implementaciones originales
                ...
            interfaces/                    # Interfaces DAO sin modificaciones
              ...
          factory/
            DAOFactory.java                # Actualizado para soportar Hibernate
            HibernateDAOFactory.java       # Nueva factoría para Hibernate
            SQLiteDAOFactory.java          # Factoría original
          hibernate/
            HibernateUtil.java             # Clase utilidad para gestionar SessionFactory
          model/                          # Clases modelo con anotaciones JPA
            Cliente.java
            Pedido.java
            ZonaEnvio.java
          ui/                             # Interfaces de usuario (sin modificaciones)
            ...
          Main.java                       # Clase principal actualizada
    resources/
      hibernate.cfg.xml                    # Configuración de Hibernate
      pedidos.sql                          # Script SQL original
      logback.xml                          # Configuración de logs
```

## Detalles de implementación

### 1. Anotaciones JPA en clases modelo

Se han añadido anotaciones JPA a las clases `Cliente`, `Pedido` y `ZonaEnvio`:

```java
@Entity
@Table(name = "Clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private int idCliente;
    
    // Resto de atributos y métodos sin modificar
}
```

### 2. Configuración de Hibernate

Se ha creado un archivo `hibernate.cfg.xml` en el directorio `resources`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.url">jdbc:sqlite:src/main/resources/pedidos.db</property>
        <property name="hibernate.connection.driver_class">org.sqlite.JDBC</property>
        <property name="hibernate.dialect">org.hibernate.community.dialect.SQLiteDialect</property>
        <property name="hibernate.hbm2ddl.auto">update</property>
        <!-- Otras propiedades -->
        
        <!-- Mapeo de clases -->
        <mapping class="acceso.datos.model.ZonaEnvio"/>
        <mapping class="acceso.datos.model.Cliente"/>
        <mapping class="acceso.datos.model.Pedido"/>
    </session-factory>
</hibernate-configuration>
```

### 3. Clase HibernateUtil

Se ha creado una clase `HibernateUtil` para gestionar la SessionFactory:

```java
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
```

### 4. Implementación de DAOs para Hibernate

Se han creado implementaciones de DAO que utilizan Hibernate para cada entidad:

```java
public class HibernateClienteDAO implements ClienteDAO {
    // Implementación de métodos utilizando Hibernate
}
```

### 5. Actualización de las factorías

Se ha extendido `DAOFactory` y se ha creado `HibernateDAOFactory` para soportar Hibernate:

```java
public class HibernateDAOFactory extends DAOFactory {
    @Override
    public ClienteDAO createClienteDAO() {
        return new HibernateClienteDAO();
    }
    
    // Otros métodos
}
```

### 6. Configuración de base de datos para Hibernate

Se ha implementado `HibernateConfig` que adapta la API de Hibernate a la interfaz `DatabaseConfig` existente.

## Cómo ejecutar la aplicación

### Desde VS Code

Puedes usar las configuraciones de lanzamiento definidas en `.vscode/launch.json`:

1. **Interfaz de Consola (SQLite)**: Ejecuta la aplicación con interfaz de consola usando SQLite
2. **Interfaz Gráfica (SQLite)**: Ejecuta la aplicación con interfaz gráfica usando SQLite
3. **Interfaz de Consola (Hibernate)**: Ejecuta la aplicación con interfaz de consola usando Hibernate
4. **Interfaz Gráfica (Hibernate)**: Ejecuta la aplicación con interfaz gráfica usando Hibernate

### Desde línea de comandos

Compilar el proyecto:
```bash
mvn clean package
```

Ejecutar con diferentes configuraciones:
```bash
# Interfaz de consola con SQLite (predeterminado)
java -jar target/tarea4_1-1.0-SNAPSHOT.jar

# Interfaz gráfica con SQLite
java -jar target/tarea4_1-1.0-SNAPSHOT.jar -i grafica

# Interfaz de consola con Hibernate
java -jar target/tarea4_1-1.0-SNAPSHOT.jar -db hibernate

# Interfaz gráfica con Hibernate
java -jar target/tarea4_1-1.0-SNAPSHOT.jar -i grafica -db hibernate
```

## Comparación con la implementación original

Esta implementación:

1. **Mantiene la interfaz de usuario intacta**: No hay cambios en la experiencia del usuario final.
2. **Preserva las interfaces DAO**: Las interfaces se mantienen sin cambios.
3. **Usa el mismo esquema de base de datos**: No hay modificaciones en la estructura de las tablas.
4. **Añade soporte para Hibernate**: Permite usar Hibernate o SQLite indistintamente.

## Depuración y solución de problemas

### Problemas comunes y soluciones

1. **Error "No CurrentSessionContext configured"**:
   - Verifica la configuración en `hibernate.cfg.xml`
   - Asegúrate de usar el método correcto para obtener la sesión

2. **Errores con SQLite y claves foráneas**:
   - SQLite tiene limitaciones con las restricciones de claves foráneas
   - Verifica que están activadas con la propiedad `foreign_keys=true`

3. **Problemas con el mapeo de entidades**:
   - Comprueba las anotaciones JPA en las clases modelo
   - Verifica que las clases están registradas en `hibernate.cfg.xml`

### Logs

Los logs se configuran en `logback.xml` y proporcionan información detallada sobre las operaciones de Hibernate.

## Licencia

Este proyecto es parte de un ejercicio académico y no está licenciado para uso comercial.
