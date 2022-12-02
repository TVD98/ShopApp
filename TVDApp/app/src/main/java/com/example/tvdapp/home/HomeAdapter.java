package com.example.tvdapp.home;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.home.order.HomeOrderViewHolder;
import com.example.tvdapp.home.order.model.HomeOrderEvent;
import com.example.tvdapp.home.order.model.OrderDataResponse;
import com.example.tvdapp.home.order.model.OrderDataResponseList;
import com.example.tvdapp.home.service.HomeServiceEvent;
import com.example.tvdapp.home.service.HomeServiceViewHolder;
import com.example.tvdapp.home.service.model.ServiceDataResponse;
import com.example.tvdapp.home.service.model.ServiceDataResponseList;
import com.example.tvdapp.home.turnover.HomeTurnoverEvent;
import com.example.tvdapp.home.turnover.HomeTurnoverViewHolder;
import com.example.tvdapp.home.turnover.model.TurnoverDataResponse;
import com.example.tvdapp.home.turnover.model.TurnoverDataResponseList;
import com.example.tvdapp.utilities.Constant;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private HomeItem[] itemList;
    private Context context;
    private HomeServiceEvent homeServiceEvent;
    private HomeOrderEvent homeOrderEvent;
    private HomeTurnoverEvent homeTurnoverEvent;

    public void setHomeServiceEvent(HomeServiceEvent homeServiceEvent) {
        this.homeServiceEvent = homeServiceEvent;
    }

    public void setHomeOrderEvent(HomeOrderEvent homeOrderEvent) {
        this.homeOrderEvent = homeOrderEvent;
    }

    public void setHomeTurnoverEvent(HomeTurnoverEvent homeTurnoverEvent) {
        this.homeTurnoverEvent = homeTurnoverEvent;
    }

    private TurnoverDataResponseList turnoverData = new TurnoverDataResponseList(new TurnoverDataResponse[]{
            new TurnoverDataResponse(0, 2),
            new TurnoverDataResponse(1, 0),
            new TurnoverDataResponse(2, 1),
    });

    private ServiceDataResponseList serviceData = new ServiceDataResponseList(new ServiceDataResponse[]{
            new ServiceDataResponse(0),
            new ServiceDataResponse(1),
            new ServiceDataResponse(2),
            new ServiceDataResponse(3),
    });

    private OrderDataResponseList orderData = new OrderDataResponseList(new OrderDataResponse[]{
            new OrderDataResponse(0, 1),
            new OrderDataResponse(1, 2)
    });

    public HomeAdapter(HomeItem[] itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case Constant.turnoverValue:
                View turnoverView = inflater.inflate(R.layout.item_home_turnover, parent, false);
                HomeTurnoverViewHolder turnoverViewHolder = new HomeTurnoverViewHolder(turnoverView, context, homeTurnoverEvent);
                turnoverViewHolder.initModel(turnoverData);
                return turnoverViewHolder;
            case Constant.serviceValue:
                View serviceView = inflater.inflate(R.layout.item_home_service, parent, false);
                HomeServiceViewHolder serviceViewHolder = new HomeServiceViewHolder(serviceView, context, homeServiceEvent);
                serviceViewHolder.initModel(serviceData);
                return serviceViewHolder;
            case Constant.orderValue:
                View orderView = inflater.inflate(R.layout.item_home_order, parent, false);
                HomeOrderViewHolder orderViewHolder = new HomeOrderViewHolder(orderView, context, homeOrderEvent);
                orderViewHolder.initModel(orderData);
                return orderViewHolder;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return itemList[position].getValue();
    }

    @Override
    public int getItemCount() {
        return itemList.length;
    }

    enum HomeItem {
        turnover, service, order, support;

        int getValue() {
            switch (this) {
                case turnover:
                    return Constant.turnoverValue;
                case service:
                    return Constant.serviceValue;
                case order:
                    return Constant.orderValue;
                case support:
                    return Constant.supportValue;
                default:
                    return 0;
            }
        }
    }
}
