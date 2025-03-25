package ProductManangementMVC.View;

import ProductManangementMVC.Model.Product;

import java.util.List;

public class ProductView {
    public static void displayProducts(List<Product> products)
    {
        System.out.println("Product List");
        for(Product product : products)
        {
            System.out.println(product);
        }

    }
    public void showMessage(String message)
    {
        System.out.println(message);
    }



}
