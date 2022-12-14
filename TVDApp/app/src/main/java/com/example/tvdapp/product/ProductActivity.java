package com.example.tvdapp.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.tvdapp.R;
import com.example.tvdapp.editProduct.EditProductActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private View createProductView;
    private ProductAdapter adapter;
    private ProductModel model = new ProductModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_product);

        initUI();
        initModel();
    }

    private void setupNavigation() {
        setTitle(R.string.product_activity_title);
    }

    private void initUI() {
        setupNavigation();

        recyclerView = findViewById(R.id.product_recycler_view);
        createProductView = findViewById(R.id.product_create_product_view);

        setupUI();
    }

    private void setupUI() {
        adapter = new ProductAdapter(new ArrayList<>(), this);
        adapter.setEvent(new ProductAdapter.ProductViewHolderEvent() {
            @Override
            public void selectProductView(String productId) {
                goToEditProductActivity(productId);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditProductActivity("");
            }
        });
    }

    private void initModel() {
        model.setEvent(new ProductModel.ProductModelEvent() {
            @Override
            public void fetchDataSuccess(List<ProductViewEntity> productViewEntityList) {
                adapter.setProductViewEntities(productViewEntityList);
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

    private void goToEditProductActivity(String productId) {
        Intent editProductIntent = new Intent(this, EditProductActivity.class);
        editProductIntent.putExtra("productId", productId);
        startActivity(editProductIntent);
    }
}