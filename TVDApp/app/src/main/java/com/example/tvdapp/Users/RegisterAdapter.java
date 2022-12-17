package com.example.tvdapp.Users;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tvdapp.R;

public class RegisterAdapter extends BaseAdapter {

    private Activity activity;//giao diện chứa gridView
    private String[] items;


    public RegisterAdapter(Activity activity, String[]items){
        this.activity = activity;
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
        return 0;
    }

    public void setItems (String[]items){
        this.items = items;
    }

    @Override
    public View getView ( int i, View view, ViewGroup viewGroup){
         // Gọi layoutInflater ra để bắt đầu ánh xạ view và data.
        LayoutInflater inflater = activity.getLayoutInflater();

        // Đổ dữ liệu vào biến View, view này chính là những gì nằm trong item_name.xml
        view = inflater.inflate(R.layout.item_layout_spinner, null);

        // Đặt chữ cho từng view trong danh sách.
        TextView tvName = (TextView) view.findViewById(R.id.text_view_item);
        tvName.setText(items[i]);

        // Trả về view kết quả.
        return view;
    }
}