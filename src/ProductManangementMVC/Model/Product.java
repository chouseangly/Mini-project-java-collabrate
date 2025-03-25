package ProductManangementMVC.Model;
import java.time.LocalDate;
public class Product {
    private int id;
    private String name;
    private double UnitPrice;
    private int Qty;
    private LocalDate data;
    public Product(int id, String name, double UnitPrice, int Qty, LocalDate data) {
        this.id = id;
        this.name = name;
        this.UnitPrice = UnitPrice;
        this.Qty = Qty;
        this.data = data;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", UnitPrice=" + UnitPrice +
                ", Qty=" + Qty +
                ", data=" + data +
                '}';
    }
}
