package controller;

import model.*;
import view.*;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductController {
    static List<Product> products;
    static int currentPage = 1;
    static int row_per_page = 3;
    static String updateType;
    private List<Product> arrProducts;
    private List<Product> arrProducts2;
    public int num = 0;
    public int id;
    private ProductDaoImpl productDao;
    private Scanner scanner;
    public int isSave=0;

    public ProductController() {
        this.scanner = new Scanner(System.in);
        this.productDao = new ProductDaoImpl();
        this.arrProducts = new ArrayList<>();
        this.arrProducts2 = new ArrayList<>();
    }

    public void menu() {
        ProductView productView = new ProductView();
        while (true) {
            displayProducts();
            showAllProducts();
            productView.view_Menu_Describe();
            System.out.println("=> Choose on option() : ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "N":
                case "n":
                    nextPage();
                    break;
                case "P":
                case "p":
                    previousPage();
                    break;
                case "F":
                case "f":
                    firstPage();
                    break;
                case "L":
                case "l":
                    lastPage();
                    break;
                case "G":
                case "g":
                    gotoPage();
                    break;
                case "W":
                case "w":
                    write();
                    break;
                case "R":
                case "r":
                    read();
                    break;
                case "U":
                case "u":
                    update();
                    break;
                case "D":
                case "d":
                    delete();
                    break;
                case "S":
                case "s":
                    search();
                    break;
                case "Se":
                case "se":
                    setRow();
                    break;
                case "Sa":
                case "sa":
                    save();
                    break;
                case "Un":
                case "un":
                    unsaved();
                    break;
                case "Ba":
                case "ba":
                    backUp();
                    break;
                case "Re":
                case "re":
                    reStore();
                    break;
                case "E":
                case "e":
                    System.out.println("(^_^) Good Bye (^_^)");
                    return;
                default:
                    System.out.println("Invalid choice");


            }
        }
    }
//  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Show Product  >>>>>>>>>>>>>>>>>>>>>>>>>
    public void showAllProducts() {
        products = productDao.getAllProducts();
    }

    public void displayProducts() {
        List<Product> products = productDao.getProductsByPage(currentPage, row_per_page);
        CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table t = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER_WIDE, ShownBorders.ALL);

        t.setColumnWidth(0, 22, 24);
        t.setColumnWidth(1, 8, 10);
        t.setColumnWidth(2, 18, 20);
        t.setColumnWidth(3, 12, 14);
        t.setColumnWidth(4, 12, 14);

        t.addCell("ID", numberStyle);
        t.addCell("Name", numberStyle);
        t.addCell("Unit Price", numberStyle);
        t.addCell("Qty", numberStyle);
        t.addCell("Import Date", numberStyle);
        if (products.isEmpty()) {
            System.out.println("No products found");
        } else {
            for (Product product : products) {
                t.addCell(String.valueOf(product.getId()),numberStyle);
                t.addCell(product.getName(),numberStyle);
                t.addCell(String.valueOf(product.getUnitPrice()),numberStyle);
                t.addCell(String.valueOf(product.getQty()),numberStyle);
                t.addCell(String.valueOf(product.getImportDate()),numberStyle);
            }
        }
        int totalRecords = productDao.getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / row_per_page);

        String pageInfo = "Page: " + currentPage + " of " + totalPages;
        String totalInfo = "Total Records: " + totalRecords;

        t.addCell(pageInfo, numberStyle, 2);  // "Page" spans 2 columns
        t.addCell(totalInfo, numberStyle, 3); // "Total Records" spans 3 columns// Merge all 5 columns for pagination info


        String table = t.render();
        System.out.println(table);
    }

//  <<<<<<<<<<<<<<<<<<<<<<<<<<<< case Pagination >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public void nextPage() {
        int totalRecords = productDao.getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / row_per_page);

        if (currentPage < totalPages) {
            currentPage++;
            displayProducts();
        } else {
            System.out.println("You are already on the last page.");
        }
    }

    public void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            displayProducts();
        } else {
            System.out.println("You are already on the first page.");
        }
    }

    public void firstPage() {
        currentPage = 1;
        displayProducts();
    }

    public void lastPage() {
        int totalRecords = productDao.getTotalRecords();
        currentPage = (int) Math.ceil((double) totalRecords / row_per_page);
        displayProducts();
    }

    public void gotoPage() {
        System.out.print("Enter the page number: ");
        int pageNumber = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        int totalRecords = productDao.getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / row_per_page);

        if (pageNumber >= 1 && pageNumber <= totalPages) {
            currentPage = pageNumber;
            displayProducts();
        } else {
            System.out.println("Invalid page number. Please enter a number between 1 and " + totalPages);
        }
    }

//  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Case (Write) to (Exit) >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//    <<<<<<<<<<<<<<< Write >>>>>>>>>>>>
    public void write() {
        int size = products.size();
        System.out.println("Enter product id: " + (size +1));
        System.out.println("Enter product name: ");
        String name = scanner.next();
        System.out.println("Enter product unitPrice: ");
        double unitPrice = scanner.nextDouble();
        System.out.println("Enter product qty: ");
        int qty = scanner.nextInt();
        arrProducts.add(new Product(name, unitPrice, qty));
        System.out.println("add to array");
    }
//    <<<<<<<<<<<<<<<<Read>>>>>>>>>>>>>>>
    public void read() {
        System.out.print("Enter id to update: ");
        id = 0;
        id = scanner.nextInt();
        boolean has = false;
        for (Product product : products) {
            if (product.getId() == id) {
                has = true;
                CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.center);
                CellStyle columnStyle = new CellStyle(CellStyle.HorizontalAlign.center);
                Table t = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER_WIDE, ShownBorders.ALL);

                t.setColumnWidth(0, 22, 24);
                t.setColumnWidth(1, 8, 10);
                t.setColumnWidth(2, 18, 20);
                t.setColumnWidth(3, 12, 14);
                t.setColumnWidth(4, 12, 14);


                t.addCell("ID", numberStyle);
                t.addCell("Name", numberStyle);
                t.addCell("Unit Price", numberStyle);
                t.addCell("Qty", numberStyle);
                t.addCell("Import Date", numberStyle);
                t.addCell(String.valueOf(product.getId()),numberStyle);
                t.addCell(product.getName(),numberStyle);
                t.addCell(String.valueOf(product.getUnitPrice()),numberStyle);
                t.addCell(String.valueOf(product.getQty()),numberStyle);
                t.addCell(String.valueOf(product.getImportDate()),numberStyle);
                String table = t.render();
                System.out.println(table);
            }
        }
    }

//    <<<<<<<<<<<<Update>>>>>>>>>>>>>>>>>
    public void update() {
        System.out.print("Enter id to update: ");
        id = 0;
        id = scanner.nextInt();
        boolean has = false;
        for (Product product : products) {
            if (product.getId() == id) {
                has = true;
                CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.center);
                CellStyle columnStyle = new CellStyle(CellStyle.HorizontalAlign.center);
                Table t = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER_WIDE, ShownBorders.ALL);

                t.setColumnWidth(0, 22, 24);
                t.setColumnWidth(1, 8, 10);
                t.setColumnWidth(2, 18, 20);
                t.setColumnWidth(3, 12, 14);
                t.setColumnWidth(4, 12, 14);


                t.addCell("ID", numberStyle);
                t.addCell("Name", numberStyle);
                t.addCell("Unit Price", numberStyle);
                t.addCell("Qty", numberStyle);
                t.addCell("Import Date", numberStyle);
                t.addCell(String.valueOf(product.getId()),numberStyle);
                t.addCell(product.getName(),numberStyle);
                t.addCell(String.valueOf(product.getUnitPrice()),numberStyle);
                t.addCell(String.valueOf(product.getQty()),numberStyle);
                t.addCell(String.valueOf(product.getImportDate()),numberStyle);
                String table = t.render();
                System.out.println(table);
                System.out.println("\t1. Name\t\t2. Unit Price\t\t3. Qty\t\t4. All Field\t\t5. Exit");
                System.out.println("Choose an option : ");
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        updateName();
                        break;
                    case 2:
                        updateUnitPrice();
                        break;
                    case 3:
                        updateqQty();
                        break;
                    case 4:
                        updateAll();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        }
        if (!has) {
            System.out.println("Product not found");
        }
    }

    public void updateName() {
        System.out.println("Enter name to update: ");
        String name = scanner.nextLine();
        updateType = "name";
        products.get(id).setName(name);
        arrProducts2.add(new Product(id, name,products.get(id).getUnitPrice(),products.get(id).getQty(), products.get(id).getImportDate()));
    }
    public void updateqQty() {
        System.out.println("Enter qty to update: ");
        int qty = scanner.nextInt();
        updateType = "qty";
        products.get(id).setQty(qty);
        arrProducts2.add(new Product(id, products.get(id).getName(),products.get(id).getUnitPrice(),qty,products.get(id).getImportDate()));
    }
    public void updateUnitPrice() {
        System.out.println("Enter unitPrice to update: ");
        double unitPrice = scanner.nextDouble();
        updateType = "unitPrice";
        products.get(id).setUnitPrice(unitPrice);
        arrProducts2.add(new Product(id, products.get(id).getName(),unitPrice,products.get(id).getQty(),products.get(id).getImportDate()));
    }
    public void updateAll() {
        System.out.println("Enter name to update: ");
        String name = scanner.nextLine();
        System.out.println("Enter unitPrice to update: ");
        double unitPrice = scanner.nextDouble();
        System.out.println("Enter qty to update: ");
        int qty = scanner.nextInt();
        updateType = "all";
        products.get(id).setName(name);
        products.get(id).setUnitPrice(unitPrice);
        products.get(id).setQty(qty);
        arrProducts2.add(new Product(id, name, unitPrice, qty));
    }

//    <<<<<<<<<<<<<< Delete >>>>>>>>>>>>>>>>>
public void delete() {
    System.out.print("Enter id to delete: ");
    int id = scanner.nextInt();
    productDao.deleteProduct(id);
}

//    <<<<<<<<<<<<<<<Search>>>>>>>>>>>>>>>>>>
    public void search() {
        System.out.println("Enter name to search : ");
        String name = scanner.nextLine();
        products = productDao.getProductsByName(name);
        CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        CellStyle columnStyle = new CellStyle(CellStyle.HorizontalAlign.center);
        Table t = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER_WIDE, ShownBorders.ALL);

        t.setColumnWidth(0, 22, 24);
        t.setColumnWidth(1, 8, 10);
        t.setColumnWidth(2, 18, 20);
        t.setColumnWidth(3, 12, 14);
        t.setColumnWidth(4, 12, 14);

        t.addCell("ID", numberStyle);
        t.addCell("Name", numberStyle);
        t.addCell("Unit Price", numberStyle);
        t.addCell("Qty", numberStyle);
        t.addCell("Import Date", numberStyle);

        for (Product product : products) {
            t.addCell(String.valueOf(product.getId()),numberStyle);
            t.addCell(product.getName(),numberStyle);
            t.addCell(String.valueOf(product.getUnitPrice()),numberStyle);
            t.addCell(String.valueOf(product.getQty()),numberStyle);
            t.addCell(String.valueOf(product.getImportDate()),numberStyle);
        }
        String table = t.render();
        System.out.println(table);
        System.out.println("\t\t\t\t\t\t\t_______________________________");
    }

//    <<<<<<<<<<<<<<Set rows >>>>>>>>>>>>>
    public void setRow() {
        System.out.print("Enter number of to set set: ");
        row_per_page = scanner.nextInt();
    }

//  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Case Save >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public void save() {
        int size = products.size();
        System.out.println("\'ui\' for save for save insert product and \'uu\' for save update product or \'b\' for back to menu");
        System.out.print("Enter your choice : ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "ui":
            case "Ui":
                saveUi();
                break;
            case "uu":
            case "Uu":
                saveUu();
                break;
            case "b":
            case "B":
                return;
            default:
                System.out.println("Invalid choice");
        }
    }

    public void saveUi() {
        for (Product product : arrProducts) {
            productDao.addProduct(product);
        }
        num = arrProducts.size();
        System.out.println("save to database successfully");
        arrProducts.clear();
        isSave=1;
    }

    public void saveUu() {
        if (updateType.equals("all")) {
            for (int i = 0; i < arrProducts2.size(); i++) {
                productDao.updateProduct(id, arrProducts2.get(i).getName(), arrProducts2.get(i).getUnitPrice(), arrProducts2.get(i).getQty());
            }
            arrProducts2.clear();
        } else if (updateType.equals("name")) {
            for (int i = 0; i < arrProducts2.size(); i++) {
                productDao.updateName(id, arrProducts2.get(i).getName());
            }
            arrProducts2.clear();
        } else if (updateType.equals("unitPrice")) {
            for (int i = 0; i < arrProducts2.size(); i++) {
                productDao.updateUnitPrice(id, arrProducts2.get(i).getUnitPrice());
            }
            arrProducts2.clear();
        } else if (updateType.equals("qty")) {
            for (int i = 0; i < arrProducts2.size(); i++) {
                productDao.updateQty(id, arrProducts2.get(i).getQty());
            }
            arrProducts2.clear();
        }
        isSave = 1;
    }
//<<<<<<<<<<<<<<<<<<<<<<< Unsaved >>>>>>>>>>>>>>>>>>>>
    public void unsaved() {
        System.out.println("\'ui\' for unsaved of insert product and \'uu\' for unsaved of update product or \'b\' for back to menu");
        System.out.println("Enter your option: ");
        String option = scanner.nextLine();
        switch (option) {
            case "ui":
            case "Ui":
                unsavedUi();
                break;
            case "uu":
            case "Uu":
                unsavedUu();
                break;
            case "b":
            case "B":
                return;
            default:
                System.out.println("Invalid option");

        }
    }

    public void unsavedUi() {
        if(isSave==1) {
            System.out.println("No element that is unsaved");
        } else {
            CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.center);
            CellStyle columnStyle = new CellStyle(CellStyle.HorizontalAlign.center);
            Table t = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER_WIDE, ShownBorders.ALL);

            t.setColumnWidth(0, 22, 24);
            t.setColumnWidth(1, 8, 10);
            t.setColumnWidth(2, 18, 20);
            t.setColumnWidth(3, 12, 14);
            t.setColumnWidth(4, 12, 14);


            t.addCell("ID", numberStyle);
            t.addCell("Name", numberStyle);
            t.addCell("Unit Price", numberStyle);
            t.addCell("Qty", numberStyle);
            t.addCell("Import Date", numberStyle);
            for (Product p : arrProducts) {
                t.addCell(String.valueOf(p.getId()),numberStyle);
                t.addCell(String.valueOf(p.getName()),numberStyle);
                t.addCell(String.valueOf(p.getUnitPrice()),numberStyle);
                t.addCell(String.valueOf(p.getQty()),numberStyle);
                t.addCell(String.valueOf(p.getImportDate()),numberStyle);
            }
            String table = t.render();
            System.out.println(table);
        }
    }
    public void unsavedUu() {
        if(isSave==1) {
            System.out.println("No element that is unsaved");
        } else {
            CellStyle numberStyle = new CellStyle(CellStyle.HorizontalAlign.center);
            CellStyle columnStyle = new CellStyle(CellStyle.HorizontalAlign.center);
            Table t = new Table(5, BorderStyle.UNICODE_BOX_HEAVY_BORDER_WIDE, ShownBorders.ALL);

            t.setColumnWidth(0, 22, 24);
            t.setColumnWidth(1, 8, 10);
            t.setColumnWidth(2, 18, 20);
            t.setColumnWidth(3, 12, 14);
            t.setColumnWidth(4, 12, 14);


            t.addCell("ID", numberStyle);
            t.addCell("Name", numberStyle);
            t.addCell("Unit Price", numberStyle);
            t.addCell("Qty", numberStyle);
            t.addCell("Import Date", numberStyle);
            for (Product p : arrProducts2) {
                t.addCell(String.valueOf(p.getId()),numberStyle);
                t.addCell(String.valueOf(p.getName()),numberStyle);
                t.addCell(String.valueOf(p.getUnitPrice()),numberStyle);
                t.addCell(String.valueOf(p.getQty()),numberStyle);
                t.addCell(String.valueOf(p.getImportDate()),numberStyle);
            }
            String table = t.render();
            System.out.println(table);
        }
    }

//    <<<<<<<<<<<<<<<<<<< Backup >>>>>>>>>>>>>>>>>>
    public void backUp() {
        productDao.backUp();
    }

//    <<<<<<<<<<<<<<<<<<<< Restore >>>>>>>>>>>>>>>
    public void reStore() {
        arrProducts.clear();
        arrProducts = productDao.reStore();
        for (Product p : arrProducts) {
            productDao.addProduct(p);
        }
        System.out.println("Product restore successfully");
    }
}

