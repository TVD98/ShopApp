package com.example.tvdapp.home.order;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.tvdapp.R;
import com.example.tvdapp.home.HomeViewHolder;
import com.example.tvdapp.home.order.model.HomeOrderEvent;
import com.example.tvdapp.home.order.model.OrderDataResponse;
import com.example.tvdapp.home.order.model.OrderDataResponseList;
import com.example.tvdapp.home.service.ServiceItem;
import com.example.tvdapp.home.service.ServiceViewEntity;
import com.example.tvdapp.home.service.model.ServiceDataResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class HomeOrderViewHolder extends HomeViewHolder {
    private Context context;
    private TextView waitingTitleTextView;
    private TextView waitingValueTextView;
    private TextView processingTitleTextView;
    private TextView processingValueTextView;
    private View waitingView;
    private View processingView;
    private TextView allTitleTextView;
    private Hashtable<OrderItem, OrderViewEntity> dictionary = new Hashtable<OrderItem, OrderViewEntity>();
    private List<OrderItem> orderTypeList = new ArrayList<>();
    private HomeOrderEvent homeOrderEvent;

    public HomeOrderViewHolder(@NonNull View itemView, Context context, HomeOrderEvent event) {
        super(itemView);
        this.context = context;
        this.homeOrderEvent = event;

        waitingTitleTextView = itemView.findViewById(R.id.item_order_waiting_title);
        waitingValueTextView = itemView.findViewById(R.id.item_order_waiting_value);
        processingTitleTextView = itemView.findViewById(R.id.item_order_processing_title);
        processingValueTextView = itemView.findViewById(R.id.item_order_processing_value);
        waitingView = itemView.findViewById(R.id.waiting_order_view);
        processingView = itemView.findViewById(R.id.processing_order_view);
        allTitleTextView = itemView.findViewById(R.id.home_order_all_title);

        setupEvent();
    }

    private void setupEvent() {
        waitingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeOrderEvent != null) {
                    homeOrderEvent.selectOrderItem(OrderItem.waiting);
                }
            }
        });

        processingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeOrderEvent != null) {
                    homeOrderEvent.selectOrderItem(OrderItem.processing);
                }
            }
        });

        allTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeOrderEvent != null) {
                    homeOrderEvent.selectOrderItem(OrderItem.all);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initModel(OrderDataResponseList response) {
        OrderItem[] orderTypes = OrderItem.values();
        for (OrderDataResponse data : response.data) {
            OrderItem type = Arrays.stream(orderTypes)
                    .filter(orderType -> orderType.getId() == data.orderId)
                    .findAny()
                    .orElse(null);
            if (type != null) {
                String title = context.getString(type.getStringId());
                OrderViewEntity entity = new OrderViewEntity(title, Integer.toString(data.value));
                dictionary.put(type, entity);
                orderTypeList.add(type);
            }
        }

        initUI();
    }

    private void initUI() {
        OrderViewEntity waitingOrderInfo = dictionary.get(OrderItem.waiting);
        if (waitingOrderInfo != null) {
            waitingTitleTextView.setText(waitingOrderInfo.title);
            waitingValueTextView.setText(waitingOrderInfo.value);
        }

        OrderViewEntity processingOrderInfo = dictionary.get(OrderItem.processing);
        if (processingOrderInfo != null) {
            processingTitleTextView.setText(processingOrderInfo.title);
            processingValueTextView.setText(processingOrderInfo.value);
        }
    }
}
