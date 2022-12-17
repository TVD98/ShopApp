package com.example.tvdapp.confirmImportProduct;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tvdapp.R;
import com.example.tvdapp.confirmOrder.ConfirmOrderViewHolder;

public class ConfirmImportProductInfoViewHolder extends ConfirmOrderViewHolder implements TextWatcher, ConfirmImportProductAdapter.ConfirmImportProductAdapterEvent {
    private TextView priceTextView;
    private TextView discountTextView;
    private TextView amountTextView;
    private TextView totalTextView;
    private TextView costsIncurredTextView;
    private EditText noteEditText;
    private EditText nameEditText;
    private View addItemsView;
    private ConfirmImportProductViewHolderEvent event;

    public ConfirmImportProductInfoViewHolder(@NonNull View itemView, ConfirmImportProductViewHolderEvent event) {
        super(itemView);
        this.event = event;

        priceTextView = itemView.findViewById(R.id.confirm_import_product_total_money_text);
        amountTextView = itemView.findViewById(R.id.confirm_import_product_total_amount_text);
        discountTextView = itemView.findViewById(R.id.confirm_import_product_discount_text);
        totalTextView = itemView.findViewById(R.id.confirm_import_total_money_text);
        costsIncurredTextView = itemView.findViewById(R.id.confirm_import_product_costs_incurred_text);
        noteEditText = itemView.findViewById(R.id.confirm_import_product_note_edit_text);
        nameEditText = itemView.findViewById(R.id.confirm_import_product_supplier_edit_text);
        addItemsView = itemView.findViewById(R.id.confirm_import_product_add_items_view);

        noteEditText.addTextChangedListener(this);
        nameEditText.addTextChangedListener(this);

        addItemsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event != null) {
                    event.addItems();
                }
            }
        });
    }

    public void bindData(ConfirmImportProductInfoViewEntity viewEntity) {
        priceTextView.setText(viewEntity.price);
        amountTextView.setText(viewEntity.amount);
        discountTextView.setText(viewEntity.discount);
        totalTextView.setText(viewEntity.total);
        costsIncurredTextView.setText(viewEntity.costsIncurred);

        nameEditText.removeTextChangedListener(this);
        noteEditText.removeTextChangedListener(this);
        nameEditText.setText(viewEntity.supplierName);
        noteEditText.setText(viewEntity.note);
        noteEditText.addTextChangedListener(this);
        nameEditText.addTextChangedListener(this);
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
        } else {
            event.nameDidChange(text);
        }
    }

    @Override
    public void totalPriceDidChange(String price, String total, String amount) {
        priceTextView.setText(price);
        amountTextView.setText(amount);
        totalTextView.setText(total);
    }

    interface ConfirmImportProductViewHolderEvent {
        void nameDidChange(String name);

        void noteDidChange(String note);

        void addItems();
    }
}
