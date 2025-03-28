package ProductManangementMVC.Controll;

import ProductManangementMVC.Model.Product;
import ProductManangementMVC.Model.ProjectDAO;
import ProductManangementMVC.View.ProductView;
import ProductManangementMVC.Model.ProjectDAOImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductController {
    private final Scanner scanner = new Scanner(System.in);
    private final ProjectDAO projectDAO = new ProjectDAOImpl();
    private final ProductView productView = new ProductView();
    private List<Product> updatedProducts = new ArrayList<>();
    private int ROWS_PER_PAGE = 5; // Rows per page
    private int currentPage = 1;  // Current page number
    private List<Product> temporaryProducts = new ArrayList<>(); // Temporary storage for products

    public void startApplication() {
        String choice;
        do {
            displayProductsFromDatabase();
            productView.displayMenu();
            System.out.print("⇒ Choose an option: ");
            choice = scanner.nextLine().trim().toUpperCase();
            switch (choice) {
                case "W": insertProduct(); break;
                case "R": displayProducts(); break;
                case "U": updateProduct(); break;
                case "D": deleteProduct(); break;
                case "S": searchProduct(); break;
                case "SA": save(); break;
                case "UN": unsaved(); break;
                case "BA": backupProducts(); break;
                case "RE": restoreProducts(); break;
                case "N": nextPage(); break;
                case "P": previousPage(); break;
                case "F": firstPage(); break;
                case "L": lastPage(); break;
                case "G": gotoPage(); break;
                case "SE": setRowsPerPage(); break;
                case "E": System.out.println("Exiting the program. Goodbye!"); break;
                default: System.out.println("Invalid choice! Please choose a valid option.");
            }
        } while (!choice.equals("E"));
    }

    private void insertProduct() {
        Product product = productView.insertProduct(temporaryProducts.size() + 1);
        temporaryProducts.add(product);
    }

    private void displayProducts() {
        productView.displayProducts(temporaryProducts, currentPage, ROWS_PER_PAGE);
    }
    private void displayProductsFromDatabase() {
        List<Product> products = projectDAO.getProductsByPage(currentPage, ROWS_PER_PAGE); // Fetch paginated data from database
        int totalRecords = projectDAO.getTotalProductCount(); // Fetch total count of products from the database
        productView.displayProductsFromDatabase(products, currentPage, ROWS_PER_PAGE, totalRecords); // Display the fetched products
    }
    public void save() {
        if (temporaryProducts.isEmpty()) {
            System.out.println("No products in the collection to save.");
            return;
        }

        System.out.println("\'ui\' for save inserted products from collection to database and \'uu\' for save updated products to database or \'b\' for back to menu");
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine().trim().toUpperCase();

        switch (choice) {
            case "UI":
                saveUi(); // Save products from the collection to the database
                break;
            case "UU":
                saveUu(); // Save updated products to the database
                break;
            case "B":
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }
    private void saveUi() {
        if (temporaryProducts.isEmpty()) {
            System.out.println("No products in the collection to save to the database.");
            return;
        }

        try {
            for (Product product : temporaryProducts) {
                projectDAO.addProduct(product); // Save each product to the database
            }
            System.out.println("Products saved to the database successfully.");
            temporaryProducts.clear(); // Clear the collection after saving to the database
        } catch (Exception e) {
            System.out.println("An error occurred while saving products to the database.");
            e.printStackTrace();
        }
    }

    private void saveToDatabase() {
        if (temporaryProducts.isEmpty()) {
            System.out.println("No products to save.");
            return;
        }
        for (Product product : temporaryProducts) {
            projectDAO.addProduct(product);  // Save to the database
        }
//        temporaryProducts.clear();
        // Clear the temporary collection
        System.out.println("All products saved to the database.");
    }


    private void unsaveProduct() {
        int id = productView.promptForProductId("Enter the product ID to unsave: ");
        boolean deletedFromDB = projectDAO.deleteProduct(id);  // Remove from database
        boolean deletedFromCollection = temporaryProducts.removeIf(product -> product.getId() == id);  // Remove from collection

        if (deletedFromDB && deletedFromCollection) {
            System.out.println("Product with ID " + id + " unsaved successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }
    public void unsaved() {
        System.out.println("\'ui\' for unsaved inserted products and \'uu\' for unsaved updated products or \'b\' for back to menu");
        System.out.print("Enter your option: ");
        String option = scanner.nextLine().trim().toUpperCase();

        switch (option) {
            case "UI":
                unsaveUi(); // Display unsaved inserted products
                break;
            case "UU":
                unsaveUu(); // Display unsaved updated products
                break;
            case "B":
                return; // Return to the main menu
            default:
                System.out.println("Invalid option!");
        }
    }
    private void unsaveUi() {
        System.out.print("Enter the ID of the inserted product to unsave: ");
        int id = Integer.parseInt(scanner.nextLine());

        // Attempt to delete the product from the database
        boolean isDeleted = projectDAO.deleteProduct(id);

        if (isDeleted) {
            System.out.println("Inserted product with ID " + id + " successfully unsaved from the database.");
        } else {
            System.out.println("No inserted product with ID " + id + " found in the database.");
        }
    }
    private void unsaveUu() {
        System.out.print("Enter the ID of the updated product to unsave: ");
        int id = Integer.parseInt(scanner.nextLine());

        // Attempt to delete the product from the database
        boolean isDeleted = projectDAO.deleteProduct(id);

        if (isDeleted) {
            System.out.println("Updated product with ID " + id + " successfully unsaved from the database.");
        } else {
            System.out.println("No updated product with ID " + id + " found in the database.");
        }
    }
    private void backupProducts() {
        if (temporaryProducts.isEmpty()) {
            System.out.println("No products in the collection to backup.");
            return;
        }

        projectDAO.backupProductsToFile("backup.txt");
        System.out.println("Backup completed successfully.");
    }



    private void restoreProducts() {
        projectDAO.restoreProductsFromFile("backup.txt");  // Restore from backup file
        System.out.println("Products restored successfully.");
    }


    private void updateProduct() {
        int id = productView.promptForProductId("Enter the product ID to update: ");

        // Find the product in the temporary collection
        for (int i = 0; i < temporaryProducts.size(); i++) {
            if (temporaryProducts.get(i).getId() == id) {
                // Get updated details from the user
                Product updatedProduct = productView.updateProduct(id);

                // Replace the old product with the updated one
                temporaryProducts.set(i, updatedProduct);
                updatedProducts.add(updatedProduct); // Add to updatedProducts list
                System.out.println("Product updated successfully in the collection.");
                return;
            }
        }

        System.out.println("Product with ID " + id + " not found in the collection.");
    }
    private void saveUu() {
        if (updatedProducts.isEmpty()) {
            System.out.println("No updated products to save to the database.");
            return;
        }

        try {
            for (Product product : updatedProducts) {
                projectDAO.updateProduct(product); // Save updated product to the database
            }
            System.out.println("Updated products saved to the database successfully.");
            updatedProducts.clear(); // Clear the updatedProducts list after saving
        } catch (Exception e) {
            System.out.println("An error occurred while saving updated products to the database.");
            e.printStackTrace();
        }
    }

    private void deleteProduct() {
        int id = productView.promptForProductId("Enter the product ID to delete: ");

        // Check if product exists before deleting
        boolean removed = temporaryProducts.removeIf(p -> p.getId() == id);

        if (removed) {
            System.out.println("Product deleted successfully from the collection.");
        } else {
            System.out.println("Product not found.");
        }

        displayProducts(); // Refresh display after deletion
    }


    private void searchProduct() {
        String keyword = productView.promptForKeyword("Enter search keyword: ");
        List<Product> results = projectDAO.searchProduct(keyword);
        productView.displaySearchResults(results);
    }

    private void nextPage() {
        int totalRecords = temporaryProducts.size();
        int totalPages = (int) Math.ceil((double) totalRecords / ROWS_PER_PAGE);
        if (currentPage < totalPages) {
            currentPage++;
            displayProducts();
        } else {
            System.out.println("You are already on the last page.");
        }
    }

    private void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            displayProducts();
        } else {
            System.out.println("You are already on the first page.");
        }
    }

    private void firstPage() {
        currentPage = 1;
        displayProducts();
    }

    private void lastPage() {
        int totalRecords = temporaryProducts.size();
        currentPage = (int) Math.ceil((double) totalRecords / ROWS_PER_PAGE);
        displayProducts();
    }

    private void setRowsPerPage() {
        int rows = productView.promptForRowsPerPage("Enter the number of rows per page: ");
        if (rows > 0) {
            ROWS_PER_PAGE = rows;
            System.out.println("Rows per page set to " + ROWS_PER_PAGE);
            displayProducts();
        } else {
            System.out.println("Invalid input. Please enter a positive number.");
        }
    }

    private void gotoPage() {
        int pageNumber = productView.promptForPageNumber("Enter the page number: ");
        int totalRecords = temporaryProducts.size();
        int totalPages = (int) Math.ceil((double) totalRecords / ROWS_PER_PAGE);
        if (pageNumber >= 1 && pageNumber <= totalPages) {
            currentPage = pageNumber;
            displayProducts();
        } else {
            System.out.println("Invalid page number. Please enter a number between 1 and " + totalPages);
        }
    }
}