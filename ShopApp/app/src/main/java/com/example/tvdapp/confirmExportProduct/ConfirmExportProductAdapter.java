package com.example.tvdapp.confirmExportProduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmOrder.ConfirmOrderViewHolder;
import com.example.tvdapp.confirmOrder.ConfirmProductViewHolder;
import com.example.tvdapp.confirmOrder.ConfirmProductViewHolderEvent;
import com.example.tvdapp.order.ProductOrderViewEntity;

import java.util.List;

public class ConfirmExportProductAdapter extends RecyclerView.Adapter<ConfirmOrderViewHolder> {
    private final int productViewType = 0;
    private final int infoViewType = 1;

    private List<ProductOrderViewEntity> productOrderViewEntities;
    private ConfirmExportProductInfoViewEntity confirmExportProductInfoViewEntity;
    private ConfirmExportProductInfoViewHolder.ConfirmExportProductInfoViewHolderEvent confirmExportProductInfoViewHolderEvent;
    private ConfirmProductViewHolderEvent confirmProductViewHolderEvent;
    private Context context;

    public void setConfirmProductViewHolderEvent(ConfirmProductViewHolderEvent confirmProductViewHolderEvent) {
        this.confirmProductViewHolderEvent = confirmProductViewHolderEvent;
    }

    public void setConfirmExportProductInfoViewHolderEvent(ConfirmExportProductInfoViewHolder.ConfirmExportProductInfoViewHolderEvent confirmExportProductInfoViewHolderEvent) {
        this.confirmExportProductInfoViewHolderEvent = confirmExportProductInfoViewHolderEvent;
    }

    public ConfirmExportProductAdapter(List<ProductOrderViewEntity> productOrderViewEntities, ConfirmExportProductInfoViewEntity confirmExportProductInfoViewEntity, Context context) {
        this.productOrderViewEntities = productOrderViewEntities;
        this.confirmExportProductInfoViewEntity = confirmExportProductInfoViewEntity;
        this.context = context;
    }

    @NonNull
    @Override
    public ConfirmOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case productViewType:
                View confirmProductView = inflater.inflate(R.layout.item_confirm_product, parent, false);
                ConfirmProductViewHolder confirmProductViewHolder = new ConfirmProductViewHolder(confirmProductView, context, confirmProductViewHolderEvent);
                return confirmProductViewHolder;
            default:
                View confirmExportInfoView = inflater.inflate(R.layout.item_confirm_export_product_info, parent, false);
                ConfirmExportProductInfoViewHolder confirmExportProductInfoViewHolder = new ConfirmExportProductInfoViewHolder(confirmExportInfoView, context, confirmExportProductInfoViewHolderEvent);
                return confirmExportProductInfoViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmOrderViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case productViewType:
                ProductOrderViewEntity entity = productOrderViewEntities.get(position);
                ConfirmProductViewHolder productViewHolder = (ConfirmProductViewHolder) holder;
                productViewHolder.bindData(entity);
                break;
            default:
                ConfirmExportProductInfoViewHolder infoViewHolder = (ConfirmExportProductInfoViewHolder) holder;
                infoViewHolder.bindData(confirmExportProductInfoViewEntity);
        }
    }

    @Override
    public int getItemCount() {
        return productOrderViewEntities.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == productOrderViewEntities.size()) {
            return infoViewType;
        } else {
            return productViewType;
        }
    }

    public void removeProduct(ProductOrderViewEntity entity) {
        int index = productOrderViewEntities.indexOf(entity);
        productOrderViewEntities.remove(entity);
        notifyItemRemoved(index);
    }
}
