package com.example.tvdapp.confirmImportProduct;

import com.example.tvdapp.confirmOrder.model.DiscountEntity;
import com.example.tvdapp.confirmOrder.model.PaymentMethodsEntity;

public class ConfirmImportProductEntity {
    public String name = "";
    public DiscountEntity discount;
    public int price;
    public int costsIncurred;
    public String note = "";
    public PaymentMethodsEntity paymentMethods;

    public ConfirmImportProductEntity(String name, DiscountEntity discount, int price, int costsIncurred, String note, PaymentMethodsEntity paymentMethods) {
        this.name = name;
        this.discount = discount;
        this.price = price;
        this.costsIncurred = costsIncurred;
        this.note = note;
        this.paymentMethods = paymentMethods;
    }
}
