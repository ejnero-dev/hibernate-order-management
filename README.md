# Sistema de Gestión de Pedidos con Hibernate ORM (Tarea 5.1)

Este proyecto implementa un sistema de gestión de pedidos utilizando Hibernate ORM como capa de acceso a datos, siguiendo las dos opciones del enunciado:

- **Opción A**: Modificar el código original para usar directamente la API de Hibernate/JPA.
- **Opción B**: Adaptar el sistema original manteniendo su estructura y creando una capa de traducción entre la API existente y Hibernate/JPA.

## Descripción

El sistema permite gestionar:
- Zonas de envío con tarifas asociadas
- Clientes pertenecientes a distintas zonas
- Pedidos realizados por los clientes

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

## Implementaciones

### Opción A: Implementación directa con API de Hibernate

Esta implementación modifica el código original para utilizar directamente Hibernate. Utiliza un enfoque más limpio y simplificado, aprovechando al máximo las características de Hibernate.

#### Clases principales

- `SessionManager`: Gestiona la SessionFactory de Hibernate y proporciona métodos para ejecutar operaciones dentro de transacciones.
- `DirectClienteDAO`, `DirectPedidoDAO`, `DirectZonaEnvioDAO`: Implementaciones DAO que utilizan directamente la API de Hibernate.
- `DirectDAOFactory`: Factoría que proporciona instancias de las implementaciones DAO.
- `DirectMain`: Punto de entrada para esta implementación.

#### Características

- Uso optimizado de sesiones y transacciones de Hibernate
- Gestión centralizada de la SessionFactory mediante el patrón Singleton
- Ejecución de operaciones mediante expresiones lambda dentro de transacciones
- Código más limpio y directo al eliminar capas intermedias

#### Estructura del código (Opción A)

```
src/
  main/
    java/
      acceso/
        datos/
          dao/
            direct/
              DirectClienteDAO.java        # DAO de clientes con API directa de Hibernate
              DirectPedidoDAO.java         # DAO de pedidos con API directa de Hibernate
              DirectZonaEnvioDAO.java      # DAO de zonas con API directa de Hibernate
          factory/
            DirectDAOFactory.java          # Factoría para los DAOs directos
          hibernate/
            SessionManager.java            # Gestor de sesiones de Hibernate
          model/                          # Clases modelo con anotaciones JPA (compartidas)
            Cliente.java
            Pedido.java
            ZonaEnvio.java
          DirectMain.java                 # Punto de entrada para la implementación directa
```

### Opción B: Adaptación manteniendo la estructura original

Esta implementación adapta el sistema existente para usar Hibernate sin modificar su estructura original. Se añaden solo las anotaciones JPA necesarias y se crean adaptadores que permiten que la estructura original funcione con Hibernate.

#### Clases principales

- `HibernateUtil`: Gestiona la SessionFactory de Hibernate.
- `HibernateClienteDAO`, `HibernatePedidoDAO`, `HibernateZonaEnvioDAO`: Implementaciones DAO que adaptan Hibernate a la interfaz existente.
- `HibernateDAOFactory`: Factoría para crear instancias de los DAOs de Hibernate.
- `HibernateConfig`: Configuración de base de datos para Hibernate.
- Clases modelo con anotaciones JPA.

#### Características

- Preserva la estructura original del sistema
- Mantiene la interfaz de usuario y la experiencia del usuario final intactas
- Usa Hibernate como una implementación alternativa a SQLite
- Añade soporte para Hibernate mediante una capa de adaptación

#### Estructura del código (Opción B)

```
src/
  main/
    java/
      acceso/
        datos/
          config/
            DatabaseConfig.java            # Interfaz actualizada para soportar Hibernate
            DatabaseConfigFactory.java     # Factoría actualizada
            DatabaseType.java              # Enum actualizado con tipo HIBERNATE
            HibernateConfig.java           # Nueva implementación para Hibernate
            SQLiteConfig.java              # Configuración original
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
            HibernateUtil.java             # Clase utilidad para SessionFactory
          Main.java                       # Clase principal actualizada
```

## Detalles de implementación

### Anotaciones JPA en clases modelo (compartido por ambas opciones)

Las clases modelo han sido anotadas con JPA para su uso con Hibernate:

```java
@Entity
@Table(name = "Clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private int idCliente;
    
    // Resto de atributos y métodos
}
```

### Configuración de Hibernate (compartido por ambas opciones)

Archivo `hibernate.cfg.xml` en el directorio `resources`:

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

### Ejemplo de implementación DAO

#### Opción A (Implementación directa)

```java
public class DirectClienteDAO implements ClienteDAO {
    private final SessionManager sessionManager;

    public DirectClienteDAO() {
        this.sessionManager = SessionManager.getInstance();
    }

    @Override
    public void insert(Cliente cliente) throws SQLException {
        try {
            sessionManager.execute(session -> {
                session.persist(cliente);
            });
        } catch (Exception e) {
            throw new SQLException("Error al insertar cliente: " + e.getMessage(), e);
        }
    }
    
    // Otros métodos
}
```

#### Opción B (Adaptación)

```java
public class HibernateClienteDAO implements ClienteDAO {
    @Override
    public void insert(Cliente cliente) throws SQLException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(cliente);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new SQLException("Error al insertar cliente: " + e.getMessage(), e);
        }
    }
    
    // Otros métodos
}
```

## Cómo ejecutar la aplicación

### Desde VS Code

Puedes usar las configuraciones de lanzamiento definidas en `.vscode/launch.json`:

**Opción B (Adaptación):**
1. **Interfaz de Consola (SQLite)**: Ejecuta la aplicación adaptada con interfaz de consola usando SQLite
2. **Interfaz Gráfica (SQLite)**: Ejecuta la aplicación adaptada con interfaz gráfica usando SQLite
3. **Interfaz de Consola (Hibernate)**: Ejecuta la aplicación adaptada con interfaz de consola usando Hibernate
4. **Interfaz Gráfica (Hibernate)**: Ejecuta la aplicación adaptada con interfaz gráfica usando Hibernate

**Opción A (Implementación directa):**
1. **Direct - Interfaz de Consola (Hibernate)**: Ejecuta la implementación directa con interfaz de consola
2. **Direct - Interfaz Gráfica (Hibernate)**: Ejecuta la implementación directa con interfaz gráfica

### Desde línea de comandos

Compilar el proyecto:
```bash
mvn clean package
```

**Opciones de ejecución B (Adaptación):**
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

**Opciones de ejecución A (Implementación directa):**
```bash
# Interfaz de consola con Hibernate (implementación directa)
java -cp target/tarea4_1-1.0-SNAPSHOT.jar acceso.datos.DirectMain

# Interfaz gráfica con Hibernate (implementación directa)
java -cp target/tarea4_1-1.0-SNAPSHOT.jar acceso.datos.DirectMain -i grafica
```

## Comparación entre implementaciones

### Opción A (Implementación directa)

**Ventajas:**
- Código más limpio y conciso
- Mejor aprovechamiento de las capacidades de Hibernate
- Gestión más eficiente de sesiones y transacciones
- Eliminación de capas intermedias innecesarias

**Inconvenientes:**
- Requiere reestructurar el código existente
- Mayor esfuerzo de migración inicial
- Menos compatibilidad con código legado

### Opción B (Adaptación)

**Ventajas:**
- Mantiene la estructura original del código
- Facilita la transición gradual hacia Hibernate
- Mayor compatibilidad con el código existente
- Permite seguir utilizando la implementación original

**Inconvenientes:**
- Añade capas adicionales de complejidad
- Código menos optimizado para Hibernate
- Puede tener peor rendimiento debido a las capas adicionales

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

## Conclusión

Este proyecto demuestra dos enfoques diferentes para integrar Hibernate en una aplicación Java existente:

1. **Opción A**: Modificación directa para aprovechar al máximo las capacidades de Hibernate.
2. **Opción B**: Adaptación con mínimas modificaciones para mantener la compatibilidad con el código existente.

Ambas implementaciones cumplen con el objetivo de utilizar Hibernate como capa de acceso a datos, pero cada una tiene sus ventajas e inconvenientes que la hacen más adecuada según el contexto y los requisitos específicos del proyecto.

## Licencia

Este proyecto es parte de un ejercicio académico y no está licenciado para uso comercial.