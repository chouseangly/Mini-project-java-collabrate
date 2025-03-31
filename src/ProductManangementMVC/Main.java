package ProductManangementMVC;
import ProductManangementMVC.Controll.ProductController;
import ProductManangementMVC.Model.Product;
import ProductManangementMVC.Model.ProductDAOImpl;
import ProductManangementMVC.Model.Validate;
import ProductManangementMVC.View.ProductView;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        ProductDAOImpl productDAO = new ProductDAOImpl();
        ProductView productView = new ProductView();
        ProductController productController = new ProductController(productDAO, productView);
        Validate validate = new Validate();
        Scanner scanner = new Scanner(System.in);
         final String GREEN = "\u001B[32m";
         final String RESET = "\u001B[0m";
         final String RED = "\u001B[31m";
         final String YELLOW = "\u001B[33m";
        int pageSize = productController.getPageSize();
        int currentPage = 1;
        int totalPages;


        while (true) {
            List<Product> products = productController.getAllProducts();

            totalPages = (int) Math.ceil((double) products.size() / pageSize);
            totalPages = Math.max(1, totalPages); // Ensure at least 1 page


            currentPage = Math.max(1, Math.min(currentPage, totalPages));

            productView.displayProducts(products, currentPage, pageSize, totalPages);
            productView.showMenu();
            String choice ;
            do{

                choice = scanner.nextLine().toUpperCase().trim();
                if(!Pattern.matches(validate.CPMRegex, choice)){
                    System.out.println(RED+"Invalid option entered. Please try again."+RESET);
                }
            }while(!Pattern.compile(validate.CPMRegex, Pattern.DOTALL).matcher(choice).matches());


            switch (choice) {
                case "N":
                    if (currentPage < totalPages) currentPage++;
                    else productView.showMessage(GREEN+"Already on last page"+RESET);
                    break;

                case "P":
                    if (currentPage > 1) currentPage--;
                    else productView.showMessage(GREEN+"Already on first page"+RESET);
                    break;

                case "F":
                    currentPage = 1;
                    break;

                case "L":
                    currentPage = totalPages;
                    break;

                case "G":
                    System.out.print("Enter page number: ");
                    try {
                        int page = Integer.parseInt(scanner.nextLine());
                        if (page >= 1 && page <= totalPages) currentPage = page;
                        else productView.showMessage(RED+"Invalid page number"+RESET);
                    } catch (NumberFormatException e) {
                        productView.showMessage(GREEN+"Please enter a valid number"+RESET);
                    }
                    break;

                case "SE":
                    System.out.print("Enter rows per page: ");
                    try {
                        pageSize = Integer.parseInt(scanner.nextLine());
                        productController.setPageSize(pageSize);
                        currentPage = 1;
                    } catch (NumberFormatException e) {
                        productView.showMessage(GREEN+"Please enter a valid number"+RESET);
                    }
                    break;

                case "W":
                    try {
                        int id = products.stream()
                                .mapToInt(Product::getId)
                                .max()
                                .orElse(0) + 1;
                        System.out.println(YELLOW+"Product ID: " + id + RESET);
                        String name;
                        do {
                            System.out.print("Enter product name: ");
                            name = scanner.nextLine().trim();
                            if (!Pattern.matches(validate.PRODUCT_NAME_REGEX, name)) {
                                System.out.println(RED+"Invalid product name! Please try again:"+RESET);
                            }
                        } while (!Pattern.matches(validate.PRODUCT_NAME_REGEX, name));

                       double price;
                       do{
                           System.out.print("Enter product price: ");
                           price = Double.parseDouble(scanner.nextLine().trim());
                           if(!Pattern.matches(validate.PRODUCT_UNIT_PRICE,String.valueOf(price)))
                           {
                               System.out.println(RED+"Invalid product price! Please try again:"+RESET);
                           }

                       }while (!Pattern.compile(validate.PRODUCT_UNIT_PRICE).matcher(String.valueOf(price)).matches());
                        int qty;
                        String qtyInput;
                        do {
                            System.out.print("Enter product quantity: ");
                            qtyInput = scanner.nextLine().trim();

                            if (!qtyInput.matches(validate.PRODUCT_STOCK_QTY)) {
                                System.out.println(RED+"Invalid quantity! Must be a positive whole number (e.g., 10)"+RESET);
                            }
                        } while (!qtyInput.matches(validate.PRODUCT_STOCK_QTY));

                        qty = Integer.parseInt(qtyInput);
                        productController.addProduct(id,name, price, qty, LocalDate.now());
                        productView.showMessage(GREEN+"Product added to temporary list"+RESET);
                    } catch (NumberFormatException e) {
                        productView.showMessage(RED+"Invalid input format"+RESET);
                    }
                    break;

                case "R":
                    productController.read();
                    break;

                case "U":
                    try {
                        productController.updateProductInTempList();
                        productView.showMessage(GREEN+"Product updated in temporary list"+RESET);
                    } catch (NumberFormatException e) {
                        productView.showMessage(RED+"Invalid input format"+RESET);
                    }
                    break;

                case "D":
                    try {
                        int id;
                        do{
                            System.out.print("Enter ID to delete: ");
                            id = Integer.parseInt(scanner.nextLine());
                            if(!Pattern.matches(validate.INT, String.valueOf(id)))
                            {
                                System.out.println(RED+"Invalid ID! Please try again:"+RESET);
                            }
                            if (productController.deleteProduct(id)) {
                                productView.showMessage(GREEN+"Product deleted successfully"+RESET);
                            }
                        }while (!Pattern.compile(validate.INT, Pattern.DOTALL).matcher(String.valueOf(id)).matches());



                    } catch (NumberFormatException e) {
                        productView.showMessage(RED+"Invalid product ID"+RESET);
                    }
                    break;

                case "S":
                    System.out.print("Enter product name to search: ");
                    String searchName = scanner.nextLine();
                    List<Product> results = productController.searchProducts(searchName);
                    if (results.isEmpty()) {
                        productView.showMessage(RED+"No products found"+RESET);
                    } else {
                        productView.displayProducts(results, 1, results.size(), 1);
                    }
                    break;

                case "SA":
                    String saveOption;
                    do{
                        System.out.print("Save options: (UI) Save insert product to database | (UU) Save update product to database:");
                         saveOption = scanner.nextLine().trim().toUpperCase();
                        if(!Pattern.matches(validate.CPMRegex, saveOption))
                        {
                            System.out.println(RED+"Invalid cpm option! Please try again:"+RESET);
                        }
                    }while (!Pattern.compile(validate.CPMRegex).matcher(saveOption).matches());
                    switch (saveOption) {
                        case "UI":
                            productController.saveToDatabase();
                            break;
                        case "UU":
                            productController.updateInDB();
                            break;
                        default:
                            productView.showMessage(RED+"Invalid save option"+RESET);
                    }
                    break;

                case "UN":
                        while (true)
                        {
                            System.out.println("'UI' viewing Unsave insert and 'UU' viewing Unsave update");
                            System.out.println("Please choose one of the following options:");
                            String option = scanner.nextLine().trim().toUpperCase();
                            switch (option) {
                                case "UI":
                                    productController.unsaveInsertProducts();
                                    break;
                                case "UU":
                                    productController.unsaveUpdateProducts();
                                    break;
                                    default:
                                        productView.showMessage(RED+"Invalid unsave insert option"+RESET);
                                        break;
                            }
                            break;

                        }
                        break;

                case "BA":
                    productController.createBackup();
                    productView.showMessage(GREEN+"Backup created successfully"+RESET);
                    break;

                case "RE":
                    productController.restoreFromCSV();
                    break;

                case "E":
                    productView.showMessage(GREEN+"Exiting Program..."+RESET);
                    System.out.println(GREEN+"Goodbye ^_^"+RESET);
                    scanner.close();
                    return;

                default:
                    productView.showMessage(RED+"Invalid option, please try again"+RESET);
            }
        }
    }
}