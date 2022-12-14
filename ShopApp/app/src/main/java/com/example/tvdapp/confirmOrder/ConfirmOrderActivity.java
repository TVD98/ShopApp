package com.example.tvdapp.confirmOrder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.tvdapp.R;
import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.orderDetails.OrderDetailActivity;

public class ConfirmOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ConfirmOrderAdapter adapter;
    private View bottomView;
    private Button deliveryLaterButton;
    private Button quickSellButton;
    private ProgressDialog loadingDialog;
    private ConfirmOrderModel model = new ConfirmOrderModel(this);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_confirm_order);

        Intent intent = getIntent();
        model.parseData(
                intent.getStringExtra("products"),
                intent.getStringExtra("info")
        );

        initUI();
    }

    private void setupNavigation() {
        setTitle(R.string.confirm_order_activity_title);
    }

    private void initUI() {
        setupNavigation();
        recyclerView = findViewById(R.id.confirm_order_recycler_view);
        bottomView = findViewById(R.id.confirm_order_bottom_view);
        deliveryLaterButton = findViewById(R.id.confirm_order_delivery_later_button);
        quickSellButton = findViewById(R.id.confirm_order_quick_sell_button);

        setupUI();
        setupModel();
    }

    private void setupUI() {
        updateStatusBottomView(false);
        adapter = new ConfirmOrderAdapter(
                model.getProductOrderViewEntities(),
                model.getConfirmOrderInfoViewEntity(),
                this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setAddItemsViewHolderEvent(new AddItemsViewHolder.AddItemsViewHolderEvent() {
            @Override
            public void selectAddItems() {
                backOrderProductActivity();
            }
        });

        adapter.setConfirmProductViewHolderEvent(new ConfirmProductViewHolderEvent() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void productDidChange(ProductOrderViewEntity entity) {
                if (entity.count == 0) {
                    adapter.removeProduct(entity);

                    if (adapter.cartIsEmpty()) {
                        updateStatusBottomView(true);
                    }
                }

                model.updateConfirmOrderInfoEntity();
                adapter.bindDataToInfoView(model.getConfirmOrderInfoViewEntity());
            }
        });

        adapter.setConfirmInfoViewHolderEvent(new ConfirmInfoViewHolder.ConfirmInfoViewHolderEvent() {
            @Override
            public void nameDidChange(String name) {
                model.nameDidChange(name);
            }

            @Override
            public void noteDidChange(String note) {
                model.noteDidChange(note);
            }

            @Override
            public void addressDidChange(String address) {
                model.addressDidChange(address);
            }
        });

        quickSellButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                quickSell();
            }
        });

        deliveryLaterButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                deliveryLater();
            }
        });
    }

    private void setupModel() {
        model.setEvent(new ConfirmOrderModel.ConfirmOrderModelEvent() {
            @Override
            public void createOrderSuccess(String orderId) {
                loadingDialog.cancel();
                goToOrderDetails(orderId);
            }
        });
    }

    private void updateStatusBottomView(boolean isHide) {
        if (isHide) {
            bottomView.setVisibility(View.GONE);
        } else {
            bottomView.setVisibility(View.VISIBLE);
        }
    }

    private void showLoadingDialog() {
        loadingDialog = ProgressDialog.show(this, "",
                "Uploading. Please wait...", true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backOrderProductActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void deliveryLater() {
        showLoadingDialog();
        model.deliveryLater();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void quickSell() {
        showLoadingDialog();
        model.quickSell();
    }

    private void backOrderProductActivity() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("products", model.getProductOrderViewEntitiesJson());
        resultIntent.putExtra("info", model.getConfirmOrderInfoEntityJson());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void goToOrderDetails(String orderId) {
        Intent orderDetailsIntent = new Intent(this, OrderDetailActivity.class);
        orderDetailsIntent.putExtra("orderId", orderId);
        orderDetailsIntent.putExtra("parentActivity", OrderDetailActivity.fromCreateOder);
        startActivity(orderDetailsIntent);
    }
}