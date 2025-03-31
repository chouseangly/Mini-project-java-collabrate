package ProductManagementMVC;
import ProductManagementMVC.Controller.ProductController;
import ProductManagementMVC.Model.Connect;
import ProductManagementMVC.Model.ProductDaoImpl;
import ProductManagementMVC.View.ProductView;
import java.sql.Connection;
import java.sql.SQLException;
public class Main {

    public static void main(String[] args) {
        try (Connection connection = Connect.getConnection()) {
            ProductDaoImpl productImplement = new ProductDaoImpl(connection);
            ProductView productView = new ProductView();
            ProductController productController = new ProductController(productImplement, productView);
            productController.startApplication();

        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
