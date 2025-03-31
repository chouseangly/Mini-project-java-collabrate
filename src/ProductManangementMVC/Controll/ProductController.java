package ProductManangementMVC.Controll;

import ProductManangementMVC.Model.Product;
import ProductManangementMVC.Model.ProductDAO;
import ProductManangementMVC.View.ProductView;

import java.time.LocalDate;
import java.util.List;

public class ProductController {
    private final ProductDAO productDAO;
    private final ProductView productView;

    public ProductController(ProductDAO productDAO, ProductView productView) {
        this.productDAO = productDAO;
        this.productView = productView;
    }

    // Pagination
    public int getPageSize() {
        return productDAO.getSetRow();
    }

    public void setPageSize(int pageSize) {
        productDAO.setRow(pageSize);
    }

    // Product operations
    public List<Product> getAllProducts() {
        return productDAO.getAllProduct();
    }

    public void addProduct(int id ,String name, double price, int qty, LocalDate date) {
        productDAO.tempProductList(new Product(id,name, price, qty, date));
    }


    public boolean deleteProduct(int id) {
        return productDAO.deleteProductFromDB(id);
    }

    public List<Product> searchProducts(String name) {
        return productDAO.searchProductsByName(name);
    }
    public  void read(){
     productDAO.read();
    }


    public void saveToDatabase() {
        productDAO.addProductToDB(productDAO.getTempProductList());
        productDAO.clearTempProducts();
        productView.showMessage("Products saved to database");
    }

    public void updateInDB() {
        productDAO.updateProductIntoDB();
    }


    public void createBackup() {
        productDAO.backupToCSV();
    }
    public Product updateProductInTempList(){
        productDAO.updateProductInTempList();
        return null;
    }
    List<Product> getAllProductsFromDB(){
        return productDAO.getAllProductsFromDB();
    }
    public Product getStockItemByID(int ID){
        return productDAO.getStockItemByID(ID);
    }

    public void unsaveUpdateProducts() {
        productDAO.unsaveUpdateProducts();
    }
    public void unsaveInsertProducts() {
        productDAO.unsaveInsertProducts();
    }
    public void restoreFromCSV() {
        productDAO.restoreFromCSV();
    }



}