package com.example.tvdapp.Users;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterModel {
    private DatabaseReference mDatabase;
    private RegisterModelEvent event;
    public RegisterViewEntity registerViewEntity = new RegisterViewEntity(
            "",
            "",
            "",
            "",
            SexType.male.getId(),
            UserType.employee.getId(),
            "",
            "",
            "",
            ""
    );

    public void setEvent(RegisterModelEvent event) {
        this.event = event;
    }

    public RegisterModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public boolean isValid() {
        if (registerViewEntity.account_name.isEmpty()) {
            return false;
        }

        if (registerViewEntity.name_employee.isEmpty()) {
            return false;

        }

        if (registerViewEntity.pass.compareTo(registerViewEntity.pass_again) != 0) {
            return false;
        }

        return true;
    }

    public boolean isValidPassword() {
        return registerViewEntity.pass.compareTo(registerViewEntity.pass_again) == 0;
    }

    public void signUp() {
        mDatabase.child(String.format("users/%s", registerViewEntity.account_name)).setValue(registerViewEntity).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (event != null) {
                    event.signUpSuccess();
                }
            }
        });
    }

    interface RegisterModelEvent {
        void signUpSuccess();
    }
}
