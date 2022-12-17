package com.example.tvdapp.order;

import android.net.Uri;

public class ProductOrderViewEntity {
    public String id;
    public String name;
    public int price;
    public String imageLink;
    public int amount;
    public int count;

    public ProductOrderViewEntity() {
    }

    public ProductOrderViewEntity(String id, String name, int price, int count, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.amount = amount;
    }
}
