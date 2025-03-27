package ProductManagementMVC.Model;

import java.util.List;

public interface ProductDao {
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(int id);
    Product getProductById(int id);
    List<Product> getAllProduct();
    int getTotalRecords();
    List<Product> getProductsByPage(int page, int rowsPerPage);
    List<Product> searchProduct(String keyword);
    void backupProductsToFile(String filePath);
    void restoreProductsFromFile(String filePath);
    int getTotalProductCount();
}