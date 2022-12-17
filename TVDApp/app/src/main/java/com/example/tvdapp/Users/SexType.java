package com.example.tvdapp.Users;

import com.example.tvdapp.R;
import com.example.tvdapp.utilities.Constant;

public enum SexType {
    male, female;

    public int getId() {
        switch (this) {
            case male:
                return Constant.maleId;
            case female:
                return Constant.femaleId;
            default:
                return 0;
        }
    }

    public int getStringId() {
        switch (this) {
            case male:
                return R.string.user_male_title;
            case female:
                return R.string.user_female_title;
            default:
                return 0;
        }
    }
}
