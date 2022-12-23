package com.example.tvdapp.order;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.InputEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tvdapp.R;
import com.example.tvdapp.utilities.Constant;
import com.example.tvdapp.utilities.InputFilterMinMax;
import com.example.tvdapp.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder> {
    private Context context;
    private List<ProductOrderViewEntity> entityList = new ArrayList<>();
    private OrderProductEvent event;

    public void setEntityList(List<ProductOrderViewEntity> entityList) {
        this.entityList = entityList;

        notifyDataSetChanged();
    }

    public OrderProductAdapter(Context context, List<ProductOrderViewEntity> entityList, OrderProductEvent event) {
        this.context = context;
        this.entityList = entityList;
        this.event = event;
    }

    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderView = inflater.inflate(R.layout.item_order, parent, false);
        OrderProductViewHolder orderViewHolder = new OrderProductViewHolder(orderView, context);
        return orderViewHolder;
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {
        ProductOrderViewEntity entity = entityList.get(position);
        holder.bindData(entity);
    }

    class OrderProductViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView priceTextView;
        private EditText countEditText;
        private ImageView minusImageView;
        private ImageView plusImageView;
        private View productCountView;
        private ImageView productImageView;
        private TextView amountTextView;
        private ProductOrderViewEntity entity;
        private Context context;

        public OrderProductViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            nameTextView = itemView.findViewById(R.id.order_product_name_title);
            priceTextView = itemView.findViewById(R.id.order_product_price_title);
            productCountView = itemView.findViewById(R.id.order_product_count_view);
            countEditText = itemView.findViewById(R.id.order_product_count_edit_text);
            minusImageView = itemView.findViewById(R.id.order_product_minus_image);
            plusImageView = itemView.findViewById(R.id.order_product_plus_image);
            productImageView = itemView.findViewById(R.id.order_product_image);
            amountTextView = itemView.findViewById(R.id.order_product_amount_text);

            int widthItem = (Utilities.getScreenWidth() - 120) / 3;
            itemView.getLayoutParams().width = widthItem;
            itemView.getLayoutParams().height = (widthItem * 11) / 10;
            itemView.requestLayout();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProductCount(1);
                }
            });

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

            countEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int currentCount = 0;
                    String number = countEditText.getText().toString();
                    if (!number.isEmpty()) {
                        currentCount = Integer.parseInt(number);
                    }

                    if (event != null) {
                        event.productCurrentCountDidChange(getAdapterPosition(), currentCount);
                    }
                }
            });

            countEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        String currentCount = countEditText.getText().toString();
                        if (currentCount.isEmpty()) {
                            changeProductCount(0);
                        }
                    }
                }
            });
        }

        public void bindData(ProductOrderViewEntity entity) {
            this.entity = entity;
            nameTextView.setText(entity.name);
            priceTextView.setText(String.format("%,d", entity.price));
            countEditText.setText(Integer.toString(entity.count));
            Glide.with(context)
                    .load(entity.imageLink)
                    .centerCrop()
                    .into(productImageView);

            if (entity.count == 0) {
                productCountView.setVisibility(View.INVISIBLE);
            } else {
                productCountView.setVisibility(View.VISIBLE);
            }

            amountTextView.setText(getAmountString(entity.limit));

            countEditText.setFilters(new InputFilter[]{ new InputFilterMinMax(0, entity.limit)});
        }

        private void changeProductCount(int currentCount) {
            int newCount = currentCount;
            if (currentCount > entity.limit && entity.limit != Constant.noLimit) {
                newCount = entity.limit;
            }
            countEditText.setText(Integer.toString(newCount));

            if (productCountView.getVisibility() == View.INVISIBLE && newCount > 0) {
                productCountView.setVisibility(View.VISIBLE);
            } else if (productCountView.getVisibility() == View.VISIBLE && newCount == 0) {
                productCountView.setVisibility(View.INVISIBLE);
            }
        }

        private void addProductCount(int count) {
            int currentCount = -1;
            String number = countEditText.getText().toString();
            if (!number.isEmpty()) {
                currentCount = Integer.parseInt(number);
            }

            if (currentCount != -1) {
                int newCount = currentCount + count;
                changeProductCount(newCount);
            }
        }

        private String getAmountString(int amount) {
            if (amount == Constant.noLimit) {
                return "";
            } else {
                if (amount == 0) {
                    return context.getString(R.string.order_product_no_product_title);
                } else {
                    String description = context.getString(R.string.product_amount_description);
                    return String.format("%s: %d", description, amount);
                }
            }
        }
    }

    interface OrderProductEvent {
        void productCurrentCountDidChange(int position, int currentCount);
    }
}
