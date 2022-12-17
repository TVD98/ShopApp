package com.example.tvdapp.orderDetails.model;

import com.example.tvdapp.home.order.OrderItem;

public class OrderDetailOrderInfoViewEntity {

    public String codeBill;
    public String dateBil;
    public OrderItem orderItem;
    public String priceProduct;
    public boolean statusPayment;
    public String nameCustomer;
    public String imageCustomer;

    public OrderDetailOrderInfoViewEntity(String codeBill, String dateBil, OrderItem orderItem, String priceProduct, boolean statusPayment, String nameCustomer, String imageCustomer) {
        this.codeBill = codeBill;
        this.dateBil = dateBil;
        this.orderItem = orderItem;
        this.priceProduct = priceProduct;
        this.statusPayment = statusPayment;
        this.nameCustomer = nameCustomer;
        this.imageCustomer = imageCustomer;
    }
}
