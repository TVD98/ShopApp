package com.example.tvdapp.orderDetails;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.home.order.OrderItem;
import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.orderDetails.model.OrderDetailBillInfoViewEntity;
import com.example.tvdapp.orderDetails.model.OrderDetailOrderInfoViewEntity;
import com.example.tvdapp.orderDetails.model.OrderDetailPriceProductViewEntity;
import com.example.tvdapp.orderMangager.model.OrderManagerResponse;
import com.example.tvdapp.product.ProductViewEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderDetailModel {


    private OrderManagerResponse orderManagerResponses;
    private DatabaseReference mDatabase;
    private OrderDetailModel.OrderDetailModelEvent event;
    public String orderId;


    public void setEvent(OrderDetailModel.OrderDetailModelEvent event) {
        this.event = event;
    }

    interface OrderDetailModelEvent {
        void fetchDataSuccess();

    }

    public OrderDetailModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void fetchData() {
        ValueEventListener postListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderManagerResponses = dataSnapshot.getValue(OrderManagerResponse.class);

                if (event != null) {
                    event.fetchDataSuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("orders/" + orderId).addValueEventListener(postListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public OrderItem getOrderType() {
        if (orderManagerResponses != null) {
            return Arrays.stream(OrderItem.values())
                    .filter(type -> type.getId() == orderManagerResponses.statusId)
                    .findAny()
                    .orElse(null);
        } else {
            return OrderItem.delivered;
        }
    }

    public OrderDetailBillInfoViewEntity getOrderDetailBillInfoViewEntity() {
        if (orderManagerResponses != null) {
            int total = orderManagerResponses.price - orderManagerResponses.discount + orderManagerResponses.transitionFee;
            return new OrderDetailBillInfoViewEntity(
                    String.format("%,d", orderManagerResponses.price),
                    String.format("-%,d", orderManagerResponses.discount),
                    "0",
                    String.format("%,d", orderManagerResponses.transitionFee),
                    String.format("%,d", total)
            );
        } else {
            return new OrderDetailBillInfoViewEntity(
                    "0",
                    "-0",
                    "0",
                    "0",
                    "0"
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<OrderDetailPriceProductViewEntity> getOrderDetailPriceProductViewEntityList() {
        if (orderManagerResponses != null) {
            return orderManagerResponses.productOrderViewEntities.stream()
                    .map(product -> new OrderDetailPriceProductViewEntity(
                            product.imageLink,
                            product.name,
                            String.format("%,d", product.price),
                            product.count))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public OrderDetailOrderInfoViewEntity getOrderDetailOrderInfoViewEntity() {
        if (orderManagerResponses != null) {
            return new OrderDetailOrderInfoViewEntity(
                    orderManagerResponses.id,
                    orderManagerResponses.time,
                    getOrderType(),
                    String.format("%,d", orderManagerResponses.price),
                    orderManagerResponses.paymentStatus,
                    orderManagerResponses.customerName,
                    ""
            );
        } else {
            return new OrderDetailOrderInfoViewEntity(
                    "",
                    "",
                    OrderItem.processing,
                    "",
                    false,
                    "",
                    ""
            );
        }
    }


    public void confirmDelivered() {
        mDatabase.child(String.format("orders/%s/statusId", orderId)).setValue(OrderItem.delivered.getId());
        mDatabase.child(String.format("orders/%s/paymentStatus", orderId)).setValue(true);
    }

    public void confirmCancel() {
        mDatabase.child(String.format("orders/%s", orderId)).setValue(null);
    }
}
