import controller.ProductController;
import model.ProductDaoImpl;
import view.ProductView;

public class Main {
    public static void main(String[] args) {
        ProductDaoImpl productDao = new ProductDaoImpl();
        ProductController controller = new ProductController(productDao);
        ProductView view = new ProductView(controller);

        // Load initial data and start the view
        view.displayProducts(controller.getAllProducts());
    }
}