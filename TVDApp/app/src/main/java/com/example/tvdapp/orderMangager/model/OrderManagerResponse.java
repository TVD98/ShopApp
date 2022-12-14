package com.example.tvdapp.orderMangager.model;

import com.example.tvdapp.order.ProductOrderViewEntity;

import java.util.List;

public class OrderManagerResponse {
    public String id;
    public String customerName;
    public String address;
    public String note;
    public String time;
    public int price;
    public int discount;
    public int transitionFee;
    public int total;
    public int statusId;
    public boolean paymentStatus;
    public List<ProductOrderViewEntity> productOrderViewEntities;

    public OrderManagerResponse(String id, String customerName, String address, String note,  String time, int price, int discount, int transitionFee,
                                int total, int statusId, boolean paymentStatus, List<ProductOrderViewEntity> productOrderViewEntities) {
        this.id = id;
        this.customerName = customerName;
        this.address = address;
        this.time = time;
        this.note = note;
        this.price = price;
        this.discount = discount;
        this.transitionFee = transitionFee;
        this.total = total;
        this.statusId = statusId;
        this.paymentStatus = paymentStatus;
        this.productOrderViewEntities = productOrderViewEntities;
    }

    public OrderManagerResponse() {

    }
}
