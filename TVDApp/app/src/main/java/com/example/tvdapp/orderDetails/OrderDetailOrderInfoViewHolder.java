package com.example.tvdapp.orderDetails;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmOrder.model.ConfirmOrderInfoViewEntity;
import com.example.tvdapp.orderDetails.model.OrderDetailOrderInfoViewEntity;

public class OrderDetailOrderInfoViewHolder extends OrderDetailViewHolder {
    private TextView orderDetailCodeBillText;
    private EditText orderDetailDateBillEditText;
    private TextView orderDetailPriceText;
    private EditText orderDetailStatusPayEditText;
    private TextView orderDetailStatusDeliveredText;
    private Button orderDetailSendInvoiceButton;
    private ImageView orderDetailImageCustomer;
    private TextView orderDetailCustomerNameText;


    public OrderDetailOrderInfoViewHolder(@NonNull View itemView) {
        super(itemView);

        orderDetailCodeBillText = itemView.findViewById(R.id.order_detail_code_bill_text);
        orderDetailStatusDeliveredText = itemView.findViewById(R.id.order_detail_status_text);
        orderDetailDateBillEditText = itemView.findViewById(R.id.order_detail_time_bill_edit_text);
        orderDetailPriceText = itemView.findViewById(R.id.order_detail_price_bill_text);
        orderDetailStatusPayEditText = itemView.findViewById(R.id.order_detail_status_pay_edit_text);
        orderDetailSendInvoiceButton = itemView.findViewById(R.id.order_detail_send_invoice_button);
        orderDetailImageCustomer = itemView.findViewById(R.id.order_detail_customer_image);
        orderDetailCustomerNameText = itemView.findViewById(R.id.order_detail_customer_name_text);

    }

    public void bindData(OrderDetailOrderInfoViewEntity orderDetailOrderInfoViewEntity) {
        orderDetailCodeBillText.setText(orderDetailOrderInfoViewEntity.codeBill);
        orderDetailStatusDeliveredText.setText(orderDetailOrderInfoViewEntity.statusBill);
        orderDetailDateBillEditText.setText(orderDetailOrderInfoViewEntity.dateBil);
        orderDetailPriceText.setText(orderDetailOrderInfoViewEntity.priceProduct);
        orderDetailStatusPayEditText.setText(orderDetailOrderInfoViewEntity.statusPayment);
        orderDetailCustomerNameText.setText(orderDetailOrderInfoViewEntity.nameCustomer);

    }

}