package ProductManagementMVC.Model;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts();

    void deleteProduct(int id);

    void addProduct(Product product);

    void updateProduct(int id, String name, double unitPrice, int qty);

    Product getProductById(int id);

    void backUp(String fileName);

    void updateName(int id, String name);

    void updateUnitPrice(int id, double unitPrice);

    void updateQty(int id, int qty);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByPage(int page, int rowsPerPage);

    int getTotalRecords();

    List<Product> reStore(String filename);
}