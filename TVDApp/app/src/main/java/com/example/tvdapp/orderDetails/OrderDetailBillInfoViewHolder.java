package com.example.tvdapp.orderDetails;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tvdapp.R;
import com.example.tvdapp.orderDetails.model.OrderDetailBillInfoViewEntity;
import com.example.tvdapp.orderDetails.model.OrderDetailOrderInfoViewEntity;

public class OrderDetailBillInfoViewHolder extends  OrderDetailViewHolder {

    private TextView orderDetailBillInfoPriceProductText;
    private TextView orderDetailBillInfoDiscountText;
    private TextView orderDetailBillInfoVATFeeText;
    private TextView orderDetailBillInfoTransitionFeeText;
    private TextView orderDetailBillInfoTotalText;


    public OrderDetailBillInfoViewHolder(@NonNull View itemView) {
        super(itemView);

        orderDetailBillInfoPriceProductText = itemView.findViewById(R.id.order_detail_bill_info_price_text);
        orderDetailBillInfoDiscountText = itemView.findViewById(R.id.order_detail_bill_info_discount_text);
        orderDetailBillInfoVATFeeText = itemView.findViewById(R.id.order_detail_bill_info_VAT_fee_text);
        orderDetailBillInfoTransitionFeeText = itemView.findViewById(R.id.order_detail_bill_info_transition_fee_text);
        orderDetailBillInfoTotalText = itemView.findViewById(R.id.order_detail_bill_info_total_text);
    }


    public void bindData(OrderDetailBillInfoViewEntity orderDetailBillInfoViewEntity) {
        orderDetailBillInfoPriceProductText.setText(orderDetailBillInfoViewEntity.priceProduct_billInfo);
        orderDetailBillInfoDiscountText.setText(orderDetailBillInfoViewEntity.discountBill);
        orderDetailBillInfoVATFeeText.setText(orderDetailBillInfoViewEntity.VATfee);
        orderDetailBillInfoTransitionFeeText.setText(orderDetailBillInfoViewEntity.transitionFee);
        orderDetailBillInfoTotalText.setText(orderDetailBillInfoViewEntity.totalPayment);

    }

}
