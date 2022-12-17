package com.example.tvdapp.confirmOrder;

import android.content.Context;
import android.view.View;


import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.tvdapp.R;
import com.example.tvdapp.order.ProductOrderViewEntity;

public class ConfirmProductViewHolder extends ConfirmOrderViewHolder {
    private TextView nameTextView;
    private TextView totalTextView;
    private TextView countTextView;
    private ImageView minusImageView;
    private ImageView plusImageView;
    private ImageView productImageView;

    private ProductOrderViewEntity entity;
    private Context context;
    private ConfirmProductViewHolderEvent event;

    public ConfirmProductViewHolder(@NonNull View itemView, Context context, ConfirmProductViewHolderEvent event) {
        super(itemView);
        this.context = context;
        this.event = event;

        nameTextView = itemView.findViewById(R.id.confirm_order_product_name_text);
        totalTextView = itemView.findViewById(R.id.confirm_order_total_text);
        countTextView = itemView.findViewById(R.id.confirm_order_product_count_edit_text);
        minusImageView = itemView.findViewById(R.id.confirm_order_product_minus_image);
        plusImageView = itemView.findViewById(R.id.confirm_order_product_plus_image);
        productImageView = itemView.findViewById(R.id.confirm_order_product_image);

        minusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductCount(-1);
            }
        });

        plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductCount(1);
            }
        });
    }

    public void bindData(ProductOrderViewEntity entity) {
        this.entity = entity;

        nameTextView.setText(entity.name);
        totalTextView.setText(getTotalMoney());
        countTextView.setText(Integer.toString(entity.count));
        Glide.with(context)
                .load(entity.imageLink)
                .centerCrop()
                .into(productImageView);
    }

    private String getTotalMoney() {
        int total = entity.price * entity.count;
        return String.format("%,d", total);
    }

    private void addProductCount(int count) {
        int newCount = entity.count + count;
        updateUIByCount(newCount);

        if (event != null) {
            event.productDidChange(entity);
        }
    }

    private void updateUIByCount(int newCount) {
        entity.count = newCount;
        countTextView.setText(Integer.toString(entity.count));
        totalTextView.setText(getTotalMoney());
    }
}
