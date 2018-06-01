package nextradio.nranalytics.controllers;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gkondati on 12/18/2017.
 */

class NRTimer {
    //private static final String TAG = "NRTimer";

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
        //Log.d(TAG, "start 2MinTimer: ");
        disposable.clear();
        disposable.add(Observable.interval(2, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.single())
                .subscribe(aLong -> {
                    //Log.d(TAG, "update 2Min Timer: ");

                    //if there is no location updates or location updates not started for some reason, try to check again
                    if (!NRLocationAdapter.getInstance().isLocationUpdatesStarted()) {
                        NRLocationAdapter.getInstance().startLocationUpdates();
                    }

                    NRLocationAdapter.getInstance().saveLocationData();
                    NRListeningSessionLogger.getInstance().updateListeningSession();
                    NRReportingTracker.getInstance().reportDataToServer();

                }, Throwable::printStackTrace));
    }
}
