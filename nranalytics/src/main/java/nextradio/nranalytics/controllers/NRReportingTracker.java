package nextradio.nranalytics.controllers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import nextradio.nranalytics.objects.reporting.Meta;
import nextradio.nranalytics.objects.reporting.ReportingDataObject;
import nextradio.nranalytics.utils.AppUtils;
import nextradio.nranalytics.utils.DateUtils;
import nextradio.nranalytics.utils.GsonConverter;
import nextradio.nranalytics.web.TagStationApiClientRequest;

/**
 * Created by gkondati on 12/13/2017.
 */

public class NRReportingTracker {

    private static final String TAG = "NRReportingTracker";

    private static NRReportingTracker _instance;

    //private CompositeDisposable disposable = new CompositeDisposable();

    private boolean isDataSendingToServer;

    public static NRReportingTracker getInstance() {
        if (_instance == null) {
            _instance = new NRReportingTracker();
        }
        return _instance;
    }

    private NRReportingTracker() {
    }

    void reportDataToServer() {
        Log.d(TAG, "reportDataToServer: " + isDataSendingToServer);
        if (!isDataSendingToServer) {
            isDataSendingToServer = true;
            Single.fromCallable(NRReportingTracker.this::getReportingData)
                    .subscribeOn(Schedulers.computation())
                    .subscribe(this::createWebReportingRequest, this::handleError);
        }
    }

    private void createWebReportingRequest(ReportingDataObject<Object> reportingDataObject) {
        Log.d(TAG, "deviceID: " + NRPersistedAppStorage.getInstaince().getDeviceId());
        try {
            TagStationApiClientRequest.getInstance()
                    .reportData(URLEncoder.encode(NRPersistedAppStorage.getInstaince().getDeviceId(), "UTF-8"), reportingDataObject)
                    .subscribeOn(Schedulers.io())
                    .subscribe(() -> {
                        Log.d(TAG, "data send completed: ");
                        //we need this timer to avoid multiple requests (duplicate data)
                        Single.timer(2, TimeUnit.SECONDS).subscribe(aLong -> {
                            NRPersistedAppStorage.getInstaince().clearReportingData();
                            isDataSendingToServer = false;
                        });
                    }, this::handleError);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleError(Throwable error) {
        //reg failed
        Log.d(TAG, "handleError: " + error.getLocalizedMessage());
        isDataSendingToServer = false;
    }

    private ReportingDataObject<Object> getReportingData() {
        isDataSendingToServer = true;
        String utcOffset = recordUtcOffset();
        String locationData = NRPersistedAppStorage.getInstaince().getLocationData();
        String listeningSessionData = NRPersistedAppStorage.getInstaince().getListeningData();
        String currentListingData = currentListeningSession();
        String radioImpressionData = NRPersistedAppStorage.getInstaince().getRadioEventImpression();

        ReportingDataObject<Object> reportingDataObject = new ReportingDataObject<>();
        reportingDataObject.setMeta(new Meta().setVersion(NextRadioReportingSDK.SDK_VERSION));

        //no need to send any records to server if there is no following data.
        if (AppUtils.isNullOrEmpty(locationData) &&
                AppUtils.isNullOrEmpty(listeningSessionData) &&
                AppUtils.isNullOrEmpty(currentListingData) &&
                AppUtils.isNullOrEmpty(radioImpressionData)) {
            return null;
        }
        addData(reportingDataObject, utcOffset);
        addData(reportingDataObject, locationData);
        addData(reportingDataObject, listeningSessionData);
        addData(reportingDataObject, currentListingData);
        addData(reportingDataObject, radioImpressionData);

        Log.d(TAG, "reportDataToServer: " + GsonConverter.getInstance().serializeToJson(reportingDataObject));

        return reportingDataObject;
    }

    /**
     * adding data to our final list
     */
    private void addData(ReportingDataObject<Object> reportingDataObject, String savedData) {
        if (savedData == null || savedData.isEmpty()) {
            return;
        }
        List<Object> list = reportingDataObject.getData();
        if (list != null) {
            list.addAll(GsonConverter.getInstance().getSavedData(savedData));
            reportingDataObject.setData(list);
        } else {
            reportingDataObject.setData(GsonConverter.getInstance().getSavedData(savedData));
        }
    }

    /**
     * device UTC as close as possible to when the PUT is being made to the API endpoint
     */
    private String recordUtcOffset() {
        if (NRPersistedAppStorage.getInstaince().getUTcOfSetUpdateFlag()) {
            //need to send UTC offset data only once per day
            JSONArray jsonArray = new JSONArray();
            JSONObject utcJsonObject = new JSONObject();
            try {
                utcJsonObject.put("type", "Utc.Offset");
                utcJsonObject.put("clientRequestTime", DateUtils.getCurrentUtcTime());
                jsonArray.put(utcJsonObject);
                NRPersistedAppStorage.getInstaince().setUtcSendFlag(false);
                return jsonArray.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String currentListeningSession() {
        String currentSession = NRPersistedAppStorage.getInstaince().getCurrentListeningData();
        if (currentSession != null && !currentSession.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject(currentSession);
                jsonArray.put(jsonObject);
                return jsonArray.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private String convertDataToString(ReportingDataObject<Object> reportingDataObject) {
        return GsonConverter.getInstance().serializeToJson(reportingDataObject);
    }
}
