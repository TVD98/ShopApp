package com.example.tvdapp.order;

import android.net.Uri;

public class ProductOrderViewEntity {
    public String id;
    public String name;
    public int price;
    public String imageLink;
    public int count;

    public ProductOrderViewEntity() {
    }

    public ProductOrderViewEntity(String id, String name, int price, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
