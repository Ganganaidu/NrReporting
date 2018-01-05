package nextradio.nranalytics.controllers;

import android.location.Location;

import org.json.JSONObject;

import nextradio.nranalytics.utils.DateUtils;
import nextradio.nranalytics.utils.GsonConverter;

/**
 * Created by gkondati on 12/20/2017.
 */

class NRRadioImpressionLogger {

    private static NRRadioImpressionLogger instance;
    private long previousFrequencyHz = 0;
    private String previousArtist = "";
    private String previousTitle = "";

    public static NRRadioImpressionLogger getInstance() {
        if (instance == null) {
            instance = new NRRadioImpressionLogger();
        }
        return instance;
    }

    private NRPersistedAppStorage persistedAppStorage;

    private NRRadioImpressionLogger() {
        persistedAppStorage = new NRPersistedAppStorage();
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
        if (isIdenticalData(frequencyHz, artist, title, eventMetadata)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "Impression.RadioEvent");
            jsonObject.put("createTime", DateUtils.getCurrentUtcTime());
            jsonObject.put("artist", artist);
            jsonObject.put("title", title);
            jsonObject.put("eventMetadata", eventMetadata);
            jsonObject.put("deliveryType", String.valueOf(deliveryType));
            jsonObject.put("frequencyHz", String.valueOf(frequencyHz));
            jsonObject.put("frequencySubChannel", String.valueOf(frequencySubChannel));
            jsonObject.put("callLetters", callLetters);
            try {
                Location location = NRLocationAdapter.getInstance().getCurrentLocation();
                if (location != null) {
                    jsonObject.put("latitude", Double.toString(location.getLatitude()));
                    jsonObject.put("longitude", Double.toString(location.getLatitude()));
                } else {
                    jsonObject.put("latitude", "null");
                    jsonObject.put("longitude", "null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String data = GsonConverter.getInstance().createJsonObjectToString(persistedAppStorage.getRadioEventImpression(), jsonObject);
            persistedAppStorage.saveRadioEventImpression(data);

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
        boolean isIdentical = previousFrequencyHz == frequencyHz && (previousArtist.equals(artist)) && previousTitle.equals(title);

        previousFrequencyHz = frequencyHz;
        previousArtist = artist;
        previousTitle = title;
        return isIdentical;
    }
}

