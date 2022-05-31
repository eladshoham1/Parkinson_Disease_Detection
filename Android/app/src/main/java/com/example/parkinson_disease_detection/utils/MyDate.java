package com.example.parkinson_disease_detection.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class MyDate {
    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static String makeDateString(long date) {
        DateFormat timeFormat = new SimpleDateFormat(DATE_FORMAT);
        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        return timeFormat.format(date);
    }
}
