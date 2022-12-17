package com.example.tvdapp.users;

public class UserInfoResponse {
    public String account_name;
    public String name_employee;
    public int gender;
    public int position_employee;
    public String area;
    public String cccd;
    public String phone;
    public String address;

    public UserInfoResponse() {
    }

    public UserInfoResponse(String account_name, String name_employee, int gender, int position_employee, String area, String cccd, String phone, String address) {
        this.account_name = account_name;
        this.name_employee = name_employee;
        this.gender = gender;
        this.position_employee = position_employee;
        this.area = area;
        this.cccd = cccd;
        this.phone = phone;
        this.address = address;
    }
}
