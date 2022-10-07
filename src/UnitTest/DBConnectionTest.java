package UnitTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnectionTest {
    private static Connection c = null;

    public static Connection getInstance() throws SQLException { //connessione db
        if (c == null || c.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:db/TEST.db");
                c.setAutoCommit(false);
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(-1);
            }
        }
        return c;
    }
}
