package com.example.tvdapp.confirmImportProduct;

public class ConfirmImportProductInfoViewEntity {
    public String price;
    public String discount;
    public String amount;
    public String total;
    public String costsIncurred;
    public String note;
    public String supplierName;

    public ConfirmImportProductInfoViewEntity(String price, String discount, String amount, String total, String costsIncurred, String note, String supplierName) {
        this.price = price;
        this.discount = discount;
        this.amount = amount;
        this.total = total;
        this.costsIncurred = costsIncurred;
        this.note = note;
        this.supplierName = supplierName;
    }
}
