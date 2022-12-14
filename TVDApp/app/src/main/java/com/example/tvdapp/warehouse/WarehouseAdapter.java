package com.example.tvdapp.warehouse;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.product.ProductViewEntity;

import java.util.List;

public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.WarehouseViewHolder> {
    private List<ProductViewEntity> productViewEntities;
    private Context context;

    public WarehouseAdapter(List<ProductViewEntity> productViewEntities, Context context) {
        this.productViewEntities = productViewEntities;
        this.context = context;
    }

    @NonNull
    @Override
    public WarehouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WarehouseViewHolder holder, int position) {
        ProductViewEntity entity = productViewEntities.get(position);
        holder.bindData(entity);
    }

    @Override
    public int getItemCount() {
        return productViewEntities.size();
    }

    class WarehouseViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView idTextView;
        private TextView amountTextView;
        private TextView totalTextView;
        private Context context;

        public WarehouseViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
        }

        public void bindData(ProductViewEntity entity) {
            nameTextView.setText(entity.name);
            idTextView.setText(entity.id);
            amountTextView.setText(getAmountText(entity.amount));
            totalTextView.setText(entity.price);
        }

        private String getAmountText(int amount) {
            return String.format("%s: %d", context.getString(R.string.warehouse_inventory), amount);
        }
    }
}
