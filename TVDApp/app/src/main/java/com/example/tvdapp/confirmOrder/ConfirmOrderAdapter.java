package com.example.tvdapp.confirmOrder;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmOrder.model.ConfirmOrderInfoViewEntity;
import com.example.tvdapp.order.ProductOrderViewEntity;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderViewHolder> {
    private final int addItemsViewType = 0;
    private final int productViewType = 1;
    private final int infoViewType = 2;

    private List<ProductOrderViewEntity> productOrderViewEntities = new ArrayList<>();
    private ConfirmOrderInfoViewEntity confirmOrderInfoViewEntity;
    private Context context;
    private ConfirmOrderAdapterEvent confirmOrderAdapterEvent;
    private AddItemsViewHolder.AddItemsViewHolderEvent addItemsViewHolderEvent;
    private ConfirmProductViewHolder.ConfirmProductViewHolderEvent confirmProductViewHolderEvent;
    private ConfirmInfoViewHolder.ConfirmInfoViewHolderEvent confirmInfoViewHolderEvent;

    public void setConfirmInfoViewHolderEvent(ConfirmInfoViewHolder.ConfirmInfoViewHolderEvent confirmInfoViewHolderEvent) {
        this.confirmInfoViewHolderEvent = confirmInfoViewHolderEvent;
    }

    public void setConfirmProductViewHolderEvent(ConfirmProductViewHolder.ConfirmProductViewHolderEvent confirmProductViewHolderEvent) {
        this.confirmProductViewHolderEvent = confirmProductViewHolderEvent;
    }

    public void setAddItemsViewHolderEvent(AddItemsViewHolder.AddItemsViewHolderEvent addItemsViewHolderEvent) {
        this.addItemsViewHolderEvent = addItemsViewHolderEvent;
    }

    public ConfirmOrderAdapter(List<ProductOrderViewEntity> productOrderViewEntities, ConfirmOrderInfoViewEntity confirmOrderInfoViewEntity, Context context) {
        this.productOrderViewEntities = productOrderViewEntities;
        this.confirmOrderInfoViewEntity = confirmOrderInfoViewEntity;
        this.context = context;
    }

    @NonNull
    @Override
    public ConfirmOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case addItemsViewType:
                View addItemsView = inflater.inflate(R.layout.item_add_products, parent, false);
                AddItemsViewHolder addItemsViewHolder = new AddItemsViewHolder(addItemsView, addItemsViewHolderEvent);
                return addItemsViewHolder;
            case productViewType:
                View confirmProductView = inflater.inflate(R.layout.item_confirm_product, parent, false);
                ConfirmProductViewHolder confirmProductViewHolder = new ConfirmProductViewHolder(confirmProductView, context, confirmProductViewHolderEvent);
                return confirmProductViewHolder;
            default:
                View confirmInfoView = inflater.inflate(R.layout.item_confirm_order_info, parent, false);
                ConfirmInfoViewHolder confirmInfoViewHolder = new ConfirmInfoViewHolder(confirmInfoView, confirmInfoViewHolderEvent);
                confirmOrderAdapterEvent = confirmInfoViewHolder;
                return confirmInfoViewHolder;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ConfirmOrderViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == productViewType) {
            ProductOrderViewEntity entity = productOrderViewEntities.get(position - 1);
            ConfirmProductViewHolder confirmProductViewHolder = (ConfirmProductViewHolder) holder;
            confirmProductViewHolder.bindData(entity);
        } else if (viewType == infoViewType) {
            ConfirmInfoViewHolder confirmInfoViewHolder = (ConfirmInfoViewHolder) holder;
            confirmInfoViewHolder.bindData(confirmOrderInfoViewEntity);
        }
    }

    @Override
    public int getItemCount() {
        return productOrderViewEntities.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return addItemsViewType;
        } else if (position == getItemCount() - 1) {
            return infoViewType;
        } else {
            return productViewType;
        }
    }

    public void removeProduct(ProductOrderViewEntity entity) {
        int index = productOrderViewEntities.indexOf(entity);
        productOrderViewEntities.remove(entity);
        notifyItemRemoved(index + 1);
    }

    public boolean cartIsEmpty() {
        return productOrderViewEntities.isEmpty();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void bindDataToInfoView(ConfirmOrderInfoViewEntity confirmOrderInfoViewEntity) {
        confirmOrderAdapterEvent.totalPriceDidChange(confirmOrderInfoViewEntity.price, confirmOrderInfoViewEntity.total);
    }

    interface ConfirmOrderAdapterEvent {
        void totalPriceDidChange(String price, String total);
    }
}
