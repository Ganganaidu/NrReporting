package nextradio.nranalytics.controllers;

import android.content.Context;
import android.location.Location;
import android.media.AudioManager;
import android.util.Log;

import org.json.JSONObject;

import nextradio.nranalytics.utils.AppUtils;
import nextradio.nranalytics.utils.DateUtils;
import nextradio.nranalytics.utils.GsonConverter;

/**
 * Created by gkondati on 12/11/2017.
 */

class NRListeningSessionLogger {
    private static final String TAG = "NRListeningSession";

    private static NRListeningSessionLogger _instance;

    private AudioManager audioManager;

    private NRPersistedAppStorage appStorage;

    public static NRListeningSessionLogger getInstance() {
        if (_instance == null) {
            _instance = new NRListeningSessionLogger();
        }
        return _instance;
    }

    private NRListeningSessionLogger() {
        audioManager = (AudioManager) NRAppContext.getAppContext().getSystemService(Context.AUDIO_SERVICE);
        appStorage = new NRPersistedAppStorage();
    }

    /**
     * @return current volume of the device
     */
    private int getCurrentVolume() {
        try {
            return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        } catch (NullPointerException e) {
            return 25;
        }
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
        //no need to report or save listening session if current system value is zero
        if (getCurrentVolume() <= 0) {
            return;
        }
        boolean isEqualToCurrentStation = isEqualToCurrentTune(frequencyHz, frequencySubChannel, deliveryType);
        Log.d(TAG, "isEqualToCurrentStation:" + isEqualToCurrentStation);

        //end current session before inserting new one
        if (!isEqualToCurrentStation) {
            endCurrentListeningSession();
            createNewListeningSession(frequencyHz, frequencySubChannel, deliveryType, callLetters);
        } else {
            //update current listening session with current time stamp
            updateListeningSession();
        }
    }

    private void createNewListeningSession(long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters) {
        String currentUTCString = DateUtils.getCurrentUtcTime();
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("type", "Session.Listening.Channel");
            jsonObject.put("startTime", currentUTCString);
            jsonObject.put("deliveryType", String.valueOf(deliveryType));
            jsonObject.put("frequencyHz", String.valueOf(frequencyHz));
            jsonObject.put("frequencySubChannel", String.valueOf(frequencySubChannel));
            jsonObject.put("callLetters", callLetters);
            jsonObject.put("endTime", currentUTCString);
            jsonObject.put("isEnded", false);
            try {
                Location location = NRLocationAdapter.getInstance().getCurrentLocation();
                if (location != null) {
                    jsonObject.put("latitude", Double.toString(location.getLatitude()));
                    jsonObject.put("longitude", Double.toString(location.getLatitude()));
                } else {
                    jsonObject.put("latitude", "null");
                    jsonObject.put("longitude", "null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            appStorage.saveCurrentListeningData(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateListeningSession() {
        try {
            String currentSession = appStorage.getCurrentListeningData();
            if (!AppUtils.isNullOrEmpty(currentSession)) {
                JSONObject jsonObject = new JSONObject(currentSession);
                jsonObject.putOpt("endTime", DateUtils.getCurrentUtcTime());
                appStorage.saveCurrentListeningData(jsonObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void endCurrentListeningSession() {
        String previousSessionValue = appStorage.getCurrentListeningData();
        try {
            if (!AppUtils.isNullOrEmpty(previousSessionValue)) {
                JSONObject jsonObject = new JSONObject(previousSessionValue);
                jsonObject.put("endTime", DateUtils.getCurrentUtcTime());
                jsonObject.put("isEnded", true);
                appStorage.saveCurrentListeningData("");

                String data = GsonConverter.getInstance().createJsonObjectToString(appStorage.getListeningData(), jsonObject);
                appStorage.saveListeningData(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isEqualToCurrentTune(long frequencyHz, int frequencySubChannel, int deliveryType) {
        String currentListeningData = appStorage.getCurrentListeningData();
        try {
            if (!AppUtils.isNullOrEmpty(currentListeningData)) {
                JSONObject jsonObject = new JSONObject(currentListeningData);
                if (frequencyHz == jsonObject.getLong("frequencyHz") &&
                        frequencySubChannel == jsonObject.getInt("frequencySubChannel") &&
                        deliveryType == jsonObject.getInt("deliveryType")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
