package ProductManangementMVC.View;

import ProductManangementMVC.Model.Product;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ProductView {
    public static void displayProducts(List<Product> products)
    {
        System.out.println("Product List");
        Table t = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
        t.setColumnWidth(1,10,15);
        t.setColumnWidth(2,10,15);
        t.setColumnWidth(3,10,15);
        t.setColumnWidth(4,10,15);
        t.setColumnWidth(5,10,15);
        for(Product product : products)
        {
           t.addCell(String.valueOf(product.getId()));
           t.addCell(product.getName());
           t.addCell(String.valueOf(product.getUnitPrice()));
           t.addCell(String.valueOf(product.getQty()));
           t.addCell(String.valueOf(product.getData()));
        }
        System.out.println(t.render());

    }
    public void showMessage(String message)
    {
        System.out.println(message);
    }



}
