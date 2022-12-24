package com.example.tvdapp.confirmExportProduct.model;

import com.example.tvdapp.order.ProductOrderViewEntity;

import java.util.List;

public class ConfirmExportProductResponse {
    public String id;
    public String employeeId;
    public int exportId;
    public String note;
    public List<ProductOrderViewEntity> productOrderViewEntities;

    public ConfirmExportProductResponse(String id, String employeeId, int exportId, String note, List<ProductOrderViewEntity> productOrderViewEntities) {
        this.id = id;
        this.employeeId = employeeId;
        this.exportId = exportId;
        this.note = note;
        this.productOrderViewEntities = productOrderViewEntities;
    }
}
