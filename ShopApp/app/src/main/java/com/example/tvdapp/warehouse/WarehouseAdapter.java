package com.example.tvdapp.warehouse;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tvdapp.R;
import com.example.tvdapp.product.ProductViewEntity;

import java.util.List;

public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.WarehouseViewHolder> {
    private List<ProductViewEntity> productViewEntities;

    public void setProductViewEntities(List<ProductViewEntity> productViewEntities) {
        this.productViewEntities = productViewEntities;

        notifyDataSetChanged();
    }

    private Context context;

    public WarehouseAdapter(List<ProductViewEntity> productViewEntities, Context context) {
        this.productViewEntities = productViewEntities;
        this.context = context;
    }

    @NonNull
    @Override
    public WarehouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View warehouseView = inflater.inflate(R.layout.item_warehouse_product, parent, false);
        WarehouseViewHolder warehouseViewHolder = new WarehouseViewHolder(warehouseView, context);
        return warehouseViewHolder;
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

            imageView = itemView.findViewById(R.id.warehouse_product_image_view);
            nameTextView = itemView.findViewById(R.id.warehouse_product_name_text);
            idTextView = itemView.findViewById(R.id.warehouse_product_id_text);
            amountTextView = itemView.findViewById(R.id.warehouse_product_amount_text);
            totalTextView = itemView.findViewById(R.id.warehouse_product_total_text);
        }

        public void bindData(ProductViewEntity entity) {
            nameTextView.setText(entity.name);
            idTextView.setText(entity.id);
            amountTextView.setText(getAmountText(entity.amount));
            totalTextView.setText(entity.price);
            Glide.with(context)
                    .load(entity.imageLink)
                    .centerCrop()
                    .into(imageView);
        }

        private String getAmountText(int amount) {
            return String.format("%s: %d", context.getString(R.string.warehouse_inventory), amount);
        }
    }
}
