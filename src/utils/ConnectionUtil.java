package src.utils;

import src.enums.LoggingType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static src.utils.LoggingUtil.logln;

public class ConnectionUtil {
    private Connection connection;
    private static final ConnectionUtil connectionUtil = new ConnectionUtil();
    private ConnectionUtil() {}

    public static ConnectionUtil getConnectionUtil() {
        return connectionUtil;
    }
    public Connection getConnection () {
        if (connection ==  null) {
            try {
                final String URL = "jdbc:postgresql://localhost:5432/puskesmas";
                final String USER = "postgres";
                final String PASSWORD = "967832";
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                logln("Connection established", LoggingType.DEBUG);
            } catch (SQLException e) {
                logln(e.getMessage(), LoggingType.ERROR);
            }
        }
        return connection;
    }
}
