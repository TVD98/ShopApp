package com.example.tvdapp.employeeManager;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.tvdapp.users.SexType;
import com.example.tvdapp.users.UserInfoResponse;
import com.example.tvdapp.users.UserType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeManagerModel {
    private EmployeeManagerModelEvent event;
    private DatabaseReference mDatabase;
    private List<UserInfoResponse> userInfoResponses = new ArrayList<>();
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setEvent(EmployeeManagerModelEvent event) {
        this.event = event;
    }

    public EmployeeManagerModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void fetchData() {
        ValueEventListener postListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfoResponses.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserInfoResponse response = snapshot.getValue(UserInfoResponse.class);
                    userInfoResponses.add(response);
                }

                if (event != null) {
                    event.fetchDataSuccess(convertResponse());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("users_info").addValueEventListener(postListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<EmployeeManagerViewEntity> convertResponse() {
        return userInfoResponses.stream()
                .map(user -> new EmployeeManagerViewEntity(
                        user.name_employee,
                        user.account_name,
                        getPositionString(user.position_employee),
                        getSexString(user.gender)
                )).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getPositionString(int userId) {
        UserType type = Arrays.stream(UserType.values())
                .filter(user -> user.getId() == userId)
                .findAny()
                .orElse(null);
        return context.getString(type.getStringId());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getSexString(int sexId) {
        SexType type = Arrays.stream(SexType.values())
                .filter(sex -> sex.getId() == sexId)
                .findAny()
                .orElse(null);
        return context.getString(type.getStringId());
    }

    interface EmployeeManagerModelEvent {
        void fetchDataSuccess(List<EmployeeManagerViewEntity> employeeManagerViewEntities);
    }
}
