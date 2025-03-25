package model;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ProductDaoImpl implements ProductDao {
    private Connection connection;

    public ProductDaoImpl() {
        this.connection = Connect.getConnection();
    }

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (name, price, qty, import_date) VALUES (?,?,?,?)";
        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(new Date());
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getUnitPrice());
            stmt.setInt(3, product.getQty());
            stmt.setString(4, formattedDate);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getProductById(int id) {
        List<Product> products = new ArrayList<Product>();
        String slectQuery = "SELECT * FROM products";
        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(slectQuery);
             Statement stmt1 = connection.createStatement();
             ResultSet rs = stmt1.executeQuery(slectQuery)) {
            System.out.println("Products List: ");
            while (rs.next()) {
                int id1 = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("qty");
                String date = rs.getString("import_date");
                products.add(new Product(id1, name, price, quantity, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products[id]


    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        String slectQuery = "SELECT * FROM products";
        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(slectQuery);
             Statement stmt1 = connection.createStatement();
             ResultSet rs = stmt1.executeQuery(slectQuery)) {
            System.out.println("Products List: ");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("qty");
                String date = rs.getString("import_date");
                products.add(new Product(id, name, price, quantity, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void updateProduct(Product product) {}

    @Override
    public void deleteProduct(int id) {}

}
