package com.example.tvdapp.home.turnover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.utilities.Constant;

import java.util.List;

public class TurnoverAdapter extends RecyclerView.Adapter<TurnoverAdapter.TurnoverViewHolder> {
    private List<TurnoverViewEntity> turnoverItems;
    private Context context;
    private TurnoverViewHolderEvent event;

    public TurnoverAdapter(List<TurnoverViewEntity> turnoverItems, Context context, TurnoverViewHolderEvent event) {
        this.turnoverItems = turnoverItems;
        this.context = context;
        this.event = event;
    }

    @NonNull
    @Override
    public TurnoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View turnoverView = inflater.inflate(R.layout.item_turnover, parent, false);
        TurnoverViewHolder turnoverViewHolder = new TurnoverViewHolder(turnoverView);
        return turnoverViewHolder;
    }

    @Override
    public int getItemCount() {
        return turnoverItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoverViewHolder holder, int position) {
        TurnoverViewEntity item = turnoverItems.get(position);
        holder.bindData(item);
    }

    class TurnoverViewHolder extends RecyclerView.ViewHolder {
        private View backgroundView;
        private TextView titleTextView;
        private TextView valueTextView;
        private ImageView iconImageView;

        public TurnoverViewHolder(@NonNull View itemView) {
            super(itemView);

            backgroundView = itemView.findViewById(R.id.item_turnover_background);
            titleTextView = itemView.findViewById(R.id.item_turnover_title);
            valueTextView = itemView.findViewById(R.id.item_turnover_value);
            iconImageView = itemView.findViewById(R.id.item_turnover_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (event != null) {
                        event.selectTurnoverItem(getAdapterPosition());
                    }
                }
            });
        }

        public void bindData(TurnoverViewEntity viewEntity) {
            backgroundView.setBackgroundResource(viewEntity.colorId);
            titleTextView.setText(viewEntity.title);
            valueTextView.setText(viewEntity.value);
            iconImageView.setImageResource(viewEntity.imageId);
        }
    }

    interface TurnoverViewHolderEvent {
        void selectTurnoverItem(int position);
    }
}
