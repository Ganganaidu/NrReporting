package nextradio.reportingsdk;

import android.app.Application;

import nextradio.nranalytics.controllers.NextRadioReportingSDK;

/**
 * Created by gkondati on 12/5/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NextRadioReportingSDK.initializeSdk(this);
        NextRadioReportingSDK.registerApp("NR SDK TEST");
    }
}
