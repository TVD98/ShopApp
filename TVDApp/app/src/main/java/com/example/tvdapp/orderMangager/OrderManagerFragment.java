package com.example.tvdapp.orderMangager;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.home.order.OrderItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderManagerFragment extends Fragment {
    public static final String orderId = "orderId";
    private RecyclerView recyclerView;
    private OrderItem orderItem;
    private OrderManagerFragmentAdapter adapter;
    private List<OrderManagerViewEntity> entityList;
    private OrderManagerFragmentAdapter.OrderManagerFragmentViewHolderEvent orderManagerFragmentViewHolderEvent;

    public OrderManagerFragment(List<OrderManagerViewEntity> entityList, OrderManagerFragmentAdapter.OrderManagerFragmentViewHolderEvent orderManagerFragmentViewHolderEvent) {
        this.entityList = entityList;
        this.orderManagerFragmentViewHolderEvent = orderManagerFragmentViewHolderEvent;
    }

    public void setEntityList(List<OrderManagerViewEntity> entityList) {
        this.entityList = entityList;

        if (adapter != null) {
            adapter.setOrderManagerViewEntities(entityList);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_manager_collection, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        int orderId = args.getInt(OrderManagerFragment.orderId);
        orderItem = Arrays.stream(OrderItem.values())
                .filter(item -> item.getId() == orderId)
                .findAny()
                .orElse(null);

        recyclerView = view.findViewById(R.id.order_manager_recycler_view);

        setupUI();
    }

    private void setupUI() {
        adapter = new OrderManagerFragmentAdapter(getContext(), entityList, orderManagerFragmentViewHolderEvent);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
