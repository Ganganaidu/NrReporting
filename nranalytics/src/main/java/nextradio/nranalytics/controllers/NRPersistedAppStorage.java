package nextradio.nranalytics.controllers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import nextradio.nranalytics.objects.registerdevice.DeviceRegResponseInfo;

/**
 * Created by gkondati on 11/1/2017.
 */

public class NRPersistedAppStorage {

    // private static final String TAG = "NRPersistedAppStorage";

    private static final String DefaultTAGURL = "http://api.tagstation.com/";
    private static String pckName = "nextradio.nranalytics.";

    private static final String OVERRIDE_URL = pckName + "apiurl";
    private static final String DEVICE_ID = pckName + "deviceID";//is nothing but TS ID
    private static final String CACHING_ID = pckName + "cachingID";
    private static final String AD_GROUP = pckName + "adGroup";
    private static final String FEED_USER_ID = pckName + "feedUserID";
    private static final String SELECTED_COUNTRY = pckName + "country";
    private static final String DEVELOPER_MODE = pckName + "enabledev";
    private static final String DEVICE_STRING = pckName + "DeviceString";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private static NRPersistedAppStorage nrPersistedAppStorage;

    public static NRPersistedAppStorage getInstance() {
        if (nrPersistedAppStorage == null) {
            nrPersistedAppStorage = new NRPersistedAppStorage();
        }
        return nrPersistedAppStorage;
    }

    /**
     * Saving data in shared preferences which will store life time of Application
     */
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

    public void setCachingID(String ID) {
        prefsEditor.putString(CACHING_ID, ID).apply();
    }

    public void setAdGroupID(String ID) {
        prefsEditor.putString(AD_GROUP, ID).apply();
    }

    public DeviceRegResponseInfo getDeviceRegistrationInfo() {
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

    public String getTagURL() {
        if (appSharedPrefs.getBoolean(DEVELOPER_MODE, false)) {
            String value = appSharedPrefs.getString(OVERRIDE_URL, null);
            if (value != null)
                return value;
            else
                return DefaultTAGURL;
        } else {
            return DefaultTAGURL;
        }
    }

    public void setTagURL(String url) {
        prefsEditor.putString(OVERRIDE_URL, url).apply();
    }

    String getDeviceString() {
        return appSharedPrefs.getString(DEVICE_STRING, null);
    }


    void setDeviceString(String newDeviceRegistrationString) {
        prefsEditor.putString(DEVICE_STRING, newDeviceRegistrationString).apply();
    }

    public String getCountryString() {
        return appSharedPrefs.getString(SELECTED_COUNTRY, null);
    }

    public void setCountryString(String countryString) {
        prefsEditor.putString(SELECTED_COUNTRY, countryString).apply();
    }

    public void saveVisualImpressions(String visualImpressionData) {
        prefsEditor.putString(pckName + "visual_impression", visualImpressionData).apply();
    }

    public String getVisualImpression() {
        return appSharedPrefs.getString(pckName + "visual_impression", "");
    }

    void saveAppSessionData(String visualImpressionData) {
        prefsEditor.putString(pckName + "app_session", visualImpressionData).apply();
    }

    String getAppSessionData() {
        return appSharedPrefs.getString(pckName + "app_session", "");
    }

    public void saveUTCoffSetData(String visualImpressionData) {
        prefsEditor.putString(pckName + "UTCoffSetData", visualImpressionData).apply();
    }

    public String getUtcOffSetData() {
        return appSharedPrefs.getString(pckName + "UTCoffSetData", "");
    }


    /**
     * @param date: save UTC date time
     */
    public void saveUtcDateTime(String date) {
        prefsEditor.putString(pckName + "utcCurrentDate", date).apply();
    }

    /**
     * @return UTC current date time
     */
    public String getUtcDateTime() {
        return appSharedPrefs.getString(pckName + "utcCurrentDate", "");
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


    private void saveLocationTime(String sourceName) {
        prefsEditor.putString(pckName + "saveLocationTime", sourceName).apply();
    }

    private String getLocationTime() {
        return appSharedPrefs.getString(pckName + "saveLocationTime", "");
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
    public String getPreviousLat() {
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
    private String getPreviousLongitude() {
        return appSharedPrefs.getString(pckName + "savePreviousLong", "");
    }


    /**
     * save end time for the current listening session
     */
    void saveEndTime(String endTIme) {
        prefsEditor.putString(pckName + "listeningEndTime", endTIme).apply();
    }

    /**
     * @return current listening end time
     */
    String getEndTIme() {
        return appSharedPrefs.getString(pckName + "listeningEndTime", "");
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

