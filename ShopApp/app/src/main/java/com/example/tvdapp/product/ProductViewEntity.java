package com.example.tvdapp.product;

public class ProductViewEntity {
    public String id;
    public String name;
    public String imageLink;
    public int amount;
    public String price;
    public SelectionProductStatus selectionStatus = SelectionProductStatus.hide;

    public ProductViewEntity(String id, String name, String imageLink, int amount, String price) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
        this.amount = amount;
        this.price = price;
    }
}
