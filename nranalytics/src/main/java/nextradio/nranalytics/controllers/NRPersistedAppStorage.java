package nextradio.nranalytics.controllers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import nextradio.nranalytics.objects.registerdevice.DeviceRegResponseInfo;

/**
 * Created by gkondati on 11/1/2017.
 */

public class NRPersistedAppStorage {

    private static final String TAG = "NRPersistedAppStorage";

    private static final String DefaultTAGURL = "http://dev-api.tagstation.com/";

    private static final String OVERRIDE_URL = "apiurl";
    private static final String DEVICE_ID = "deviceID";//is nothing but TS ID
    private static final String CACHING_ID = "cachingID";
    private static final String AD_GROUP = "adGroup";
    private static final String FEED_USER_ID = "adGroup";
    private static final String SELECTED_COUNTRY = "country";
    private static final String DEVELOPER_MODE = "enabledev";
    private static final String DEVICE_STRING = "DeviceString";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    /**
     * Saving data in shared preferences which will store life time of Application
     */
    public NRPersistedAppStorage() {
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
        Log.d(TAG, "getTsd: " + deviceInfo.getTsd());
        Log.d(TAG, "getCachingGroup: " + deviceInfo.getCachingGroup());
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
        prefsEditor.putString("visual_impression", visualImpressionData).apply();
    }

    public String getVisualImpression() {
        return appSharedPrefs.getString("visual_impression", "");
    }

    void saveAppSessionData(String visualImpressionData) {
        prefsEditor.putString("app_session", visualImpressionData).apply();
    }

    String getAppSessionData() {
        return appSharedPrefs.getString("app_session", "");
    }

    public void saveUTCoffSetData(String visualImpressionData) {
        prefsEditor.putString("UTCoffSetData", visualImpressionData).apply();
    }

    public String getUtcOffSetData() {
        return appSharedPrefs.getString("UTCoffSetData", "");
    }


    /**
     * @param date: save UTC date time
     */
    public void saveUtcDateTime(String date) {
        prefsEditor.putString("utcCurrentDate", date).apply();
    }

    /**
     * @return UTC current date time
     */
    public String getUtcDateTime() {
        return appSharedPrefs.getString("utcCurrentDate", "");
    }


    /**
     * @param date: save location json data
     */
    public void saveLocationData(String date) {
        prefsEditor.putString("jsonLocationData", date).apply();
    }

    /**
     * @return get location json data
     */
    public String getLocationData() {
        return appSharedPrefs.getString("jsonLocationData", "");
    }


    public void saveLocationTime(String sourceName) {
        prefsEditor.putString("saveLocationTime", sourceName).apply();
    }

    public String getLocationTime() {
        return appSharedPrefs.getString("saveLocationTime", "");
    }

    /**
     * we need this for comparing current and previous lat to avoid duplicate data
     */
    public void savePreviousLat(String prsLat) {
        prefsEditor.putString("savePreviousLat", prsLat).apply();
    }

    /**
     * @return last saved latitude
     */
    public String getPreviousLat() {
        return appSharedPrefs.getString("savePreviousLat", "");
    }


    /**
     * save end time for the current listening session
     */
    void saveEndTime(String endTIme) {
        prefsEditor.putString("listeningEndTime", endTIme).apply();
    }

    /**
     * @return current listening end time
     */
    String getEndTIme() {
        return appSharedPrefs.getString("listeningEndTime", "");
    }


    /**
     * save current listening data
     */
    void saveCurrentListeningData(String listeningData) {
        prefsEditor.putString("currentListeningData", listeningData).apply();
    }

    /**
     * @return saved current listening data
     */
    String getCurrentListeningData() {
        return appSharedPrefs.getString("currentListeningData", "");
    }

    /**
     * save listening data
     */
    void saveListeningData(String listeningData) {
        prefsEditor.putString("listeningData", listeningData).apply();
    }

    /**
     * @return saved listening data
     */
    String getListeningData() {
        return appSharedPrefs.getString("listeningData", "");
    }


    void saveRadioEventImpression(String radioImpressionData) {
        prefsEditor.putString("RadioEventImpression", radioImpressionData).apply();
    }


    String getRadioEventImpression() {
        return appSharedPrefs.getString("RadioEventImpression", "");
    }
}

