package com.example.tvdapp.home.service;

import com.example.tvdapp.R;
import com.example.tvdapp.utilities.Constant;

public enum ServiceItem {
    createOrder, order, product, report, more;

    public int getId() {
        switch (this) {
            case createOrder:
                return Constant.createOrderItemValue;
            case order:
                return Constant.orderServiceItemValue;
            case product:
                return Constant.productItemValue;
            case report:
                return Constant.reportItemValue;
            case more:
                return Constant.moreItemValue;
            default:
                return 0;
        }
    }

    public int getStringId() {
        switch (this) {
            case createOrder:
                return R.string.create_order_title;
            case order:
                return R.string.order_service_title;
            case product:
                return R.string.product_title;
            case report:
                return R.string.report_title;
            case more:
                return R.string.more_title;
            default:
                return 0;
        }
    }

    public int getImageId() {
        switch (this) {
            case createOrder:
                return R.drawable.add_clipboard;
            case order:
                return R.drawable.clipboard;
            case product:
                return R.drawable.box;
            case report:
                return R.drawable.grow_up;
            case more:
                return R.drawable.application;
            default:
                return 0;
        }
    }
}
