package nextradio.reportingsdk;

import android.app.Application;

/**
 * Created by gkondati on 12/5/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NextRadioAnalyticsHelper.initNRReportingSDK(this);
    }
}
