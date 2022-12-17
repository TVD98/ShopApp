package com.example.tvdapp.users;

public class UserResponse {
    public String username;
    public String password;
    public int userId;

    public UserResponse() {
    }

    public UserResponse(String username, String password, int userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
    }
}
