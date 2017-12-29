package nextradio.nranalytics.utils;

import android.os.SystemClock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


/**
 * Created by gkondati on 4/7/2016.
 */
public class DateUtils {

    //private static final String contestStartDate = "20160929";
    private static final String contestStartDate = "20161001";
    private static final String contestEndDate = "20161231";

    private static SimpleDateFormat simpleDateFormat;

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    /*@return TRUE if current date is grater than the start date.*/
    public static boolean contestStartDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        try {
            Date strDate = sdf.parse(contestStartDate);
            if (new Date().after(strDate)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /*Don't show contest if it returns TRUE
    * @return TRUE if current date is grater than the given end date*/
    public static boolean contestEndDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        try {
            Date endDate = sdf.parse(contestEndDate);
            if (new Date().after(endDate)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private static SimpleDateFormat getSimpleDateFormat() {
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
        return simpleDateFormat;
    }

    /*Gives you SNTP(Simple Network Time Protocol) time,
    which is global time, irrespective of device time
    @return offset based on server and device time*/
    public static long getUTCTime() {
        long diffInSec = -1;
        SntpClient sntpClient = new SntpClient();
        //you can use any one of the NTP server
        //time-a.nist.gov
        //pool.ntp.org
        if (sntpClient.requestTime("0.africa.pool.ntp.org", 30000)) {
            long nowAsPerServer = sntpClient.getNtpTime() + SystemClock.elapsedRealtime() - sntpClient.getNtpTimeReference();
            long diff = nowAsPerServer - System.currentTimeMillis();
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diff);
            String presentDate = DateFormats.iso8601FormatUTC(new Date(nowAsPerServer));
            if (presentDate.equals("1970-01-01 00:00:00")) {
                return -1;
            }
        }
        return diffInSec;
    }

    /**
     * get the diff b/w current time and saved location time
     */
    public static long getTimeDiff(String currentTime, String oldTime) {
        long diff = 0;
        Date date1, date2;
        SimpleDateFormat format = DateUtils.getSimpleDateFormat();
        try {
            date1 = format.parse(currentTime);
            date2 = format.parse(oldTime);
            long difference = Math.abs(date1.getTime() - date2.getTime());
            diff = difference / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    public static String getCurrentUtcTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return DateFormats.msSqlDateFormat(cal.getTime());
    }
}
