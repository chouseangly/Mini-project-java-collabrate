package ProductManagementMVC.Controller;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import ProductManagementMVC.Constant.Constant;
import ProductManagementMVC.Model.ProductDao;
import ProductManagementMVC.Model.ProductDaoImpl;
import ProductManagementMVC.Utils.Config;
import ProductManagementMVC.Model.Validate;
import ProductManagementMVC.View.ProductView;
public class ProductController {
    private ProductDaoImpl pim;
    private ProductView pv;
    private final ArrayList<ProductDao> tempInsertProducts;
    private final ArrayList<ProductDao> tempUpdateProducts;
    private final String backupDirectory = "backups/";
    Validate v = new Validate();
    Constant c = new Constant();

    public ProductController(ProductDaoImpl pim, ProductView pv) {
        this.pim = pim;
        this.pv = pv;
        this.tempInsertProducts = new ArrayList<>();
        this.tempUpdateProducts = new ArrayList<>();
        createBackupDirectoryIfNotExists();
    }

    private void createBackupDirectoryIfNotExists() {
        File directory = new File(backupDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void startApplication() {
        try {
            List<ProductDao> products = pim.getAllProducts();
        } catch (Exception e) {
            System.err.println(c.RED + "An error occurred: " + e.getMessage() + c.RESET);
        }
    }

    public int generateNewProductId(List<ProductDao> products, List<ProductDao> tempoLists) {
        int maxDbId = products.stream()
                .mapToInt(ProductDao::getId)
                .max()
                .orElse(0);

        int maxTempId = tempoLists.stream()
                .mapToInt(ProductDao::getId)
                .max()
                .orElse(0);

        return Math.max(maxDbId, maxTempId) + 1;

    }

    public void createNewProduct() {
        try {

            ArrayList<ProductDao> initialProductLists = pim.getAllProducts();
            int newId = generateNewProductId(initialProductLists, tempInsertProducts);

            ProductDao product = pv.getInputForNewProduct(newId);

            product.setId(newId);

            product.setImportDate(LocalDate.now());

            tempInsertProducts.add(product);

            System.out.println(c.GREEN + "Product added to the temporary list. Use 'Save' to push to the database." + c.RESET);

        } catch (Exception e) {
            System.err.println(c.RED + "An error occurred while creating the product: " + e.getMessage() + c.RESET);
        }
    }

    public void displayUnsavedProductsForInsert() {
        if (tempInsertProducts.isEmpty()) {
            System.out.println(c.RED + "No unsaved products available for insertion." + c.RESET);

            return;
        }

        System.out.println(c.GREEN + "Unsaved Products Available for Insertion:" + c.RESET);
        pv.displayProducts(tempInsertProducts);
    }

    public void displayUnsavedProductsForUpdate() {
        if (tempUpdateProducts.isEmpty()) {
            System.out.println(c.RED + "No unsaved products available for updating." + c.RESET);

            return;
        }

        System.out.println(c.GREEN + "Unsaved Products Available for Update:" + c.RESET);
        pv.displayProducts(tempUpdateProducts); // Display the unsaved products
    }

    public void saveUnsavedInsertProducts() {
        if (tempInsertProducts.isEmpty()) {
            System.out.println(c.RED + "No unsaved insert products to save." + c.RESET);

            return;
        }

        try {
            for (ProductDao product : tempInsertProducts) {
                pim.addProduct(product); // Insert each product into the database
            }

            System.out.println(c.GREEN + tempInsertProducts.size() + " insert product(s) saved to the database." + c.RESET);
            tempInsertProducts.clear(); // Clear the temporary insert list after saving

            // Refetch the latest data from the database
            List<ProductDao> updatedProducts = pim.getAllProducts();
            // Pass the updated products back to the view
            pv.displayAllProductsAndMenu(updatedProducts, this);
        } catch (Exception e) {
            System.err.println(c.RED + "An error occurred while saving insert products: " + e.getMessage() + c.RESET);
        }
    }

    private boolean productExistsInTempUpdateList(int productId) {
        return tempUpdateProducts.stream()
                .anyMatch(product -> product.getId() == productId);
    }

    public ProductDao displayUpdateProductTable(int productId) throws SQLException {

        boolean isProductExistsInTempUpdateList = productExistsInTempUpdateList(productId);

        Optional<ProductDao> productToUpdate;
        if (isProductExistsInTempUpdateList) {
            productToUpdate = tempUpdateProducts.stream()
                    .filter(product -> product.getId() == productId)
                    .findFirst();
        } else {
            productToUpdate = pim.getAllProducts().stream()
                    .filter(product -> product.getId() == productId)
                    .findFirst();
        }

        if (productToUpdate.isPresent()) {
            ProductDao product = productToUpdate.get();

            if (!tempUpdateProducts.contains(product)) {
                tempUpdateProducts.add(product);
            }

            return product;
        } else {
            System.out.println(c.RED + "Product with ID " + productId + " not found." + c.RESET);
            return null;
        }

    }

    public void updateProductTable(int productId, String name, Double unitPrice, Integer stockQty) {

        Optional<ProductDao> productToUpdate = tempUpdateProducts.stream()
                .filter(product -> product.getId() == productId)
                .findFirst();

        if (productToUpdate.isPresent()) {
            // Update the existing product
            ProductDao product = productToUpdate.get();
            if (name != null) {
                product.setName(name);
            }
            if (unitPrice != null) {
                product.setUnitPrice(unitPrice);
            }
            if (stockQty != null) {
                product.setStockQty(stockQty);
            }

            System.out.println(c.GREEN + "Product updated in the temporary update list." + c.RESET);
        } else {
            // Fetch the product from the database and add it to the temporary update list
            try {
                ProductDao product = displayUpdateProductTable(productId);
                if (product != null) {
                    if (name != null) {
                        product.setName(name);
                    }
                    if (unitPrice != null) {
                        product.setUnitPrice(unitPrice);
                    }
                    if (stockQty != null) {
                        product.setStockQty(stockQty);
                    }

                    tempUpdateProducts.add(product); // Add the updated product to the list
                    System.out.println(c.GREEN + "Product added to the temporary update list." + c.RESET);

                }
            } catch (SQLException e) {
                System.err.println(c.RED + "An error occurred while fetching the product: " + e.getMessage() + c.RESET);
            }
        }
    }

    public void saveUnsavedUpdateProducts() {
        if (tempUpdateProducts.isEmpty()) {
            System.out.println(c.RED + "No unsaved update products to save." + c.RESET);
            return;
        }

        try {
            for (ProductDao product : tempUpdateProducts) {
                // Extract fields from the product
                int productId = product.getId();
                String name = product.getName();
                Double unitPrice = product.getUnitPrice();
                Integer stockQty = product.getStockQty();

                pim.updateProduct(productId, name, unitPrice, stockQty);
            }

            System.out.println(c.GREEN + tempUpdateProducts.size() + " update product(s) saved to the database." + c.RESET);
            tempUpdateProducts.clear(); // Clear the temporary update list after saving

            // Refetch the latest data from the database
            List<ProductDao> updatedProducts = pim.getAllProducts();
            pv.displayAllProductsAndMenu(updatedProducts, this);
        } catch (Exception e) {
            System.err.println(c.RED + "An error occurred while saving update products: " + e.getMessage() + c.RESET);
        }
    }

    public ProductDao displayDeleteProductById(int productId) throws SQLException {
        Optional<ProductDao> product = pim.getAllProducts().stream().filter(productFilter -> productFilter.getId() == productId).findFirst();

        return product.orElse(null);
    }

    public void deleteProductById(int productId) throws SQLException {
        pim.deleteProductById(productId);

        // Refetch the latest data from the database
        List<ProductDao> updatedProducts = pim.getAllProducts();

        pv.displayAllProductsAndMenu(updatedProducts, this);
    }

    public void searchProductsByName(String name) {
        try {
            List<ProductDao> products = pim.searchProductByName(name);

            if (products.isEmpty()) {
                System.out.println(c.RED + "No products found matching the search term: " + name + c.RESET);
            } else {
                System.out.println(c.BLUE + "Search Results : " + c.RESET);
                pv.displayProductsByName(products);
            }
        } catch (SQLException e) {
            System.err.println(c.RED + "An error occurred while searching for products: " + e.getMessage() + c.RESET);
        }
    }

//    public int getRowLimit() {
//        try {
//            return pim.getRowLimit();
//        } catch (SQLException e) {
//            System.err.println("An error occurred while fetching the row limit: " + e.getMessage());
//            return 3; // Default value
//        }
//    }

    public void updateRowLimit(int rowLimit) {
        try {
            pim.updateRowLimit(rowLimit);
        } catch (SQLException e) {
            System.err.println(c.RED + "An error occurred while updating the row limit: " + e.getMessage() + c.RESET);
        }
    }

    public void executeRestore(String backupFilePath) {
        String choice = v.validateInput(c.ARE_YOU_SURE, c.YELLOW + "=> Are you sure you want to restore the data? (y/n) : " + c.RESET, s -> s).trim().toUpperCase();

        if (choice.equals("Y")) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(
                        "C:\\Program Files\\PostgreSQL\\17\\bin\\pg_restore",
                        Config.get("DB_NAME_PG"), //pg_restore
                        "--clean",
                        backupFilePath
                );
                Process process = processBuilder.start();
                int exitCode = process.waitFor();

                if (exitCode == 0) {
                    System.out.println(c.GREEN + "Restore completed successfully!" + c.RESET);
                } else {
                    System.err.println(c.RED + "Restore failed. Exit code: " + exitCode + c.RESET);
                }
            } catch (Exception e) {
                System.err.println(c.RED + "Restore error: " + e.getMessage() + c.RESET);
            }
        } else {
            System.out.println(c.RED + "Restore canceled." + c.RESET);
        }
    }

    public void handleRestore() {
    }

    public ProductDao fetchProductById() {
        return null;
    }
}
