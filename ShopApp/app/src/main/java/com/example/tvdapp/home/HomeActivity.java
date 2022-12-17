package com.example.tvdapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.tvdapp.R;
import com.example.tvdapp.employeeManager.EmployeeManagerActivity;
import com.example.tvdapp.employeeManager.EmployeeManagerModel;
import com.example.tvdapp.home.order.OrderItem;
import com.example.tvdapp.home.order.model.HomeOrderEvent;
import com.example.tvdapp.home.order.model.OrderDataResponseList;
import com.example.tvdapp.home.service.HomeServiceEvent;
import com.example.tvdapp.home.service.ServiceItem;
import com.example.tvdapp.home.service.model.ServiceDataResponseList;
import com.example.tvdapp.home.turnover.HomeTurnoverEvent;
import com.example.tvdapp.home.turnover.TurnoverItem;
import com.example.tvdapp.home.turnover.model.TurnoverDataResponseList;
import com.example.tvdapp.order.OrderProductActivity;
import com.example.tvdapp.orderMangager.OrderManagerActivity;
import com.example.tvdapp.product.ProductActivity;
import com.example.tvdapp.warehouse.WarehouseActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView homeRecyclerView;
    private HomeAdapter adapter;
    private List<HomeAdapter.HomeItem> homeItemList = new ArrayList<>();
    private HomeModel model = new HomeModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        model.userType = intent.getStringExtra("user_type");

        initUI();
        setupEvent();
        initModel();
    }

    private void initModel() {
        model.fetchHomeItemList();
    }

    private void initUI() {
        homeRecyclerView = findViewById(R.id.home_recycler_view);
        setupUI();
    }

    private void setupUI() {
        adapter = new HomeAdapter(homeItemList, this);
        homeRecyclerView.setAdapter(adapter);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupEvent() {
        adapter.setHomeServiceEvent(new HomeServiceEvent() {
            @Override
            public void selectServiceItem(ServiceItem serviceItem) {
                switch (serviceItem) {
                    case createOrder:
                        goToOrderActivity();
                        break;
                    case order:
                        goToOrderManager(0);
                        break;
                    case product:
                        goToProductActivity();
                        break;
                    case warehouse:
                        goToWarehouseActivity();
                        break;
                    case employee:
                        goToEmployeeManager();
                        break;
                    default:
                        break;
                }
            }
        });

        adapter.setHomeOrderEvent(new HomeOrderEvent() {
            @Override
            public void selectOrderItem(OrderItem type) {
                int position = type.getId() + 1;
                goToOrderManager(position);
            }
        });

        adapter.setHomeTurnoverEvent(new HomeTurnoverEvent() {
            @Override
            public void selectTurnoverItem(TurnoverItem turnoverItem) {
                Log.i("TVD", Integer.toString(turnoverItem.getId()));
            }
        });

        model.setEvent(new HomeModel.HomeModelEvent() {
            @Override
            public void fetchHomeItemListSuccess(List<HomeAdapter.HomeItem> itemList) {
                homeItemList = itemList;
                adapter.setItemList(itemList);
            }

            @Override
            public void fetchOrderDataListSuccess(OrderDataResponseList orderDataResponseList) {
                adapter.setOrderData(orderDataResponseList);
            }

            @Override
            public void fetchServiceDataListSuccess(ServiceDataResponseList serviceDataResponseList) {
                adapter.setServiceData(serviceDataResponseList);
            }

            @Override
            public void fetchTurnoverDataListSuccess(TurnoverDataResponseList turnoverDataResponseList) {
                adapter.setTurnoverData(turnoverDataResponseList);
            }
        });
    }

    private void goToOrderActivity() {
        Intent orderIntent = new Intent(this, OrderProductActivity.class);
        startActivity(orderIntent);
    }

    private void goToOrderManager(int position) {
        Intent orderManagerIntent = new Intent(this, OrderManagerActivity.class);
        orderManagerIntent.putExtra("tabSelection", position);
        startActivity(orderManagerIntent);
    }

    private void goToProductActivity() {
        Intent productIntent = new Intent(this, ProductActivity.class);
        startActivity(productIntent);
    }

    private void goToWarehouseActivity() {
        Intent warehouseIntent = new Intent(this, WarehouseActivity.class);
        startActivity(warehouseIntent);
    }

    private void goToEmployeeManager() {
        Intent employeeMangerIntent = new Intent(this, EmployeeManagerActivity.class);
        startActivity(employeeMangerIntent);
    }
}