package model;

import java.sql.Date;

public class Product {
    private int id;
    private String name;
    private double unitPrice;
    private int qty;
    private Date importDate; // Change from String to Date

//    >>>>>>>>>>>>>>>>>>>>Constructor with id
    public Product(int id, String name, double unitPrice, int qty, Date importDate) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.importDate = importDate;
    }

//    >>>>>>>>>>>>>>>>>>>>Constructor without id
    public Product(String name, double unitPrice, int qty, Date importDate) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.importDate = importDate;
    }

//    >>>>>>>>>>>>>>>>>>>>>>Constructor without date (for new products)
    public Product(String name, double unitPrice, int qty) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.importDate = new Date(System.currentTimeMillis()); // Default to current date
    }

    public Product(int id, String name, double unitPrice, int qty) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.qty = qty;
    }

//    >>>>>>>>>>>>>>>>>>>>>Getters
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
        return qty;
    }
    public Date getImportDate() {
        return importDate;
    }

//    >>>>>>>>>>>>>>>>>>>>>>>>Setters
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
        this.qty = qty;
    }
    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

//    >>>>>>>>>>>>>>>>>>>>>>>>>> To String
    @Override
    public String toString() {
        return id +
                "," + name +
                "," + unitPrice +
                "," + qty +
                "," + importDate;
    }
}
