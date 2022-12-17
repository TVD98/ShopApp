package com.example.tvdapp.Users;

import com.example.tvdapp.R;
import com.example.tvdapp.utilities.Constant;

public enum UserType {
    manager, employee;

    public int getId() {
        switch (this) {
            case manager:
                return Constant.managerId;
            case employee:
                return Constant.employeeId;
            default:
                return 0;
        }
    }

    public int getStringId() {
        switch (this) {
            case manager:
                return R.string.user_manager_title;
            case employee:
                return R.string.user_employee_title;
            default:
                return 0;
        }
    }
}
