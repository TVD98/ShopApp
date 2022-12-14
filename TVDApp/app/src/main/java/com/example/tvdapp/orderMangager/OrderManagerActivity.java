package com.example.tvdapp.orderMangager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmOrder.ConfirmOrderActivity;
import com.example.tvdapp.home.order.OrderItem;
import com.example.tvdapp.order.OrderProductActivity;
import com.example.tvdapp.orderDetails.OrderDetailsActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class OrderManagerActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private OrderManagerCollectionAdapter adapter;
    private TabLayout tabLayout;
    private View createOrderView;
    private int tabSelection = 0;
    private OrderManagerModel model = new OrderManagerModel();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_order_manager);

        Intent intent = getIntent();
        tabSelection = intent.getIntExtra("tabSelection", 0);

        initUI();
    }

    private void setupNavigation() {
        setTitle(R.string.order_service_title);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initUI() {
        setupNavigation();
        viewPager = findViewById(R.id.order_manager_view_pager);
        tabLayout = findViewById(R.id.order_manager_tab_layout);
        createOrderView = findViewById(R.id.order_manager_create_order_view);

        setupUI();
        initModel();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupUI() {
        adapter = new OrderManagerCollectionAdapter(this);
        adapter.setOrderManagerFragmentViewHolderEvent(new OrderManagerFragmentAdapter.OrderManagerFragmentViewHolderEvent() {
            @Override
            public void confirmDelivered(String orderId) {
                model.confirmDelivered(orderId);
            }

            @Override
            public void confirmCancel(String orderId) {
                model.confirmCancel(orderId);
            }

            @Override
            public void selectOrder(String orderId) {
                goToOrderDetails(orderId);
            }
        });
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(getTabTitle(position))
        ).attach();

        tabLayout.getTabAt(tabSelection).select();

        createOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateOrder();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initModel() {
        model.setEvent(new OrderManagerModel.OrderManagerModelEvent() {
            @Override
            public void fetchDataSuccess(Hashtable<OrderItem, List<OrderManagerViewEntity>> orderItemListHashtable) {
                adapter.reloadOrderManagerFragment(orderItemListHashtable);
            }
        });

        model.initModel();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getTabTitle(int position) {
        int orderId = position - 1;
        OrderItem orderItem = Arrays.stream(OrderItem.values())
                .filter(item -> item.getId() == orderId)
                .findAny()
                .orElse(null);
        if (orderItem != null) {
            return getResources().getString(orderItem.getStringId());
        } else {
            return "";
        }
    }

    private void goToOrderDetails(String orderId) {
        Intent orderDetailsIntent = new Intent(this, OrderDetailsActivity.class);
        orderDetailsIntent.putExtra("orderId", orderId);
        startActivity(orderDetailsIntent);
    }

    private void goToCreateOrder() {
        Intent createOrderIntent = new Intent(this, OrderProductActivity.class);
        startActivity(createOrderIntent);
    }
}