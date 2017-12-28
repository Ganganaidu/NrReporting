package nextradio.nranalytics.interfaces;

import nextradio.nranalytics.objects.ActionPayload;

/**
 * Created by gkondati on 11/8/2017.
 * <p>
 * Contains all methods that we need to report and record data
 */

public interface IReportingDatabase {

    void recordAppSession();

    void recordListeningReporting(String frequencyHz, int frequencySubChannel, int deliveryType, String callLetters);

    void recordVisualImpression(ActionPayload payload);

    void recordActionImpression(ActionPayload payload, int action, int source);

    void recordSearchQuery(String searchString, int searchResults, int stationID);

    void recordStreamOffset(String streamSource, String trackingID);

    void recordStreamFailure(String streamSource, int failureType, int source);

    void recordNewsFeedReportData(String newsItemId, String newsItemTrackingId, int action, int source);

}




