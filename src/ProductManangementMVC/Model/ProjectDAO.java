package ProductManangementMVC.Model;

import ProductManangementMVC.Model.Product;

import java.util.List;

public interface ProjectDAO {

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
    final ProjectDAO projectDAO = new ProjectDAOImpl();
    int getTotalProductCount();
}