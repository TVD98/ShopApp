package com.example.tvdapp.orderMangager;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tvdapp.home.order.OrderItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class OrderManagerCollectionAdapter extends FragmentStateAdapter {
    private Hashtable<Integer, OrderManagerFragment> orderManagerFragmentHashtable = new Hashtable<>();
    private Hashtable<Integer, List<OrderManagerViewEntity>> orderItemListHashtable = new Hashtable<>();
    private OrderManagerFragmentAdapter.OrderManagerFragmentViewHolderEvent orderManagerFragmentViewHolderEvent;

    public void setOrderManagerFragmentViewHolderEvent(OrderManagerFragmentAdapter.OrderManagerFragmentViewHolderEvent orderManagerFragmentViewHolderEvent) {
        this.orderManagerFragmentViewHolderEvent = orderManagerFragmentViewHolderEvent;
    }

    public OrderManagerCollectionAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        List<OrderManagerViewEntity> firstEntityList = orderItemListHashtable.get(position);
        OrderManagerFragment fragment;
        if (firstEntityList != null) {
            fragment = new OrderManagerFragment(firstEntityList, orderManagerFragmentViewHolderEvent);
        } else {
            fragment = new OrderManagerFragment(new ArrayList<>(), orderManagerFragmentViewHolderEvent);
        }

        orderManagerFragmentHashtable.put(position, fragment);
        Bundle args = new Bundle();
        args.putInt(OrderManagerFragment.orderId, position - 1);
        fragment.setArguments(args);

        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private OrderItem getOrderItem(int position) {
        int orderId = position - 1;
        return Arrays.stream(OrderItem.values())
                .filter(item -> item.getId() == orderId)
                .findAny()
                .orElse(null);
    }

    @Override
    public int getItemCount() {
        return OrderItem.values().length;
    }

    public void reloadOrderManagerFragment(Hashtable<OrderItem, List<OrderManagerViewEntity>> orderItemListHashtable) {
        this.orderItemListHashtable.clear();

        for (OrderItem orderItem : OrderItem.values()) {
            List<OrderManagerViewEntity> entityList = new ArrayList<>();
            if (orderItemListHashtable.containsKey(orderItem)) {
                entityList = orderItemListHashtable.get(orderItem);
            }
            int position = orderItem.getId() + 1;

            OrderManagerFragment fragment = orderManagerFragmentHashtable.get(position);

            if (fragment != null) {
                fragment.setEntityList(entityList);
            }
            this.orderItemListHashtable.put(position, entityList);
        }
    }
}
