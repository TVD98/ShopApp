package com.example.tvdapp.confirmImportProduct;

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
import android.widget.TextView;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmOrder.ConfirmProductViewHolderEvent;
import com.example.tvdapp.editProduct.EditProductActivity;
import com.example.tvdapp.order.ProductOrderViewEntity;
import com.example.tvdapp.warehouse.WarehouseActivity;

public class ConfirmImportProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button paymentButton;
    private Button paymentLaterButton;
    private View bottomView;
    private ConfirmImportProductAdapter adapter;
    private ProgressDialog loadingDialog;
    private ConfirmImportProductModel model = new ConfirmImportProductModel();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_confirm_import_product);

        Intent intent = getIntent();
        model.parseData(
                intent.getStringExtra("products"),
                intent.getStringExtra("info"));

        initUI();
        setupModel();
    }

    private void setupNavigation() {
        setTitle(R.string.confirm_import_product_activity_title);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initUI() {
        setupNavigation();

        recyclerView = findViewById(R.id.confirm_import_product_recycler_view);
        paymentButton = findViewById(R.id.confirm_import_product_payment_button);
        paymentLaterButton = findViewById(R.id.confirm_import_product_payment_later_button);
        bottomView = findViewById(R.id.confirm_import_product_bottom_view);

        setupUI();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupUI() {
        adapter = new ConfirmImportProductAdapter(
                model.getProductOrderViewEntities(),
                model.getConfirmImportProductViewEntity(),
                this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateStatusBottomView(!model.isValid());

        adapter.setConfirmProductViewHolderEvent(new ConfirmProductViewHolderEvent() {
            @Override
            public void productDidChange(ProductOrderViewEntity entity) {
                if (entity.count == 0) {
                    adapter.removeProduct(entity);

                    if (!model.isValid()) {
                        updateStatusBottomView(true);
                    }
                }

                model.updateConfirmImportProductInfoEntity();
                adapter.bindDataToInfoView(model.getConfirmImportProductViewEntity());
            }
        });

        adapter.setConfirmImportProductViewHolderEvent(new ConfirmImportProductInfoViewHolder.ConfirmImportProductViewHolderEvent() {
            @Override
            public void nameDidChange(String name) {
                model.nameDidChange(name);

                updateStatusBottomView(!model.isValid());
            }

            @Override
            public void noteDidChange(String note) {
                model.noteDidChange(note);
            }

            @Override
            public void addItems() {
                backOrderProductActivity();
            }
        });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                showLoadingDialog();
                model.payment();
            }
        });
    }

    private void setupModel() {
        model.setContext(this);
        model.setEvent(new ConfirmImportProductModel.ConfirmImportProductModelEvent() {
            @Override
            public void createImportProductSuccess() {
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
        loadingDialog = ProgressDialog.show(ConfirmImportProductActivity.this, "",
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