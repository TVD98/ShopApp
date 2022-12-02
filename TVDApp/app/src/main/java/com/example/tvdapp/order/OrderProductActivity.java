package com.example.tvdapp.order;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tvdapp.R;

import java.util.ArrayList;
import java.util.List;

public class OrderProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private View cartView;
    private TextView cartProductCountTextView;
    private TextView cartProductTotalMoneyTextView;
    private OrderProductAdapter adapter;
    private List<ProductOrderViewEntity> productOrderViewEntities = new ArrayList<>();
    private OrderProductModel model = new OrderProductModel();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_order_product);

        initUI();
        initModel();
    }

    private void initUI() {
        setupNavigation();

        recyclerView = findViewById(R.id.order_recycler_view);
        cartView = findViewById(R.id.cart_view);
        cartProductCountTextView = findViewById(R.id.cart_product_count_text);
        cartProductTotalMoneyTextView = findViewById(R.id.cart_product_total_money_text);

        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setupNavigation() {
        setTitle(R.string.order_activity_title);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initModel() {
        model.setEvent(new OrderProductModel.OrderProductModelEvent() {
            @Override
            public void fetchProductListSuccess(List<ProductOrderViewEntity> entityList) {
                productOrderViewEntities = entityList;
                setupUI();
            }

            @Override
            public void cartDidChange(int productCount, String total) {
                if (productCount == 0) {
                    hideCartView();
                } else {
                    showCartView(productCount, total);
                }
            }

            @Override
            public void fetchProductImageSuccess(int position) {
                adapter.notifyItemChanged(position);
            }
        });

        model.fetchData();
    }

    private void setupUI() {
        adapter = new OrderProductAdapter(this, productOrderViewEntities, new OrderProductAdapter.OrderProductEvent() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void productCurrentCountDidChange(int position, int currentCount) {
                model.changeProductCurrentCount(position, currentCount);
            }

            @Override
            public void endEditProductCount(int position) {
                adapter.notifyItemChanged(position);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCartView(int productCount, String total) {
        cartView.setVisibility(View.VISIBLE);
        cartProductCountTextView.setText(Integer.toString(productCount));
        cartProductTotalMoneyTextView.setText(total);
    }

    private void hideCartView() {
        cartView.setVisibility(View.GONE);
    }
}