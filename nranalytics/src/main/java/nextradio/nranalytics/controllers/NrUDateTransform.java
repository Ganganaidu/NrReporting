package nextradio.nranalytics.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @author gkondati
 * <p>
 * Helper class to change Date objects into Strings of various formats
 * and obtain Date type objects from various String representations.
 */
class NrUDateTransform {

    static final SimpleDateFormat iso8601FormatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    static final SimpleDateFormat msSqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    /**
     * @param date - The Date object to transform
     * @return -  String representation of the Date in iso8601UTC format: "yyyy-MM-dd HH:mm:ss"
     */
    static String iso8601FormatUTC(Date date) {
        synchronized (iso8601FormatterUTC) {
            iso8601FormatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            return iso8601FormatterUTC.format(date);
        }
    }

    /**
     * @param date - The Date object to transform
     * @return -String representation of the Date in msSql format: "yyyy-MM-dd'T'HH:mm:ss"
     */
    static String msSqlDateFormat(Date date) {
        synchronized (msSqlDateFormatter) {
            msSqlDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return msSqlDateFormatter.format(date);
        }
    }
}
