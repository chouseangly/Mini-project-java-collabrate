package ProductManangementMVC.Model;

import java.util.List;

public interface ProductDAO {
    List<Product> getAllProduct();
    List<Product> addProductToDB(List<Product> products);
    boolean deleteProductFromDB(int id);
    List<Product> searchProductsByName(String name);
    void read();
    Product getStockItemByID(int ID);
    void backupToCSV();
    void tempProductList(Product product);
    List<Product> getTempProductList();
    void clearTempProducts();
    int getSetRow();
    void setRow(int pageSize);
    Product updateProductInTempList();
    List<Product> getAllProductsFromDB();
    void updateProductIntoDB();
    void unsaveInsertProducts();
    void unsaveUpdateProducts();
    void restoreFromCSV();

}