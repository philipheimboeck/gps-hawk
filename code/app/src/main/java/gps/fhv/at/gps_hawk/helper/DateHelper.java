package gps.fhv.at.gps_hawk.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 * Created by Tobias on 24.10.2015.
 */
public class DateHelper {

    public static String toSql(Calendar cal) {
        return String.format("%tF %tT",cal,cal);
    }

    /**
     * Format a given date to a
     * String with the given format
     *
     * @param date
     * @return String
     */
    public static String formatDate(Calendar date, String format) {
        return new SimpleDateFormat(format).format(date);
    }


}
