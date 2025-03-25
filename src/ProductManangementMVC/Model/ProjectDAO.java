package ProductManangementMVC.Model;

import java.util.List;

public interface ProjectDAO {
    void addProduct(Product product);
    List<Product> getAllProduct();
}
