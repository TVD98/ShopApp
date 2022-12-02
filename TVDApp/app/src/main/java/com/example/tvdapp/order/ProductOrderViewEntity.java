package com.example.tvdapp.order;

import android.net.Uri;

public class ProductOrderViewEntity {
    public String name;
    public String price;
    public Uri uri;
    public int count;

    public ProductOrderViewEntity(String name, String price, int count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }
}
