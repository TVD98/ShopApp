package com.example.tvdapp.confirmOrder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tvdapp.R;

public class AddItemsViewHolder extends ConfirmOrderViewHolder {
    private TextView addItemsTextView;
    private AddItemsViewHolderEvent event;

    public AddItemsViewHolder(@NonNull View itemView, AddItemsViewHolderEvent event) {
        super(itemView);
        this.event = event;

        addItemsTextView = itemView.findViewById(R.id.confirm_order_add_items_text);

        addItemsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event != null) {
                    event.selectAddItems();
                }
            }
        });
    }

    interface AddItemsViewHolderEvent {
        void selectAddItems();
    }
}
