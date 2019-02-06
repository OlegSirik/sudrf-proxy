package com.vsk.courtrf.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dt {
    public static Date truncToday() {
        return trunc(new Date());
    }
    public static Date trunc(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String getDaysBefore(int pDays) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1 * pDays);

        return String.format("%02d", c.get(Calendar.DATE)) + "."
                + String.format("%02d", ( c.get(Calendar.MONTH) + 1) )
                + "." + c.get(Calendar.YEAR);
    }

    public static String toChar(Date date, String format) {
        Format formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static Date toDate(String date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( format );
        try {
            Date eventDate = simpleDateFormat.parse( date );
            return eventDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
