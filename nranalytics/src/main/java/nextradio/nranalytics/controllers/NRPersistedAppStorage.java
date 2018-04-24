package nextradio.nranalytics.controllers;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import nextradio.nranalytics.objects.registerdevice.DeviceRegResponseInfo;

/**
 * Created by gkondati on 11/1/2017.
 */

class NRPersistedAppStorage {

    // private static final String TAG = "NRPersistedAppStorage";

    static final String PROD_TAG_URL = "http://api.tagstation.com/sdk/v1.0/";
    static final String DEV_TAG_URL = "http://dev-api.tagstation.com/sdk/v1.0/";

    private static String pckName = "nextradio.nranalytics.";

    private static final String OVERRIDE_URL = pckName + "apiurl";
    private static final String DEVICE_ID = pckName + "deviceID";//is nothing but TS ID
    private static final String CACHING_ID = pckName + "cachingID";
    private static final String AD_GROUP = pckName + "adGroup";
    private static final String FEED_USER_ID = pckName + "feedUserID";
    private static final String DEVICE_STRING = pckName + "DeviceString";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private static NRPersistedAppStorage nrPersistedAppStorage;

    static NRPersistedAppStorage getInstance() {
        if (nrPersistedAppStorage == null) {
            nrPersistedAppStorage = new NRPersistedAppStorage();
        }
        return nrPersistedAppStorage;
    }

    /**
     * Saving data in shared preferences which will store life time of Application
     */
    @SuppressLint("CommitPrefEdits")
    private NRPersistedAppStorage() {
        appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(NRAppContext.getAppContext());
        this.prefsEditor = appSharedPrefs.edit();
    }

    void clearReportingData() {
        saveLocationData("");
        saveAppSessionData("");
        saveListeningData("");
        saveRadioEventImpression("");
    }

    void setDeviceId(String tsId) {
        prefsEditor.putString(DEVICE_ID, tsId).apply();
    }

    String getDeviceId() {
        return appSharedPrefs.getString(DEVICE_ID, "");
    }

    DeviceRegResponseInfo getDeviceRegistrationInfo() {
        String tsd = appSharedPrefs.getString(DEVICE_ID, "");
        String ad = appSharedPrefs.getString(AD_GROUP, "");
        String cache = appSharedPrefs.getString(CACHING_ID, "");

        DeviceRegResponseInfo di = new DeviceRegResponseInfo();
        di.setAdGroup(ad);
        di.setCachingGroup(cache);
        di.setTsd(tsd);
        return di;
    }

    void setDeviceRegistration(DeviceRegResponseInfo deviceInfo) {
        if (deviceInfo == null) {
            return;
        }
        // Log.d(TAG, "getTsd: " + deviceInfo.getTsd());
        //Log.d(TAG, "getCachingGroup: " + deviceInfo.getCachingGroup());
        prefsEditor
                .putString(DEVICE_ID, deviceInfo.getTsd())
                .putString(CACHING_ID, deviceInfo.getCachingGroup())
                .putString(AD_GROUP, deviceInfo.getAdGroup())
                .putString(FEED_USER_ID, deviceInfo.getFeedUser())
                .apply();
    }

    String getTagURL() {
        String value = appSharedPrefs.getString(OVERRIDE_URL, null);
        if (value != null)
            return value;
        else
            return PROD_TAG_URL;
    }

    void setTagURL(String url) {
        prefsEditor.putString(OVERRIDE_URL, url).apply();
    }

    String getDeviceString() {
        return appSharedPrefs.getString(DEVICE_STRING, null);
    }


    void setDeviceString(String newDeviceRegistrationString) {
        prefsEditor.putString(DEVICE_STRING, newDeviceRegistrationString).apply();
    }

    void saveAppSessionData(String visualImpressionData) {
        prefsEditor.putString(pckName + "app_session", visualImpressionData).apply();
    }

    String getAppSessionData() {
        return appSharedPrefs.getString(pckName + "app_session", "");
    }


    /**
     * @param date: save location json data
     */
    void saveLocationData(String date) {
        prefsEditor.putString(pckName + "jsonLocationData", date).apply();
    }

    /**
     * @return get location json data
     */
    String getLocationData() {
        return appSharedPrefs.getString(pckName + "jsonLocationData", "");
    }


    /**
     * we need this for comparing current and previous lat to avoid duplicate data
     */
    void savePreviousLat(String prsLat) {
        prefsEditor.putString(pckName + "savePreviousLat", prsLat).apply();
    }

    /**
     * @return last saved latitude
     */
    String getPreviousLat() {
        return appSharedPrefs.getString(pckName + "savePreviousLat", "");
    }

    /**
     * we need this for comparing current and previous Longitude to avoid duplicate data
     */
    void savePreviousLongitude(String prsLat) {
        prefsEditor.putString(pckName + "savePreviousLong", prsLat).apply();
    }

    /**
     * @return last saved Longitude
     */
    String getPreviousLongitude() {
        return appSharedPrefs.getString(pckName + "savePreviousLong", "");
    }

    /**
     * save current listening data
     */
    void saveCurrentListeningData(String listeningData) {
        prefsEditor.putString(pckName + "currentListeningData", listeningData).apply();
    }

    /**
     * @return saved current listening data
     */
    String getCurrentListeningData() {
        return appSharedPrefs.getString(pckName + "currentListeningData", "");
    }

    /**
     * save listening data
     */
    void saveListeningData(String listeningData) {
        prefsEditor.putString(pckName + "listeningData", listeningData).apply();
    }

    /**
     * @return saved listening data
     */
    String getListeningData() {
        return appSharedPrefs.getString(pckName + "listeningData", "");
    }


    void saveRadioEventImpression(String radioImpressionData) {
        prefsEditor.putString(pckName + "RadioEventImpression", radioImpressionData).apply();
    }


    String getRadioEventImpression() {
        return appSharedPrefs.getString(pckName + "RadioEventImpression", "");
    }

    void setUtcSendFlag(boolean updateUTCOfSet) {
        prefsEditor.putBoolean(pckName + "updateUTCOfSet", updateUTCOfSet).apply();
    }


    boolean getUTcOfSetUpdateFlag() {
        return appSharedPrefs.getBoolean(pckName + "updateUTCOfSet", false);
    }
}

