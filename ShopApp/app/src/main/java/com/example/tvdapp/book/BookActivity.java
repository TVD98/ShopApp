package com.example.tvdapp.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.tvdapp.R;
import com.example.tvdapp.book.model.BookViewEntity;
import com.example.tvdapp.confirmImportProduct.ConfirmImportProductActivity;
import com.example.tvdapp.order.OrderActivityType;
import com.example.tvdapp.order.OrderProductActivity;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private View createImportProductView;
    private ProgressDialog loadingDialog;
    private BookModel model = new BookModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_book);

        initUI();
        initModel();
    }

    private void setupNavigation() {
        setTitle(R.string.book_activity_title);
    }

    private void initUI() {
        setupNavigation();

        recyclerView = findViewById(R.id.book_recycler_view);
        createImportProductView = findViewById(R.id.book_create_import_product_view);

        setupUI();
    }

    private void setupUI() {
        adapter = new BookAdapter(new ArrayList<>(), this);
        adapter.setEvent(new BookAdapter.BookViewHolderEvent() {
            @Override
            public void payment(String id) {
                showLoadingDialog();
                model.payment(id);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createImportProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToImportProducts();
            }
        });
    }

    private void initModel() {
        model.setEvent(new BookModel.BoolModelEvent() {
            @Override
            public void fetchDataSuccess(List<BookViewEntity> bookViewEntityList) {
                adapter.setBookViewEntityList(bookViewEntityList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void paymentSuccess() {
                loadingDialog.cancel();
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

    private void showLoadingDialog() {
        loadingDialog = ProgressDialog.show(this, "",
                "Uploading. Please wait...", true);
    }

    private void goToImportProducts() {
        Intent orderIntent = new Intent(this, OrderProductActivity.class);
        orderIntent.putExtra("order_activity_type", OrderActivityType.importProduct);
        startActivity(orderIntent);
    }
}