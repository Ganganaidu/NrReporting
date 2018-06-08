package nextradio.nranalytics.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static nextradio.nranalytics.controllers.NrUDateTransform.msSqlDateFormatter;


/**
 * Created by gkondati on 4/7/2016.
 */
class NrUDateUtils {

    /**
     * @return difference between two time in seconds
     */
    static long getTimeDiff(String currentTime, String oldTime) {
        long diff = 0;
        Date date1, date2;
        try {
            date1 = msSqlDateFormatter.parse(currentTime);
            date2 = msSqlDateFormatter.parse(oldTime);
            long difference = Math.abs(date1.getTime() - date2.getTime());
            diff = difference / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    static String getCurrentUtcTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return NrUDateTransform.msSqlDateFormat(cal.getTime());
    }

    static int compareEndTime(String endTime) {
        int diffMinutes = 0;
        if (endTime != null && endTime.length() > 0) {
            SimpleDateFormat iso8601FormatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            String currentUTCString = NrUDateTransform.iso8601FormatUTC(cal.getTime());

            //Log.d("ReportingTrackAdapter", "currentUTCString:" + currentUTCString);
            //Log.d("ReportingTrackAdapter", "endTime:" + endTime);
            try {
                Date d1 = iso8601FormatterUTC.parse(endTime);//end UTC time
                Date d2 = iso8601FormatterUTC.parse(currentUTCString);//current UTC time

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();
                //long diffSeconds = diff / 1000 % 60;
                diffMinutes = (int) diff / (60 * 1000) % 60;
                //long diffHours = diff / (60 * 60 * 1000) % 24;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return diffMinutes;
    }

    static int compareEndTimeWithStartTime(String startTime, String endTime) {

        int diffMinutes = 0;
        if (endTime != null && endTime.length() > 0) {
            //Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            //String currentUTCString = DateFormats.iso8601FormatUTC(cal.getTime());
            try {
                Date dStart = NrUDateTransform.iso8601FormatterUTC.parse(startTime);
                Date dEnd = NrUDateTransform.iso8601FormatterUTC.parse(endTime);//end UTC time

                //in milliseconds
                long diff = dEnd.getTime() - dStart.getTime();
                diffMinutes = (int) diff / (60 * 1000) % 60;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return diffMinutes;
    }
}
