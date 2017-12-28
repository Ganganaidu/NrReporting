package nextradio.nranalytics.controllers;

import android.app.Application;

/**
 * Created by gkondati on 11/1/2017.
 */


public class NraController {

    private static final String TAG = "NraController";

    static String SDK_VERSION = "1.0.2";

    private static NraController instance;

    private NRRegisterDeviceLogger nrRegisterDevice;

    private boolean sdkInitialized;

    public static NraController getInstance() {
        if (instance == null) {
            instance = new NraController();
        }
        return instance;
    }

    /**
     * This function initializes the NextRadio Reporting SDK. This function should call on the main application start up
     *
     * @param application The application context
     */
    public synchronized void initializeSdk(Application application) {
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
    private void initLocalObjects() {
        NRPersistedAppStorage mPrefStorage = new NRPersistedAppStorage();
        nrRegisterDevice = new NRRegisterDeviceLogger(mPrefStorage);

        NRLocationAdapter.getInstance().init(mPrefStorage);
    }

    /**
     * Indicates whether the NextRadio Reporting SDK has been initialized.
     *
     * @return true if initialized, false if not
     */
    private synchronized boolean isInitialized() {
        return sdkInitialized;
    }

    /**
     * Register application with NextRadio and generate unique ID to identify the application
     */
    public void registerApp(String advertisingId, String radioSourceName) {
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "registerApp");
        }
        registerWithSdk(advertisingId, radioSourceName);
    }

    /**
     * Notifies the events system that the app has launched & logs an activatedApp event.  Should be
     * called whenever your app becomes active, typically in the Application create method of each
     * long-running of your app.
     */
    public void activateApp() {
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "activateApp");
        }
        NRTimer.getInstance().create2MinTimer();
        //JobUtils.scheduleJob(NRAppContext.getAppContext());// schedule the job
    }

    /**
     * Notifies the events system that the app has been deactivated (put in the background) and
     * tracks the application session information(Send data to server or stop collecting data).
     * Should be called whenever your app becomes inactive, typically in the onPause() method of main Activity of your app,
     * if your app is not playing FM station. If app is active(playing FM) don't call this in onPause().
     * <p>
     * <p>
     * Also call this on your Main/Base Activity onDestroy() method
     * </>
     */
    public void deActivateApp() {
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "deActivateApp");
        }
        //NRSessionLogger.getInstance().stopSessionUpdates();
        NRLocationAdapter.getInstance().stopLocationUpdates();
    }

    /**
     * Register application with NextRadio and generate unique ID to identify the application
     */
    private void registerWithSdk(String advertisingId, String radioSourceName) {
        nrRegisterDevice.registerDevice(advertisingId, radioSourceName);
    }

    /**
     * Record listening data based on the station and freq
     *
     * @param frequencyHz         raw frequency that we need to identify station  ex: 9310000(93.1)
     * @param frequencySubChannel tells what kind of station it is, like HD or HD2
     * @param deliveryType        type of the station, number 1=FM|2=Stream|3=AM,
     * @param callLetters         optional value, callLetters for the station
     */
    public void startListeningSession(long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters) {
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "startListeningSession");
        }
        NRListeningSessionLogger.getInstance().recordListeningSession(frequencyHz, frequencySubChannel, deliveryType, callLetters);
    }

    public void stopListeningSession() {
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
    public void recordRadioImpressionEvent(String artist, String title, String eventMetadata,
                                           long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters) {
        if (!isInitialized()) {
            throw new IllegalArgumentException("The NextRadio Reporting sdk must be initialized before calling " + "recordRadioImpressionEvent");
        }
        NRRadioImpressionLogger.getInstance().recordRadioImpressionEvent(artist, title, eventMetadata, deliveryType, frequencyHz, frequencySubChannel, callLetters);
    }
}
