package nextradio.nranalytics.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @author Matt Miner
 *         <p>
 *         Helper class to change Date objects into Strings of various formats
 *         and obtain Date type objects from various String representations.
 */
public class DateTransform {

    private static final SimpleDateFormat iso8601Formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private static final SimpleDateFormat iso8601FormatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private static final SimpleDateFormat msSqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

    /**
     * @param date - The Date to transform
     * @return - the String representation of the Date in iso8601 format: "yyyy-MM-dd HH:mm:ss"
     */
    private static String iso8601Format(Date date) {
        synchronized (iso8601Formatter) {
            return iso8601Formatter.format(date);
        }
    }

    /**
     * @param date - The Date object to transform
     * @return -  String representation of the Date in iso8601UTC format: "yyyy-MM-dd HH:mm:ss"
     */
    public static String iso8601FormatUTC(Date date) {
        synchronized (iso8601FormatterUTC) {
            iso8601FormatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            return iso8601FormatterUTC.format(date);
        }
    }

    /**
     * @param dateString - the String that is to be parsed into a Date.  Must be in
     *                   iso8601 format
     * @return - The Date object from the parsed String
     * @throws ParseException - thrown if the String was malformed
     */
    public static Date iso8601Parse(String dateString) throws ParseException {
        synchronized (iso8601Formatter) {
            return iso8601Formatter.parse(dateString);
        }
    }

    /**
     * @param date - The Date object to transform
     * @return -String representation of the Date in msSql format: "yyyy-MM-dd'T'HH:mm:ss"
     */
    public static String msSqlDateFormat(Date date) {
        synchronized (msSqlDateFormatter) {
            return msSqlDateFormatter.format(date);
        }
    }

    /**
     * @param dateString - The string to transform, in msSql format: "yyyy-MM-dd'T'HH:mm:ss"
     * @return - The Date object created from this String
     * @throws ParseException - thrown if the String provided was malformed
     */
    private static Date msSqlDateParse(String dateString) throws ParseException {
        synchronized (msSqlDateFormatter) {
            return msSqlDateFormatter.parse(dateString);
        }
    }
}
