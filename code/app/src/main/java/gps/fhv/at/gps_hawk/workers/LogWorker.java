package gps.fhv.at.gps_hawk.workers;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.Exception2Log;

/**
 * Created by Tobias on 11.11.2015.
 */
public class LogWorker {

    private Thread mTaskReader;
    private static final Calendar mStartupTime = Calendar.getInstance();
    private static final Pattern mPattern = Pattern.compile("([0-9]{1,2})-([0-9]{1,2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})\\.([0-9]{3})([ ]+)(E|I|D|V|W|F)(.*)");


    public void initialize() {

        // Start new thread because is blocking
        mTaskReader = new Thread() {
            @Override
            public void run() {

                try {
                    Process process = Runtime.getRuntime().exec("logcat -v time -s " + Constants.PREFERENCES + "");
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    DbFacade db = DbFacade.getInstance();

                    StringBuilder log = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        LogMatchResult rs;
                        if ((rs = isToLog(line)) != null) {

                            Exception2Log toLog = new Exception2Log();
                            toLog.setMessage(rs.mLine);
                            toLog.setDateTime(rs.mCal);
                            toLog.setLevel(rs.mLevel);

                            db.saveEntity(toLog);
                        }
                    }
                    Log.i(Constants.PREFERENCES, "Should never be here!");
                } catch (IOException e) {
                    Log.e(Constants.PREFERENCES, "Error when reading logs", e);
                }
            }
        };

        mTaskReader.start();

    }

    /**
     * examines whether log is earlier than starup-time of app - if so, returns the parsed date, otherwise null
     *
     * @param line
     * @return
     */
    private LogMatchResult isToLog(String line) {

        Matcher m = mPattern.matcher(line);

        if (m.matches()) {

            // Parse Date
            Calendar parse = Calendar.getInstance();
            MatchResult mRs = m.toMatchResult();
            parse.set(mStartupTime.get(
                            Calendar.YEAR),
                    getMatchComp(mRs, 1) - 1, // Setting month, January starts at 0!!
                    getMatchComp(mRs, 2),
                    getMatchComp(mRs, 3),
                    getMatchComp(mRs, 4),
                    getMatchComp(mRs, 5));

            // if is old message, return
            if (mStartupTime.after(parse)) return null;

            // Create successfull response
            LogMatchResult rs = new LogMatchResult();
            rs.mCal = parse;
            rs.mLine = mRs.group(9);
            rs.setLevel(mRs.group(8));

            return rs;
        }
        return null;
    }

    private static int getMatchComp(MatchResult m, int i) {
        return Integer.parseInt(m.group(i));
    }

    private class LogMatchResult {
        public Calendar mCal;
        public String mLine;
        public int mLevel;

        public void setLevel(String lvl) {
            switch (lvl.charAt(0)) {
                case 'D':
                    mLevel = Log.DEBUG;
                    break;
                case 'I':
                    mLevel = Log.INFO;
                    break;
                case 'F':
                    mLevel = Log.ASSERT;
                    break;
                case 'W':
                    mLevel = Log.WARN;
                    break;
                case 'V':
                    mLevel = Log.VERBOSE;
                    break;
                case 'E':
                    mLevel = Log.ERROR;
                    break;
            }
        }
    }

}
