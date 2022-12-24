package com.example.tvdapp.warehouse;

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
import android.widget.TextView;

import com.example.tvdapp.R;
import com.example.tvdapp.book.BookActivity;
import com.example.tvdapp.order.OrderActivityType;
import com.example.tvdapp.order.OrderProductActivity;
import com.example.tvdapp.product.ProductViewEntity;

import java.util.ArrayList;
import java.util.List;

public class WarehouseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView productCodeCountTextView;
    private Button exportButton;
    private Button importButton;
    private TextView existentialValueTextView;
    private TextView productsCountTextView;
    private View importProductBookView;
    private WarehouseAdapter adapter;
    private WarehouseModel model = new WarehouseModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_warehouse);

        initUI();
        initModel();
    }

    private void setupNavigation() {
        setTitle(R.string.warehouse_activity_title);
    }

    private void initUI() {
        setupNavigation();

        recyclerView = findViewById(R.id.warehouse_recycler_view);
        productCodeCountTextView = findViewById(R.id.warehouse_product_code_count_text);
        importButton = findViewById(R.id.warehouse_import_button);
        exportButton = findViewById(R.id.warehouse_export_button);
        existentialValueTextView = findViewById(R.id.warehouse_existential_value_text);
        productsCountTextView = findViewById(R.id.warehouse_products_count_text);
        importProductBookView = findViewById(R.id.warehouse_import_product_book_view);

        setupUI();
    }

    private void setupUI() {
        adapter = new WarehouseAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToImportProducts();
            }
        });

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToExportProducts();
            }
        });

        importProductBookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToBookActivity();
            }
        });
    }

    private void initModel() {
        model.setEvent(new WarehouseModel.WarehouseModelEvent() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void fetchDataSuccess(List<ProductViewEntity> productViewEntityList) {
                adapter.setProductViewEntities(productViewEntityList);
                showWarehouseInfo(
                        model.getProductsCount(),
                        model.getExistentialValue(),
                        model.getAmount()
                );
            }

            @Override
            public void fetchProductImageSuccess(int position) {
                adapter.notifyItemChanged(position);
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

    private void showWarehouseInfo(int productsCount, String existentialValue, int amount) {
        productCodeCountTextView.setText(String.format("%d %s", productsCount, getString(R.string.warehouse_product_code)));
        existentialValueTextView.setText(existentialValue);
        productsCountTextView.setText(Integer.toString(amount));
    }

    private void goToImportProducts() {
        Intent orderIntent = new Intent(this, OrderProductActivity.class);
        orderIntent.putExtra("order_activity_type", OrderActivityType.importProduct);
        startActivity(orderIntent);
    }

    private void goToExportProducts() {
        Intent orderIntent = new Intent(this, OrderProductActivity.class);
        orderIntent.putExtra("order_activity_type", OrderActivityType.exportProduct);
        startActivity(orderIntent);
    }

    private void goToBookActivity() {
        Intent bookIntent = new Intent(this, BookActivity.class);
        startActivity(bookIntent);
    }
}