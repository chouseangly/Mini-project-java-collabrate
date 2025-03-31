package ProductManangementMVC.Model;

import java.time.LocalDate;

public class Product {
    private int id;
    private String name;
    private double unitPrice;
    private int qty;
    private LocalDate date;

    public Product(String name, double unitPrice, int qty, LocalDate date) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.date = date != null ? date : LocalDate.now();
    }

    public Product(int id, String name, double unitPrice, int qty, LocalDate date) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.date = date != null ? date : LocalDate.now();
    }

    // Getters and Setters
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
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                ", date=" + date +
                '}';
    }
}



