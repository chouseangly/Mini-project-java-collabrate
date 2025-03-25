package model;

import java.sql.*;
import java.util.Date;
import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts();
    void deleteProduct(int id);
    void addProduct(Product product);
    void updateProduct(Product product);
    void getProductById(int id);
}
