import ProductManangementMVC.Model.Product;
import ProductManangementMVC.Model.ProjectDAOImpl;
import ProductManangementMVC.Model.Validate;
import ProductManangementMVC.View.ProductView;
import java.time.LocalDate;
import java.util.List;

public class ProductController {
    private ProjectDAOImpl pim;
    private ProductView pv;
    Validate v = new Validate();
    Constant c = new Constant();


    //start an application
    public void startApplication() {
        try {
            List<Product> products = pim.getAllProduct();

            pv.displayAllProductsAndMenu(products, this);
        } catch (Exception e) {
            System.err.println(c.RED + "An error occurred: " + e.getMessage() + c.RESET);
        }
    }

    public int generateNewProductId(List<Product> products, List<Product> tempoLists) {
        int maxDbId = products.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0);

        int maxTempId = tempoLists.stream()
                .mapToInt(Product::getId)
                .max()
                .orElse(0);
        return Math.max(maxDbId, maxTempId) + 1;

    }

    //Create new Product
    public void createNewProduct() {
        try {
            List<Product> initialProductLists = pim.getAllProduct();
            List<Product> tempInsertProducts = List.of();
            int newId = generateNewProductId(initialProductLists, tempInsertProducts);
            Product product = pv.getInputForNewProduct(newId);
            product.setId(newId);
            product.setImportDate(LocalDate.now());
            tempInsertProducts.add(product);
            System.out.println(c.GREEN + "Product added to the temporary list. Use 'Save' to push to the database." + c.RESET);

        } catch (Exception e) {
            System.err.println(c.RED + "An error occurred while creating the product: " + e.getMessage() + c.RESET);
        }
    }

}
