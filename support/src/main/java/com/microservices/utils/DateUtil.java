package com.microservices.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String dealDateFormat(Date  date) {
        if (date == null){
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }
}
