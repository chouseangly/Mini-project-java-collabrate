package ProductManangementMVC.Model;

import java.util.List;

public interface ProjectDAO {

    // Method to add a new product
    void addProduct(Product product);

    // Method to get all products
    List<Product> getAllProduct();

    // Method to get products by page for pagination
    List<Product> getProductsByPage(int page, int rowsPerPage);

    // Method to get the total number of records (for pagination)
    int getTotalRecords();

    // Method to update a product
    void updateProduct(Product product);

    // Method to delete a product by ID
    boolean deleteProduct(int productId);

    // Method to search for products by name or other attributes
    List<Product> searchProduct(String keyword);
}
