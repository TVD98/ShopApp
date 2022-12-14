package com.example.tvdapp.order.model;

public class ProductResponse {
    public String id;
    public String name;
    public String imageURL;
    public int price;
    public int costPrice;
    public int amount;

    public ProductResponse() {

    }

    public ProductResponse(String id, String name, String imageURL, int price, int costPrice, int amount) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.price = price;
        this.costPrice = costPrice;
    }
}
