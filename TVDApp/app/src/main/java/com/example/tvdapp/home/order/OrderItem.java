package com.example.tvdapp.home.order;

import com.example.tvdapp.R;
import com.example.tvdapp.utilities.Constant;

public enum OrderItem {
    all, waiting, processing;

    public int getId() {
        switch (this) {
            case waiting:
                return Constant.waitingItemValue;
            case processing:
                return Constant.processingItemValue;
            default:
                return Constant.allValue;
        }
    }

    public int getStringId() {
        switch (this) {
            case waiting:
                return R.string.waiting_title;
            case processing:
                return R.string.processing_title;
            default:
                return 0;
        }
    }
}
