# Hibernate Order Management

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)
![Hibernate](https://img.shields.io/badge/Hibernate-6.4.1-59666C?style=flat-square&logo=hibernate)
![SQLite](https://img.shields.io/badge/SQLite-3.48-003B57?style=flat-square&logo=sqlite)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)

A complete order management system demonstrating **Hibernate ORM** integration with enterprise design patterns. Features dual database backends (JDBC/Hibernate) and dual user interfaces (Console/Swing).

## Overview

This project showcases a production-ready approach to building data-driven Java applications:

- **Clean Architecture**: Separation of concerns with DAO pattern and Abstract Factory
- **ORM Integration**: Full Hibernate/JPA implementation alongside native JDBC
- **Dual Interfaces**: Both console and graphical (Swing) user interfaces
- **Transaction Management**: Proper session and transaction handling

## Features

- CRUD operations for Customers, Orders, and Shipping Zones
- Customer management with zone assignment
- Order tracking with automatic total calculation
- Shipping zone management with configurable rates
- Query customers by zone and calculate total spending
- Switch between database backends at runtime

## Technologies

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Core language |
| Hibernate ORM | 6.4.1 | Object-Relational Mapping |
| Jakarta Persistence | 3.1.0 | JPA API |
| SQLite | 3.48.0 | Embedded database |
| HikariCP | 6.2.1 | Connection pooling (JDBC mode) |
| SLF4J + Logback | 2.1.0 / 1.5.16 | Logging framework |
| Apache Commons CLI | 1.5.0 | Command-line parsing |
| Maven | 3.6+ | Build tool |

## Architecture

```
+-------------------------------------------------------------+
|                      User Interface                          |
|                  +----------+  +----------+                  |
|                  | Console  |  |  Swing   |                  |
|                  |   UI     |  |   UI     |                  |
|                  +----+-----+  +----+-----+                  |
|                       +------+------+                        |
|                              |                               |
|  +---------------------------+---------------------------+   |
|  |                    DAO Interfaces                     |   |
|  |     ClienteDAO  |  PedidoDAO  |  ZonaEnvioDAO         |   |
|  +---------------------------+---------------------------+   |
|                              |                               |
|         +--------------------+--------------------+          |
|         |                    |                    |          |
|  +------+------+     +-------+-----+     +-------+-----+     |
|  |   SQLite    |     |  Hibernate  |     |   Direct    |     |
|  |    DAOs     |     |    DAOs     |     |    DAOs     |     |
|  |   (JDBC)    |     |   (JPA)     |     | (Hibernate) |     |
|  +------+------+     +------+------+     +------+------+     |
|         |                   |                    |           |
|         +---------+---------+----------+---------+           |
|                   |                    |                     |
|            +------+------+      +------+------+              |
|            |  HikariCP   |      |   Session   |              |
|            |    Pool     |      |   Manager   |              |
|            +------+------+      +------+------+              |
|                   +-----------+--------+                     |
|                               |                              |
|                    +----------+--------+                     |
|                    |    SQLite DB      |                     |
|                    |   pedidos.db      |                     |
|                    +-------------------+                     |
+-------------------------------------------------------------+
```

## Project Structure

```
src/main/java/com/emilio/orders/
├── Main.java                 # Entry point with Factory pattern
├── DirectMain.java           # Entry point for direct Hibernate
├── config/                   # Database configuration
│   ├── DatabaseConfig.java   # Configuration interface
│   ├── DatabaseConfigFactory.java
│   ├── DatabaseProperties.java
│   ├── DatabaseType.java     # Enum: SQLITE, HIBERNATE
│   ├── HibernateConfig.java
│   └── SQLiteConfig.java
├── dao/
│   ├── interfaces/           # DAO contracts
│   │   ├── ClienteDAO.java
│   │   ├── PedidoDAO.java
│   │   └── ZonaEnvioDAO.java
│   ├── impl/
│   │   ├── hibernate/        # Hibernate implementations
│   │   └── sqlite/           # JDBC implementations
│   └── direct/               # Direct Hibernate DAOs
├── factory/                  # Abstract Factory pattern
│   ├── DAOFactory.java
│   ├── HibernateDAOFactory.java
│   ├── SQLiteDAOFactory.java
│   └── DirectDAOFactory.java
├── hibernate/                # Hibernate utilities
│   └── SessionManager.java   # Singleton session management
├── model/                    # JPA entities
│   ├── Cliente.java
│   ├── Pedido.java
│   └── ZonaEnvio.java
├── ui/                       # User interfaces
│   ├── UI.java               # Interface contract
│   ├── ConsoleUI.java
│   ├── GraphicalUI.java
│   └── SwingMenuBuilder.java
└── util/                     # Utilities
    ├── DatabaseException.java
    ├── QueryUtils.java
    └── TransactionUtils.java
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/hibernate-order-management.git
cd hibernate-order-management
```

2. Build the project:
```bash
mvn clean package
```

### Running the Application

**Console UI with SQLite (JDBC):**
```bash
java -jar target/hibernate-order-management-1.0-SNAPSHOT.jar -i consola -db sqlite
```

**Graphical UI with Hibernate:**
```bash
java -jar target/hibernate-order-management-1.0-SNAPSHOT.jar -i grafica -db hibernate
```

**Direct Hibernate mode:**
```bash
java -cp target/hibernate-order-management-1.0-SNAPSHOT.jar com.emilio.orders.DirectMain -i consola
```

### Command Line Options

| Option | Values | Description |
|--------|--------|-------------|
| `-i, --interfaz` | `consola`, `grafica` | UI mode (default: consola) |
| `-db, --database` | `sqlite`, `hibernate` | Database backend (default: sqlite) |

## Screenshots

*Screenshots will be added after building the project*

<!--
![Console Menu](docs/screenshots/console-menu.png)
![Swing UI](docs/screenshots/swing-main.png)
-->

## Design Decisions

### Why Two DAO Implementations?

This project demonstrates two approaches to database access:

1. **JDBC (SQLite DAOs)**: Direct SQL with connection pooling via HikariCP. Offers fine-grained control and better performance for simple queries.

2. **Hibernate (JPA DAOs)**: Object-Relational Mapping with automatic transaction management. Provides abstraction, portability, and reduced boilerplate.

Both implementations share the same interfaces, making them interchangeable at runtime.

### Why Dual User Interfaces?

- **Console UI**: Lightweight, scriptable, suitable for servers or automated tasks
- **Swing UI**: Rich graphical interface for end-users who prefer visual interaction

### Design Patterns Used

| Pattern | Implementation |
|---------|----------------|
| **DAO** | `ClienteDAO`, `PedidoDAO`, `ZonaEnvioDAO` interfaces with multiple implementations |
| **Abstract Factory** | `DAOFactory` creates families of related DAOs |
| **Singleton** | `SessionManager` ensures single SessionFactory instance |
| **Strategy** | Switchable database configurations at runtime |
| **Builder** | `DatabaseProperties.Builder` for configuration |

## Database Schema

```sql
-- Shipping Zones
CREATE TABLE Zonas_Envio (
    id_zona INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_zona VARCHAR(100) NOT NULL,
    tarifa_envio REAL NOT NULL
);

-- Customers
CREATE TABLE Clientes (
    id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    id_zona INTEGER,
    FOREIGN KEY (id_zona) REFERENCES Zonas_Envio(id_zona)
);

-- Orders
CREATE TABLE Pedidos (
    id_pedido INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha DATE NOT NULL,
    importe_total REAL NOT NULL,
    id_cliente INTEGER,
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente)
);
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Built with Java and Hibernate ORM
