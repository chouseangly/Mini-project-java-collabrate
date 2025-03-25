package ProductManangementMVC;

import ProductManangementMVC.Controll.ProductController;
import ProductManangementMVC.Model.ProjectDAOImpl;
import ProductManangementMVC.View.ProductView;

public class Main {
    public static void main(String[] args) {
        ProductController productController = new ProductController(new ProjectDAOImpl(), new ProductView());
            productController.showProducts();


    }
}
