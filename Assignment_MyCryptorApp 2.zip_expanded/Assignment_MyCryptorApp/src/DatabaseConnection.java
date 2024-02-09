import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://security-db.cypcdqzpxlhm.ap-southeast-2.rds.amazonaws.com:3306/CloudSecurity";
    private static final String USER = "root";
    private static final String PASSWORD = "xxxxxx";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error loading MySQL JDBC Driver: " + e.getMessage());
        }
    }
}
