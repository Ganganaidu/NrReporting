package nextradio.nranalytics.controllers;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by gkondati on 12/18/2017.
 */

class NRTimer {
    private static final String TAG = "NRTimer";

    private static NRTimer _instance;

    private final CompositeDisposable disposable;

    public static NRTimer getInstance() {
        if (_instance == null) {
            _instance = new NRTimer();
        }
        return _instance;
    }

    private NRTimer() {
        disposable = new CompositeDisposable();
    }

    void create2MinTimer() {
        disposable.clear();
        disposable.add(Observable.interval(100, TimeUnit.SECONDS).subscribe(aLong -> {
            Log.d(TAG, "create2MinTimer: ");

            //if there is no location updates or location updates not started for some reason, try to check again
            if (!NRLocationAdapter.getInstance().isLocationUpdatesStarted()) {
                NRLocationAdapter.getInstance().startLocationUpdates();
            }
            NRLocationAdapter.getInstance().saveLocationData();
            NRListeningSessionLogger.getInstance().updateListeningSession();

            sendDataToServer();
        }, Throwable::printStackTrace));
    }

    private void sendDataToServer() {
        Observable.timer(2, TimeUnit.SECONDS).subscribe(aLong -> NRReportingTracker.getInstance().reportDataToServer());
    }
}
