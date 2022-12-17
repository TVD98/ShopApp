package com.example.tvdapp.employeeManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;

import java.util.List;

public class EmployeeManagerAdapter extends RecyclerView.Adapter<EmployeeManagerAdapter.EmployeeManagerViewHolder> {
    private List<EmployeeManagerViewEntity> employeeManagerViewEntityList;
    private EmployeeManagerViewHolderEvent event;
    private Context context;

    public void setEvent(EmployeeManagerViewHolderEvent event) {
        this.event = event;
    }

    public void setEmployeeManagerViewEntityList(List<EmployeeManagerViewEntity> employeeManagerViewEntityList) {
        this.employeeManagerViewEntityList = employeeManagerViewEntityList;

        notifyDataSetChanged();
    }

    public EmployeeManagerAdapter(List<EmployeeManagerViewEntity> employeeManagerViewEntityList, Context context) {
        this.employeeManagerViewEntityList = employeeManagerViewEntityList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View employeeManagerView = inflater.inflate(R.layout.item_employee_manager, parent, false);
        EmployeeManagerViewHolder employeeManagerViewHolder = new EmployeeManagerViewHolder(employeeManagerView, event);
        return employeeManagerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeManagerViewHolder holder, int position) {
        EmployeeManagerViewEntity entity = employeeManagerViewEntityList.get(position);
        holder.bindData(entity);
    }

    @Override
    public int getItemCount() {
        return employeeManagerViewEntityList.size();
    }

    class EmployeeManagerViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView usernameTextView;
        private TextView positionTextView;
        private TextView sexTextView;
        private EmployeeManagerViewEntity entity;
        private EmployeeManagerViewHolderEvent viewHolderEvent;

        public EmployeeManagerViewHolder(@NonNull View itemView, EmployeeManagerViewHolderEvent event) {
            super(itemView);
            this.viewHolderEvent = event;

            nameTextView = itemView.findViewById(R.id.employee_manager_name_text);
            usernameTextView = itemView.findViewById(R.id.employee_manager_username_text);
            positionTextView = itemView.findViewById(R.id.employee_manager_position_text);
            sexTextView = itemView.findViewById(R.id.employee_manager_sex_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewHolderEvent != null) {
                        viewHolderEvent.selectItem(entity.username);
                    }
                }
            });
        }

        public void bindData(EmployeeManagerViewEntity entity) {
            this.entity = entity;

            nameTextView.setText(entity.name);
            usernameTextView.setText(entity.username);
            positionTextView.setText(entity.position);
            sexTextView.setText(entity.sex);
        }
    }

    interface EmployeeManagerViewHolderEvent {
        void selectItem(String username);
    }
}
