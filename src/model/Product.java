package model;

import java.time.LocalDate;

public class Product {
    private int id;
    private String name;
    private double unitPrice;
    private int Qty;
    private String date;

    public Product(int id, String name, double unitPrice, int Qty, String date) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.Qty = Qty;
        this.date = date;
    }

    public Product(String name, double unitPrice, int Qty, String date) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.Qty = Qty;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getUnitPrice() {
        return unitPrice;
    }
    public int getQty() {
        return Qty;
    }
    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public void setQty(int qty) {
        this.Qty = qty;
    }
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", UnitPrice=" + unitPrice +
                ", Qty=" + Qty +
                ", data=" + date +
                '}';
    }
}
