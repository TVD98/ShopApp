package com.example.tvdapp.orderMangager;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.home.order.OrderItem;
import com.example.tvdapp.orderMangager.model.OrderManagerResponse;
import com.example.tvdapp.utilities.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class OrderManagerModel {
    private DatabaseReference mDatabase;
    private List<OrderManagerResponse> orderManagerResponses = new ArrayList<>();
    private OrderManagerModelEvent event;

    public void setEvent(OrderManagerModelEvent event) {
        this.event = event;
    }

    interface OrderManagerModelEvent {
        void fetchDataSuccess(Hashtable<OrderItem, List<OrderManagerViewEntity>> orderItemListHashtable);
    }

    public OrderManagerModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initModel() {
        ValueEventListener postListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderManagerResponses.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderManagerResponse response = snapshot.getValue(OrderManagerResponse.class);
                    orderManagerResponses.add(response);
                }

                sortOrderManagerResponses();

                if (event != null) {
                    event.fetchDataSuccess(convertResponses());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("orders").addValueEventListener(postListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sortOrderManagerResponses() {
        Comparator<OrderManagerResponse> comp = (OrderManagerResponse a, OrderManagerResponse b) -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Constant.hh_mm_dd_MM_yyyy);
            LocalDateTime localDateTimeA = LocalDateTime.parse(a.time, dtf);
            LocalDateTime localDateTimeB = LocalDateTime.parse(b.time, dtf);
            return localDateTimeB.compareTo(localDateTimeA);
        };
        orderManagerResponses.sort(comp);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private OrderManagerViewEntity convertResponse(OrderManagerResponse response) {
        OrderItem orderItem = Arrays.stream(OrderItem.values())
                .filter(item -> item.getId() == response.statusId)
                .findAny()
                .orElse(null);
        return new OrderManagerViewEntity(
                response.id,
                response.customerName,
                response.time,
                orderItem,
                String.format("%,d", response.total),
                response.paymentStatus
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Hashtable<OrderItem, List<OrderManagerViewEntity>> convertResponses() {
        Hashtable<OrderItem, List<OrderManagerViewEntity>> orderItemListHashtable = new Hashtable<>();
        orderItemListHashtable.put(OrderItem.all, new ArrayList<>());

        for (OrderManagerResponse response : orderManagerResponses) {
            OrderManagerViewEntity entity = convertResponse(response);
            orderItemListHashtable.get(OrderItem.all).add(entity);
            if (orderItemListHashtable.containsKey(entity.orderItem)) {
                orderItemListHashtable.get(entity.orderItem).add(entity);
            } else {
                orderItemListHashtable.put(entity.orderItem, new ArrayList<OrderManagerViewEntity>() {
                    {
                        add(entity);
                    }
                });
            }
        }

        return orderItemListHashtable;
    }

    public void confirmDelivered(String orderId) {
        mDatabase.child(String.format("orders/%s/statusId", orderId)).setValue(OrderItem.delivered.getId());
        mDatabase.child(String.format("orders/%s/paymentStatus", orderId)).setValue(true);
    }

    public void confirmCancel(String orderId) {
        mDatabase.child(String.format("orders/%s", orderId)).setValue(null);
    }
}
