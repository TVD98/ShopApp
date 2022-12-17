package com.example.tvdapp.orderDetails;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmOrder.model.ConfirmOrderInfoViewEntity;
import com.example.tvdapp.home.order.OrderItem;
import com.example.tvdapp.orderDetails.model.OrderDetailOrderInfoViewEntity;

public class OrderDetailOrderInfoViewHolder extends OrderDetailViewHolder {
    private TextView orderDetailCodeBillText;
    private TextView orderDetailDateBillEditText;
    private TextView orderDetailPriceText;
    private TextView orderDetailStatusPayEditText;
    private TextView orderDetailStatusDeliveredText;
    private CardView statusBackgroundView;
    private Button orderDetailSendInvoiceButton;
    private ImageView orderDetailImageCustomer;
    private TextView orderDetailCustomerNameText;
    private Context context;


    public OrderDetailOrderInfoViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;

        orderDetailCodeBillText = itemView.findViewById(R.id.order_detail_code_bill_text);
        orderDetailStatusDeliveredText = itemView.findViewById(R.id.order_detail_status_text);
        orderDetailDateBillEditText = itemView.findViewById(R.id.order_detail_time_bill_text);
        orderDetailPriceText = itemView.findViewById(R.id.order_detail_price_bill_text);
        orderDetailStatusPayEditText = itemView.findViewById(R.id.order_detail_status_pay_text);
        orderDetailSendInvoiceButton = itemView.findViewById(R.id.order_detail_send_invoice_button);
        orderDetailImageCustomer = itemView.findViewById(R.id.order_detail_customer_image);
        orderDetailCustomerNameText = itemView.findViewById(R.id.order_detail_customer_name_text);
        statusBackgroundView = itemView.findViewById(R.id.order_detail_contain_status_view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void bindData(OrderDetailOrderInfoViewEntity orderDetailOrderInfoViewEntity) {
        orderDetailCodeBillText.setText(orderDetailOrderInfoViewEntity.codeBill);
        orderDetailDateBillEditText.setText(orderDetailOrderInfoViewEntity.dateBil);
        orderDetailPriceText.setText(orderDetailOrderInfoViewEntity.priceProduct);
        orderDetailCustomerNameText.setText(orderDetailOrderInfoViewEntity.nameCustomer);

        setupStatusView(orderDetailOrderInfoViewEntity.orderItem);
        setupPaymentStatusText(orderDetailOrderInfoViewEntity.statusPayment);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupStatusView(OrderItem orderItem) {
        statusBackgroundView.setCardBackgroundColor(context.getColor(getStatusBackgroundColorId(orderItem)));
        orderDetailStatusDeliveredText.setText(context.getString(orderItem.getStringId()));
        orderDetailStatusDeliveredText.setTextColor(context.getColor(getStatusTextColorId(orderItem)));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setupPaymentStatusText(boolean paid) {
        orderDetailStatusPayEditText.setText(getPaymentStatusStringId(paid));
        orderDetailStatusPayEditText.setTextColor(context.getColor(getPaymentStatusTextColorId(paid)));
    }

    private int getStatusTextColorId(OrderItem orderItem) {
        switch (orderItem) {
            case processing:
                return R.color.text_yellow;
            case delivered:
                return R.color.SeaGreen;
            default:
                return R.color.black;
        }
    }

    private int getStatusBackgroundColorId(OrderItem orderItem) {
        switch (orderItem) {
            case processing:
                return R.color.background_yellow;
            case delivered:
                return R.color.background_green;
            default:
                return R.color.black;
        }
    }

    private int getPaymentStatusTextColorId(boolean paid) {
        if (paid) {
            return R.color.Green;
        } else {
            return R.color.text_red;
        }
    }

    private int getPaymentStatusStringId(boolean paid) {
        if (paid) {
            return R.string.order_manager_paid;
        } else {
            return R.string.order_manager_unpaid;
        }
    }
}