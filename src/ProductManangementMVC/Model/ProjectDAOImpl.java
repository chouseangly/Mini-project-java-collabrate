package ProductManangementMVC.Model;

import ProductManangementMVC.Model.Product;
import ProductManangementMVC.Model.ProjectDAO;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/product_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "12345";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public boolean addProduct(Product product) {
        String query = "INSERT INTO product (name, uint_price, qty, import_date, import_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getUnitPrice());
            stmt.setInt(3, product.getQty());
            stmt.setDate(4, Date.valueOf(product.getImportDate()));
            stmt.setTime(5, Time.valueOf(product.getImportTime()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while adding product: " + e.getMessage());
            return false;
        }
    }
    @Override
    public int getTotalProductCount() {
        String query = "SELECT COUNT(*) FROM product"; // SQL query to get the total product count
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1); // Return the count
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching product count: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean updateProduct(Product product) {
        String query = "UPDATE product SET name = ?, uint_price = ?, qty = ?, import_date = ?, import_time = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getUnitPrice());
            stmt.setInt(3, product.getQty());
            stmt.setDate(4, Date.valueOf(product.getImportDate()));
            stmt.setTime(5, Time.valueOf(product.getImportTime()));
            stmt.setInt(6, product.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while updating product: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int id) {
        String query = "DELETE FROM product WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error while deleting product: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Product getProductById(int id) {
        String query = "SELECT * FROM product WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("uint_price"),
                        rs.getInt("qty"),
                        rs.getDate("import_date").toLocalDate(),
                        rs.getTime("import_time").toLocalTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching product: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product ORDER BY id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("uint_price"),
                        rs.getInt("qty"),
                        rs.getDate("import_date").toLocalDate(),
                        rs.getTime("import_time").toLocalTime()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching all products: " + e.getMessage());
        }
        return products;
    }

    @Override
    public int getTotalRecords() {
        String query = "SELECT COUNT(*) AS total FROM product";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error while counting records: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public List<Product> getProductsByPage(int page, int rowsPerPage) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product ORDER BY id LIMIT ? OFFSET ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, rowsPerPage);
            stmt.setInt(2, (page - 1) * rowsPerPage);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("uint_price"),
                        rs.getInt("qty"),
                        rs.getDate("import_date").toLocalDate(),
                        rs.getTime("import_time").toLocalTime()
                ));
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error while fetching paginated products: " + e.getMessage());
        }
        return products;
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE name ILIKE ? OR CAST(unit_price AS TEXT) ILIKE ? OR CAST(qty AS TEXT) ILIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("uint_price"),
                        rs.getInt("qty"),
                        rs.getDate("import_date").toLocalDate(),
                        rs.getTime("import_time").toLocalTime()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error while searching products: " + e.getMessage());
        }
        return products;
    }

    @Override
    public void backupProductsToFile(String filePath) {
        // Fetch all products from the database
        List<Product> products = getAllProduct();

        if (products.isEmpty()) {
            System.out.println("No products to back up.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the header row
            String header = String.join(",", "Name", "UnitPrice", "Qty", "ImportDate", "ImportTime");
            writer.write(header);
            writer.newLine();

            // Write each product's details
            for (Product product : products) {
                String productData = String.join(",",
                        product.getName(),
                        String.valueOf(product.getUnitPrice()),
                        String.valueOf(product.getQty()),
                        product.getImportDate().toString(), // LocalDate formatted as yyyy-MM-dd
                        product.getImportTime().toString()  // LocalTime formatted as HH:mm:ss
                );
                writer.write(productData);
                writer.newLine();
            }

            System.out.println("Backup completed successfully. File saved at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error during backup: " + e.getMessage());
        }
    }


    @Override
    public void restoreProductsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split(",");
                if (productData.length == 5) { // Now we expect 5 fields
                    try {
                        String name = productData[0].trim();
                        double unitPrice = Double.parseDouble(productData[1].trim());
                        int qty = Integer.parseInt(productData[2].trim());
                        LocalDate importDate = LocalDate.parse(productData[3].trim());
                        LocalTime importTime = LocalTime.parse(productData[4].trim()); // Parsing import_time
                        Product product = new Product(0, name, unitPrice, qty, importDate, importTime);
                        addProduct(product);
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.out.println("Error parsing line: " + line);
                    }
                } else {
                    System.out.println("Invalid product data format: " + line);
                }
            }
            System.out.println("Restore completed successfully.");
        } catch (IOException e) {
            System.out.println("Error during restore: " + e.getMessage());
        }
    }

}