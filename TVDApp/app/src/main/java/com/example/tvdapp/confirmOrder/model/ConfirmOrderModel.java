package com.example.tvdapp.confirmOrder.model;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.R;
import com.example.tvdapp.home.order.OrderItem;
import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.orderMangager.model.OrderManagerResponse;
import com.example.tvdapp.utilities.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderModel {
    public List<ProductOrderViewEntity> getProductOrderViewEntities() {
        return productOrderViewEntities;
    }

    private List<ProductOrderViewEntity> productOrderViewEntities = new ArrayList<>();
    private ConfirmOrderInfoEntity confirmOrderInfoEntity;
    private Context context;
    private DatabaseReference mDatabase;

    public ConfirmOrderModel(Context context) {
        this.context = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void parseData(String cartJson, String infoJson) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ProductOrderViewEntity>>() {
        }.getType();
        productOrderViewEntities = gson.fromJson(cartJson, listType);

        if (infoJson.isEmpty()) {
            createConfirmOrderInfo();
        } else {
            confirmOrderInfoEntity = gson.fromJson(infoJson, ConfirmOrderInfoEntity.class);
            updateConfirmOrderInfoEntity();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createConfirmOrderInfo() {
        String discountName = context.getResources().getString(R.string.confirm_order_choose_discount);
        DiscountEntity discountEntity = new DiscountEntity(discountName, 0);
        PaymentMethodsEntity paymentMethodsEntity = new PaymentMethodsEntity(context.getResources().getString(R.string.confirm_order_choose_money_payment_methods));
        int price = getProductTotalMoney();
        confirmOrderInfoEntity = new ConfirmOrderInfoEntity(discountEntity, price, 0, paymentMethodsEntity);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getProductTotalMoney() {
        int totalMoney = productOrderViewEntities.stream()
                .reduce(0, (total, product) -> total + (product.price * product.count), Integer::sum);
        return totalMoney;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateConfirmOrderInfoEntity() {
        confirmOrderInfoEntity.price = getProductTotalMoney();
    }

    public ConfirmOrderInfoViewEntity getConfirmOrderInfoViewEntity() {
        int total = getTotal();
        ConfirmOrderInfoViewEntity confirmOrderInfoViewEntity = new ConfirmOrderInfoViewEntity(
                confirmOrderInfoEntity.discount.name,
                String.format("%,d", confirmOrderInfoEntity.price),
                String.format("-%,d", confirmOrderInfoEntity.discount.discount),
                String.format("%,d", confirmOrderInfoEntity.transportFee),
                String.format("%,d", total),
                confirmOrderInfoEntity.paymentMethods.name
        );
        confirmOrderInfoViewEntity.name = confirmOrderInfoEntity.name;
        confirmOrderInfoViewEntity.address = confirmOrderInfoEntity.address;
        confirmOrderInfoViewEntity.note = confirmOrderInfoEntity.note;

        return confirmOrderInfoViewEntity;
    }

    public String getProductOrderViewEntitiesJson() {
        Gson gson = new Gson();
        return gson.toJson(productOrderViewEntities);
    }

    public String getConfirmOrderInfoEntityJson() {
        Gson gson = new Gson();
        return gson.toJson(confirmOrderInfoEntity);
    }

    public void nameDidChange(String name) {
        confirmOrderInfoEntity.name = name;
    }

    public void noteDidChange(String note) {
        confirmOrderInfoEntity.note = note;
    }

    public void addressDidChange(String address) {
        confirmOrderInfoEntity.address = address;
    }

    private String getCustomerName() {
        String customerName = confirmOrderInfoEntity.name;
        if (customerName.isEmpty()) {
            return context.getString(R.string.order_manager_no_customer_name);
        } else {
            return customerName;
        }
    }

    private int getTotal() {
        return confirmOrderInfoEntity.price + confirmOrderInfoEntity.transportFee - confirmOrderInfoEntity.discount.discount;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void quickSell() {
        createOrder(true, OrderItem.delivered);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deliveryLater() {
        createOrder(false, OrderItem.processing);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOrder(boolean paid, OrderItem orderItem) {
        String id = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Constant.hh_mm_dd_MM_yyyy);
        LocalDateTime now = LocalDateTime.now();

        OrderManagerResponse item = new OrderManagerResponse(
                id,
                getCustomerName(),
                confirmOrderInfoEntity.address,
                confirmOrderInfoEntity.note,
                dtf.format(now),
                confirmOrderInfoEntity.price,
                confirmOrderInfoEntity.discount.discount,
                confirmOrderInfoEntity.transportFee,
                getTotal(),
                orderItem.getId(),
                paid,
                productOrderViewEntities
        );

        mDatabase.child(String.format("orders/%s", id)).setValue(item);
    }
}
