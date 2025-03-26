package ProductManangementMVC;

import ProductManangementMVC.Model.Connect;
import ProductManangementMVC.Model.Product;
import ProductManangementMVC.Model.ProjectDAOImpl;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProjectDAOImpl projectDAO = new ProjectDAOImpl();
    private static final Pattern optionPattern = Pattern.compile("[A-Za-z]+");
    private static final int ROWS_PER_PAGE = 5; // Number of products per page
    private static int currentPage = 1;

    public static void main(String[] args) {
        try (Connection connection = Connect.getConnection()) {
            if (connection != null) {
                System.out.println("Connection to the database is successful!");
                handleProductManagement();
            } else {
                System.out.println("Connection failed!");
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
        }
    }

    public static void handleProductManagement() {
        String choice;
        do {
            displayMenu();
            System.out.print("\n⇒ Choose an option() : ");
            choice = scanner.nextLine().trim().toUpperCase();

            if (!optionPattern.matcher(choice).matches()) {
                System.out.println("Invalid input! Please enter a valid letter option.");
                continue;
            }

            switch (choice) {
                case "W": insertProduct(); break;
                case "R": displayProducts(); break;
                case "U": updateProduct(); break;
                case "D": deleteProduct(); break;
                case "S": searchProduct(); break;
                case "Se": SetRow(); break;
                case "Sa": Save(); break;
                case "Un": Unsave(); break;
                case "Ba": Backup(); break;
                case "Re": Restore(); break;
                case "E": System.out.println("Exiting the program. Goodbye!"); break;
                default: System.out.println("Invalid choice! Please choose a valid option.");
            }
        } while (!choice.equals("E"));
    }



    private static void displayMenu() {
        System.out.println("\n+----+--------------+------------+-----+------------+");
        System.out.println("| ID | Name        | Unit Price | Qty | Import Date |");
        System.out.println("+----+--------------+------------+-----+------------+");
        System.out.println("(Sample data here - Implement pagination)");
        System.out.println("+----+--------------+------------+-----+------------+");
        System.out.println("Page: 1 of 1   Total Records: 3");
        System.out.println("\nN) Next Page  P) Previous Page  F) First Page  L) Last Page  G) Goto\n");
        System.out.println("W) Write  R) Read  U) Update  D) Delete  S) Search  Se) Set Rows");
        System.out.println("Sa) Save  Un) Unsave  Ba) Back Re) Restore  E) Exit");
    }

    private static void insertProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product unit price: ");
        double unitPrice = scanner.nextDouble();

        System.out.print("Enter product quantity: ");
        int qty = scanner.nextInt();
        scanner.nextLine();

        LocalDate importDate = LocalDate.now();
        LocalTime importTime = LocalTime.now();

        Product product = new Product(0, name, unitPrice, qty, importDate, importTime);
        projectDAO.addProduct(product);
        System.out.println("Product inserted successfully at " + importTime);
    }

    private static void displayProducts() {
        List<Product> products = projectDAO.getAllProduct();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("Product List:");
            for (Product productItem : products) {
                System.out.println(productItem);
            }
        }
    }

    private static void updateProduct() {
        System.out.println("Feature not implemented yet.");
    }

    private static void deleteProduct() {
        System.out.println("Feature not implemented yet.");
    }

    private static void searchProduct() {
        System.out.println("Feature not implemented yet.");
    }
    private static void Unsave() {
        System.out.println("Unsave");
    }

    private static void Save() {
        System.out.println("Save");
    }

    private static void SetRow() {
        System.out.println("UnImplement");
    }
    private static void Backup() {
        System.out.println("Backup");
    }
    private static void Restore() {
        System.out.println("Restore");
    }
}
