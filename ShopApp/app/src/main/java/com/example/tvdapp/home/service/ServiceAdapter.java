package com.example.tvdapp.home.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.utilities.Utilities;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<ServiceViewEntity> serviceViewEntityList;
    private Context context;
    private ServiceViewHolderEvent serviceViewHolderEvent;

    public ServiceAdapter(List<ServiceViewEntity> serviceViewEntityList, Context context, ServiceViewHolderEvent event) {
        this.serviceViewEntityList = serviceViewEntityList;
        this.context = context;
        this.serviceViewHolderEvent = event;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View turnoverView = inflater.inflate(R.layout.item_service, parent, false);
        ServiceViewHolder serviceViewHolder = new ServiceViewHolder(turnoverView);
        serviceViewHolder.setEvent(serviceViewHolderEvent);
        return serviceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceViewEntity serviceData = serviceViewEntityList.get(position);
        holder.bindData(serviceData);
    }

    @Override
    public int getItemCount() {
        return serviceViewEntityList.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private ImageView iconImageView;
        private ServiceViewHolderEvent event;

        public void setEvent(ServiceViewHolderEvent event) {
            this.event = event;
        }

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.item_service_title);
            iconImageView = itemView.findViewById(R.id.item_service_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (event != null) {
                        event.selectServiceItem(getAdapterPosition());
                    }
                }
            });

            int widthItem = (Utilities.getScreenWidth() - 160) / 4;

            itemView.getLayoutParams().width = widthItem;
            itemView.getLayoutParams().height = widthItem;
            itemView.requestLayout();
        }

        public void bindData(ServiceViewEntity entity) {
            titleTextView.setText(entity.title);
            iconImageView.setImageResource(entity.imageId);
        }
    }

    interface ServiceViewHolderEvent {
        void selectServiceItem(int position);
    }
}
