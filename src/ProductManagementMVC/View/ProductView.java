package ProductManagementMVC.View;

import ProductManagementMVC.Model.Product;

import java.util.List;

public class ProductView {
    public static void displayMenu(List<Product> products)
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