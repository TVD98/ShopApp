package com.example.tvdapp.order.model;

public class ProductResponse {
    public String id;
    public String name;
    public String imageURL;
    public int price;

    public ProductResponse() {

    }

    public ProductResponse(String id, String name, String imageURL, int price) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.price = price;
    }
}
