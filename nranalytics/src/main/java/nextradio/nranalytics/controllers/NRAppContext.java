package nextradio.nranalytics.controllers;

import android.app.Application;

/**
 * Created by gkondati on 12/7/2017.
 */

class NRAppContext {
    private static Application application;

    static Application getAppContext() {
        return application;
    }

    static void setAppContext(Application context) {
        application = context;
    }
}
