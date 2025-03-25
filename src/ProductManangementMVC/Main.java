package ProductManangementMVC;

import ProductManangementMVC.Model.Connect;
import ProductManangementMVC.Model.Product;
import ProductManangementMVC.Model.ProjectDAOImpl;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;  // Import LocalTime class
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Connection connection = Connect.getConnection()) {
            if (connection != null) {
                System.out.println("Connection to the database is successful!");

                // Call the function to handle product insertion and retrieval
                handleProductManagement();

            } else {
                System.out.println("Connection failed!");
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
        }
    }

    public static void handleProductManagement() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Write Data (Insert Product)");
            System.out.println("2. Read Data (Display All Products)");
            System.out.println("3. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character after integer input

            ProjectDAOImpl projectDAO = new ProjectDAOImpl();

            // Get current time for display
            LocalTime currentTime = LocalTime.now();
            System.out.println("Current time: " + currentTime);  // Display current time

            switch (choice) {
                case 1:
                    // Case 1: Write Data (Insert Product)
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter product unit price: ");
                    double unitPrice = scanner.nextDouble();

                    System.out.print("Enter product quantity: ");
                    int qty = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character after integer input

                    // Use current date and time for import date
                    LocalDate importDate = LocalDate.now();
                    LocalTime importTime = LocalTime.now();  // Get current time

                    // Create a Product instance and insert into the database
                    Product product = new Product(0, name, unitPrice, qty, importDate, importTime);
                    projectDAO.addProduct(product);
                    System.out.println("Product inserted successfully at " + importTime);
                    break;

                case 2:
                    // Case 2: Read Data (Display All Products)
                    List<Product> products = projectDAO.getAllProduct();

                    if (products.isEmpty()) {
                        System.out.println("No products found.");
                    } else {
                        System.out.println("Product List (Current time: " + currentTime + "):");
                        for (Product productItem : products) {
                            System.out.println(productItem);
                        }
                    }
                    break;

                case 3:
                    // Case 3: Exit
                    System.out.println("Exiting the program. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice! Please choose a valid option.");
                    break;
            }

        } while (choice != 3);

        scanner.close();
    }
}
