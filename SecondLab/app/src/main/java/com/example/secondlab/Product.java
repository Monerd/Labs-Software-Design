package com.example.secondlab;

public class Product {
    private int count;
    private int workshopNumber;
    private String productName;

    public Product(int count,
                   int workshopNumber,
                   String productName) {
        this.count = count;
        this.workshopNumber = workshopNumber;
        this.productName = productName;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setWorkshopNumber(int workshopNumber) {
        this.workshopNumber = workshopNumber;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return this.productName;
    }

    public int getCount() {
        return this.count;
    }

    public int getWorkshopNumber() {
        return this.workshopNumber;
    }
}
