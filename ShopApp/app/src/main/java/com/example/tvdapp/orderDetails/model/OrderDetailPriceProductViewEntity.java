package com.example.tvdapp.orderDetails.model;

public class OrderDetailPriceProductViewEntity {

    public String imageProduct;
    public String nameProduct;
    public String priceBill;
    public int countProduct;

    public OrderDetailPriceProductViewEntity(String imageProduct, String nameProduct, String priceBill, int countProduct) {
        this.imageProduct = imageProduct;
        this.nameProduct = nameProduct;
        this.priceBill = priceBill;
        this.countProduct = countProduct;
    }
}
