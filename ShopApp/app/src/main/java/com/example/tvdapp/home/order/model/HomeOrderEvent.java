package com.example.tvdapp.home.order.model;

import com.example.tvdapp.home.order.OrderItem;

public interface HomeOrderEvent {
    void selectOrderItem(OrderItem type);
}
