package ProductManagementMVC.Model;


import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        Connection connection = Connect.getConnection();

        if (connection != null) {
            System.out.println("Database connection successful!");
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}
