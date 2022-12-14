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

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private Context context;
    private HomeServiceEvent homeServiceEvent;
    private HomeOrderEvent homeOrderEvent;
    private HomeTurnoverEvent homeTurnoverEvent;

    public void setItemList(List<HomeItem> itemList) {
        this.itemList = itemList;

        notifyDataSetChanged();
    }

    private List<HomeItem> itemList;

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

    private ServiceDataResponseList serviceData = new ServiceDataResponseList(new ServiceDataResponse[]{});

    public void setServiceData(ServiceDataResponseList serviceData) {
        this.serviceData = serviceData;

        reloadServiceView();
    }

    private OrderDataResponseList orderData = new OrderDataResponseList(new OrderDataResponse[]{});

    public void setOrderData(OrderDataResponseList orderData) {
        this.orderData = orderData;

        reloadOrderView();
    }

    public HomeAdapter(List<HomeItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    private void reloadServiceView() {
        int index = itemList.indexOf(HomeItem.service);
        if (index != -1) {
            notifyItemChanged(index);
        }
    }

    private void reloadOrderView() {
        int index = itemList.indexOf(HomeItem.order);
        if (index != -1) {
            notifyItemChanged(index);
        }
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
                return turnoverViewHolder;
            case Constant.serviceValue:
                View serviceView = inflater.inflate(R.layout.item_home_service, parent, false);
                HomeServiceViewHolder serviceViewHolder = new HomeServiceViewHolder(serviceView, context, homeServiceEvent);
                return serviceViewHolder;
            case Constant.orderValue:
                View orderView = inflater.inflate(R.layout.item_home_order, parent, false);
                HomeOrderViewHolder orderViewHolder = new HomeOrderViewHolder(orderView, context, homeOrderEvent);
                return orderViewHolder;
            default:
                return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Constant.turnoverValue:
                HomeTurnoverViewHolder turnoverViewHolder = (HomeTurnoverViewHolder)holder;
                turnoverViewHolder.initModel(turnoverData);
                break;
            case Constant.serviceValue:
                HomeServiceViewHolder serviceViewHolder = (HomeServiceViewHolder)holder;
                serviceViewHolder.initModel(serviceData);
                break;
            case Constant.orderValue:
                HomeOrderViewHolder orderViewHolder = (HomeOrderViewHolder) holder;
                orderViewHolder.initModel(orderData);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getValue();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
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
