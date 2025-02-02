package acceso.datos.util;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionUtils {
    public static void executeInTransaction(Connection conn, TransactionOperation operation) 
            throws SQLException {
        boolean autoCommit = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);
            operation.execute(conn);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(autoCommit);
        }
    }
}