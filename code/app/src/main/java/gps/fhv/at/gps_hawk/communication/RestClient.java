package gps.fhv.at.gps_hawk.communication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gps.fhv.at.gps_hawk.Constants;
import gps.fhv.at.gps_hawk.domain.IJSONable;
import gps.fhv.at.gps_hawk.exceptions.NoConnectionException;
import gps.fhv.at.gps_hawk.helper.TokenHelper;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class RestClient {

    protected static final String REST_SERVER = "http://46.101.192.214:8080/webservice/fhvgis/";
//    protected static final String REST_SERVER = "http://192.168.0.3:8080/webservice/fhvgis/";

    protected Context mContext;

    public RestClient(Context context) {
        this.mContext = context;
    }

    /**
     * GET the URL data
     *
     * @param url     The url to GET
     * @param headers The headers to add
     * @return The Answer of the server
     * @throws NoConnectionException
     * @throws IOException
     */
    public HTTPAnswer get(URL url, HashMap<String, String> headers) throws NoConnectionException, IOException {
        if (!checkConnection()) {
            throw new NoConnectionException();
        }

        InputStream inputStream = null;
        HttpURLConnection connection = null;
        HTTPAnswer answer = null;

        try {
            connection = createConnection(url, "GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // Provide the headers
            if (headers != null) {
                for (String header : headers.keySet()) {
                    connection.setRequestProperty(header, headers.get(header));
                }
            }

            // Start the query
            connection.connect();

            // Get the response code
            answer = new HTTPAnswer();
            answer.responseCode = connection.getResponseCode();
            answer.contentType = connection.getContentType();

            // Get the content
            if (answer.responseCode >= 400) {
                inputStream = connection.getErrorStream();
            } else {
                inputStream = connection.getInputStream();
            }

            answer.content = getContent(inputStream);

        } finally {

            // Close stream and connection
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        // Token Invalid?
        if (answer.responseCode == 401) {
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.BROADCAST_INVALID_TOKEN));
        }

        return answer;
    }

    /**
     * GET the URL data
     *
     * @param url The url to GET
     * @return The Answer of the server
     * @throws NoConnectionException
     * @throws IOException
     */
    public HTTPAnswer get(URL url) throws NoConnectionException, IOException {
        return get(url, null);
    }

    private HTTPAnswer post(URL url, String content, Map<String, String> headers) throws IOException, NoConnectionException {
        if (!checkConnection()) {
            throw new NoConnectionException();
        }

        InputStream inputStream = null;
        HttpURLConnection connection = null;
        HTTPAnswer answer = null;

        try {
            connection = createConnection(url, "POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

            // Provide the headers
            if (headers != null) {
                for (String header : headers.keySet()) {
                    connection.setRequestProperty(header, headers.get(header));
                }
            }

            // Start the query
            connection.connect();

            // Send the data
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(content);

            writer.flush();
            writer.close();
            os.close();

            // Get the response code
            answer = new HTTPAnswer();
            answer.responseCode = connection.getResponseCode();
            answer.contentType = connection.getContentType();

            // Get the content
            if (answer.responseCode >= 400) {
                inputStream = connection.getErrorStream();
            } else {
                inputStream = connection.getInputStream();
            }
            answer.content = getContent(inputStream);

        } finally {

            // Close stream and connection
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        // Token Invalid?
        if (answer.responseCode == 401) {
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(Constants.BROADCAST_INVALID_TOKEN));
        }

        return answer;

    }

    public HTTPAnswer post(URL url, HashMap<String, String> params) throws IOException, NoConnectionException {
        return post(url, params, null);
    }

    public HTTPAnswer post(URL url, HashMap<String, String> params, Map<String, String> headers) throws IOException, NoConnectionException {
        String content = getPostDataString(params);
        return post(url, content, headers);
    }

    /**
     * Returns a json string of the entities
     *
     * @param list
     * @param <T>
     * @return
     */
    public <T extends IJSONable> JSONArray getJsonArray(List<T> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (IJSONable o : list) {
            jsonArray.put(o.toJSON());
        }

        return jsonArray;
    }

    /**
     * Returns an encoded data string
     *
     * @param params The params to encode
     * @return The data string
     * @throws UnsupportedEncodingException
     */
    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    /**
     * Returns the content of the input stream
     *
     * @param inputStream The stream which will be read
     * @return The content of the stream
     * @throws IOException
     */
    private String getContent(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        InputStreamReader reader = new InputStreamReader(inputStream);
        int data;
        while ((data = reader.read()) != -1) {
            stringBuilder.append((char) data);
        }

        return stringBuilder.toString();
    }

    /**
     * Checks if the device has a connection
     *
     * @return true if the device is connected to the internet
     */
    public boolean checkConnection() {
        // First check for a valid connection
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Creates a connection to the url. The connection object won't be open.
     *
     * @param url    The url to connect to
     * @param method The HTTP method
     * @return The
     * @throws IOException
     */
    private HttpURLConnection createConnection(URL url, String method) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(15000);
        connection.setConnectTimeout(30000);
        connection.setRequestMethod(method);
        connection.setDoInput(true);

        if (method.equals("POST"))
            connection.setDoOutput(true);

        return connection;
    }

    @NonNull
    protected HashMap<String, String> getAuthorizationHeaders() {
        String authorizationToken = TokenHelper.getToken(mContext);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", authorizationToken);
        return headers;
    }
}
