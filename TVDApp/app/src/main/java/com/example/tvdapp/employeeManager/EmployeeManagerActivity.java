package com.example.tvdapp.employeeManager;

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
import com.example.tvdapp.users.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManagerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private View createUserView;
    private EmployeeManagerAdapter adapter;
    private EmployeeManagerModel model = new EmployeeManagerModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_employee_manager);

        initUI();
        initModel();
    }

    private void setupNavigation() {
        setTitle(R.string.employee_manager_title);
    }

    private void initUI() {
        setupNavigation();

        recyclerView = findViewById(R.id.employee_manager_recycler_view);
        createUserView = findViewById(R.id.employee_manager_create_user_view);

        setupUI();
    }

    private void setupUI() {
        adapter = new EmployeeManagerAdapter(new ArrayList<>(), this);
        adapter.setEvent(new EmployeeManagerAdapter.EmployeeManagerViewHolderEvent() {
            @Override
            public void selectItem(String username) {

            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterActivity();
            }
        });
    }

    private void initModel() {
        model.setContext(this);
        model.setEvent(new EmployeeManagerModel.EmployeeManagerModelEvent() {
            @Override
            public void fetchDataSuccess(List<EmployeeManagerViewEntity> employeeManagerViewEntities) {
                adapter.setEmployeeManagerViewEntityList(employeeManagerViewEntities);
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

    private void goToRegisterActivity() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}