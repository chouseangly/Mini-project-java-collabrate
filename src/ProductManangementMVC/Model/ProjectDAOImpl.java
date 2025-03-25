package ProductManangementMVC.Model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {
    @Override
    public void addProduct(Product product) {
        String query = "insert into product(ID,Name,Uint_Price,Qty,Import_Date) values(?,?,?,?)";
        try(Connection con = Connect.getConnection()){
            PreparedStatement ps = con.prepareStatement(query);
          ps.setString(1,product.getName());
          ps.setDouble(2,product.getUnitPrice());
          ps.setInt(3,product.getQty());
          ps.setDate(4, Date.valueOf(product.getData()));
        }catch (SQLException e)
        {
            e.printStackTrace();

        }
    }

    @Override
    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<Product>();
        String query = "select * from StockManagment";
        try (Connection conn = Connect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                   int id = rs.getInt(1);
                   String name = rs.getString(2);
                   double unitPrice = rs.getDouble(3);
                   int qty = rs.getInt(4);
                   Date date = rs.getDate(5);
                LocalDate localDate =(date!=null)?date.toLocalDate():null;
                Product product = new Product(id,name,unitPrice,qty,localDate);
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
}
