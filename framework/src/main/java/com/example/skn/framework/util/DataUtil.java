package com.example.skn.framework.util;

import android.content.Context;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hf
 */
public class DataUtil {

//    public static String getCurrentTime() {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int hour = calendar.get(Calendar.HOUR);
//        int minute = calendar.get(Calendar.MINUTE);
//        int second = calendar.get(Calendar.SECOND);
//        return year + "-" + (month + 1) + "-" + day + " " + hour + ":" + minute + ":" + second;
//    }


    public static String getTime(long time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        return df.format(time);
    }
//    public static String getCurrentTime(String type) {
//        DateFormat df = new SimpleDateFormat(type,Locale.CHINESE);
//        return df.format(new Date());
//    }
}
