package nextradio.nranalytics.controllers;

import android.app.Application;
import android.util.Log;

/**
 * Created by gkondati on 11/1/2017.
 */


public class NextRadioReportingSDK {

    public static final String TAG = "NextRadioReportingSDK";

    static String SDK_VERSION = "ts.reporting-android-0.0.5";

    private static NRRegisterDeviceLogger nrRegisterDevice;

    private static boolean sdkInitialized;


    /**
     * This function initializes the NextRadio Reporting SDK. This function should call on the main application start up
     *
     * @param application The application context
     */
    public static synchronized void initializeSdk(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("context must be non-null");
        }
        NRAppContext.setAppContext(application);
        NRActivityHelper.startTracking(application);

        sdkInitialized = true;
        initLocalObjects();
    }

    /**
     * set app url to dev or prod mode
     *
     * @param isInProdMode TRUE for production mode, FALSE for dev mode
     */
    public static void setAppForProductionMode(boolean isInProdMode) {
        if (isInProdMode) {
            NRPersistedAppStorage.getInstance().setTagURL(NRPersistedAppStorage.PROD_TAG_URL);
        } else {
            NRPersistedAppStorage.getInstance().setTagURL(NRPersistedAppStorage.DEV_TAG_URL);
        }
    }

    /**
     * initialize all classes and variables that we need for this SDK setup
     */
    private static void initLocalObjects() {
        nrRegisterDevice = new NRRegisterDeviceLogger();
        NRLocationAdapter.getInstance().init();
    }

    /**
     * Indicates whether the NextRadio Reporting SDK has been initialized.
     *
     * @return true if initialized, false if not
     */
    private static boolean isInitialized() {
        return !sdkInitialized;
    }

    /**
     * Register application with NextRadio and generate unique ID to identify the application
     */
    public static void registerApp(String radioSourceName) {
        if (isInitialized()) {
            Log.e(TAG, "The NextRadio Reporting sdk must be initialized before calling " + "registerApp");
            return;
        }
        registerWithSdk(radioSourceName, null);
    }

    /**
     * Register application with NextRadio and generate unique ID to identify the application
     */
    public static void registerAppWithFmSource(String radioSourceName, String fmSource) {
        if (isInitialized()) {
            Log.e(TAG, "The NextRadio Reporting sdk must be initialized before calling " + "registerApp");
            return;
        }
        registerWithSdk(radioSourceName, fmSource);
    }

    /**
     * Notifies the events system that the app has launched & logs an activatedApp event.  Should be
     * called whenever your app becomes active, typically in the Activity  onCreate() method of your MainActivity class
     */
    public static void activateApp() {
        if (isInitialized()) {
            Log.e(TAG, "The NextRadio Reporting sdk must be initialized before calling " + "activateApp");
            return;
        }
        NRTimer.getInstance().create2MinTimer();
    }

    /**
     * Notifies the events system that the app has been deactivated (put in the background) and
     * tracks the application session information(Send data to server or stop collecting data).
     * Should be called whenever your app becomes inactive, typically in the onDestroy() method of MainActivity of your app.
     * <p>
     * NOTE: Do not deActivateApp(), if your app(FM Radio) is playing.
     * </p>
     */
    public static void deActivateApp() {
        if (isInitialized()) {
            Log.e(TAG, "The NextRadio Reporting sdk must be initialized before calling " + "deActivateApp");
            return;
        }
        //NRSessionLogger.getInstance().stopSessionUpdates();
        NRLocationAdapter.getInstance().stopLocationUpdates();
        nrRegisterDevice.clear();
    }

    /**
     * Register application with NextRadio and generate unique ID to identify the application
     */
    private static void registerWithSdk(String radioSourceName, String fmSourceName) {
        nrRegisterDevice.initSdk(radioSourceName, fmSourceName);
    }

    /**
     * Record listening data based on the station and freq
     *
     * @param frequencyHz         raw frequency that we need to identify station  ex: 9310000(93.1)
     * @param frequencySubChannel tells what kind of station it is, like HD or HD2
     * @param deliveryType        type of the station, number 1=FM|2=Stream|3=AM,
     * @param callLetters         optional value, callLetters for the station
     */
    public static void startListeningSession(long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters) {
        if (isInitialized()) {
            Log.e(TAG, "The NextRadio Reporting sdk must be initialized before calling " + "startListeningSession");
            return;
        }
        NRListeningSessionLogger.getInstance().recordListeningSession(frequencyHz, frequencySubChannel, deliveryType, callLetters);
    }

    /**
     * Record listening data based on the station and freq
     *
     * @param frequencyHz         raw frequency that we need to identify station  ex: 9310000(93.1)
     * @param frequencySubChannel tells what kind of station it is, like HD or HD2
     * @param deliveryType        type of the station, number 1=FM|2=Stream|3=AM,
     * @param callLetters         optional value, callLetters for the station
     * @param publicStationId     (integer) An ID used by TagStation to identify a radio station.( pass null if there is  no value to send here)
     * @param isSimulcast         (boolean)true|false - indicates whether or not user is hearing simulcast audio when streaming a station (deliveryType = 2)
     *                            (pass null if there is  no value to send here)
     */
    public static void startListeningSession(long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters, Object publicStationId, Object isSimulcast) {
        if (isInitialized()) {
            Log.e(TAG, "The NextRadio Reporting sdk must be initialized before calling " + "startListeningSession");
            return;
        }
        NRListeningSessionLogger.getInstance().recordListeningSession(frequencyHz, frequencySubChannel, deliveryType, callLetters, publicStationId, isSimulcast);
    }


    /**
     * stops reporting listening data and send data to server
     * <p>
     * Call this when your app stops playing FM radio
     */
    public static void stopListeningSession() {
        if (isInitialized()) {
            Log.e(TAG, "The NextRadio Reporting sdk must be initialized before calling " + "stopListeningSession");
            return;
        }
        NRListeningSessionLogger.getInstance().endCurrentListeningSession(true);
        NRReportingTracker.getInstance().reportDataToServer();
    }

    /**
     * send on playout event (listening session start, event changes)
     *
     * @param artist              : name of the current playing song/album(skip it if you have song metadata)
     * @param title               : title of the current playing song(skip it if you have song metadata)
     * @param eventMetadata       : meta data for current playing song
     * @param deliveryType        : number  1=FM | 2=Stream | 3=AM
     * @param frequencyHz         : frequency in hertz ex: 9310000(93.1)
     * @param frequencySubChannel : tells you weather channel is HD or HD2 or normal station
     * @param callLetters         : optional send call letters for the current station ex: WXRT(93.1)
     */
    public static void recordRadioImpressionEvent(String artist, String title, String eventMetadata,
                                                  long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters) {
        if (isInitialized()) {
            Log.e(TAG, "The NextRadio Reporting sdk must be initialized before calling " + "recordRadioImpressionEvent");
            return;
        }
        NRRadioImpressionLogger.getInstance().recordRadioImpressionEvent(artist, title, eventMetadata, deliveryType, frequencyHz, frequencySubChannel, callLetters);
    }

    /**
     * send on playout event (listening session start, event changes)
     *
     * @param artist              : name of the current playing song/album(skip it if you have song metadata)
     * @param title               : title of the current playing song(skip it if you have song metadata)
     * @param eventMetadata       : meta data for current playing song
     * @param deliveryType        : number  1=FM | 2=Stream | 3=AM
     * @param frequencyHz         : frequency in hertz ex: 9310000(93.1)
     * @param frequencySubChannel : tells you weather channel is HD or HD2 or normal station
     * @param callLetters         : optional send call letters for the current station ex: WXRT(93.1)
     * @param publicStationId     (integer) An ID used by TagStation to identify a radio station.( pass null if there is  no value to send here)
     * @param trackingId          : tracking id for the current event
     */
    public static void recordRadioImpressionEvent(String artist, String title, String eventMetadata,
                                                  long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters, Object publicStationId, Object trackingId) {
        if (isInitialized()) {
            Log.e(TAG, "The NextRadio Reporting sdk must be initialized before calling " + "recordRadioImpressionEvent");
            return;
        }
        NRRadioImpressionLogger.getInstance().recordRadioImpressionEvent(artist, title,
                eventMetadata, deliveryType, frequencyHz, frequencySubChannel, callLetters, publicStationId, trackingId);
    }
}
