package ProductManangementMVC.View;

import ProductManangementMVC.Model.Product;
import java.util.List;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class ProductView {
    public ProductView() {
    }

    public void displayProducts(List<Product> products, int currentPage, int pageSize, int totalPages) {
        if (products == null || products.isEmpty()) {
            showMessage("No products to display.");
            return;
        }


        currentPage = Math.max(1, Math.min(currentPage, totalPages));

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, products.size());


        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        CellStyle align = new CellStyle(CellStyle.HorizontalAlign.center);

        table.setColumnWidth(0, 20, 100);
        table.setColumnWidth(1, 20, 100);
        table.setColumnWidth(2, 20, 100);
        table.setColumnWidth(3, 20, 100);
        table.setColumnWidth(4, 20, 100);

        table.addCell("ID", align);
        table.addCell("Name", align);
        table.addCell("Unit Price", align);
        table.addCell("Qty", align);
        table.addCell("Import Date", align);

        for (int i = start; i < end; ++i) {
            Product product = products.get(i);
            table.addCell(String.valueOf(product.getId()), align);
            table.addCell(product.getName(), align);
            table.addCell(String.format("%.2f", product.getUnitPrice()), align);
            table.addCell(String.valueOf(product.getQty()), align);
            table.addCell(product.getDate().toString(), align);
        }


        table.addCell("Page: " + currentPage + " of " + totalPages, align, 2);
        table.addCell("Total Records: " + products.size(), align, 3);
        System.out.println(table.render());
    }

    public void showMenu() {
        System.out.println("\n===== Product Management System =====");
        System.out.println("Page Navigation: (N) Next | (P) Previous | (F) First | (L) Last | (G) Go to Page | (SE) Set Rows");
        System.out.println("Actions: (W) Write | (R) Read | (U) Update | (D) Delete | (S) Search | (SA) Save | (UN) Unsave | (BA) Backup | (RE) Restore | (E) Exit");
        System.out.print("Enter your choice: ");
    }

    public void showMessage(String message) {
        final String GREEN = "\u001B[32m";
        final String RESET = "\u001B[0m";

        System.out.println(GREEN + "> " + message + RESET);
    }
}