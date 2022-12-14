package com.example.tvdapp.orderMangager;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.home.order.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderManagerFragmentAdapter extends RecyclerView.Adapter<OrderManagerFragmentAdapter.OrderManagerFragmentViewHolder> {
    private Context context;
    private List<OrderManagerViewEntity> orderManagerViewEntities;
    private OrderManagerFragmentViewHolderEvent orderManagerFragmentViewHolderEvent;

    public OrderManagerFragmentAdapter(Context context, List<OrderManagerViewEntity> orderManagerViewEntities, OrderManagerFragmentViewHolderEvent orderManagerFragmentViewHolderEvent) {
        this.context = context;
        this.orderManagerViewEntities = orderManagerViewEntities;
        this.orderManagerFragmentViewHolderEvent = orderManagerFragmentViewHolderEvent;
    }

    public void setOrderManagerViewEntities(List<OrderManagerViewEntity> orderManagerViewEntities) {
        this.orderManagerViewEntities = orderManagerViewEntities;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderManagerFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View orderManagerView = inflater.inflate(R.layout.item_order_manager, parent, false);
        OrderManagerFragmentViewHolder orderManagerViewHolder = new OrderManagerFragmentViewHolder(orderManagerView, context, orderManagerFragmentViewHolderEvent);
        return orderManagerViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull OrderManagerFragmentViewHolder holder, int position) {
        OrderManagerViewEntity entity = orderManagerViewEntities.get(position);
        holder.bindData(entity);
    }

    @Override
    public int getItemCount() {
        return orderManagerViewEntities.size();
    }

    class OrderManagerFragmentViewHolder extends RecyclerView.ViewHolder {
        private TextView customerNameTextView;
        private TextView statusTextView;
        private CardView statusBackgroundView;
        private TextView timeTextView;
        private TextView totalTextView;
        private TextView paymentStatusTextView;
        private View bottomView;
        private Button deliveredButton;
        private Button cancelButton;
        private Context context;
        private OrderManagerViewEntity entity;
        private OrderManagerFragmentAdapter.OrderManagerFragmentViewHolderEvent orderManagerFragmentViewHolderEvent;

        public OrderManagerFragmentViewHolder(@NonNull View itemView, Context context, OrderManagerFragmentAdapter.OrderManagerFragmentViewHolderEvent orderManagerFragmentViewHolderEvent) {
            super(itemView);
            this.context = context;
            this.orderManagerFragmentViewHolderEvent = orderManagerFragmentViewHolderEvent;

            customerNameTextView = itemView.findViewById(R.id.order_manager_customer_name_text);
            statusTextView = itemView.findViewById(R.id.order_manager_status_text);
            statusBackgroundView = itemView.findViewById(R.id.order_manager_contain_status_view);
            timeTextView = itemView.findViewById(R.id.order_manager_time_text);
            totalTextView = itemView.findViewById(R.id.order_manager_total_text);
            paymentStatusTextView = itemView.findViewById(R.id.order_manager_payment_status_text);
            bottomView = itemView.findViewById(R.id.order_manager_bottom_view);
            deliveredButton = itemView.findViewById(R.id.order_manager_delivered_button);
            cancelButton = itemView.findViewById(R.id.order_manager_cancel_button);

            deliveredButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderManagerFragmentViewHolderEvent != null) {
                        orderManagerFragmentViewHolderEvent.confirmDelivered(entity.id);
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderManagerFragmentViewHolderEvent != null) {
                        orderManagerFragmentViewHolderEvent.confirmCancel(entity.id);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (orderManagerFragmentViewHolderEvent != null) {
                        orderManagerFragmentViewHolderEvent.selectOrder(entity.id);
                    }
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void bindData(OrderManagerViewEntity entity) {
            this.entity = entity;
            customerNameTextView.setText(entity.customerName);
            setupStatusView(entity.orderItem);
            setupPaymentStatusText(entity.paid);
            setupBottomView(entity.orderItem);
            totalTextView.setText(entity.total);
            timeTextView.setText(String.format("%s - %s", entity.time, entity.id));
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void setupStatusView(OrderItem orderItem) {
            statusBackgroundView.setCardBackgroundColor(context.getColor(getStatusBackgroundColorId(orderItem)));
            statusTextView.setText(context.getString(orderItem.getStringId()));
            statusTextView.setTextColor(context.getColor(getStatusTextColorId(orderItem)));
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void setupPaymentStatusText(boolean paid) {
            paymentStatusTextView.setText(getPaymentStatusStringId(paid));
            paymentStatusTextView.setTextColor(context.getColor(getPaymentStatusTextColorId(paid)));
        }

        private void setupBottomView(OrderItem orderItem) {
            switch (orderItem) {
                case processing:
                    bottomView.setVisibility(View.VISIBLE);
                    break;
                default:
                    bottomView.setVisibility(View.GONE);
                    break;
            }
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

    interface OrderManagerFragmentViewHolderEvent {
        void confirmDelivered(String orderId);
        void confirmCancel(String orderId);
        void selectOrder(String orderId);
    }
}
