package com.example.tvdapp.orderDetails.model;

public class OrderDetailBillInfoViewEntity {
    public String priceProduct_billInfo="";
    public String discountBill="";
    public String VATFee ="";
    public String transitionFee="";
    public String totalPayment ="";

    public OrderDetailBillInfoViewEntity(String priceProduct_billInfo, String discountBill, String VATFee, String transitionFee, String totalPayment) {
        this.priceProduct_billInfo = priceProduct_billInfo;
        this.discountBill = discountBill;
        this.VATFee = VATFee;
        this.transitionFee = transitionFee;
        this.totalPayment = totalPayment;
    }
}
