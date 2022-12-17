package com.example.tvdapp.home;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.home.order.OrderItem;
import com.example.tvdapp.home.order.model.OrderDataResponse;
import com.example.tvdapp.home.order.model.OrderDataResponseList;
import com.example.tvdapp.home.service.model.ServiceDataResponse;
import com.example.tvdapp.home.service.model.ServiceDataResponseList;
import com.example.tvdapp.home.turnover.model.TurnoverDataResponse;
import com.example.tvdapp.home.turnover.model.TurnoverDataResponseList;
import com.example.tvdapp.orderMangager.model.OrderManagerResponse;
import com.example.tvdapp.utilities.Constant;
import com.example.tvdapp.utilities.Utilities;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeModel {
    public HomeModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    interface HomeModelEvent {
        void fetchHomeItemListSuccess(List<HomeAdapter.HomeItem> itemList);

        void fetchOrderDataListSuccess(OrderDataResponseList orderDataResponseList);

        void fetchServiceDataListSuccess(ServiceDataResponseList serviceDataResponseList);

        void fetchTurnoverDataListSuccess(TurnoverDataResponseList turnoverDataResponseList);
    }

    private HomeModelEvent event;
    private DatabaseReference mDatabase;
    public String userType = "employee";
    private List<OrderManagerResponse> orderManagerResponsesToday = new ArrayList<>();

    public void setEvent(HomeModelEvent event) {
        this.event = event;
    }

    public void fetchHomeItemList() {
        ValueEventListener postListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String homeItemsString = dataSnapshot.getValue(String.class);
                String[] splitForHome = homeItemsString.split(",");

                HomeAdapter.HomeItem[] homeTypes = HomeAdapter.HomeItem.values();
                List<HomeAdapter.HomeItem> homeItemList = new ArrayList<>();
                for (String id : splitForHome) {
                    HomeAdapter.HomeItem type = Arrays.stream(homeTypes)
                            .filter(homeType -> homeType.getValue() == Integer.parseInt(id))
                            .findAny()
                            .orElse(null);
                    if (type != null) {
                        homeItemList.add(type);
                    }
                }

                if (event != null) {
                    event.fetchHomeItemListSuccess(homeItemList);
                }

                fetchServiceItemList();
                fetchOrderDataList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child(userType + "/homeItem").addValueEventListener(postListener);
    }

    private void fetchServiceItemList() {
        ValueEventListener servicePostListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String serviceItemsString = dataSnapshot.getValue(String.class);
                String[] splitForService = serviceItemsString.split(",");

                List<ServiceDataResponse> serviceDataResponses = new ArrayList<>();
                for (String id : splitForService) {
                    serviceDataResponses.add(new ServiceDataResponse(Integer.parseInt(id)));
                }

                if (event != null) {
                    event.fetchServiceDataListSuccess(new ServiceDataResponseList(serviceDataResponses.toArray(new ServiceDataResponse[0])));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.child(userType + "/orderItem").addValueEventListener(servicePostListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private TurnoverDataResponseList getTurnoverDataResponseList() {
        int price = orderManagerResponsesToday.stream()
                .reduce(0, (total, order) -> total + order.price, Integer::sum);
        return new TurnoverDataResponseList(new TurnoverDataResponse[]{
                new TurnoverDataResponse(0, price),
                new TurnoverDataResponse(1, orderManagerResponsesToday.size()),
                new TurnoverDataResponse(2, price)
        });
    }

    private void fetchOrderDataList() {
        ValueEventListener orderPostListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderManagerResponsesToday.clear();
                OrderDataResponse processingOrderResponse = new OrderDataResponse(OrderItem.processing.getId(), 0);
                OrderDataResponse waitingOrderResponse = new OrderDataResponse(OrderItem.waiting.getId(), 0);

                String todayTime = Utilities.getTodayString(Constant.dd_MM_yyyy);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OrderManagerResponse response = snapshot.getValue(OrderManagerResponse.class);
                    String time = Utilities.convertDateString(response.time, Constant.hh_mm_dd_MM_yyyy, Constant.dd_MM_yyyy);
                    if (time.compareTo(todayTime) == 0) {
                        orderManagerResponsesToday.add(response);
                    }

                    if (response.statusId == processingOrderResponse.orderId) {
                        processingOrderResponse.value += 1;
                    } else if (response.statusId == waitingOrderResponse.orderId) {
                        waitingOrderResponse.value += 1;
                    }
                }

                if (event != null) {
                    OrderDataResponse[] orderDataResponses = new OrderDataResponse[]{waitingOrderResponse, processingOrderResponse};
                    event.fetchOrderDataListSuccess(new OrderDataResponseList(orderDataResponses));
                    event.fetchTurnoverDataListSuccess(getTurnoverDataResponseList());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.child("orders").addValueEventListener(orderPostListener);
    }
}
