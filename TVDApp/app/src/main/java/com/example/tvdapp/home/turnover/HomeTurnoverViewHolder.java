package com.example.tvdapp.home.turnover;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvdapp.R;
import com.example.tvdapp.home.HomeViewHolder;
import com.example.tvdapp.home.turnover.model.TurnoverDataResponse;
import com.example.tvdapp.home.turnover.model.TurnoverDataResponseList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeTurnoverViewHolder extends HomeViewHolder {
    private Context context;
    private RecyclerView recyclerView;
    private TextView reportTitleTextView;
    private TurnoverAdapter adapter;
    private List<TurnoverViewEntity> itemList = new ArrayList<>();
    private List<TurnoverItem> turnoverItems = new ArrayList<>();
    private HomeTurnoverEvent event;

    public HomeTurnoverViewHolder(@NonNull View itemView, Context context, HomeTurnoverEvent event) {
        super(itemView);
        this.context = context;
        this.event = event;

        recyclerView = itemView.findViewById(R.id.turnover_recycler_view);
        reportTitleTextView = itemView.findViewById(R.id.home_turnover_report_title);

        setupEvent();
    }

    private void setupEvent() {
        reportTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (event != null) {
                    event.selectTurnoverItem(TurnoverItem.all);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initModel(TurnoverDataResponseList response) {
        TurnoverItem[] turnoverTypes = TurnoverItem.values();
        for (TurnoverDataResponse data : response.data) {
            TurnoverItem type = Arrays.stream(turnoverTypes)
                    .filter(turnoverType -> turnoverType.getId() == data.turnoverId)
                    .findAny()
                    .orElse(null);
            if (type != null) {
                String title = context.getString(type.getStringId());
                TurnoverViewEntity entity = new TurnoverViewEntity(type.getColorId(), title
                        , Integer.toString(data.value), type.getImageId());
                itemList.add(entity);
                turnoverItems.add(type);
            }
        }

        initUI();
    }

    private  void initUI() {
        adapter = new TurnoverAdapter(itemList, context, new TurnoverAdapter.TurnoverViewHolderEvent() {
            @Override
            public void selectTurnoverItem(int position) {
                if (event != null) {
                    event.selectTurnoverItem(turnoverItems.get(position));
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }
}
