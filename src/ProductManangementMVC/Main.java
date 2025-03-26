package ProductManangementMVC;

import ProductManangementMVC.Model.Product;
import ProductManangementMVC.Model.ProjectDAOImpl;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ProjectDAOImpl projectDAO = new ProjectDAOImpl();
    private static  int ROWS_PER_PAGE = 5;
    private static int currentPage = 1;

    private static List<Product> temporaryProducts = new ArrayList<>();  // Collection to hold products temporarily

    public static void main(String[] args) {
        handleProductManagement();
    }

    public static void handleProductManagement() {
        String choice;
        do {
            displayMenu();
            System.out.print("\n⇒ Choose an option: ");
            choice = scanner.nextLine().trim().toUpperCase();  // This converts all input to uppercase

            switch (choice) {
                case "W": insertProduct(); break;
                case "R": displayProducts(); break;
                case "U": updateProduct(); break;
                case "D": deleteProduct(); break;
                case "S": searchProduct(); break;
                case "SA": saveToDatabase(); break;  // Save to Database
                case "UN": unsaveProduct(); break;
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

    private static void displayMenu() {
        System.out.println("\nN) Next Page  P) Previous Page  F) First Page  L) Last Page  G) Goto\n");
        System.out.println("W) Write  R) Read  U) Update  D) Delete  S) Search  Se) Set Rows");
        System.out.println("Sa) Save  Un) Unsave  Ba) Backup  Re) Restore  E) Exit");
    }

    private static int productIdCounter = 1;

    private static Product insertProduct() {
        // Generate the next ID using the counter and increment it for the next product
        int id = productIdCounter++;

        // Display the ID first
        System.out.println("Product ID: " + id);

        // Now ask for product details
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product unit price: ");
        double unitPrice = scanner.nextDouble();

        System.out.print("Enter product quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        // Create a Product object with the generated ID
        Product product = new Product(id, name, unitPrice, qty, LocalDate.now(), LocalTime.now());

        // Temporarily store in the collection
        temporaryProducts.add(product);

        return product;
    }


    private static void saveToDatabase() {
        // Check if there are any products in the temporary list
        if (temporaryProducts.isEmpty()) {
            System.out.println("No products to save.");
            return;
        }

        // Loop through the products and save them to the database
        for (Product product : temporaryProducts) {
            projectDAO.addProduct(product);
        }

        System.out.println("All products saved to the database.");

        // Clear the temporary list after saving
        temporaryProducts.clear();
    }
    private static void unsaveProduct() {
        System.out.print("Enter the product ID to unsave: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Try to find and delete the product
        if (projectDAO.deleteProduct(id)) {
            System.out.println("Product unsaved successfully.");
        } else {
            System.out.println("Product not found.");
        }
    }
    private static void backupProducts() {
        List<Product> products = projectDAO.getAllProduct();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("backup.txt"))) {
            // Iterate through the products and write each product's details to the backup file
            for (Product product : products) {
                // Writing each product's data in the format: name,unitPrice,qty,importDate
                String productData = product.getName() + "," +
                        product.getUnitPrice() + "," +
                        product.getQty() + "," +
                        product.getImportDate();
                writer.write(productData);
                writer.newLine();  // Move to the next line for the next product
            }
            System.out.println("Products have been backed up.");
        } catch (IOException e) {
            System.out.println("Error while backing up products: " + e.getMessage());
        }
    }

    private static void restoreProducts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("backup.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Ensure proper line format (i.e., splitting by commas)
                String[] productData = line.split(",");

                if (productData.length == 4) {  // Ensure there are exactly 4 fields per product
                    try {
                        String name = productData[0].trim();  // Product name
                        double unitPrice = Double.parseDouble(productData[1].trim());  // Unit price
                        int qty = Integer.parseInt(productData[2].trim());  // Quantity
                        LocalDate importDate = LocalDate.parse(productData[3].trim());  // Import date

                        // Create a new Product object and add it to the database
                        Product product = new Product(0, name, unitPrice, qty, importDate, LocalTime.now());
                        projectDAO.addProduct(product);  // Assuming projectDAO adds the product to the database
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.out.println("Error parsing line: " + line);
                    }
                } else {
                    System.out.println("Invalid product data format: " + line);
                }
            }
            System.out.println("Products have been restored.");
        } catch (IOException e) {
            System.out.println("Error while restoring products: " + e.getMessage());
        }
    }



    private static void displayProducts() {
        // Fetch the products for the current page from the temporary collection
        int startIndex = (currentPage - 1) * ROWS_PER_PAGE;
        int endIndex = Math.min(startIndex + ROWS_PER_PAGE, temporaryProducts.size());

        List<Product> products = temporaryProducts.subList(startIndex, endIndex);

        // Create table styles
        CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        CellStyle columnStyle = new CellStyle(CellStyle.HorizontalAlign.center);

        // Initialize the table with 5 columns for product data
        Table t = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER_WIDE, ShownBorders.ALL);

        // Set column widths for the product data columns
        t.setColumnWidth(0, 22, 24);
        t.setColumnWidth(1, 18, 20);
        t.setColumnWidth(2, 12, 14);
        t.setColumnWidth(3, 8, 10);
        t.setColumnWidth(4, 18, 20);

        // Add column headers for product information
        t.addCell("ID", numberStyle);
        t.addCell("Name", numberStyle);
        t.addCell("Unit Price", numberStyle);
        t.addCell("Qty", numberStyle);
        t.addCell("Import Date", numberStyle);

        // Add product rows
        if (products.isEmpty()) {
            t.addCell("No products found.", numberStyle);
        } else {
            for (Product product : products) {
                t.addCell(String.valueOf(product.getId()), numberStyle);
                t.addCell(product.getName(), numberStyle);
                t.addCell(String.valueOf(product.getUnitPrice()), numberStyle);
                t.addCell(String.valueOf(product.getQty()), numberStyle);
                t.addCell(String.valueOf(product.getImportDate()), numberStyle);
            }
        }

        // Add a special row for the pagination information (Page and Total Records) across all columns
        int totalRecords = temporaryProducts.size(); // Use size of temporaryProducts
        int totalPages = (int) Math.ceil((double) totalRecords / ROWS_PER_PAGE);

        // Pagination info in one row, across all 5 columns
        // "Page" will span 2 columns and "Total Records" will span 3 columns
        String pageInfo = "Page: " + currentPage + " of " + totalPages;
        String totalInfo = "Total Records: " + totalRecords;

        // Adding the pagination info row with the appropriate column spans
        t.addCell(pageInfo, numberStyle, 2);  // "Page" spans 2 columns
        t.addCell(totalInfo, numberStyle, 3); // "Total Records" spans 3 columns

        // Render the full table
        String table = t.render();
        System.out.println(table);
    }


    private static void updateProduct() {
        System.out.print("Enter the product ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new unit price: ");
        double unitPrice = scanner.nextDouble();

        System.out.print("Enter new quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product(id, name, unitPrice, qty, LocalDate.now(), LocalTime.now());
        projectDAO.updateProduct(product);
        System.out.println("Product updated successfully.");
    }

    private static void deleteProduct() {
        System.out.print("Enter the product ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        projectDAO.deleteProduct(id);
        System.out.println("Product deleted successfully.");
    }

    private static void searchProduct() {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();

        List<Product> products = projectDAO.searchProduct(keyword);
        if (products.isEmpty()) {
            System.out.println("No products found matching the keyword.");
        } else {
            System.out.println("Search Results:");
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }

    private static void nextPage() {
        int totalRecords = projectDAO.getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / ROWS_PER_PAGE);

        if (currentPage < totalPages) {
            currentPage++;
            displayProducts();
        } else {
            System.out.println("You are already on the last page.");
        }
    }

    private static void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            displayProducts();
        } else {
            System.out.println("You are already on the first page.");
        }
    }

    private static void firstPage() {
        currentPage = 1;
        displayProducts();
    }

    private static void lastPage() {
        int totalRecords = projectDAO.getTotalRecords();
        currentPage = (int) Math.ceil((double) totalRecords / ROWS_PER_PAGE);
        displayProducts();
    }
    private static void setRowsPerPage() {
        System.out.print("Enter the number of rows per page: ");
        int rows = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (rows > 0) {
            ROWS_PER_PAGE = rows;
            System.out.println("Rows per page set to " + ROWS_PER_PAGE);
            displayProducts();  // Refresh the displayed products with the new row setting
        } else {
            System.out.println("Invalid input. Please enter a positive number.");
        }
    }

    private static void gotoPage() {
        System.out.print("Enter the page number: ");
        int pageNumber = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        int totalRecords = projectDAO.getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / ROWS_PER_PAGE);

        if (pageNumber >= 1 && pageNumber <= totalPages) {
            currentPage = pageNumber;
            displayProducts();
        } else {
            System.out.println("Invalid page number. Please enter a number between 1 and " + totalPages);
        }
    }
}
