package nextradio.nranalytics.controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import nextradio.nranalytics.utils.DateFormats;
import nextradio.nranalytics.utils.GsonConverter;

/**
 * Created by gkondati on 11/1/2017.
 */

class NRSessionLogger {
   // private static final String TAG = "NRSessionLogger";

    private Timer mSessionTimer;
    private static NRSessionLogger instance;

    public static NRSessionLogger getInstance() {
        if (instance == null) {
            instance = new NRSessionLogger();
        }
        return instance;
    }

    private NRSessionLogger() {
    }

    /**
     * Start app session here. We update app session for every 5 Sec to track active session of the application
     */
    private void startSession() {
        stopSessionUpdates();

        mSessionTimer = new Timer();
        try {
            mSessionTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //create or update app session
                    recordAppSession();
                }
            }, 5000, 5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop session check
     * <p>
     * {@link NextRadioReportingSDK} for more information
     */
    private void stopSessionUpdates() {
        try {
            if (mSessionTimer != null) {
                mSessionTimer.cancel();
                mSessionTimer.purge();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * creating and updating app session based on time diff
     * <p>
     * If the existing session End-Datetime is less than two minutes old, then update the session’s end datetime value.
     * other wise create new session with unique timestamp
     * </P>
     * <p>
     * NOTE: Do not try to change json object keys here(which makes some issue for updating data), we use them in other places
     * </p>
     */
    private void recordAppSession() {
        String savedValue = NRPersistedAppStorage.getInstance().getAppSessionData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.ENGLISH);
        String uniqueId = sdf.format(new Date());

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        String currentUTCString = DateFormats.iso8601FormatUTC(cal.getTime());

        String savedUniqueId = checkIsSessionAvailable(savedValue);
        //Log.d(TAG, "savedUniqueId:" + savedUniqueId);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("endTime", currentUTCString);
            if (savedUniqueId == null || savedUniqueId.isEmpty()) {
                jsonObject.put("uniqueId", uniqueId);
                jsonObject.put("startTime", currentUTCString);
                jsonObject.put("type", "Session.AppSession"); //getNewBatchId()

                String data = GsonConverter.getInstance().createJsonObjectToString(savedValue, jsonObject);
                NRPersistedAppStorage.getInstance().saveAppSessionData(data);
            } else {
                jsonObject = GsonConverter.getInstance().updateSessionEndTime(savedValue, currentUTCString);
                String data = GsonConverter.getInstance().updateJsonObject(savedValue, jsonObject);
                NRPersistedAppStorage.getInstance().saveAppSessionData(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return unique id if session available, NULL if session not available or expires
     */
    private String checkIsSessionAvailable(String savedValue) {
        try {
            if (savedValue == null || savedValue.isEmpty()) {
                return null;
            }
            JSONArray jsonArray = new JSONArray(savedValue);
            JSONObject jsonObject = new JSONObject(jsonArray.get(jsonArray.length() - 1).toString());
            String uniqueId = jsonObject.getString("uniqueId");
            String endTime = jsonObject.getString("endTime");
            if (uniqueId == null || uniqueId.length() == 0) {
                return null;
            } else if (DateFormats.compareEndTime(endTime) <= 2) {//check session time
                // && DateFormats.compareEndTime(endTime) <= 2
                //If the existing session End-Datetime is less than two minutes old, then update the session’s end datetime value.
                //update end time here
                return uniqueId;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
