package nextradio.nranalytics.controllers;

import android.location.Location;
import android.util.Log;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import nextradio.nranalytics.utils.AppUtils;
import nextradio.nranalytics.utils.GsonConverter;
import nextradio.nranalytics.utils.NrDateUtils;

import static nextradio.nranalytics.controllers.NextRadioReportingSDK.TAG;


/**
 * Created by gkondati on 12/20/2017.
 */

class NRRadioImpressionLogger {

    private static NRRadioImpressionLogger instance;
    private long previousFrequencyHz = 0;
    private String previousArtist = "";
    private String previousTitle = "";

    private CompositeDisposable disposable = new CompositeDisposable();

    static NRRadioImpressionLogger getInstance() {
        if (instance == null) {
            instance = new NRRadioImpressionLogger();
        }
        return instance;
    }


    private NRRadioImpressionLogger() {
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
    void recordRadioImpressionEvent(String artist, String title, String eventMetadata, int deliveryType,
                                    long frequencyHz, int frequencySubChannel, String callLetters) {
        disposable.clear();
        disposable.add(Observable.fromCallable(() -> isIdenticalData(frequencyHz, artist, title, eventMetadata))
                .subscribeOn(Schedulers.computation())
                .subscribe(isIdenticalData -> {
                    //insert only if data is not identical
                    if (!isIdenticalData) {
                        updateRadioImpressionData(artist, title, eventMetadata, deliveryType,
                                frequencyHz, frequencySubChannel, callLetters, null, null);
                    }
                }, Throwable::printStackTrace));
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
     * @param publicStationId     : An ID used by TagStation to identify a radio station.
     * @param trackingId          : tracking id for the current event
     */
    void recordRadioImpressionEvent(String artist, String title, String eventMetadata, int deliveryType,
                                    long frequencyHz, int frequencySubChannel, String callLetters, Object publicStationId, Object trackingId) {
        disposable.clear();
        disposable.add(Observable.fromCallable(() -> isIdenticalData(frequencyHz, artist, title, eventMetadata))
                .subscribeOn(Schedulers.computation())
                .subscribe(isIdenticalData -> {
                    //insert only if data is not identical
                    if (!isIdenticalData) {
                        updateRadioImpressionData(artist, title, eventMetadata, deliveryType,
                                frequencyHz, frequencySubChannel, callLetters, publicStationId, trackingId);
                    }
                }, Throwable::printStackTrace));
    }

    private void updateRadioImpressionData(String artist, String title, String eventMetadata, int deliveryType,
                                           long frequencyHz, int frequencySubChannel, String callLetters, Object publicStationId, Object trackingId) {
        //We don't report to server for any subsequent calls without an active session
        String currentSession = NRPersistedAppStorage.getInstance().getCurrentListeningData();
        if (AppUtils.isNullOrEmpty(currentSession)) {
            Log.e(TAG, "updateRadioImpressionData: " + " Please start listening session before recording any impression event");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "Impression.RadioEvent");
            jsonObject.put("createTime", NrDateUtils.getCurrentUtcTime());
            jsonObject.put("artist", artist);
            jsonObject.put("title", title);
            jsonObject.put("eventMetadata", eventMetadata);
            jsonObject.put("deliveryType", String.valueOf(deliveryType));
            jsonObject.put("frequencyHz", String.valueOf(frequencyHz));
            jsonObject.put("frequencySubChannel", String.valueOf(frequencySubChannel));
            jsonObject.put("callLetters", callLetters);
            jsonObject.put("publicStationId", publicStationId);
            jsonObject.put("trackingId", trackingId);

            try {
                Location location = NRLocationAdapter.getInstance().getCurrentLocation();
                if (location != null && NRPersistedAppStorage.getInstance().isGdprApproved()) {
                    jsonObject.put("latitude", Double.toString(location.getLatitude()));
                    jsonObject.put("longitude", Double.toString(location.getLongitude()));
                } else {
                    jsonObject.put("latitude", null);
                    jsonObject.put("longitude", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String data = GsonConverter.getInstance().createJsonObjectToString(NRPersistedAppStorage.getInstance().getRadioEventImpression(), jsonObject);
            NRPersistedAppStorage.getInstance().saveRadioEventImpression(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * there is no need to report identical data, doing data validation here by comparing previous values
     *
     * @param frequencyHz : we need this to identify station (frequency in hertz ex: 9310000(93.1)
     * @param title       : name of the current playing song
     * @param artist      : name of the current playing song
     */
    private boolean isIdenticalData(long frequencyHz, String artist, String title, String eventMetadata) {
        if ((artist == null || artist.isEmpty()) && (title == null || title.isEmpty()) && (eventMetadata == null || eventMetadata.isEmpty())) {
            return true;
        }
        boolean isIdentical = previousFrequencyHz == frequencyHz && (previousArtist.equals(artist) && previousTitle.equals(title));

        previousFrequencyHz = frequencyHz;
        previousArtist = artist;
        previousTitle = title;
        return isIdentical;
    }
}

