package acceso.datos.util;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface TransactionOperation {
    void execute(Connection conn) throws SQLException;
}