package ProductManagementMVC;

import ProductManagementMVC.Controll.ProductController;

public class Main {
    public static void main(String[] args) {
        // Create an instance of ProductController
        ProductController productController = new ProductController();
        // Start the application
        productController.startApplication();
    }
}