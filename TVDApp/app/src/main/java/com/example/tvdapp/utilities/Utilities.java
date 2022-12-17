package com.example.tvdapp.utilities;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utilities {
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTodayString(String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertDateString(String date, String fromFormat, String toFormat) {
        DateTimeFormatter fromFormatter = DateTimeFormatter.ofPattern(fromFormat);
        LocalDateTime fromDate = LocalDateTime.parse(date, fromFormatter);
        DateTimeFormatter toFormatter = DateTimeFormatter.ofPattern(toFormat);
        return toFormatter.format(fromDate);
    }
}
