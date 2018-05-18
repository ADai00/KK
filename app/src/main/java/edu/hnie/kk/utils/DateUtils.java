package edu.hnie.kk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:dd");
        return simpleDateFormat.format(date);
    }

    public static Date stringToDate(String birthday) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = simpleDateFormat.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    public static int getAge(String birthday) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String now = simpleDateFormat.format(new Date());
        birthday = birthday.substring(0,4);
        return Integer.parseInt(now) - Integer.parseInt(birthday);
    }

    public static int getAge(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String now = simpleDateFormat.format(new Date());
        String birthday = simpleDateFormat.format(date);
        return Integer.parseInt(now) - Integer.parseInt(birthday);
    }


}
