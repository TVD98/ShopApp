package com.example.tvdapp.orderMangager;

import com.example.tvdapp.home.order.OrderItem;

public class OrderManagerViewEntity {
    public String id;
    public String customerName;
    public String time;
    public OrderItem orderItem;
    public String total;
    public boolean paid;

    public OrderManagerViewEntity(String id, String customerName, String time, OrderItem orderItem, String total, boolean paid) {
        this.id = id;
        this.customerName = customerName;
        this.time = time;
        this.orderItem = orderItem;
        this.total = total;
        this.paid = paid;
    }
}
