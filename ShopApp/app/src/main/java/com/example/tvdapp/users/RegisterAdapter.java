package com.example.tvdapp.users;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tvdapp.R;

public class RegisterAdapter extends BaseAdapter {

    private Context context;//giao diện chứa gridView
    private SelectionItem[] items;


    public RegisterAdapter(Context context, SelectionItem[]items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount () {
        return items.length;
    }

    @Override
    public Object getItem ( int i){
        return items[i];
    }

    @Override
    public long getItemId ( int i){
        return items[i].id;
    }

    @Override
    public View getView ( int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View baseView = inflater.inflate(R.layout.item_layout_spinner, null);
        TextView tvName = baseView.findViewById(R.id.text_view_item);
        tvName.setText(items[i].title);

        return baseView;
    }
}