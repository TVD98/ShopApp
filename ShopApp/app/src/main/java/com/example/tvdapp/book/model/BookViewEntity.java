package com.example.tvdapp.book.model;

public class BookViewEntity {
    public String id;
    public String supplierName;
    public String time;
    public String total;
    public boolean paid;

    public BookViewEntity(String id, String supplierName, String time, String total, boolean paid) {
        this.id = id;
        this.supplierName = supplierName;
        this.time = time;
        this.total = total;
        this.paid = paid;
    }
}
