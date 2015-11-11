package gps.fhv.at.gps_hawk.workers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import gps.fhv.at.gps_hawk.Constants;

/**
 * Created by Tobias on 11.11.2015.
 */
public class LogWorker {

    private Thread mTaskReader;

    public void initialize() {

        // Start new thread because is blocking
        mTaskReader = new Thread() {
            @Override
            public void run() {

                try {
                    Process process = Runtime.getRuntime().exec("logcat -s " + Constants.PREFERENCES + "");
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    StringBuilder log = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        log.append(line);
                    }
                    Log.i(Constants.PREFERENCES,"Should never be here!");
                } catch (IOException e) {
                    Log.e(Constants.PREFERENCES, "Error when reading logs", e);
                }
            }
        };

        mTaskReader.start();

    }

}
