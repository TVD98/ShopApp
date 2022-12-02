package com.example.tvdapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.tvdapp.R;
import com.example.tvdapp.home.order.OrderItem;
import com.example.tvdapp.home.order.model.HomeOrderEvent;
import com.example.tvdapp.home.service.HomeServiceEvent;
import com.example.tvdapp.home.service.ServiceItem;
import com.example.tvdapp.home.turnover.HomeTurnoverEvent;
import com.example.tvdapp.home.turnover.TurnoverItem;
import com.example.tvdapp.order.OrderProductActivity;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView homeRecyclerView;
    private HomeAdapter adapter;
    private final HomeAdapter.HomeItem[] itemList = { HomeAdapter.HomeItem.turnover, HomeAdapter.HomeItem.service,
            HomeAdapter.HomeItem.order };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        setupEvent();
    }

    private void initUI() {
        homeRecyclerView = findViewById(R.id.home_recycler_view);
        setupUI();
    }

    private void setupUI() {
        adapter = new HomeAdapter(itemList, this);
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
                    default:
                        break;
                }
            }
        });

        adapter.setHomeOrderEvent(new HomeOrderEvent() {
            @Override
            public void selectOrderItem(OrderItem type) {

            }
        });

        adapter.setHomeTurnoverEvent(new HomeTurnoverEvent() {
            @Override
            public void selectTurnoverItem(TurnoverItem turnoverItem) {
                Log.i("TVD", Integer.toString(turnoverItem.getId()));
            }
        });
    }

    private void goToOrderActivity() {
        Intent orderIntent = new Intent(this, OrderProductActivity.class);
        startActivity(orderIntent);
    }
}