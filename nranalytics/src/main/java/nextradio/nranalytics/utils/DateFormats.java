package nextradio.nranalytics.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormats {

    private final static SimpleDateFormat uiFormatter = new SimpleDateFormat("M/d/yy h:mma", Locale.ENGLISH);
    private final static SimpleDateFormat iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private final static SimpleDateFormat iso8601FormatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private final static SimpleDateFormat msSqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    public static String uiFormat(Date date) {
        synchronized (uiFormatter) {
            return uiFormatter.format(date);
        }
    }

    public static String iso8601Format(Date date) {
        synchronized (iso8601Formatter) {
            return iso8601Formatter.format(date);
        }
    }

    public static String iso8601FormatUTC(Date date) {
        synchronized (iso8601FormatterUTC) {
            iso8601FormatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            return iso8601FormatterUTC.format(date);
        }
    }

    public static Date iso8601Parse(String dateString) throws ParseException {
        synchronized (iso8601Formatter) {
            return iso8601Formatter.parse(dateString);
        }
    }

    static String msSqlDateFormat(Date date) {
        synchronized (msSqlDateFormatter) {
            msSqlDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return msSqlDateFormatter.format(date);
        }
    }

    public static Date msSqlDateParse(String dateString) throws ParseException {
        synchronized (msSqlDateFormatter) {
            return msSqlDateFormatter.parse(dateString);
        }
    }

    public static DecimalFormat decimalFormat = new DecimalFormat("###.#");


    public static int compareEndTime(String endTime) {
        int diffMinutes = 0;
        if (endTime != null && endTime.length() > 0) {
            SimpleDateFormat iso8601FormatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            String currentUTCString = DateFormats.iso8601FormatUTC(cal.getTime());

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

    public static int compareEndTimeWithStartTime(String startTime, String endTime) {

        int diffMinutes = 0;
        if (endTime != null && endTime.length() > 0) {
            //Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            //String currentUTCString = DateFormats.iso8601FormatUTC(cal.getTime());
            try {
                Date dStart = iso8601FormatterUTC.parse(startTime);
                Date dEnd = iso8601FormatterUTC.parse(endTime);//end UTC time

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
