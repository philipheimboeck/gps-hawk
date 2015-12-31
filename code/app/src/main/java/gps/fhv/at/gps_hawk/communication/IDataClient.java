package gps.fhv.at.gps_hawk.communication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gps.fhv.at.gps_hawk.domain.ExportContext;
import gps.fhv.at.gps_hawk.domain.MotionValues;
import gps.fhv.at.gps_hawk.domain.Track;
import gps.fhv.at.gps_hawk.domain.Waypoint;
import gps.fhv.at.gps_hawk.exceptions.CommunicationException;
import gps.fhv.at.gps_hawk.exceptions.UnExpectedResultException;

/**
 * Author: Philip Heimböck
 * Date: 31.12.15
 */
public interface IDataClient {

    List<Track> reserveTracks() throws CommunicationException;

    Track startTrack(Calendar calendar) throws CommunicationException;

    Track startTrack(Calendar calendar, Track track) throws CommunicationException;

    Track finishTrack(Calendar calendar, Track track) throws CommunicationException;

    void exportWaypoints(ArrayList<Waypoint> waypoints) throws CommunicationException;

    void exportMotionValues(ArrayList<MotionValues> values) throws CommunicationException;
}
