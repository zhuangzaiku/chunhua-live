package com.quootta.mdate.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ryon on 16/2/16.
 */
public class TimeUtil {

    public static String getCurrentTime() {
        long currentTimeMills = Calendar.getInstance().getTimeInMillis();
        return currentTimeMills + "";
    }

    public static int getTimeDifference (String stamp) {
        long currentTimeMills = Calendar.getInstance().getTimeInMillis();
        long serverTimeMills = Long.parseLong(stamp);
        long timeDifferenceMills = currentTimeMills - serverTimeMills;

//        return stamp2day(timeDifferenceMills);

        return stamp2min(timeDifferenceMills);
    }

    private static int stamp2min(long timeDifferenceMills) {
        int min;
        min = (int) (timeDifferenceMills/(1000*60));
        return min;
    }

    private static int stamp2hours(long timeDifferenceMills) {
        int hours;
        hours = (int) (timeDifferenceMills/(1000*60*60));
        return hours;
    }

    private static int stamp2day(long timeDifferenceMills) {
        int day;
        day = (int) (timeDifferenceMills/(1000*60*60*24));
        return day;
    }

    public static String stamp2Date(String stamp) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(stamp!=null){
            String sd = sdf.format(new Date(Long.parseLong(stamp)));
            return sd;
        }
       return null;
    }

    public static String stamp2DateClear(String stamp) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(stamp!=null){
            String sd = sdf.format(new Date(Long.parseLong(stamp)));
            return sd;
        }
        return null;
    }

    public static String date2Stamp(String dateStr){
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date.getTime() + "";
    }



    public static int time2minute(String time){
        Date date = new Date();
        DateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            date = sdf.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date.getHours()*60+date.getMinutes();
    }

    public static String minuet2Time(int minute) {
        int hour = minute / 60;
        int min = minute % 60;
        if (min < 10)
            return hour + ":0" + min;
        else
            return hour + ":" + min;
    }
}
