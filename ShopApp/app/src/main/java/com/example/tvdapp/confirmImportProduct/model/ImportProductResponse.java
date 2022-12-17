package com.example.tvdapp.confirmImportProduct.model;

import com.example.tvdapp.order.ProductOrderViewEntity;

import java.util.List;

public class ImportProductResponse {
    public String id;
    public String username;
    public String supplierName;
    public String note;
    public String time;
    public int price;
    public int discount;
    public int costsIncurred;
    public int total;
    public boolean paymentStatus;
    public List<ProductOrderViewEntity> productOrderViewEntities;

    public ImportProductResponse(String id, String username, String supplierName, String note, String time, int price, int discount, int costsIncurred, int total, boolean paymentStatus, List<ProductOrderViewEntity> productOrderViewEntities) {
        this.id = id;
        this.username = username;
        this.supplierName = supplierName;
        this.note = note;
        this.time = time;
        this.price = price;
        this.discount = discount;
        this.costsIncurred = costsIncurred;
        this.total = total;
        this.paymentStatus = paymentStatus;
        this.productOrderViewEntities = productOrderViewEntities;
    }
}
