package gps.fhv.at.gps_hawk.communication;

import gps.fhv.at.gps_hawk.exceptions.RestException;

/**
 * Author: Philip Heimböck
 * Date: 11.11.15
 *
 * Client that communicates with the server
 */
public interface IAppClient {

    /**
     * Checks if there is an update
     * @param currentVersion The current version
     * @return the URL of the newer version or null if none
     */
    String getUpdateLink(String currentVersion) throws RestException;
}
