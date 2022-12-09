package com.example.tvdapp.confirmOrder.model;

public class ConfirmOrderInfoViewEntity {
    public String name = "";
    public String address = "";
    public String promotion;
    public String price;
    public String discount;
    public String transportFee;
    public String total;
    public String note = "";
    public String paymentMethods;

    public ConfirmOrderInfoViewEntity(String promotion, String price, String discount, String transportFee, String total, String paymentMethods) {
        this.promotion = promotion;
        this.price = price;
        this.discount = discount;
        this.transportFee = transportFee;
        this.total = total;
        this.paymentMethods = paymentMethods;
    }
}
