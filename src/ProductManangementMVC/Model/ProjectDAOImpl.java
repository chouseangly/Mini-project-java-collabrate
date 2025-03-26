package ProductManangementMVC.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {

    @Override
    public void addProduct(Product product) {
        String query = "INSERT INTO product (Name, Uint_Price, Qty, Import_Date, Import_Time) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Connect.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getUnitPrice());
            ps.setInt(3, product.getQty());
            ps.setDate(4, Date.valueOf(product.getImportDate()));
            ps.setTime(5, Time.valueOf(product.getImportTime()));

            ps.executeUpdate();
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";
        try (Connection conn = Connect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getDouble("Uint_Price"),
                        rs.getInt("Qty"),
                        rs.getDate("Import_Date").toLocalDate(),
                        rs.getTime("Import_Time").toLocalTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> getProductsByPage(int page, int rowsPerPage) {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * rowsPerPage;

        String query = "SELECT * FROM product LIMIT ? OFFSET ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, rowsPerPage);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getDouble("Uint_Price"),
                        rs.getInt("Qty"),
                        rs.getDate("Import_Date").toLocalDate(),
                        rs.getTime("Import_Time").toLocalTime()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }

    @Override
    public int getTotalRecords() {
        int totalRecords = 0;
        String query = "SELECT COUNT(*) FROM product";

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

    @Override
    public void updateProduct(Product product) {
        String query = "UPDATE product SET Name = ?, Uint_Price = ?, Qty = ?, Import_Date = ?, Import_Time = ? WHERE ID = ?";
        try (Connection con = Connect.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getUnitPrice());
            ps.setInt(3, product.getQty());
            ps.setDate(4, Date.valueOf(product.getImportDate()));
            ps.setTime(5, Time.valueOf(product.getImportTime()));
            ps.setInt(6, product.getId());

            ps.executeUpdate();
            System.out.println("Product updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        String query = "DELETE FROM product WHERE ID = ?";
        try (Connection con = Connect.getConnection()) {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, productId);

            ps.executeUpdate();
            System.out.println("Product deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Product> searchProduct(String keyword) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE Name LIKE ?";
        try (Connection conn = Connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getDouble("Uint_Price"),
                        rs.getInt("Qty"),
                        rs.getDate("Import_Date").toLocalDate(),
                        rs.getTime("Import_Time").toLocalTime()
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error searching products: " + e.getMessage());
        }
        return products;
    }
}
