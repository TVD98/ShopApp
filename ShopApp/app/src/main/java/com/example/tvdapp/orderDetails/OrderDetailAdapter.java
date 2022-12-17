package com.example.tvdapp.orderDetails;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.orderDetails.model.OrderDetailBillInfoViewEntity;
import com.example.tvdapp.orderDetails.model.OrderDetailOrderInfoViewEntity;
import com.example.tvdapp.orderDetails.model.OrderDetailPriceProductViewEntity;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailViewHolder> {

    private final int orderInfoViewType = 0;
    private final int productPriceInfoViewType = 1;
    private final int billInfoViewType = 2;

    private List<OrderDetailPriceProductViewEntity> orderDetailPriceProductViewEntities;
    private OrderDetailBillInfoViewEntity orderDetailBillInfoViewEntity;
    private OrderDetailOrderInfoViewEntity orderDetailOrderInfoViewEntity;
    private Context context;

    public void setOrderDetailPriceProductViewEntities(List<OrderDetailPriceProductViewEntity> orderDetailPriceProductViewEntities) {
        this.orderDetailPriceProductViewEntities = orderDetailPriceProductViewEntities;
    }

    public void setOrderDetailBillInfoViewEntity(OrderDetailBillInfoViewEntity orderDetailBillInfoViewEntity) {
        this.orderDetailBillInfoViewEntity = orderDetailBillInfoViewEntity;
    }

    public void setOrderDetailOrderInfoViewEntity(OrderDetailOrderInfoViewEntity orderDetailOrderInfoViewEntity) {
        this.orderDetailOrderInfoViewEntity = orderDetailOrderInfoViewEntity;
    }

    public OrderDetailAdapter(List<OrderDetailPriceProductViewEntity> orderDetailPriceProductViewEntities, OrderDetailBillInfoViewEntity orderDetailBillInfoViewEntity, OrderDetailOrderInfoViewEntity orderDetailOrderInfoViewEntity, Context context) {
        this.orderDetailPriceProductViewEntities = orderDetailPriceProductViewEntities;
        this.orderDetailBillInfoViewEntity = orderDetailBillInfoViewEntity;
        this.orderDetailOrderInfoViewEntity = orderDetailOrderInfoViewEntity;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case orderInfoViewType:
                View orderDetailOrderView = inflater.inflate(R.layout.item_order_detail_order_info, parent, false);
                OrderDetailOrderInfoViewHolder orderDetailOrderInfoViewHolder = new OrderDetailOrderInfoViewHolder(orderDetailOrderView, context);
                return orderDetailOrderInfoViewHolder;
            case productPriceInfoViewType:
                View orderDetailPriceProductView = inflater.inflate(R.layout.item_order_detail_product_price_info, parent, false);
                OrderDetailPriceProductViewHolder orderDetailPriceProductViewHolder = new OrderDetailPriceProductViewHolder(orderDetailPriceProductView, context);
                return orderDetailPriceProductViewHolder;
            default:
                View orderDetailBillInfoView = inflater.inflate(R.layout.item_order_detail_bill_info, parent, false);
                OrderDetailBillInfoViewHolder orderDetailBillInfoViewHolder = new OrderDetailBillInfoViewHolder(orderDetailBillInfoView);
                return orderDetailBillInfoViewHolder;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == productPriceInfoViewType) {
            OrderDetailPriceProductViewEntity entity = orderDetailPriceProductViewEntities.get(position - 1);
            OrderDetailPriceProductViewHolder orderDetailPriceProductViewHolder = (OrderDetailPriceProductViewHolder) holder;
            orderDetailPriceProductViewHolder.bindData(entity);
        } else if (viewType == billInfoViewType) {
            OrderDetailBillInfoViewHolder orderDetailBillInfoViewHolder = (OrderDetailBillInfoViewHolder) holder;
            orderDetailBillInfoViewHolder.bindData(orderDetailBillInfoViewEntity);
        } else {
            OrderDetailOrderInfoViewHolder orderDetailOrderInfoViewHolder = (OrderDetailOrderInfoViewHolder) holder;
            orderDetailOrderInfoViewHolder.bindData(orderDetailOrderInfoViewEntity);
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailPriceProductViewEntities.size() + 2;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return orderInfoViewType;
        } else if (position == getItemCount() - 1) {
            return billInfoViewType;
        } else {
            return productPriceInfoViewType;
        }
    }

}
