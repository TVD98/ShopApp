package com.example.tvdapp.orderDetails;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.orderDetails.model.OrderDetailBillInfoViewEntity;
import com.example.tvdapp.orderMangager.model.OrderManagerResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailModel {


    private OrderManagerResponse orderManagerResponses;

    private List<ProductOrderViewEntity> productOrderViewEntities = new ArrayList<>();
    private DatabaseReference mDatabase;
    private OrderDetailModel.OrderDetailModelEvent event;
    public String orderId;


    public void setEvent(OrderDetailModel.OrderDetailModelEvent event) {
        this.event = event;
    }

    interface OrderDetailModelEvent {
        void fetchDataSuccess(List<ProductOrderViewEntity> productViewEntityList);

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

                OrderDetailBillInfoViewEntity orderDetailOrderInfoViewEntity = new OrderDetailBillInfoViewEntity(

                        String.format("%,d", orderManagerResponses.price),
                        String.format("%,d", orderManagerResponses.discount),
                        "0",
                        String.format("%,d", orderManagerResponses.transitionFee),
                        String.format("%,d", orderManagerResponses.total)
                );


                if (event != null) {
                    event.fetchDataSuccess(productOrderViewEntities);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("orders/" + orderId).addValueEventListener(postListener);
    }


}
