package ProductManangementMVC.View;

import ProductManangementMVC.Model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class ProductView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("\nN) Next Page  P) Previous Page  F) First Page  L) Last Page  G) Goto");
        System.out.println("W) Write  R) Read  U) Update  D) Delete  S) Search  Se) Set Rows");
        System.out.println("Sa) Save  Un) Unsave  Ba) Backup  Re) Restore  E) Exit");
    }

    public Product insertProduct(int productId) {
        System.out.println("Product ID: " + productId);

        // Get product name
        System.out.print("Enter product name: ");
        String name = scanner.nextLine().trim();

        // Get product unit price
        double unitPrice = 0;
        boolean isValidUnitPrice = false;
        while (!isValidUnitPrice) {
            System.out.print("Enter product unit price: ");
            String unitPriceInput = scanner.nextLine().trim();
            try {
                unitPrice = Double.parseDouble(unitPriceInput);
                if (unitPrice <= 0) {
                    System.out.println("Unit price must be a positive number. Please try again.");
                } else {
                    isValidUnitPrice = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for the unit price.");
            }
        }

        // Get product quantity
        int qty = 0;
        boolean isValidQty = false;
        while (!isValidQty) {
            System.out.print("Enter product quantity: ");
            String qtyInput = scanner.nextLine().trim();
            try {
                qty = Integer.parseInt(qtyInput);
                if (qty < 0) {
                    System.out.println("Quantity cannot be negative. Please try again.");
                } else {
                    isValidQty = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for the quantity.");
            }
        }

        // Create and return the Product object
        return new Product(productId, name, unitPrice, qty, LocalDate.now(), LocalTime.now());
    }

    public void displayProducts(List<Product> products, int currentPage, int rowsPerPage) {
        CellStyle style = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER_WIDE, ShownBorders.ALL);

        table.setColumnWidth(0, 22, 24);
        table.setColumnWidth(1, 18, 20);
        table.setColumnWidth(2, 12, 14);
        table.setColumnWidth(3, 8, 10);
        table.setColumnWidth(4, 18, 20);

        table.addCell("ID", style);
        table.addCell("Name", style);
        table.addCell("Unit Price", style);
        table.addCell("Qty", style);
        table.addCell("Import Date", style);

        int startIndex = (currentPage - 1) * rowsPerPage;
        int endIndex = Math.min(startIndex + rowsPerPage, products.size());
        List<Product> paginatedProducts = products.subList(startIndex, endIndex);

        if (paginatedProducts.isEmpty()) {
            table.addCell("No products found.", style);
        } else {
            for (Product product : paginatedProducts) {
                table.addCell(String.valueOf(product.getId()), style);
                table.addCell(product.getName(), style);
                table.addCell(String.valueOf(product.getUnitPrice()), style);
                table.addCell(String.valueOf(product.getQty()), style);
                table.addCell(String.valueOf(product.getImportDate()), style);
            }
        }

        int totalRecords = products.size();
        int totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);
        String pageInfo = "Page: " + currentPage + " of " + totalPages;
        String totalInfo = "Total Records: " + totalRecords;

        table.addCell(pageInfo, style, 2);  // "Page" spans 2 columns
        table.addCell(totalInfo, style, 3); // "Total Records" spans 3 columns

        System.out.println(table.render());
    }

    public void displaySearchResults(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found matching the keyword.");
        } else {
            System.out.println("Search Results:");
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }

    public int promptForProductId(String message) {
        System.out.print(message);
        return scanner.nextInt();
    }

    public String promptForKeyword(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public int promptForRowsPerPage(String message) {
        System.out.print(message);
        return scanner.nextInt();
    }

    public int promptForPageNumber(String message) {
        System.out.print(message);
        return scanner.nextInt();
    }

    public Product updateProduct(int id) {
        System.out.print("Enter new name: ");
        String name = scanner.next();
        scanner.nextLine(); // Consume the leftover newline

        System.out.print("Enter new unit price: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input! Enter a valid number for unit price.");
            scanner.next();
        }
        double unitPrice = scanner.nextDouble();

        System.out.print("Enter new quantity: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Enter a valid integer for quantity.");
            scanner.next();
        }
        int qty = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        return new Product(id, name, unitPrice, qty, LocalDate.now(), LocalTime.now());
    }


    public void displayProductsFromDatabase(List<Product> products, int currentPage, int rowsPerPage, int totalRecords) {
        CellStyle style = new CellStyle(CellStyle.HorizontalAlign.center);
        Table table = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER_WIDE, ShownBorders.ALL);

        // Set column widths
        table.setColumnWidth(0, 22, 24);
        table.setColumnWidth(1, 18, 20);
        table.setColumnWidth(2, 12, 14);
        table.setColumnWidth(3, 8, 10);
        table.setColumnWidth(4, 18, 20);

        // Add header row
        table.addCell("ID", style);
        table.addCell("Name", style);
        table.addCell("Unit Price", style);
        table.addCell("Qty", style);
        table.addCell("Import Date", style);

        // Display products
        if (products.isEmpty()) {
            table.addCell("No products found.", style);
        } else {
            for (Product product : products) {
                table.addCell(String.valueOf(product.getId()), style);
                table.addCell(product.getName(), style);
                table.addCell(String.valueOf(product.getUnitPrice()), style);
                table.addCell(String.valueOf(product.getQty()), style);
                table.addCell(String.valueOf(product.getImportDate()), style);
            }
        }

        // Display pagination info
        int totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);
        String pageInfo = "Page: " + currentPage + " of " + totalPages;
        String totalInfo = "Total Records: " + totalRecords;

        table.addCell(pageInfo, style, 2);  // "Page" spans 2 columns
        table.addCell(totalInfo, style, 3); // "Total Records" spans 3 columns

        System.out.println(table.render());
    }

}