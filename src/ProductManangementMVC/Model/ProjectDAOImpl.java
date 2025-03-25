package ProductManangementMVC.Model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;  // Import LocalTime
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {

    @Override
    public void addProduct(Product product) {
        // Updated query to include the import time
        String query = "INSERT INTO product (Name, Uint_Price, Qty, Import_Date, Import_Time) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Connect.getConnection()) {
            // PreparedStatement for inserting a product
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getUnitPrice());
            ps.setInt(3, product.getQty());
            ps.setDate(4, Date.valueOf(product.getImportDate()));  // Setting the Import Date
            ps.setTime(5, Time.valueOf(product.getImportTime()));  // Setting the Import Time

            // Execute the insert query
            ps.executeUpdate();
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product"; // Assuming table name is `product`
        try (Connection conn = Connect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Loop through the result set to create product objects
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                double unitPrice = rs.getDouble("Uint_Price");
                int qty = rs.getInt("Qty");
                Date date = rs.getDate("Import_Date");
                Time time = rs.getTime("Import_Time");  // Retrieve Import Time
                LocalDate localDate = (date != null) ? date.toLocalDate() : null;
                LocalTime localTime = (time != null) ? time.toLocalTime() : null;  // Convert to LocalTime

                Product product = new Product(id, name, unitPrice, qty, localDate, localTime);  // Updated constructor
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
