package com.example.tvdapp.order;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmImportProduct.ConfirmImportProductActivity;
import com.example.tvdapp.confirmOrder.ConfirmOrderActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private View cartView;
    private TextView cartProductCountTextView;
    private TextView cartProductTotalMoneyTextView;
    private OrderProductAdapter adapter;
    private OrderProductModel model = new OrderProductModel();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        OrderActivityType type = (OrderActivityType) intent.getSerializableExtra("order_activity_type");
        if (type == OrderActivityType.importProduct) {
            model.type = type;
        }

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

        setupUI();
    }

    private void setupNavigation() {
        if (model.type == OrderActivityType.order) {
            setTitle(R.string.order_activity_title);
        } else {
            setTitle(R.string.warehouse_import_product);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initModel() {
        model.setEvent(new OrderProductModel.OrderProductModelEvent() {
            @Override
            public void fetchProductListSuccess(List<ProductOrderViewEntity> entityList) {
                adapter.setEntityList(entityList);
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
        adapter = new OrderProductAdapter(this, model.getProductOrderViewEntityList(), new OrderProductAdapter.OrderProductEvent() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void productCurrentCountDidChange(int position, int currentCount) {
                model.changeProductCurrentCount(position, currentCount);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        cartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.type == OrderActivityType.order) {
                    goToConfirmOrderActivity();
                } else {
                    goToConfirmImportProductActivity();
                }
            }
        });
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

    private void goToConfirmOrderActivity() {
        Intent orderIntent = new Intent(this, ConfirmOrderActivity.class);
        String cartJson = model.getCartJson();
        orderIntent.putExtra("products", cartJson);
        orderIntent.putExtra("info", model.confirmOrderInfoJson);
        someActivityResultLauncher.launch(orderIntent);
    }

    private void goToConfirmImportProductActivity() {
        Intent confirmImportProductIntent = new Intent(this, ConfirmImportProductActivity.class);
        String cartJson = model.getCartJson();
        confirmImportProductIntent.putExtra("products", cartJson);
        confirmImportProductIntent.putExtra("info", model.confirmOrderInfoJson);
        someActivityResultLauncher.launch(confirmImportProductIntent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        String cartJson = data.getStringExtra("products");
                        String infoJson = data.getStringExtra("info");
                        model.receiveCartFromConfirmOrder(cartJson, infoJson);
                    }
                }
            });

}