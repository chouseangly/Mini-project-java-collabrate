// ProductController.java

package ProductManagementMVC.Controll;

import ProductManagementMVC.Model.*;
import ProductManagementMVC.View.ProductView;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductController {
    private List<Product> products;
    private int currentPage = 1;
    private int row_per_page = 3;
    private String updateType;
    private List<Product> arrProducts;
    private List<Product> arrProducts2;
    private int id;
    private ProductDao productDao; // Changed to interface type
    private Validate validate;
    private Scanner scanner;
    private int isSave = 0;

    private ProductView productView;

    public ProductController() {
        this.scanner = new Scanner(System.in);
        this.productDao = new ProductDaoImpl(); // Implementation is fine here
        this.arrProducts = new ArrayList<>();
        this.arrProducts2 = new ArrayList<>();
        this.validate = new Validate();
        this.productView = new ProductView(this); // Inject controller into view
    }

    public void start() {
        displayProducts();
        productView.start();
    }

    public void processInput(String choice) {
        switch (choice.toLowerCase()) {
            case "n":
                nextPage();
                break;
            case "p":
                previousPage();
                break;
            case "f":
                firstPage();
                break;
            case "l":
                lastPage();
                break;
            case "g":
                gotoPage();
                break;
            case "w":
                write();
                break;
            case "r":
                read();
                break;
            case "u":
                update();
                break;
            case "d":
                delete();
                break;
            case "s":
                search();
                break;
            case "se":
                setRow();
                break;
            case "sa":
                save();
                break;
            case "un":
                unsaved();
                break;
            case "ba":
                backUp();
                break;
            case "re":
                reStore();
                break;
            case "e":
                System.out.println("(^_^) Good Bye (^_^)");
                scanner.close();
                System.exit(0); // Terminate the program
            default:
                productView.displayMessage("Invalid choice");
        }
    }

    private void showAllProducts() {
        products = productDao.getAllProducts();
    }

    private void displayProducts() {
        List<Product> products = productDao.getProductsByPage(currentPage, row_per_page);
        productView.displayProducts(products);

        int totalRecords = productDao.getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / row_per_page);

        productView.displayPaginationInfo(currentPage, totalPages, totalRecords);
    }

    // Pagination methods
    private void nextPage() {
        int totalRecords = productDao.getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / row_per_page);

        if (currentPage < totalPages) {
            currentPage++;
            displayProducts();
        } else {
            productView.displayMessage("You are already on the last page.");
        }
    }

    private void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            displayProducts();
        } else {
            productView.displayMessage("You are already on the first page.");
        }
    }

    private void firstPage() {
        currentPage = 1;
        displayProducts();
    }

    private void lastPage() {
        int totalRecords = productDao.getTotalRecords();
        currentPage = (int) Math.ceil((double) totalRecords / row_per_page);
        displayProducts();
    }

    private void gotoPage() {
        productView.displayMessage("Enter the page number: ");
        int pageNumber = validate.validateIntOption();
        scanner.nextLine();  // Consume newline

        int totalRecords = productDao.getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / row_per_page);

        if (pageNumber >= 1 && pageNumber <= totalPages) {
            currentPage = pageNumber;
            displayProducts();
        } else {
            productView.displayMessage("Invalid page number. Please enter a number between 1 and " + totalPages);
        }
    }

    // CRUD operations
    private void write() {
        productView.displayMessage("Enter product name: ");
        String name = validate.validateProductName();
        productView.displayMessage("Enter product unitPrice: ");
        double unitPrice = validate.validatePrice();
        productView.displayMessage("Enter product qty: ");
        int qty = validate.validateIntOption();
        arrProducts.add(new Product(name, unitPrice, qty));
        productView.displayMessage("Product added to temporary storage");
        isSave = 0;
    }

    private void read() {
        productView.displayMessage("Enter product ID to view: ");
        id = validate.validateIntOption();

        Product product = productDao.getProductById(id);
        if (product != null) {
            productView.displayProductDetails(product);
        } else {
            productView.displayMessage("Product not found");
        }
    }

    private void update() {
        productView.displayMessage("Enter product ID to update: ");
        id = validate.validateIntOption();

        Product product = productDao.getProductById(id);
        if (product == null) {
            productView.displayMessage("Product not found");
            return;
        }

        // Display product info
        productView.displayMessage("Current product details:");
        productView.displayProductDetails(product);

        productView.displayUpdateOptions();
        int option = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (option) {
            case 1:
                updateName();
                break;
            case 2:
                updateUnitPrice();
                break;
            case 3:
                updateQty();
                break;
            case 4:
                updateAll();
                break;
            case 5:
                return;
            default:
                productView.displayMessage("Invalid choice");
        }
    }

    private void updateName() {
        productView.displayMessage("Enter new name: ");
        String name = validate.validateProductName();
        Product product = new Product(id, name, 0, 0); // Other fields will be set from DB
        arrProducts2.add(product);
        updateType = "name";
        isSave = 0;
        productView.displayMessage("Name update prepared (not saved to database yet)");
    }

    private void updateUnitPrice() {
        productView.displayMessage("Enter new unit price: ");
        double unitPrice = validate.validatePrice();
        Product product = new Product(id, "", unitPrice, 0); // Other fields will be set from DB
        arrProducts2.add(product);
        updateType = "unitPrice";
        isSave = 0;
        productView.displayMessage("Unit price update prepared (not saved to database yet)");
    }

    private void updateQty() {
        productView.displayMessage("Enter new quantity: ");
        int qty = validate.validateIntOption();
        Product product = new Product(id, "", 0, qty); // Other fields will be set from DB
        arrProducts2.add(product);
        updateType = "qty";
        isSave = 0;
        productView.displayMessage("Quantity update prepared (not saved to database yet)");
    }

    private void updateAll() {
        productView.displayMessage("Enter new name: ");
        String name = validate.validateProductName();
        productView.displayMessage("Enter new unit price: ");
        double unitPrice = validate.validatePrice();
        productView.displayMessage("Enter new quantity: ");
        int qty = validate.validateIntOption();

        Product product = new Product(id, name, unitPrice, qty);
        arrProducts2.add(product);
        updateType = "all";
        isSave = 0;
        productView.displayMessage("Complete update prepared (not saved to database yet)");
    }

    private void delete() {
        productView.displayMessage("Enter product ID to delete: ");
        int id = validate.validateIntOption();
        productDao.deleteProduct(id);
        productView.displayMessage("Product deleted successfully");
    }

    private void search() {
        productView.displayMessage("Enter product name to search: ");
        String name = validate.validateProductName();
        List<Product> foundProducts = productDao.getProductsByName(name);

        if (foundProducts.isEmpty()) {
            productView.displayMessage("No products found");
            return;
        }

        productView.displayProducts(foundProducts);
    }

    private void setRow() {
        productView.displayMessage("Enter number of rows per page: ");
        row_per_page = validate.validateIntOption();
        productView.displayMessage("Rows per page set to: " + row_per_page);
    }

    private void save() {
        productView.displayMessage("'ui' to save new products, 'uu' to save updates, or 'b' to go back");
        productView.displayMessage("Enter your choice: ");
        String choice = validate.validateChartOption().toLowerCase();

        switch (choice) {
            case "ui":
                saveUi();
                break;
            case "uu":
                saveUu();
                break;
            case "b":
                return;
            default:
                productView.displayMessage("Invalid choice");
        }
    }

    private void saveUi() {
        if (arrProducts.isEmpty()) {
            productView.displayMessage("No new products to save");
            return;
        }

        for (Product product : arrProducts) {
            productDao.addProduct(product);
        }
        productView.displayMessage("Saved " + arrProducts.size() + " new products to database");
        arrProducts.clear();
        isSave = 1;
    }

    private void saveUu() {
        if (arrProducts2.isEmpty()) {
            productView.displayMessage("No updates to save");
            return;
        }

        for (Product product : arrProducts2) {
            switch (updateType) {
                case "name":
                    productDao.updateName(product.getId(), product.getName());
                    break;
                case "unitPrice":
                    productDao.updateUnitPrice(product.getId(), product.getUnitPrice());
                    break;
                case "qty":
                    productDao.updateQty(product.getId(), product.getQty());
                    break;
                case "all":
                    productDao.updateProduct(product.getId(), product.getName(),
                            product.getUnitPrice(), product.getQty());
                    break;
            }
        }
        productView.displayMessage("Saved " + arrProducts2.size() + " updates to database");
        arrProducts2.clear();
        isSave = 1;
    }

    private void unsaved() {
        productView.displayMessage("'ui' for unsaved new products, 'uu' for unsaved updates, or 'b' to go back");
        productView.displayMessage("Enter your option: ");
        String option = validate.validateChartOption().toLowerCase();

        switch (option) {
            case "ui":
                unsavedUi();
                break;
            case "uu":
                unsavedUu();
                break;
            case "b":
                return;
            default:
                productView.displayMessage("Invalid choice");
        }
    }

    private void unsavedUi() {
        if (isSave == 1 || arrProducts.isEmpty()) {
            productView.displayMessage("No unsaved new products");
            return;
        }

        productView.displayMessage("\nUnsaved new products:");
        System.out.println("+------+----------------------+------------+-------+");
        System.out.println("|  ID  |        Name          | Unit Price |  Qty  |");
        System.out.println("+------+----------------------+------------+-------+");

        for (Product product : arrProducts) {
            System.out.printf("| %4d | %-20s | %10.2f | %5d |\n",
                    product.getId(),
                    product.getName(),
                    product.getUnitPrice(),
                    product.getQty());
        }
        System.out.println("+------+----------------------+------------+-------+");
    }

    private void unsavedUu() {
        if (isSave == 1 || arrProducts2.isEmpty()) {
            productView.displayMessage("No unsaved updates");
            return;
        }

        productView.displayMessage("\nUnsaved updates:");
        System.out.println("+------+----------------------+------------+-------+");
        System.out.println("|  ID  |        Name          | Unit Price |  Qty  |");
        System.out.println("+------+----------------------+------------+-------+");

        for (Product product : arrProducts2) {
            System.out.printf("| %4d | %-20s | %10.2f | %5d |\n",
                    product.getId(),
                    product.getName(),
                    product.getUnitPrice(),
                    product.getQty());
        }
        System.out.println("+------+----------------------+------------+-------+");
    }

    private void backUp() {
        productView.displayMessage("Enter file name for backup: ");
        String filename = scanner.nextLine();
        productDao.backUp(filename);
        productView.displayMessage("Backup completed successfully");
    }

    private void reStore() {
        productView.displayMessage("Enter file name to restore from: ");
        String filename = scanner.nextLine();
        List<Product> restoredProducts = productDao.reStore(filename);

        if (restoredProducts == null || restoredProducts.isEmpty()) {
            productView.displayMessage("No products restored");
            return;
        }

        for (Product product : restoredProducts) {
            productDao.addProduct(product);
        }
        productView.displayMessage("Restored " + restoredProducts.size() + " products successfully");
    }

    public void menu() {
    }
}