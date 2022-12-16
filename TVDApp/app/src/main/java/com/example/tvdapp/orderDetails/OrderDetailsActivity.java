package com.example.tvdapp.orderDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.tvdapp.R;
import com.example.tvdapp.order.ProductOrderViewEntity;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private OrderDetailModel model = new OrderDetailModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_order_details);

        Intent intent = getIntent();
        model.orderId = intent.getStringExtra("orderId");

        initUI();
        initModel();
    }

    private void setupNavigation() {
        setTitle(R.string.order_details_title);
    }

    private void initUI() {
        setupNavigation();
    }

    private void initModel() {
        model.setEvent(new OrderDetailModel.OrderDetailModelEvent() {
            @Override
            public void fetchDataSuccess(List<ProductOrderViewEntity> productViewEntityList) {

            }
        });
        model.fetchData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}