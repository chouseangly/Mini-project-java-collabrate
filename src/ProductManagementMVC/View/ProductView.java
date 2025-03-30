package ProductManagementMVC.View;

import ProductManagementMVC.Controll.ProductController;
import ProductManagementMVC.Model.Product;

import java.util.List;
import java.util.Scanner;

public class ProductView {
    private ProductController productController;
    private Scanner scanner;

    public ProductView(ProductController productController) {
        this.productController = productController;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("\n+-----------------------------------------------------------------------+");
        System.out.println("|                         PRODUCT MANAGEMENT MENU                       |");
        System.out.println("+-----------------------------------------------------------------------+");
        System.out.println("| Navigation:                                                           |");
        System.out.println("|   N) Next Page    P) Previous Page    F) First Page    L) Last Page   |");
        System.out.println("|   G) Go To Page                                                       |");
        System.out.println("|                                                                       |");
        System.out.println("| Operations:                                                           |");
        System.out.println("|   W) Write        R) Read         U) Update        D) Delete           |");
        System.out.println("|   S) Search       Se) Set Rows    Sa) Save        Un) Unsaved         |");
        System.out.println("|   Ba) Backup      Re) Restore     E) Exit                             |");
        System.out.println("+-----------------------------------------------------------------------+");
        System.out.print("=> Choose an option: ");
    }

    public String getInput() {
        return scanner.nextLine();
    }

    public void displayMessage(String message) {
        System.out.println("\n" + message);
    }

    public void displayError(String error) {
        System.err.println("\nERROR: " + error);
    }

    public void displayProductDetails(Product product) {
        System.out.println("\n+------+----------------------+------------+-------+----------------+");
        System.out.println("|  ID  |        Name          | Unit Price |  Qty  |  Import Date   |");
        System.out.println("+------+----------------------+------------+-------+----------------+");
        System.out.printf("| %4d | %-20s | %10.2f | %5d | %-14s |\n",
                product.getId(),
                product.getName(),
                product.getUnitPrice(),
                product.getQty(),
                product.getImportDate());
        System.out.println("+------+----------------------+------------+-------+----------------+");
    }

    public void displayProducts(List<Product> products) {
        if (products == null || products.isEmpty()) {
            System.out.println("\nNo products found");
            return;
        }

        System.out.println("\n+------+----------------------+------------+-------+----------------+");
        System.out.println("|  ID  |        Name          | Unit Price |  Qty  |  Import Date   |");
        System.out.println("+------+----------------------+------------+-------+----------------+");

        for (Product product : products) {
            System.out.printf("| %4d | %-20s | %10.2f | %5d | %-14s |\n",
                    product.getId(),
                    product.getName(),
                    product.getUnitPrice(),
                    product.getQty(),
                    product.getImportDate());
        }
        System.out.println("+------+----------------------+------------+-------+----------------+");
    }

    public void displayPaginationInfo(int currentPage, int totalPages, int totalRecords) {
        System.out.printf("\nPage: %d of %d | Total Records: %d\n", currentPage, totalPages, totalRecords);
    }

    public void displayUpdateOptions() {
        System.out.println("\nUpdate Options:");
        System.out.println("1. Name        2. Unit Price     3. Quantity     4. All Fields     5. Cancel");
        System.out.print("Choose an option: ");
    }

    public void start() {
        while (true) {
            displayMenu();
            String choice = getInput();
            productController.processInput(choice); // Pass user input to controller
        }
    }

    public void view_Menu_Describe() {
        displayMenu();
    }
}