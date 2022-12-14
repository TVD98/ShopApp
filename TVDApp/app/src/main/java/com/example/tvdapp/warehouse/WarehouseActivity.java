package com.example.tvdapp.warehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.tvdapp.R;
import com.example.tvdapp.product.ProductViewEntity;

import java.util.List;

public class WarehouseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView productCodeCountTextView;
    private Button exportButton;
    private Button importButton;
    private TextView existentialValueTextView;
    private TextView productsCountTextView;
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

        setupUI();
    }

    private void setupUI() {

    }

    private void initModel() {
        model.setEvent(new WarehouseModel.WarehouseModelEvent() {
            @Override
            public void fetchDataSuccess(List<ProductViewEntity> productViewEntityList) {

            }

            @Override
            public void fetchProductImageSuccess(int position) {

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