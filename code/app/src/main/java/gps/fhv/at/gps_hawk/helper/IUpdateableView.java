package gps.fhv.at.gps_hawk.helper;

import gps.fhv.at.gps_hawk.tasks.ExportMetadataLoaderTask;

/**
 * Created by Tobias on 27.11.2015.
 */
public interface IUpdateableView {
    void showLoading(final boolean showLoading);
    void doDataLoadingAsnc();
}
