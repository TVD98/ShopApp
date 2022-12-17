package com.example.tvdapp.confirmImportProduct;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.confirmImportProduct.model.ImportProductResponse;
import com.example.tvdapp.confirmOrder.model.ConfirmOrderInfoEntity;
import com.example.tvdapp.confirmOrder.model.DiscountEntity;
import com.example.tvdapp.confirmOrder.model.PaymentMethodsEntity;
import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.orderMangager.model.OrderManagerResponse;
import com.example.tvdapp.utilities.Constant;
import com.example.tvdapp.utilities.Utilities;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConfirmImportProductModel {
    private List<ProductOrderViewEntity> productOrderViewEntities = new ArrayList<>();
    private ConfirmImportProductEntity confirmImportProductEntity;
    private ConfirmImportProductModelEvent event;
    private DatabaseReference mDatabase;

    public void setEvent(ConfirmImportProductModelEvent event) {
        this.event = event;
    }

    public List<ProductOrderViewEntity> getProductOrderViewEntities() {
        return productOrderViewEntities;
    }

    public ConfirmImportProductModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void parseData(String cartJson, String infoJson) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ProductOrderViewEntity>>() {
        }.getType();
        productOrderViewEntities = gson.fromJson(cartJson, listType);

        if (infoJson.isEmpty()) {
            createConfirmImportProductInfo();
        } else {
            confirmImportProductEntity = gson.fromJson(infoJson, ConfirmImportProductEntity.class);
            updateConfirmImportProductInfoEntity();
        }
    }

    public String getProductOrderViewEntitiesJson() {
        Gson gson = new Gson();
        return gson.toJson(productOrderViewEntities);
    }

    public String getConfirmProductInfoJson() {
        Gson gson = new Gson();
        return gson.toJson(confirmImportProductEntity);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getProductTotalMoney() {
        int totalMoney = productOrderViewEntities.stream()
                .reduce(0, (total, product) -> total + (product.price * product.count), Integer::sum);
        return totalMoney;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createConfirmImportProductInfo() {
        confirmImportProductEntity = new ConfirmImportProductEntity(
                "",
                new DiscountEntity("", 0),
                getProductTotalMoney(),
                0,
                "",
                new PaymentMethodsEntity("")
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getAmount() {
        int amount = productOrderViewEntities.stream()
                .reduce(0, (total, product) -> total + product.count, Integer::sum);
        return amount;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateConfirmImportProductInfoEntity() {
        confirmImportProductEntity.price = getProductTotalMoney();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ConfirmImportProductInfoViewEntity getConfirmImportProductViewEntity() {
        int total = getProductTotalMoney() - confirmImportProductEntity.discount.discount + confirmImportProductEntity.costsIncurred;
        return new ConfirmImportProductInfoViewEntity(
                String.format("%,d", getProductTotalMoney()),
                String.format("-%,d", confirmImportProductEntity.discount.discount),
                Integer.toString(getAmount()),
                String.format("%,d", total),
                String.format("%,d", confirmImportProductEntity.costsIncurred),
                confirmImportProductEntity.note,
                confirmImportProductEntity.name
        );
    }

    public boolean isValid() {
        if (productOrderViewEntities.isEmpty() || confirmImportProductEntity.name.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void nameDidChange(String name) {
        confirmImportProductEntity.name = name;
    }

    public void noteDidChange(String note) {
        confirmImportProductEntity.note = note;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void payment() {
        createImportProduct(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createImportProduct(boolean paid) {
        String id = RandomStringUtils.randomAlphanumeric(6).toUpperCase();

        int total = getProductTotalMoney() - confirmImportProductEntity.discount.discount + confirmImportProductEntity.costsIncurred;
        ConfirmImportProductInfoViewEntity viewEntity = getConfirmImportProductViewEntity();

        ImportProductResponse item = new ImportProductResponse(
                id,
                viewEntity.supplierName,
                viewEntity.note,
                Utilities.getTodayString(Constant.hh_mm_dd_MM_yyyy),
                confirmImportProductEntity.price,
                confirmImportProductEntity.discount.discount,
                confirmImportProductEntity.costsIncurred,
                total,
                paid,
                productOrderViewEntities
        );

        mDatabase.child(String.format("import_product/%s", id)).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (event != null) {
                    updateProductAmount();
                    event.createImportProductSuccess();
                }
            }
        });
    }

    private void updateProductAmount() {
        for (ProductOrderViewEntity entity : productOrderViewEntities) {
            int newAmount = entity.amount + entity.count;
            mDatabase.child(String.format("products/%s/amount", entity.id)).setValue(newAmount);
        }
    }

    interface ConfirmImportProductModelEvent {
        void createImportProductSuccess();
    }
}
