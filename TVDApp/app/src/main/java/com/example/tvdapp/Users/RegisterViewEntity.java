package com.example.tvdapp.Users;

public class RegisterViewEntity {
    public String account_name;
    public String pass;
    public String pass_again;
    public String name_employee;
    public int gender;
    public int position_employee;
    public String area;
    public String cccd;
    public String phone;
    public String address;

    public RegisterViewEntity() {
    }

    public RegisterViewEntity(String account_name, String pass, String pass_again, String name_employee, int gender, int position_employee, String area, String cccd, String phone, String address) {
        this.account_name = account_name;
        this.pass = pass;
        this.pass_again = pass_again;
        this.name_employee = name_employee;
        this.gender = gender;
        this.position_employee = position_employee;
        this.area = area;
        this.cccd = cccd;
        this.phone = phone;
        this.address = address;
    }
}
