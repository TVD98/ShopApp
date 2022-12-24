package com.example.tvdapp.confirmExportProduct;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.confirmExportProduct.model.ConfirmExportProductResponse;
import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.utilities.SaveSystem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfirmExportProductModel {
    private List<ProductOrderViewEntity> productOrderViewEntities = new ArrayList<>();
    private ConfirmExportProductInfoViewEntity confirmExportProductInfoViewEntity;
    private Context context;
    private ConfirmExportProductModelEvent event;
    private DatabaseReference mDatabase;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setEvent(ConfirmExportProductModelEvent event) {
        this.event = event;
    }

    public ConfirmExportProductModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public List<ProductOrderViewEntity> getProductOrderViewEntities() {
        return productOrderViewEntities;
    }

    public ConfirmExportProductInfoViewEntity getConfirmExportProductInfoViewEntity() {
        return confirmExportProductInfoViewEntity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void parseData(String cartJson, String infoJson) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ProductOrderViewEntity>>() {
        }.getType();
        productOrderViewEntities = gson.fromJson(cartJson, listType);

        if (infoJson.isEmpty()) {
            createConfirmExportProductInfo();
        } else {
            confirmExportProductInfoViewEntity = gson.fromJson(infoJson, ConfirmExportProductInfoViewEntity.class);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createConfirmExportProductInfo() {
        confirmExportProductInfoViewEntity = new ConfirmExportProductInfoViewEntity(
                ExportType.export.getId(),
                ""
        );
    }

    public String getProductOrderViewEntitiesJson() {
        Gson gson = new Gson();
        return gson.toJson(productOrderViewEntities);
    }

    public String getConfirmProductInfoJson() {
        Gson gson = new Gson();
        return gson.toJson(confirmExportProductInfoViewEntity);
    }

    public boolean isValid() {
        return !productOrderViewEntities.isEmpty();
    }

    public void selectExportType(ExportType type) {
        confirmExportProductInfoViewEntity.exportId = type.getId();
    }

    public void noteDidChange(String note) {
        confirmExportProductInfoViewEntity.note = note;
    }

    private ConfirmExportProductResponse getConfirmExportProductResponse() {
        String id = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        return new ConfirmExportProductResponse(
                id,
                SaveSystem.getUsername(context),
                confirmExportProductInfoViewEntity.exportId,
                confirmExportProductInfoViewEntity.note,
                productOrderViewEntities
        );
    }

    public void exportProducts() {
        ConfirmExportProductResponse response = getConfirmExportProductResponse();
        mDatabase.child(String.format("export_product/%s", response.id)).setValue(response).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (event != null) {
                    updateProductAmount();
                    event.exportProductsSuccess();
                }
            }
        });
    }

    private void updateProductAmount() {
        for (ProductOrderViewEntity entity : productOrderViewEntities) {
            int newAmount = entity.amount - entity.count;
            mDatabase.child(String.format("products/%s/amount", entity.id)).setValue(newAmount);
        }
    }

    interface ConfirmExportProductModelEvent {
        void exportProductsSuccess();
    }
}
