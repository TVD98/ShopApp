package com.example.tvdapp.confirmImportProduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmOrder.ConfirmOrderAdapter;
import com.example.tvdapp.confirmOrder.ConfirmOrderViewHolder;
import com.example.tvdapp.confirmOrder.ConfirmProductViewHolder;
import com.example.tvdapp.confirmOrder.ConfirmProductViewHolderEvent;
import com.example.tvdapp.order.ProductOrderViewEntity;

import java.util.List;

public class ConfirmImportProductAdapter extends RecyclerView.Adapter<ConfirmOrderViewHolder> {
    private final int productViewType = 0;
    private final int infoViewType = 1;

    private List<ProductOrderViewEntity> productOrderViewEntities;
    private ConfirmImportProductInfoViewEntity confirmImportProductInfoViewEntity;
    private ConfirmImportProductInfoViewHolder.ConfirmImportProductViewHolderEvent confirmImportProductViewHolderEvent;
    private ConfirmImportProductAdapterEvent confirmImportProductAdapterEvent;

    public void setConfirmImportProductViewHolderEvent(ConfirmImportProductInfoViewHolder.ConfirmImportProductViewHolderEvent confirmImportProductViewHolderEvent) {
        this.confirmImportProductViewHolderEvent = confirmImportProductViewHolderEvent;
    }

    private Context context;

    public void setConfirmProductViewHolderEvent(ConfirmProductViewHolderEvent confirmProductViewHolderEvent) {
        this.confirmProductViewHolderEvent = confirmProductViewHolderEvent;
    }

    private ConfirmProductViewHolderEvent confirmProductViewHolderEvent;

    public ConfirmImportProductAdapter(List<ProductOrderViewEntity> productOrderViewEntities, ConfirmImportProductInfoViewEntity viewEntity, Context context) {
        this.productOrderViewEntities = productOrderViewEntities;
        this.confirmImportProductInfoViewEntity = viewEntity;
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
                View confirmProductInfoView = inflater.inflate(R.layout.item_confirm_import_product_info, parent, false);
                ConfirmImportProductInfoViewHolder confirmProductInfoViewHolder = new ConfirmImportProductInfoViewHolder(confirmProductInfoView, confirmImportProductViewHolderEvent);
                confirmImportProductAdapterEvent = confirmProductInfoViewHolder;
                return confirmProductInfoViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmOrderViewHolder holder, int position) {
        if (getItemViewType(position) == productViewType) {
            ProductOrderViewEntity entity = productOrderViewEntities.get(position);
            ConfirmProductViewHolder viewHolder = (ConfirmProductViewHolder) holder;
            viewHolder.bindData(entity);
        } else {
            ConfirmImportProductInfoViewHolder infoViewHolder = (ConfirmImportProductInfoViewHolder) holder;
            infoViewHolder.bindData(confirmImportProductInfoViewEntity);
        }
    }

    @Override
    public int getItemCount() {
        return productOrderViewEntities.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
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

    public void bindDataToInfoView(ConfirmImportProductInfoViewEntity viewEntity) {
        confirmImportProductAdapterEvent.totalPriceDidChange(
                viewEntity.price,
                viewEntity.total,
                viewEntity.amount
        );
    }

    interface ConfirmImportProductAdapterEvent {
        void totalPriceDidChange(String price, String total, String amount);
    }
}
