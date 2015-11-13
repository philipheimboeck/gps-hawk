package gps.fhv.at.gps_hawk.helper;

import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.persistence.setup.Exception2LogDef;

/**
 * Created by Tobias on 24.10.2015.
 */
public class DateHelper {

    public static String toSql(Calendar cal) {
        return String.format("%tF %tT", cal, cal);
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

    public static Calendar fromSql(String calStr) throws Exception {

        if (calStr == null) return null;

        if (calStr.matches(".*[a-z].*")) return null;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTime(sdf.parse(calStr));
        return cal;
    }

}
