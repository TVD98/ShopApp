package com.example.tvdapp.book;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.book.model.BookViewEntity;
import com.example.tvdapp.home.order.OrderItem;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<BookViewEntity> bookViewEntityList;
    private Context context;
    private BookViewHolderEvent event;

    public void setBookViewEntityList(List<BookViewEntity> bookViewEntityList) {
        this.bookViewEntityList = bookViewEntityList;
    }

    public void setEvent(BookViewHolderEvent event) {
        this.event = event;
    }

    public BookAdapter(List<BookViewEntity> bookViewEntityList, Context context) {
        this.bookViewEntityList = bookViewEntityList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View bookView = inflater.inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(bookView, context, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookViewEntity entity = bookViewEntityList.get(position);
        holder.bindData(entity);
    }

    @Override
    public int getItemCount() {
        return bookViewEntityList.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView supplierNameTextView;
        private TextView totalTextView;
        private TextView paymentStatusTextView;
        private TextView timeTextView;
        private View bottomView;
        private Button paymentButton;
        private Context context;
        private BookViewEntity entity;
        private BookViewHolderEvent event;

        public BookViewHolder(@NonNull View itemView, Context context, BookViewHolderEvent event) {
            super(itemView);
            this.context = context;
            this.event = event;

            supplierNameTextView = itemView.findViewById(R.id.book_supplier_name_text);
            totalTextView = itemView.findViewById(R.id.book_total_text);
            paymentStatusTextView = itemView.findViewById(R.id.book_payment_status_text);
            timeTextView = itemView.findViewById(R.id.book_time_text);
            bottomView = itemView.findViewById(R.id.book_bottom_view);
            paymentButton = itemView.findViewById(R.id.book_payment_button);

            paymentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (event != null) {
                        event.payment(entity.id);
                    }
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void bindData(BookViewEntity entity) {
            this.entity = entity;

            supplierNameTextView.setText(entity.supplierName);
            totalTextView.setText(entity.total);
            timeTextView.setText(entity.time);
            setupPaymentStatusText(entity.paid);
            setupBottomView(entity.paid);
        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        private void setupPaymentStatusText(boolean paid) {
            paymentStatusTextView.setText(getPaymentStatusStringId(paid));
            paymentStatusTextView.setTextColor(context.getColor(getPaymentStatusTextColorId(paid)));
        }

        private int getPaymentStatusStringId(boolean paid) {
            if (paid) {
                return R.string.order_manager_paid;
            } else {
                return R.string.order_manager_unpaid;
            }
        }

        private int getPaymentStatusTextColorId(boolean paid) {
            if (paid) {
                return R.color.Green;
            } else {
                return R.color.text_red;
            }
        }

        private void setupBottomView(boolean paid) {
            if (paid) {
                bottomView.setVisibility(View.GONE);
            } else {
                bottomView.setVisibility(View.VISIBLE);
            }
        }
    }

    interface BookViewHolderEvent {
        void payment(String id);
    }
}
