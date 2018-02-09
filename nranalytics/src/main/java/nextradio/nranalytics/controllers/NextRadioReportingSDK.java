package nextradio.nranalytics.controllers;

import android.app.Application;

/**
 * Created by gkondati on 11/1/2017.
 */


public class NextRadioReportingSDK {

    //private static final String TAG = "NextRadioReportingSDK";

    static String SDK_VERSION = "1.0.2";

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
    private static synchronized boolean isInitialized() {
        return sdkInitialized;
    }

    /**
     * Register application with NextRadio and generate unique ID to identify the application
     */
    public static void registerApp(String radioSourceName) {
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "registerApp");
        }
        registerWithSdk(radioSourceName, null);
    }

    /**
     * Register application with NextRadio and generate unique ID to identify the application
     */
    public static void registerAppWithFmSource(String radioSourceName, String fmSource) {
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "registerApp");
        }
        registerWithSdk(radioSourceName, fmSource);
    }

    /**
     * Notifies the events system that the app has launched & logs an activatedApp event.  Should be
     * called whenever your app becomes active, typically in the Activity  onCreate() method of your MainActivity class
     */
    public static void activateApp() {
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "activateApp");
        }
        NRTimer.getInstance().create2MinTimer();
        //JobUtils.scheduleJob(NRAppContext.getAppContext());// schedule the job
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
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "deActivateApp");
        }
        //NRSessionLogger.getInstance().stopSessionUpdates();
        NRLocationAdapter.getInstance().stopLocationUpdates();
    }

    /**
     * Register application with NextRadio and generate unique ID to identify the application
     */
    private static void registerWithSdk(String radioSourceName, String fmSourceName) {
        nrRegisterDevice.registerDevice(radioSourceName, fmSourceName);
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
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "startListeningSession");
        }
        NRListeningSessionLogger.getInstance().recordListeningSession(frequencyHz, frequencySubChannel, deliveryType, callLetters);
    }

    /**
     * stops reporting listening data and send data to server
     * <p>
     * Call this when your app stops playing FM radio
     */
    public static void stopListeningSession() {
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "stopListeningSession");
        }
        NRListeningSessionLogger.getInstance().endCurrentListeningSession();
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
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "recordRadioImpressionEvent");
        }
        NRRadioImpressionLogger.getInstance().recordRadioImpressionEvent(artist, title, eventMetadata, deliveryType, frequencyHz, frequencySubChannel, callLetters);
    }
}
