package ProductManangementMVC.Model;


import java.sql.Connection;
import java.sql.SQLException;

import static ProductManangementMVC.Model.Connect.getConnection;

public class DatabaseTest {
    public static void main(String[] args) throws SQLException {
        Connection connection =getConnection();

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

