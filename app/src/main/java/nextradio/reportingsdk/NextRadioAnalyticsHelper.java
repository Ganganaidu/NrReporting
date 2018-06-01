package nextradio.reportingsdk;


import android.app.Application;

import nextradio.nranalytics.controllers.NextRadioReportingSDK;


/***
 * Created by gkondati on 12/28/2017.
 */

public class NextRadioAnalyticsHelper {

    /***put this in your main application class*/
    static void initNRReportingSDK(Application application) {
        NextRadioReportingSDK.initializeSdk(application);
        NextRadioReportingSDK.registerApp("nr_android");
        NextRadioReportingSDK.setAppForProductionMode(false);
    }

    /***call this on main activity onCreate() method*/
    static void activateApp() {
        NextRadioReportingSDK.activateApp();
    }

    /**
     * put this on main activity onDestroy() method
     */
    public static void deActivateApp() {
        NextRadioReportingSDK.deActivateApp();
    }

    /**
     * Call this when user click play/change station
     */
    static void startListeningSession(long frequencyHz, int frequencySubChannel, int deliveryType, String callLetters) {
        NextRadioReportingSDK.startListeningSession(frequencyHz, frequencySubChannel, deliveryType, callLetters);
    }

    /**
     * Call this when radio event change
     */
    static void recordRadioImpressionEvent(String artist, String title, String eventMetadata, int frequencyHz, int frequencySubChannel, int deliveryType, String callLetters) {
        NextRadioReportingSDK.recordRadioImpressionEvent(artist, title, eventMetadata, frequencyHz, frequencySubChannel, deliveryType, callLetters);
    }

    /**
     * Call this when radio event change
     */
    static void recordRadioImpressionEvent(String artist, String title, String eventMetadata, int frequencyHz,
                                           int frequencySubChannel, int deliveryType, String callLetters, Object publicStationId, Object isSimulcast) {
        NextRadioReportingSDK.recordRadioImpressionEvent(artist, title, eventMetadata, frequencyHz, frequencySubChannel, deliveryType, callLetters, publicStationId, isSimulcast);
    }

    /**
     * call this when radio stops playing
     */
    static void stopListeningSession() {
        NextRadioReportingSDK.stopListeningSession();
    }
}
