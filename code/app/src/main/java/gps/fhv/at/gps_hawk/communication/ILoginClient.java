package gps.fhv.at.gps_hawk.communication;

import javax.security.auth.login.LoginException;

import gps.fhv.at.gps_hawk.exceptions.RegistrationException;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public interface ILoginClient {

    /**
     * Checks if the user is already existing
     *
     * @param username
     * @return
     */
    boolean userExists(String username);

    /**
     * Registers a new user on the server
     *
     * @param username
     * @param password
     * @return Authentication Token
     * @throws RegistrationException
     */
    void register(String username, String password) throws RegistrationException;

    /**
     * Login as the user
     *
     * @param username
     * @param password
     * @param deviceID
     * @return Login Token
     * @throws LoginException
     */
    String login(String username, String password, String deviceID) throws LoginException;

    /**
     * Checks if the token is still valid
     *
     * @param token
     * @return
     */
    boolean tokenValid(String token);
}
