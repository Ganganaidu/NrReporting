package nextradio.nranalytics.controllers;

import android.location.Location;

import org.json.JSONObject;

import nextradio.nranalytics.utils.AppUtils;
import nextradio.nranalytics.utils.GsonConverter;
import nextradio.nranalytics.utils.NrDateUtils;

import static nextradio.nranalytics.utils.NrDateUtils.getCurrentUtcTime;

/**
 * Created by gkondati on 12/11/2017.
 */

class NRListeningSessionLogger {
    // private static final String TAG = "NRListeningSession";

    private static NRListeningSessionLogger _instance;

    private final String END_TIME = "endTime";
    private final String START_TIME = "startTime";
    private final String IS_ENDED = "isEnded";


    public static NRListeningSessionLogger getInstance() {
        if (_instance == null) {
            _instance = new NRListeningSessionLogger();
        }
        return _instance;
    }

    private NRListeningSessionLogger() {

    }

    /**
     * Record listening data based on the station and freq
     *
     * @param frequencyHz         raw frequency that we need to identify station  ex: 9310000(93.1)
     * @param frequencySubChannel tells what kind of station it is, like HD or HD2
     * @param deliveryType        type of the station, number 1=FM|2=Stream|3=AM,
     * @param callLetters         optional value, callLetters for the station
     */
    void recordListeningSession(long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters) {
        boolean isSameStation = isEqualToCurrentTune(frequencyHz, frequencySubChannel, deliveryType);
        if (!isSameStation) {
            endCurrentListeningSession(false);
            createNewListeningSession(frequencyHz, frequencySubChannel, deliveryType, callLetters, null, null);
            NRReportingTracker.getInstance().reportDataToServer();
        } else {
            //update current listening session with current time stamp
            updateListeningSession();
        }
    }

    /**
     * Record listening data based on the station and freq
     *
     * @param frequencyHz         raw frequency that we need to identify station  ex: 9310000(93.1)
     * @param frequencySubChannel tells what kind of station it is, like HD or HD2
     * @param deliveryType        type of the station, number 1=FM|2=Stream|3=AM,
     * @param callLetters         optional value, callLetters for the station
     * @param publicStationId     station id for current station
     * @param isSimulcast         - true|false|null - indicates whether or not user is hearing simulcast audio when streaming a station (deliveryType = 2)
     */
    void recordListeningSession(long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters, Object publicStationId, Object isSimulcast) {
        boolean isSameStation = isEqualToCurrentTune(frequencyHz, frequencySubChannel, deliveryType);
        if (!isSameStation) {
            endCurrentListeningSession(false);
            createNewListeningSession(frequencyHz, frequencySubChannel, deliveryType, callLetters, publicStationId, isSimulcast);
            NRReportingTracker.getInstance().reportDataToServer();
        } else {
            //update current listening session with current time stamp
            updateListeningSession();
        }
    }

    private void createNewListeningSession(long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters, Object publicStationId, Object isSimulcast) {
        String currentUTCString = getCurrentUtcTime();
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("type", "Session.Listening.Channel");
            jsonObject.put(START_TIME, currentUTCString);
            jsonObject.put("deliveryType", String.valueOf(deliveryType));
            jsonObject.put("frequencyHz", String.valueOf(frequencyHz));
            jsonObject.put("frequencySubChannel", String.valueOf(frequencySubChannel));
            jsonObject.put("callLetters", callLetters);
            jsonObject.put(END_TIME, currentUTCString);
            jsonObject.put(IS_ENDED, false);
            jsonObject.put("publicStationId", publicStationId);
            jsonObject.put("isSimulcast", isSimulcast);

            try {
                Location location = NRLocationAdapter.getInstance().getCurrentLocation();
                if (location != null && NRPersistedAppStorage.getInstance().isGdprApproved()) {
                    jsonObject.put("latitude", Double.toString(location.getLatitude()));
                    jsonObject.put("longitude", Double.toString(location.getLongitude()));
                } else {
                    jsonObject.put("latitude", null);
                    jsonObject.put("longitude", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            NRPersistedAppStorage.getInstance().saveCurrentListeningData(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateListeningSession() {
        try {
            String currentSession = NRPersistedAppStorage.getInstance().getCurrentListeningData();
            //Log.d(TAG, "updateListeningSession: "+currentSession);
            if (!AppUtils.isNullOrEmpty(currentSession)) {
                JSONObject jsonObject = new JSONObject(currentSession);
                jsonObject.putOpt(END_TIME, getCurrentUtcTime());
                NRPersistedAppStorage.getInstance().saveCurrentListeningData(jsonObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param isExternal TRUE : if it is coming from HOST app,  FALSE: if from SDK
     */
    void endCurrentListeningSession(boolean isExternal) {
        String previousSessionValue = NRPersistedAppStorage.getInstance().getCurrentListeningData();
        try {
            //Log.d(TAG, "updateListeningSession: "+previousSessionValue);
            if (!AppUtils.isNullOrEmpty(previousSessionValue)) {
                JSONObject jsonObject = new JSONObject(previousSessionValue);
                jsonObject.put(END_TIME, getCurrentUtcTime());
                jsonObject.put(IS_ENDED, true);
                NRPersistedAppStorage.getInstance().saveCurrentListeningData("");

                String data = GsonConverter.getInstance().createJsonObjectToString(NRPersistedAppStorage.getInstance().getListeningData(), jsonObject);
                NRPersistedAppStorage.getInstance().saveListeningData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEqualToCurrentTune(long frequencyHz, int frequencySubChannel, int deliveryType) {
        String currentListeningData = NRPersistedAppStorage.getInstance().getCurrentListeningData();
        try {
            if (!AppUtils.isNullOrEmpty(currentListeningData)) {
                JSONObject jsonObject = new JSONObject(currentListeningData);
                //String startTime = jsonObject.getString(START_TIME);
                String endTime = jsonObject.getString(END_TIME);
                if (frequencyHz == jsonObject.getLong("frequencyHz")
                        && frequencySubChannel == jsonObject.getInt("frequencySubChannel")
                        && deliveryType == jsonObject.getInt("deliveryType")) {
                    //if end time and current time difference is grater than the 2 min interval, end this session and start new session
                    return NrDateUtils.getTimeDiff(getCurrentUtcTime(), endTime) <= 120;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
