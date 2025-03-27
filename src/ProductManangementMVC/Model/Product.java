package ProductManangementMVC.Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Product {

    private int id;
    private String name;
    private double unitPrice;
    private int qty;
    private LocalDate importDate;
    private LocalTime importTime;  // Add LocalTime field

    // Update the constructor to accept LocalTime parameter
    public Product(int id, String name, double unitPrice, int qty, LocalDate importDate, LocalTime importTime) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.importDate = importDate;
        this.importTime = importTime;  // Set importTime
    }

    // Getters and setters for all fields

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

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public LocalTime getImportTime() {
        return importTime;
    }

    public void setImportTime(LocalTime importTime) {
        this.importTime = importTime;
    }

    // Override toString to display the import time as well
    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', unitPrice=" + unitPrice + ", qty=" + qty +
                ", importDate=" + importDate + ", importTime=" + importTime + '}';
    }
}