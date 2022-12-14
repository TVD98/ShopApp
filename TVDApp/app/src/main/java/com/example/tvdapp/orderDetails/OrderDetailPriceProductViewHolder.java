package com.example.tvdapp.orderDetails;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.tvdapp.R;
import com.example.tvdapp.orderDetails.model.OrderDetailPriceProductViewEntity;

public class OrderDetailPriceProductViewHolder extends  OrderDetailViewHolder{

    private ImageView orderDetailProductImage;
    private TextView orderDetailProductNameText;
    private TextView orderDetailProductPriceText;
    private TextView orderDetailProductCountText;
    private Context context;


    public OrderDetailPriceProductViewHolder(@NonNull View itemView) {
        super(itemView);

        orderDetailProductImage = itemView.findViewById(R.id.order_detail_product_image);
        orderDetailProductNameText = itemView.findViewById(R.id.order_detail_product_name_text);
        orderDetailProductPriceText = itemView.findViewById(R.id.order_detail_product_price_text);
        orderDetailProductCountText = itemView.findViewById(R.id.order_detail_count_product);
    }

    public void bindData(OrderDetailPriceProductViewEntity orderDetailPriceProductViewEntity) {
        orderDetailProductNameText.setText(orderDetailPriceProductViewEntity.nameProduct);
        orderDetailProductPriceText.setText(orderDetailPriceProductViewEntity.priceBill);
        orderDetailProductCountText.setText(orderDetailPriceProductViewEntity.countProduct);
        Glide.with(context)
                .load(orderDetailPriceProductViewEntity.imageProduct)
                .centerCrop()
                .into(orderDetailProductImage);

    }
}
