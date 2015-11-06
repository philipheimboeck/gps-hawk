package gps.fhv.at.gps_hawk.communication;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public class HTTPAnswer {
    int responseCode;
    String content;
    String contentType;

    public HTTPAnswer() {
    }

    public HTTPAnswer(String content, int responseCode, String contentType) {
        this.responseCode = responseCode;
        this.content = content;
        this.contentType = contentType;
    }
}
