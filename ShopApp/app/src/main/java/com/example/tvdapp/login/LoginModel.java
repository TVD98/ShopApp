package com.example.tvdapp.login;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.tvdapp.users.UserResponse;
import com.example.tvdapp.utilities.Constant;
import com.example.tvdapp.utilities.SaveSystem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginModel {
    private DatabaseReference mDatabase;
    private LoginModelEvent event;
    private Context context;
    public LogInViewEntity logInViewEntity = new LogInViewEntity(
            "",
            ""
    );

    public void setContext(Context context) {
        this.context = context;
    }

    public void setEvent(LoginModelEvent event) {
        this.event = event;
    }

    public LoginModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public boolean isValid() {
        return !(logInViewEntity.login_account.isEmpty() || logInViewEntity.login_pass.isEmpty());
    }

    public void login() {
        mDatabase.child(String.format("users/%s", logInViewEntity.login_account)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    if (event != null) {
                        event.loginFail();
                    }
                } else {
                    UserResponse response = task.getResult().getValue(UserResponse.class);
                    if (response == null) {
                        if (event != null) {
                            event.loginFail();
                        }
                    } else {
                        if (response.password.compareTo(logInViewEntity.login_pass) == 0) {
                            if (event != null) {
                                SaveSystem.saveUsername(response.username, context);
                                SaveSystem.saveUserType(getUserType(response.userId), context);
                                event.loginSuccess(getUserType(response.userId));
                            }
                        } else {
                            if (event != null) {
                                event.loginFail();
                            }
                        }
                    }
                }
            }
        });
    }

    private String getUserType(int userId) {
        if (userId == Constant.employeeId) {
            return "employee";
        } else {
            return "manager";
        }
    }

    interface LoginModelEvent {
        void loginSuccess(String userType);

        void loginFail();
    }
}
