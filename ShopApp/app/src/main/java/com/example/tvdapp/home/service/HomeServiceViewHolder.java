package com.example.tvdapp.home.service;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.home.HomeViewHolder;
import com.example.tvdapp.home.service.model.ServiceDataResponse;
import com.example.tvdapp.home.service.model.ServiceDataResponseList;
import com.example.tvdapp.home.turnover.TurnoverAdapter;
import com.example.tvdapp.home.turnover.TurnoverViewEntity;
import com.example.tvdapp.home.turnover.model.TurnoverDataResponse;
import com.example.tvdapp.home.turnover.model.TurnoverDataResponseList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeServiceViewHolder extends HomeViewHolder {
    private Context context;
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private List<ServiceViewEntity> itemList = new ArrayList<>();
    private List<ServiceItem> serviceTypeList = new ArrayList<>();
    private HomeServiceEvent homeServiceEvent;

    public HomeServiceViewHolder(@NonNull View itemView, Context context, HomeServiceEvent event) {
        super(itemView);
        this.context = context;
        this.homeServiceEvent = event;

        recyclerView = itemView.findViewById(R.id.service_recycler_view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initModel(ServiceDataResponseList response) {
        ServiceItem[] serviceTypes = ServiceItem.values();
        for (ServiceDataResponse data : response.data) {
            ServiceItem type = Arrays.stream(serviceTypes)
                    .filter(serviceType -> serviceType.getId() == data.serviceId)
                    .findAny()
                    .orElse(null);
            if (type != null) {
                String title = context.getString(type.getStringId());
                ServiceViewEntity entity = new ServiceViewEntity(title, type.getImageId());
                itemList.add(entity);
                serviceTypeList.add(type);
            }
        }
        String moreTitle = context.getString(ServiceItem.more.getStringId());
        itemList.add(new ServiceViewEntity(moreTitle, ServiceItem.more.getImageId()));
        serviceTypeList.add(ServiceItem.more);

        initUI();
    }

    private void initUI() {
        adapter = new ServiceAdapter(itemList, context, new ServiceAdapter.ServiceViewHolderEvent() {
            @Override
            public void selectServiceItem(int position) {
                if (homeServiceEvent != null) {
                    homeServiceEvent.selectServiceItem(serviceTypeList.get(position));
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
    }
}
