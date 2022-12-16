package com.example.tvdapp.orderDetails.model;

public class OrderDetailOrderInfoViewEntity {

    public String codeBill = "";
    public String dateBil = "";
    public String statusBill = "";
    public String priceProduct = "";
    public String statusPayment = "";
    public String nameCustomer = "";
    public String imageCustomer = "";

    public OrderDetailOrderInfoViewEntity(String codeBill, String dateBil, String statusBill, String priceProduct, String statusPayment, String nameCustomer, String imageCustomer) {
        this.codeBill = codeBill;
        this.dateBil = dateBil;
        this.statusBill = statusBill;
        this.priceProduct = priceProduct;
        this.statusPayment = statusPayment;
        this.nameCustomer = nameCustomer;
        this.imageCustomer = imageCustomer;
    }
}
