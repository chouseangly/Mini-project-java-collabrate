package model;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//Hello
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

            // Get the current date as java.sql.Date
            Date sqlDate = new Date(System.currentTimeMillis());

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getUnitPrice());
            stmt.setInt(3, product.getQty());
            stmt.setDate(4, sqlDate); // Use java.sql.Date instead of a String

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while trying to insert product");
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(int id, String name, double unitPrice, int qty) {
        String sql = "UPDATE products SET name = ?, price = ?, qty = ? WHERE id = ?";

        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set the values for the update query
            stmt.setString(1, name);
            stmt.setDouble(2, unitPrice);
            stmt.setInt(3, qty);
            stmt.setInt(4, id);

            // Execute the update query and check if any rows were affected
            int rowsUpdated = stmt.executeUpdate();

            // If rows were updated, return true, else return false
            if (rowsUpdated > 0) {
                System.out.println("Product updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public void updateName(int id, String name) {
        String sql = "UPDATE products SET name = ? WHERE id = ?";

        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set the parameters for the SQL query
            stmt.setString(1, name);
            stmt.setInt(2, id);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product name updated successfully.");
            } else {
                System.out.println("No product found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUnitPrice(int id, double unitPrice) {
        String sql = "UPDATE products SET price = ? WHERE id = ?";

        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set the parameters for the SQL query
            stmt.setDouble(1,unitPrice);
            stmt.setInt(2, id);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product name updated successfully.");
            } else {
                System.out.println("No product found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQty(int id, int qty) {
        String sql = "UPDATE products SET qty = ? WHERE id = ?";

        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set the parameters for the SQL query
            stmt.setInt(1,qty);
            stmt.setInt(2, id);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product name updated successfully.");
            } else {
                System.out.println("No product found with the given ID.");
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
                Date date = rs.getDate("import_date");
                products.add(new Product(id1, name, price, quantity, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Product product : products) {
            if (product.getId() == id) {
                return product.toString();
            }
        }
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>();
        String slectQuery = "SELECT * FROM products";
        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(slectQuery);
             Statement stmt1 = connection.createStatement();
             ResultSet rs = stmt1.executeQuery(slectQuery)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("qty");
                Date date = rs.getDate("import_date");
                products.add(new Product(id, name, price, quantity, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product deleted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while trying to insert product");
            e.printStackTrace();
        }
    }

    @Override
    public void backUp() {
        String file1 = "C:\\Users\\USER\\Desktop\\IntelliJ\\StockManagementSystem\\src\\File\\file2.svc";
        String sql = "Select * from products";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file1));
             Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             Statement stmt1 = connection.createStatement();
             ResultSet rs = stmt1.executeQuery(sql)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int quantity = rs.getInt("qty");
                    Date date = rs.getDate("import_date");
                    writer.write(String.valueOf(id));
                    writer.write(',');
                    writer.write(name);
                    writer.write(',');
                    writer.write(Double.toString(price));
                    writer.write(',');
                    writer.write(Integer.toString(quantity));
                    writer.write(',');
                    writer.write(date.toString());
                    writer.newLine();

                }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Product> getProductsByName(String name) {
        List<Product> products = new ArrayList<Product>();
        String slectQuery = "SELECT * FROM products where name = ?";
        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(slectQuery)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            // Process the result
            while (rs.next()) {
                int id = rs.getInt("id");
                String namee = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("qty");
                Date date = rs.getDate("import_date");
                products.add(new Product(id, namee, price, quantity, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public String searchById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(id));
            ResultSet rs = stmt.executeQuery();

            // Process the result
            while (rs.next()) {
                int id1 = rs.getInt("id");
                String namee = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("qty");
                Date date = rs.getDate("import_date");
                return id1 + "," + namee + "," + price + "," + quantity + "," + date.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> getProductsByPage(int page, int rowsPerPage) {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * rowsPerPage;

        String query = "SELECT * FROM products LIMIT ? OFFSET ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, rowsPerPage);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("qty"), rs.getDate("import_date")));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }
    @Override
    public int getTotalRecords() {
        int totalRecords = 0;
        String query = "SELECT COUNT(*) FROM products";

        try (Connection conn = Connect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                totalRecords = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error counting records: " + e.getMessage());
        }
        return totalRecords;
    }

    public List<Product> reStore() {
        String deleteSql = "DELETE FROM products";
        String resetSql = "ALTER SEQUENCE products_id_seq RESTART WITH 1";

        try (Connection conn = Connect.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteSql);
            System.out.println("All records deleted from the table.");
            stmt.executeUpdate(resetSql);
            System.out.println("Auto-increment value reset to 1.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        List<Product> products = new ArrayList<>();
//        String file2 = "C:\\Users\\USER\\Desktop\\IntelliJ\\StockManagementSystem\\src\\File\\file2.svc";
//        try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                int id = Integer.parseInt(parts[0]);
//                String name = parts[1];
//                double price = Double.parseDouble(parts[2]);
//                int quantity = Integer.parseInt(parts[3]);
//                Date date = new Date(Long.parseLong(parts[4]));
//                products.add(new Product(id, name, price, quantity, date));
//            }
//        } catch (IOException e) {
//            System.out.println("Error loading data from file: " + e.getMessage());
//        }
//        return products;
        List<Product> products = new ArrayList<>();
        String file2 = "C:\\Users\\USER\\Desktop\\IntelliJ\\StockManagementSystem\\src\\File\\file1.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                int quantity = Integer.parseInt(parts[3]);
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                java.util.Date date = null;
//                try {
//                    date = dateFormat.parse(parts[4]);   // Convert string date to Date object
//                } catch (ParseException e) {
//                    System.out.println("Error parsing date: " + parts[4]);
//                    e.printStackTrace();
//                }
                products.add(new Product(id, name, price, quantity));
            }
        } catch (IOException e) {
            System.out.println("Error loading data from file: " + e.getMessage());
        }

        return products;
    }
}
