package nextradio.nranalytics.controllers;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

/**
 * Created by gkondati on 11/8/2017.
 */

class NRActivityHelper {

    static void startTracking(Application application) {
        application.registerActivityLifecycleCallbacks(
                new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {
                    }

                    @Override
                    public void onActivityResumed(final Activity activity) {
                        NRPersistedAppStorage.getInstaince().setUtcSendFlag(true);
                        if (checkLocationPermission(application)) {
                            NRLocationAdapter.getInstance().startLocationUpdates();
                        }
                    }

                    @Override
                    public void onActivityPaused(final Activity activity) {
                        NRReportingTracker.getInstance().reportDataToServer();
                    }

                    @Override
                    public void onActivityStopped(Activity activity) {

                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {

                    }
                });
    }

    private static boolean checkLocationPermission(Application application) {
        return ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
