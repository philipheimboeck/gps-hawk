package gps.fhv.at.gps_hawk.tasks;

/**
 * Author: Philip Heimböck
 * Date: 17.10.15
 */
public interface IAsyncTaskCaller<Progress, Result> {

    void onPostExecute(final Result success);

    void onCancelled();

    void onProgressUpdate(Progress... progress);

    void onPreExecute();
}
