package com.example.tvdapp.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tvdapp.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductViewEntity> productViewEntities;
    private Context context;
    private ProductViewHolderEvent event;

    public void setEvent(ProductViewHolderEvent event) {
        this.event = event;
    }

    public ProductAdapter(List<ProductViewEntity> productViewEntities, Context context) {
        this.productViewEntities = productViewEntities;
        this.context = context;
    }

    public void setProductViewEntities(List<ProductViewEntity> productViewEntities) {
        this.productViewEntities = productViewEntities;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView = inflater.inflate(R.layout.item_product, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(productView, context, event);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductViewEntity entity = productViewEntities.get(position);
        holder.bindData(entity);
    }

    @Override
    public int getItemCount() {
        return productViewEntities.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView amountTextView;
        private TextView priceTextView;
        private Context context;
        private ProductViewHolderEvent event;
        private ProductViewEntity entity;

        public ProductViewHolder(@NonNull View itemView, Context context, ProductViewHolderEvent event) {
            super(itemView);
            this.context = context;
            this.event = event;

            imageView = itemView.findViewById(R.id.product_image_view);
            nameTextView = itemView.findViewById(R.id.product_name_text);
            amountTextView = itemView.findViewById(R.id.product_amount_text);
            priceTextView = itemView.findViewById(R.id.product_price_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (event != null) {
                        event.selectProductView(entity.id);
                    }
                }
            });
        }

        private String getAmountString(int amount) {
            String description = context.getString(R.string.product_amount_description);
            return String.format("%s: %d", description, amount);
        }

        public void bindData(ProductViewEntity entity) {
            this.entity = entity;
            nameTextView.setText(entity.name);
            amountTextView.setText(getAmountString(entity.amount));
            priceTextView.setText(entity.price);
            Glide.with(context)
                    .load(entity.imageLink)
                    .centerCrop()
                    .into(imageView);
        }
    }

    interface ProductViewHolderEvent {
        void selectProductView(String productId);
    }
}
