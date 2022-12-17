package com.example.tvdapp.orderDetails;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.tvdapp.R;
import com.example.tvdapp.home.HomeActivity;
import com.example.tvdapp.home.order.OrderItem;
import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.utilities.SaveSystem;
import com.example.tvdapp.warehouse.WarehouseActivity;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderDetailAdapter adapter;
    private View bottomView;
    private Button cancelButton;
    private Button deliveredButton;
    private OrderDetailModel model = new OrderDetailModel();

    public static final String fromCreateOder = "create_order";
    public static final String fromOrderManager = "order_manager";
    private String fromActivity = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_order_details);

        Intent intent = getIntent();
        model.orderId = intent.getStringExtra("orderId");
        fromActivity = intent.getStringExtra("parentActivity");

        initUI();
        initModel();
    }

    private void setupNavigation() {
        setTitle(R.string.order_details_title);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initUI() {
        setupNavigation();

        recyclerView = findViewById(R.id.order_detail_recycler_view);
        bottomView = findViewById(R.id.order_detail_bottom_view);
        cancelButton = findViewById(R.id.order_detail_cancel_button);
        deliveredButton = findViewById(R.id.order_detail_delivered_button);

        setupUI();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupUI() {
        adapter = new OrderDetailAdapter(
                model.getOrderDetailPriceProductViewEntityList(),
                model.getOrderDetailBillInfoViewEntity(),
                model.getOrderDetailOrderInfoViewEntity(),
                this
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.confirmCancel();
            }
        });

        deliveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.confirmDelivered();
            }
        });
    }

    private void initModel() {
        model.setEvent(new OrderDetailModel.OrderDetailModelEvent() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void fetchDataSuccess() {
                adapter.setOrderDetailPriceProductViewEntities(model.getOrderDetailPriceProductViewEntityList());
                adapter.setOrderDetailBillInfoViewEntity(model.getOrderDetailBillInfoViewEntity());
                adapter.setOrderDetailOrderInfoViewEntity(model.getOrderDetailOrderInfoViewEntity());
                adapter.notifyDataSetChanged();

                setupBottomView(model.getOrderType());
            }
        });
        model.fetchData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupBottomView(OrderItem orderItem) {
        switch (orderItem) {
            case processing:
                bottomView.setVisibility(View.VISIBLE);
                break;
            default:
                bottomView.setVisibility(View.GONE);
                break;
        }
    }

    private void back() {
        if (fromActivity.compareTo(OrderDetailActivity.fromCreateOder) == 0) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("user_type", SaveSystem.getUserType(this));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            finish();
        }
    }
}