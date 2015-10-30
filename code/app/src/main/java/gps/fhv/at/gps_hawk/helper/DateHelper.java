package gps.fhv.at.gps_hawk.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import gps.fhv.at.gps_hawk.persistence.setup.WaypointDef;

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

    public static Calendar fromSql(String calStr) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            cal.setTime(sdf.parse(calStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }


}
