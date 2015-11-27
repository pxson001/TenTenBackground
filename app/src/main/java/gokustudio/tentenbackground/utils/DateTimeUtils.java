package gokustudio.tentenbackground.utils;

import java.util.Calendar;

/**
 * Created by son on 10/27/15.
 */
public class DateTimeUtils {
    public static int getDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_YEAR);
        return day;
    }

    public static int getWeek() {
        Calendar cal = Calendar.getInstance();
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        return week;
    }

    public static int getMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        return month;
    }

    public static int getQuarter() {
        Calendar c
                = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);

        return (month >= Calendar.JANUARY && month <= Calendar.MARCH) ? 1
                : (month >= Calendar.APRIL && month <= Calendar.JUNE) ? 2
                : (month >= Calendar.JULY && month <= Calendar.SEPTEMBER) ? 3 : 4;
    }

    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return year;
    }
}
