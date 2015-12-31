package gps.fhv.at.gps_hawk.communication;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.MotionValues;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;
import gps.fhv.at.gps_hawk.exceptions.NoConnectionException;

/**
 * Author: Philip Heimböck
 * Date: 31.12.2015
 */
public class DataClient extends RestClient implements IDataClient {

    private static final String REST_DATA = REST_SERVER + "app/data/";
    private static final String REST_RESERVE_TRACKS = REST_DATA + "reservetracks";
    private static final String REST_START_NEW_TRACK = REST_DATA + "starttrack/$TIME";
    private static final String REST_START_TRACK = REST_START_NEW_TRACK + "?track=$TRACK";
    private static final String REST_FINISH_TRACK = REST_DATA + "finishtrack/$TIME/$TRACK";
    private static final String REST_EXPORT_WAYPOINTS = REST_DATA + "waypoints";
    private static final String REST_EXPORT_MOTIONVALUES = REST_DATA + "motionValues";


    public DataClient(Context context) {
        super(context);
    }

    @Override
    public List<Track> reserveTracks() throws CommunicationException {

        try {
            URL url = new URL(REST_RESERVE_TRACKS);
            HashMap<String, String> headers = getAuthorizationHeaders();

            // Get the tracks
            HTTPAnswer answer = get(url, headers);
            if (answer.responseCode != 200) {
                throw new CommunicationException("Error occurred while reserving tracks!");
            }

            // Now return the tracks
            ArrayList<Track> tracks = new ArrayList<>();

            JSONObject jsonObjects = new JSONObject(answer.content);
            JSONArray tracksArray = jsonObjects.getJSONArray("tracks");

            for (int i = 0; i < tracksArray.length(); i++) {
                JSONObject trackObject = tracksArray.getJSONObject(i);
                Track track = new Track();
                track.setExternalId(trackObject.getString("id"));

                tracks.add(track);
            }

            return tracks;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException | JSONException e) {
            Log.e(Constants.PREFERENCES, "Reserving tracks failed: ", e);
            throw new CommunicationException("Reserving tracks failed", e);
        }
    }

    @Override
    public Track startTrack(Calendar calendar) throws CommunicationException {
        try {
            URL url = new URL(REST_START_NEW_TRACK.replace("$TIME", String.valueOf(calendar.getTimeInMillis())));
            HashMap<String, String> headers = getAuthorizationHeaders();

            // Get the tracks
            HTTPAnswer answer = get(url, headers);
            if (answer.responseCode != 200) {
                throw new CommunicationException("Error occurred while starting track!");
            }

            // Now return the track
            JSONObject jsonObjects = new JSONObject(answer.content);
            JSONObject trackObject = jsonObjects.getJSONObject("track");

            Track track = new Track();
            track.setExternalId(trackObject.getString("id"));
            track.setStartDateTime(trackObject.getLong("startDate"));

            return track;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException | JSONException e) {
            Log.e(Constants.PREFERENCES, "Starting tracks failed: ", e);
            throw new CommunicationException("Starting tracks failed", e);
        }
    }

    @Override
    public Track startTrack(Calendar calendar, Track track) throws CommunicationException {
        try {
            URL url = new URL(REST_START_TRACK
                    .replace("$TIME", String.valueOf(calendar.getTimeInMillis()))
                    .replace("$TRACK", track.getExternalId()));
            HashMap<String, String> headers = getAuthorizationHeaders();

            // Get the tracks
            HTTPAnswer answer = get(url, headers);
            if (answer.responseCode != 200) {
                throw new CommunicationException("Error occurred while starting track!");
            }

            // Now return the track
            JSONObject jsonObjects = new JSONObject(answer.content);
            JSONObject trackObject = jsonObjects.getJSONObject("track");

            track.setStartDateTime(trackObject.getLong("startDate"));

            return track;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException | JSONException e) {
            Log.e(Constants.PREFERENCES, "Starting tracks failed: ", e);
            throw new CommunicationException("Starting tracks failed", e);
        }
    }

    @Override
    public Track finishTrack(Calendar calendar, Track track) throws CommunicationException {
        try {
            URL url = new URL(REST_FINISH_TRACK
                    .replace("$TIME", String.valueOf(calendar.getTimeInMillis()))
                    .replace("$TRACK", track.getExternalId()));
            HashMap<String, String> headers = getAuthorizationHeaders();

            // Get the tracks
            HTTPAnswer answer = get(url, headers);
            if (answer.responseCode != 200) {
                throw new CommunicationException("Error occurred while finishing track!");
            }

            // Now return the track
            JSONObject jsonObjects = new JSONObject(answer.content);
            JSONObject trackObject = jsonObjects.getJSONObject("track");

            track.setEndDateTime(trackObject.getLong("endDate"));

            return track;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException | JSONException e) {
            Log.e(Constants.PREFERENCES, "Finish track failed: ", e);
            throw new CommunicationException("Finish track failed", e);
        }
    }

    @Override
    public void exportWaypoints(ArrayList<Waypoint> waypoints) throws CommunicationException {
        try {
            URL url = new URL(REST_EXPORT_WAYPOINTS);
            HashMap<String, String> headers = getAuthorizationHeaders();
            HashMap<String, String> content = new HashMap<>();
            content.put("waypoints", getJsonArray(waypoints));

            // Get the tracks
            HTTPAnswer answer = post(url, content, headers);
            if (answer.responseCode != 200) {
                throw new CommunicationException("Error occurred while finishing track!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException e) {
            Log.e(Constants.PREFERENCES, "Finish track failed: ", e);
            throw new CommunicationException("Finish track failed", e);
        }
    }

    @Override
    public void exportMotionValues(ArrayList<MotionValues> values) throws CommunicationException {
        try {
            URL url = new URL(REST_EXPORT_MOTIONVALUES);
            HashMap<String, String> headers = getAuthorizationHeaders();
            HashMap<String, String> content = new HashMap<>();
            content.put("motionValues", getJsonArray(values));

            // Get the tracks
            HTTPAnswer answer = post(url, content, headers);
            if (answer.responseCode != 200) {
                throw new CommunicationException("Error occurred while finishing track!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException e) {
            Log.e(Constants.PREFERENCES, "Finish track failed: ", e);
            throw new CommunicationException("Finish track failed", e);
        }
    }
}
