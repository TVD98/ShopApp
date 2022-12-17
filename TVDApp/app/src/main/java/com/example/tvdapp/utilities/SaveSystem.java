package com.example.tvdapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class SaveSystem {
    public static final String SHARE_PREFERENCES_NAME = "TVD";
    public static final String USERNAME = "username";
    public static final String USER_TYPE = "user_type";

    public static void saveData(Context context, String keyDataName, Object data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        saveString(context, keyDataName, json);
    }

    public static void saveString(Context context, String keyDataName, String data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREFERENCES_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyDataName, data);
        editor.apply();
    }

    public static <T> T getData(Context context, String keyDataName, Type type) {
        String data = getString(context, keyDataName);
        if (data == null)
            return null;
        else {
            try {
                Gson gson = new Gson();
                return gson.fromJson(data, type);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static String getString(Context context, String keyDataName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(keyDataName, null);
    }

    public static void saveUsername(String username, Context context) {
        saveString(context, USERNAME, username);
    }

    public static String getUsername(Context context) {
        String username = getString(context, USERNAME);
        if (username != null) {
            return username;
        } else {
            return "";
        }
    }

    public static void saveUserType(String userType, Context context) {
        saveString(context, USER_TYPE, userType);
    }

    public static String getUserType(Context context) {
        String userType = getString(context, USER_TYPE);
        if (userType != null) {
            return userType;
        } else {
            return "";
        }
    }
}
