package com.example.tvdapp.orderDetails.model;

public class OrderDetailBillInfoViewEntity {
    public String priceProduct_billInfo="";
    public String discountBill="";
    public String VATfee ="";
    public String transitionFee="";
    public String totalPayment ="";

    public OrderDetailBillInfoViewEntity(String priceProduct_billInfo, String discountBill, String VATfee, String transitionFee, String totalPayment) {
        this.priceProduct_billInfo = priceProduct_billInfo;
        this.discountBill = discountBill;
        this.VATfee = VATfee;
        this.transitionFee = transitionFee;
        this.totalPayment = totalPayment;
    }
}
