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
import gps.fhv.at.gps_hawk.domain.Exception2Log;
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
    private static final String REST_EXPORT_TRACKS = REST_DATA + "updatetracks";
    private static final String REST_EXPORT_WAYPOINTS = REST_DATA + "waypoints";
    private static final String REST_EXPORT_MOTIONVALUES = REST_DATA + "motionValues";
    private static final String REST_EXPORT_LOGS = REST_SERVER + "app/log";


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
    public void exportTracks(List<Track> tracks) throws CommunicationException {
        try {
            URL url = new URL(REST_EXPORT_TRACKS);
            HashMap<String, String> headers = getAuthorizationHeaders();
            HashMap<String, String> content = new HashMap<>();

            content.put("tracks", new JSONObject().put("tracks", getJsonArray(tracks)).toString());

            // Get the tracks
            HTTPAnswer answer = post(url, content, headers);
            if (answer.responseCode != 200) {
                throw new CommunicationException("Error occurred while exporting tracks!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException | JSONException e) {
            Log.e(Constants.PREFERENCES, "Exporting tracks failed: ", e);
            throw new CommunicationException("Exporting tracks failed", e);
        }
    }

    @Override
    public void exportWaypoints(List<Waypoint> waypoints) throws CommunicationException {
        try {
            URL url = new URL(REST_EXPORT_WAYPOINTS);
            HashMap<String, String> headers = getAuthorizationHeaders();
            HashMap<String, String> content = new HashMap<>();

            content.put("waypoints", new JSONObject().put("waypoints", getJsonArray(waypoints)).toString());

            // Get the tracks
            HTTPAnswer answer = post(url, content, headers);
            if (answer.responseCode != 200) {
                throw new CommunicationException("Error occurred while exporting waypoints!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException | JSONException e) {
            Log.e(Constants.PREFERENCES, "Exporting waypoints failed: ", e);
            throw new CommunicationException("Exporting waypoints failed", e);
        }
    }

    @Override
    public void exportMotionValues(List<MotionValues> values) throws CommunicationException {
        try {
            URL url = new URL(REST_EXPORT_MOTIONVALUES);
            HashMap<String, String> headers = getAuthorizationHeaders();
            HashMap<String, String> content = new HashMap<>();
            content.put("motionValues", new JSONObject().put("motionValues", getJsonArray(values)).toString());

            // Get the tracks
            HTTPAnswer answer = post(url, content, headers);
            if (answer.responseCode != 200) {
                throw new CommunicationException("Error occurred while exporting motion values!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException | JSONException e) {
            Log.e(Constants.PREFERENCES, "Exporting motion values failed: ", e);
            throw new CommunicationException("Exporting motion values failed", e);
        }
    }

    @Override
    public void exportLogs(List<Exception2Log> logs) throws CommunicationException {
        try {
            URL url = new URL(REST_EXPORT_LOGS);
            HashMap<String, String> headers = getAuthorizationHeaders();
            HashMap<String, String> content = new HashMap<>();
            content.put("logs", new JSONObject().put("logs", getJsonArray(logs)).toString());

            // Get the tracks
            HTTPAnswer answer = post(url, content, headers);
            if (answer.responseCode != 200) {
                throw new CommunicationException("Error occurred while exporting logs!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL!");

        } catch (NoConnectionException | IOException | JSONException e) {
            Log.e(Constants.PREFERENCES, "Exporting logs failed: ", e);
            throw new CommunicationException("Exporting logs failed", e);
        }
    }


}
