package view;

import controller.ProductController;

public class ProductView {
    ProductController productController =  new ProductController();;

    public void view_Menu_Describe() {
        System.out.println("\tN. Next Page\t\tP. Previous Page\t\tF. First Page\t\tL. Last Page\t\tG. Goto");
        System.out.println(" ");
        System.out.println("W) Write\t\tR) Read\t\tU) Update\t\tD) Delete\t\tS) Search\t\tSe) Set rows");
        System.out.println("Sa) Save\t\tUn) Unsaved\t\tBa) Backup\t\tRe) Restore\t\tE) Exit");
        System.out.println("\t\t\t--------------------------------------");
    }

    public void viewProduct() {
        productController.menu();
    }
}