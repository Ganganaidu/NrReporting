package nextradio.reportingsdk;

import android.app.Application;

import nextradio.nranalytics.controllers.NraController;

/**
 * Created by gkondati on 12/5/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NraController.getInstance().initializeSdk(this);
        NraController.getInstance().registerApp("NR SDK TEST");
    }
}
