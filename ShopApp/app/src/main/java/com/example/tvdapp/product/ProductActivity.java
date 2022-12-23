package com.example.tvdapp.product;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.tvdapp.R;
import com.example.tvdapp.editProduct.EditProductActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private View createProductView;
    private View bottomView;
    private Button cancelButton;
    private Button deleteButton;
    private ProductAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProductModel model = new ProductModel();
    private SearchView searchView;
    private ProgressDialog loadingDialog;
    private AlertDialog alertDialog;

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
        cancelButton = findViewById(R.id.product_cancel_button);
        deleteButton = findViewById(R.id.product_delete_button);
        bottomView = findViewById(R.id.product_bottom_view);

        setupUI();
    }

    private void setupUI() {
        hideBottomView();

        adapter = new ProductAdapter(new ArrayList<>(), this);
        layoutManager = new LinearLayoutManager(this);
        adapter.setEvent(new ProductAdapter.ProductViewHolderEvent() {
            @Override
            public void selectProductView(String productId) {
                goToEditProductActivity(productId);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void selectCheckbox(String id, boolean isChecked) {
                adapter.selectCheckbox(id, isChecked);

                int count = adapter.getSelectedProductCount();
                if (count == 0) {
                    updateStatusDeleteButton(false);
                } else {
                    updateStatusDeleteButton(true);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        createProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditProductActivity("");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                turnOffDeleteMode();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
    }

    private void initModel() {
        model.setEvent(new ProductModel.ProductModelEvent() {

            @Override
            public void productAdded(ProductViewEntity productViewEntity) {
                adapter.addProduct(productViewEntity);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void productChanged(ProductViewEntity productViewEntity) {
                adapter.changeProduct(productViewEntity);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void productRemoved(String id) {
                adapter.removeProduct(id);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void fetchProductImageSuccess(String id, String imageLink) {
                adapter.fetchProductImage(id, imageLink);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void deleteProductsSuccess() {
                loadingDialog.cancel();
                turnOffDeleteMode();
            }
        });

        model.fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                turnOnDeleteMode();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void turnOnDeleteMode() {
        adapter.turnOnDeleteMode();

        showBottomView();
        reloadViewHolderBySelectionStatus();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void turnOffDeleteMode() {
        adapter.turnOffDeleteMode();

        hideBottomView();
        reloadViewHolderBySelectionStatus();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void reloadViewHolderBySelectionStatus() {
        List<ProductViewEntity> viewEntities = adapter.getProductViewEntities();
        final int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        final int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; ++i) {
            ProductAdapter.ProductViewHolder holder = (ProductAdapter.ProductViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            ProductViewEntity entity = viewEntities.stream()
                    .filter(product -> product.id.compareTo(holder.entity.id) == 0)
                    .findAny()
                    .orElse(null);
            if (entity != null) {
                holder.updateStatusCheckbox(entity.selectionStatus);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showBottomView() {
        bottomView.setVisibility(View.VISIBLE);
        updateStatusDeleteButton(false);
        createProductView.setVisibility(View.INVISIBLE);
    }

    private void hideBottomView() {
        bottomView.setVisibility(View.INVISIBLE);
        createProductView.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    public void updateStatusDeleteButton(boolean isEnable) {
        if (isEnable) {
            deleteButton.setEnabled(true);
            deleteButton.setBackgroundColor(getColor(R.color.Green));
            deleteButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            deleteButton.setEnabled(false);
            deleteButton.setBackgroundColor(getColor(R.color.LightGray));
            deleteButton.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void showLoadingDialog() {
        loadingDialog = ProgressDialog.show(this, "",
                "Uploading. Please wait...", true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showAlert() {
        int count = adapter.getSelectedProductCount();
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Xoá sản phẩm")
                .setMessage(String.format("Bạn muốn xoá %d sản phẩm", count))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                        deleteProducts();
                    }
                })
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteProducts() {
        showLoadingDialog();
        model.deleteProducts(adapter.getSelectedProducts());
    }

    private void goToEditProductActivity(String productId) {
        Intent editProductIntent = new Intent(this, EditProductActivity.class);
        editProductIntent.putExtra("productId", productId);
        startActivity(editProductIntent);
    }
}