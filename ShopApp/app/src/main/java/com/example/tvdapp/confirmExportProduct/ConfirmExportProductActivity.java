package com.example.tvdapp.confirmExportProduct;

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
import com.example.tvdapp.confirmImportProduct.ConfirmImportProductActivity;
import com.example.tvdapp.confirmOrder.ConfirmProductViewHolderEvent;
import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.warehouse.WarehouseActivity;

import java.util.ArrayList;

public class ConfirmExportProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ConfirmExportProductAdapter adapter;
    private Button exportButton;
    private View bottomView;
    private ProgressDialog loadingDialog;
    private ConfirmExportProductModel model = new ConfirmExportProductModel();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_confirm_export_product);

        Intent intent = getIntent();
        model.parseData(
                intent.getStringExtra("products"),
                intent.getStringExtra("info"));

        initUI();
        setupModel();
    }

    private void setupNavigation() {
        setTitle(R.string.confirm_export_product_activity_title);
    }

    private void initUI() {
        setupNavigation();

        recyclerView = findViewById(R.id.confirm_export_product_recycler_view);
        bottomView = findViewById(R.id.confirm_export_product_bottom_view);
        exportButton = findViewById(R.id.confirm_export_product_export_button);

        setupUI();
    }

    private void setupUI() {
        updateStatusBottomView(false);

        adapter = new ConfirmExportProductAdapter(
                model.getProductOrderViewEntities(),
                model.getConfirmExportProductInfoViewEntity(),
                this);
        adapter.setConfirmProductViewHolderEvent(new ConfirmProductViewHolderEvent() {
            @Override
            public void productDidChange(ProductOrderViewEntity entity) {
                if (entity.count == 0) {
                    adapter.removeProduct(entity);

                    if (!model.isValid()) {
                        updateStatusBottomView(true);
                    }
                }
            }
        });
        adapter.setConfirmExportProductInfoViewHolderEvent(new ConfirmExportProductInfoViewHolder.ConfirmExportProductInfoViewHolderEvent() {
            @Override
            public void selectAddItems() {
                backOrderProductActivity();
            }

            @Override
            public void selectExportType(ExportType exportType) {
                model.selectExportType(exportType);
            }

            @Override
            public void noteDidChange(String note) {
                model.noteDidChange(note);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingDialog();
                model.exportProducts();
            }
        });
    }

    private void setupModel() {
        model.setContext(this);
        model.setEvent(new ConfirmExportProductModel.ConfirmExportProductModelEvent() {
            @Override
            public void exportProductsSuccess() {
                loadingDialog.cancel();
                backWarehouseActivity();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backOrderProductActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
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

    private void backOrderProductActivity() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("products", model.getProductOrderViewEntitiesJson());
        resultIntent.putExtra("info", model.getConfirmProductInfoJson());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void backWarehouseActivity() {
        Intent intent = new Intent(this, WarehouseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}