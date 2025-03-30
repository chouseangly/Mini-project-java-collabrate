package ProductManagementMVC.Model;

import java.sql.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private Connection connection;
    private static final String BACKUP_DIRECTORY = "C:/Users/USER/Desktop/IntelliJ/StockManagementSystem/src/File/";

    public ProductDaoImpl() {
        this.connection = Connect.getConnection();
    }

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (name, price, qty, import_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getUnitPrice());
            stmt.setInt(3, product.getQty());
            stmt.setDate(4, java.sql.Date.valueOf(product.getImportDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding product: " + e.getMessage());  //Use logging framework instead
        }
    }

    @Override
    public void updateProduct(int id, String name, double unitPrice, int qty) {
        String sql = "UPDATE products SET name = ?, price = ?, qty = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setDouble(2, unitPrice);
            stmt.setInt(3, qty);
            stmt.setInt(4, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public void updateName(int id, String name) {
        String sql = "UPDATE products SET name = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating name: " + e.getMessage());
        }
    }

    @Override
    public void updateUnitPrice(int id, double unitPrice) {
        String sql = "UPDATE products SET price = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDouble(1, unitPrice);
            stmt.setInt(2, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating price: " + e.getMessage());
        }
    }

    @Override
    public void updateQty(int id, int qty) {
        String sql = "UPDATE products SET qty = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, qty);
            stmt.setInt(2, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating quantity: " + e.getMessage());
        }
    }

    @Override
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("qty"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting product by id: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY id ASC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("qty")));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all products: " + e.getMessage());
        }
        return products;
    }

    @Override
    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    @Override
    public void backUp(String fileName) {
        String timeStamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDate.now());
        String fullFilePath = BACKUP_DIRECTORY + sanitizeFilename(fileName) + "_" + timeStamp + ".csv";

        try {
            File directory = new File(BACKUP_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String sql = "SELECT id, name, price, qty, import_date FROM products"; //Specify the column explicitly to avoid *

            try (PreparedStatement stmt = connection.prepareStatement(sql); //Use PreparedStatement instead of Statement to avoid SQL injection.
                 ResultSet rs = stmt.executeQuery();
                 BufferedWriter writer = new BufferedWriter(new FileWriter(fullFilePath))) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("qty");
                    Date date = rs.getDate("import_date");

                    writer.write(id + "," + name + "," + price + "," + quantity + "," + date + "\n");
                }
                System.out.println("Backup file created: " + fullFilePath);

            } catch (SQLException ex) {
                System.err.println("Error fetching data from the database: " + ex.getMessage());  //Use logging framework instead
            } catch (IOException ex) {
                System.err.println("Error writing to file: " + ex.getMessage());  //Use logging framework instead
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());  //Use logging framework instead
        }
    }

    private String sanitizeFilename(String filename) {
        // Implement robust filename sanitization here.  This is essential to prevent
        // directory traversal attacks.  For example:
        return filename.replaceAll("[^a-zA-Z0-9_-]", ""); //Remove everything except letters, numbers and _-
    }

    @Override
    public List<Product> getProductsByName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("qty")));
            }
        } catch (SQLException e) {
            System.out.println("Error getting products by name: " + e.getMessage());
        }
        return products;
    }

    @Override
    public List<Product> getProductsByPage(int page, int rowsPerPage) {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * rowsPerPage;

        String sql = "SELECT * FROM products ORDER BY id ASC LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rowsPerPage);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("qty")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products by page: " + e.getMessage());
        }
        return products;
    }

    @Override
    public int getTotalRecords() {
        int totalRecords = 0;
        String sql = "SELECT COUNT(*) FROM products";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error counting records: " + e.getMessage());
        }
        return totalRecords;
    }

    @Override
    public List<Product> reStore(String filename) {
        List<Product> products = new ArrayList<>();
        String filePath = "C:/Users/USER/Desktop/IntelliJ/StockManagementSystem/src/File/" + filename + ".csv";

        //Delete old data
        String deleteSql = "DELETE FROM products";
        String resetSql = "ALTER SEQUENCE products_id_seq RESTART WITH 1";

        try (Connection conn = Connect.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteSql);
            System.out.println("All records deleted from the table.");
            stmt.executeUpdate(resetSql);
            System.out.println("Auto-increment value reset to 1.");
        } catch (SQLException e) {
            System.out.println("Error deleting records from the table.");
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int quantity = Integer.parseInt(parts[3].trim());
                    Date importDate = Date.valueOf(parts[4].trim());

                    Product product = new Product(id, name, price, quantity);
                    addProduct(product);
                    products.add(product);

                } catch ( ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                    System.out.println("Error parsing line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data from file: " + e.getMessage());
        }
        return products;
    }
}