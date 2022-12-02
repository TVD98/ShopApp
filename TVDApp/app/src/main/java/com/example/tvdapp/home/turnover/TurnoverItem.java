package com.example.tvdapp.home.turnover;


import com.example.tvdapp.R;
import com.example.tvdapp.utilities.Constant;

public enum TurnoverItem {
    all, turnover, order, profit;

    public int getId() {
        switch (this) {
            case turnover:
                return Constant.turnoverItemValue;
            case order:
                return Constant.orderItemValue;
            case profit:
                return Constant.profitItemValue;
            default:
                return Constant.allValue;
        }
    }

    public int getColorId() {
        switch (this) {
            case turnover:
                return R.color.NavajoWhite;
            case order:
                return R.color.LightBlue;
            case profit:
                return R.color.PaleGreen;
            default:
                return R.color.black;
        }
    }

    public int getStringId() {
        switch (this) {
            case turnover:
                return R.string.turnover_title;
            case order:
                return R.string.order_title;
            case profit:
                return R.string.profit_title;
            default:
                return 0;
        }
    }

    public int getImageId() {
        switch (this) {
            case turnover:
                return R.drawable.grow_up;
            case order:
                return R.drawable.clipboard;
            case profit:
                return R.drawable.money;
            default:
                return R.drawable.icon_arrow_right;
        }
    }
}
