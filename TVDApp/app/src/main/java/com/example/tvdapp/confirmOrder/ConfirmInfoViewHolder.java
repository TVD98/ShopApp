package com.example.tvdapp.confirmOrder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmOrder.model.ConfirmOrderInfoViewEntity;

public class ConfirmInfoViewHolder extends ConfirmOrderViewHolder implements ConfirmOrderAdapter.ConfirmOrderAdapterEvent, TextWatcher {
    private TextView priceTextView;
    private TextView promotionTextView;
    private TextView discountTextView;
    private TextView transitionFeeTextView;
    private TextView totalTextView;
    private TextView paymentMethodsTextView;
    private EditText noteEditText;
    private EditText nameEditText;
    private EditText addressEditText;

    private ConfirmInfoViewHolderEvent event;

    public ConfirmInfoViewHolder(@NonNull View itemView, ConfirmInfoViewHolderEvent event) {
        super(itemView);
        this.event = event;

        priceTextView = itemView.findViewById(R.id.confirm_info_price_text);
        promotionTextView = itemView.findViewById(R.id.confirm_order_choose_promotion_text);
        discountTextView = itemView.findViewById(R.id.confirm_order_discount_text);
        transitionFeeTextView = itemView.findViewById(R.id.confirm_order_transition_fee_text);
        totalTextView = itemView.findViewById(R.id.confirm_order_total_text);
        paymentMethodsTextView = itemView.findViewById(R.id.confirm_order_payment_methods_text);
        noteEditText = itemView.findViewById(R.id.confirm_order_note_edit_text);
        nameEditText = itemView.findViewById(R.id.confirm_order_customer_name_edit_text);
        addressEditText = itemView.findViewById(R.id.confirm_order_address_edit_text);

        noteEditText.addTextChangedListener(this);
        nameEditText.addTextChangedListener(this);
        addressEditText.addTextChangedListener(this);
    }

    public void bindData(ConfirmOrderInfoViewEntity confirmOrderInfoViewEntity) {
        priceTextView.setText(confirmOrderInfoViewEntity.price);
        promotionTextView.setText(confirmOrderInfoViewEntity.promotion);
        discountTextView.setText(confirmOrderInfoViewEntity.discount);
        transitionFeeTextView.setText(confirmOrderInfoViewEntity.transportFee);
        totalTextView.setText(confirmOrderInfoViewEntity.total);
        paymentMethodsTextView.setText(confirmOrderInfoViewEntity.paymentMethods);
        noteEditText.setText(confirmOrderInfoViewEntity.note);
        nameEditText.setText(confirmOrderInfoViewEntity.name);
        addressEditText.setText(confirmOrderInfoViewEntity.address);
    }

    @Override
    public void totalPriceDidChange(String price, String total) {
        priceTextView.setText(price);
        totalTextView.setText(total);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (event == null) {
            return;
        }

        String text = editable.toString();

        if (editable.equals(noteEditText.getEditableText())) {
            event.noteDidChange(text);
        } else if (editable.equals(nameEditText.getEditableText())) {
            event.nameDidChange(text);
        } else {
            event.addressDidChange(text);
        }
    }

    interface ConfirmInfoViewHolderEvent {
        void nameDidChange(String name);
        void noteDidChange(String note);
        void addressDidChange(String address);
    }
}
