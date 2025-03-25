package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static final String URL = "jdbc:postgresql://localhost:5432/stock_management";
    private static final String USER = "postgres";
    private static final String PASSWORD = "LSVenP@$$10092005333/";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
/*
public static Connection connect() {
        try {
            // +++++++++++++++++++++++++++++++++++++++ 1. Load PostgreSQL driver (JDBC 4.0 auto-loads the driver)
            Class.forName("org.postgresql.Driver");

            // ======================================= 2. Establish the connection
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found: " + e.getMessage());
            return null;
        }
    }
 */
