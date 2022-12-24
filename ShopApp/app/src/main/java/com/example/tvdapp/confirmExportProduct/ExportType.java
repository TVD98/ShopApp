package com.example.tvdapp.confirmExportProduct;

import com.example.tvdapp.R;
import com.example.tvdapp.utilities.Constant;

public enum ExportType {
    export, broken;

    public int getId() {
        switch (this) {
            case export:
                return Constant.exportItemValue;
            case broken:
                return Constant.brokenItemValue;
            default:
                return Constant.allValue;
        }
    }

    public int getStringId() {
        switch (this) {
            case export:
                return R.string.confirm_export_product_title;
            case broken:
                return R.string.confirm_export_product_broken;
            default:
                return R.string.all_title;
        }
    }
}
