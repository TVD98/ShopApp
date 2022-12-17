package com.example.tvdapp.confirmOrder.model;

public class ConfirmOrderInfoEntity {
    public String name = "";
    public String address = "";
    public DiscountEntity discount;
    public int price;
    public int transportFee;
    public String note = "";
    public PaymentMethodsEntity paymentMethods;

    public ConfirmOrderInfoEntity(DiscountEntity discount, int price, int transportFee, PaymentMethodsEntity paymentMethods) {
        this.discount = discount;
        this.price = price;
        this.transportFee = transportFee;
        this.paymentMethods = paymentMethods;
    }
}
